package de.unknown.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class WarShipTeam {

	public final static String PREFIX = "§7[§6WarShipTeam§7] ";

	public final static int NAME_MIN = 3;
	public final static int NAME_MAX = 14;
	public final static int ALIAS_MIN = 2;
	public final static int ALIAS_MAX = 4;

	private int wins, loses, ploses, pwins, coins;

	public static WarShipTeam getWarShipTeam(Integer id) {

		try {
			PreparedStatement ps = ConnectionHolder.prepareStatement("SELECT id FROM WSTTeams WHERE id=?");
			ps.setInt(1, id);
			ResultSet rs = ConnectionHolder.executeQuery(ps);
			if (rs.next())
				return new WarShipTeam(rs.getInt("id"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static WarShipTeam getWarShipTeam(String name) {
		try {
			return getWarShipTeam(Integer.parseInt(name));
		} catch (NumberFormatException e) {
		}
		try {
			PreparedStatement ps;
			if (name.startsWith("#")) {
				ps = ConnectionHolder.prepareStatement("SELECT id FROM WSTTeams WHERE alias=?");
				ps.setString(1, name.substring(1));
			} else {
				ps = ConnectionHolder.prepareStatement("SELECT id FROM WSTTeams WHERE name=?");
				ps.setString(1, name);
			}
			ResultSet rs = ConnectionHolder.executeQuery(ps);
			if (rs.next())
				return new WarShipTeam(rs.getInt("id"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static WarShipTeam[] getWarShipTeams() {
		try {
			PreparedStatement ps = ConnectionHolder.prepareStatement("SELECT COUNT(id) as total FROM WSTTeams");
			ResultSet rs = ConnectionHolder.executeQuery(ps);
			rs.next();
			WarShipTeam[] teams = new WarShipTeam[rs.getInt("total")];
			ps = ConnectionHolder.prepareStatement("SELECT id FROM WSTTeams");
			rs = ConnectionHolder.executeQuery(ps);
			int i = 0;
			while (rs.next()) {
				teams[i] = WarShipTeam.getWarShipTeam(rs.getInt("id"));
				i++;
			}
			return teams;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public static WarShipTeam createWarShipTeam(String name, String alias, UUID leader) {
		return new WarShipTeam(name, alias, leader);
	}

	private final int id;
	private String name;
	private String alias;
	private final HashSet<UUID> members = new HashSet<>();
	private final HashSet<UUID> admins = new HashSet<>();
	private UUID leader;

	private WarShipTeam(int id) {
		this.id = id;
		reload();
	}

	private WarShipTeam(String name, String alias, UUID leader) {
		this.name = name;
		this.alias = alias;
		try {
			PreparedStatement ps = ConnectionHolder.prepareStatement("INSERT INTO WSTTeams(name, alias) VALUES (?,?)");
			ps.setString(1, name);
			ps.setString(2, alias);
			ConnectionHolder.executeUpdate(ps);
			ps = ConnectionHolder.prepareStatement("SELECT id FROM WSTTeams WHERE name=?");
			ps.setString(1, name);
			ResultSet rs = ConnectionHolder.executeQuery(ps);
			rs.next();
			this.id = rs.getInt("id");
			setLeader(GiantPlayer.getGiantPlayer(leader));
			members.add(this.leader);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NullPointerException("SQLException");
		}
	}

	public void update() {
		try {
			PreparedStatement ps = ConnectionHolder.prepareStatement(
					"UPDATE WSTTeams SET name=?, alias=?, wins=?, loses=?, pwins=?, ploses=? WHERE id=?");
			ps.setString(1, name);
			ps.setString(2, alias);
			ps.setInt(3, wins);
			ps.setInt(4, loses);
			ps.setInt(5, pwins);
			ps.setInt(6, ploses);
			ps.setInt(7, id);
			ConnectionHolder.executeUpdate(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		members.clear();
		admins.clear();
		try {
			PreparedStatement ps = ConnectionHolder.prepareStatement("SELECT * FROM WSTTeams WHERE id=?");
			ps.setInt(1, id);
			ResultSet rs = ConnectionHolder.executeQuery(ps);
			rs.next();
			name = rs.getString("name");
			alias = rs.getString("alias");
			rs.getInt("wins");
			if (!rs.wasNull())
				wins = rs.getInt("wins");
			rs.getInt("loses");
			if (!rs.wasNull())
				loses = rs.getInt("loses");
			rs.getInt("pwins");
			if (!rs.wasNull())
				pwins = rs.getInt("pwins");
			rs.getInt("ploses");
			if (!rs.wasNull())
				ploses = rs.getInt("loses");
			coins = rs.getInt("coins");
			ps = ConnectionHolder.prepareStatement("SELECT uuid FROM Players WHERE warshipteam=?");
			ps.setInt(1, id);
			rs = ConnectionHolder.executeQuery(ps);
			while (rs.next()) {
				GiantPlayer p = GiantPlayer.getGiantPlayer(UUID.fromString(rs.getString("uuid")));
				if (p.getWarShipRang() == WarShipRang.ADMIN)
					admins.add(p.getUniqueId());
				if (p.getWarShipRang() == WarShipRang.LEADER)
					leader = p.getUniqueId();
				members.add(p.getUniqueId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public GiantPlayer[] getMembers() {
		HashSet<GiantPlayer> players = new HashSet<>();
		for (UUID uuid : members) {
			players.add(GiantPlayer.getGiantPlayer(uuid));
		}
		return players.toArray(new GiantPlayer[players.size()]);
	}

	public String[] getMembersUniqueIds() {
		return members.toArray(new String[members.size()]);
	}

	public GiantPlayer[] getAdmins() {
		HashSet<GiantPlayer> players = new HashSet<>();
		for (UUID uuid : admins) {
			players.add(GiantPlayer.getGiantPlayer(uuid));
		}
		return players.toArray(new GiantPlayer[players.size()]);
	}

	public String[] getAdminUniqueIds() {
		return admins.toArray(new String[admins.size()]);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !obj.getClass().equals(this.getClass()))
			return false;
		if (((WarShipTeam) obj).getId() == this.getId())
			return true;
		return false;
	}

	@Override
	public String toString() {
		return id + " - " + name + " [" + alias + "]";
	}

	public GiantPlayer getLeader() {
		return GiantPlayer.getGiantPlayer(leader);
	}

	public void setLeader(UUID leader) {
		setLeader(GiantPlayer.getGiantPlayer(leader));
	}

	public void setLeader(GiantPlayer leader) {
		if (leader == null)
			throw new NullPointerException("Leader is null");
		leader.setWarShipTeamId(id);
		leader.setWarShipRang(WarShipRang.LEADER);
		leader.update();
		this.leader = leader.getUniqueId();
	}

	public void disband() {
		try {
			PreparedStatement ps = ConnectionHolder
					.prepareStatement("UPDATE Players SET warshipteam=NULL, warshiprang=NULL WHERE warshipteam=?");
			ps.setInt(1, id);
			ConnectionHolder.executeUpdate(ps);

			ps = ConnectionHolder.prepareStatement("DELETE FROM WSTTeams WHERE id=?");
			ps.setInt(1, id);
			ConnectionHolder.executeUpdate(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void broadcast(String message) {
		for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			if (members.contains(p.getUniqueId()))
				p.sendMessage(PREFIX + message);
		}
	}

	public UUID getLeaderUniqueId() {
		return leader;
	}

	public int getWins() {
		return wins;
	}
	
	public int getCoins() {
		return coins;
	}

	public int getLoses() {
		return loses;
	}

	public int getPublicWins() {
		return pwins;
	}

	public int getPublicLoses() {
		return ploses;
	}

	public void win() {
		wins++;
	}

	public void lose() {
		loses++;
	}

	public void winPublic() {
		pwins++;
	}

	public void losePublic() {
		ploses++;
	}

}

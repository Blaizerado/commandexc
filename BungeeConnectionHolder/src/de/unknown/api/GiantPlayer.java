package de.unknown.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GiantPlayer {

	public static GiantPlayer getGiantPlayerbyIP(String ip) {
		try {
			PreparedStatement ps = ConnectionHolder.prepareStatement("SELECT * FROM Players WHERE ip=?");
			ps.setString(1, ip);
			ResultSet rs = ConnectionHolder.executeQuery(ps);
			if (rs.next()) {
				return getPlayer(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static GiantPlayer getGiantPlayerbyTSUID(String uid) {
		try {
			PreparedStatement ps = ConnectionHolder.prepareStatement("SELECT * FROM Players WHERE tsuid=?");
			ps.setString(1, uid);
			ResultSet rs = ConnectionHolder.executeQuery(ps);
			if (rs.next()) {
				return getPlayer(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static GiantPlayer getGiantPlayer(ProxiedPlayer p) {
		return getGiantPlayer(p.getUniqueId());
	}

	public static GiantPlayer getGiantPlayer(String name) {
		try {
			PreparedStatement ps = ConnectionHolder
					.prepareStatement("SELECT * FROM Players WHERE username=? OR uuid=?");
			ps.setString(1, name);
			ps.setString(2, name);
			ResultSet rs = ConnectionHolder.executeQuery(ps);
			if (rs.next()) {
				return getPlayer(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return uuid.toString() + " [" + name + "]";
	}

	public static GiantPlayer getGiantPlayer(UUID uuid) {
		try {
			PreparedStatement ps = ConnectionHolder.prepareStatement("SELECT * FROM Players WHERE uuid=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ConnectionHolder.executeQuery(ps);
			if (rs.next()) {
				return getPlayer(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static GiantPlayer getPlayer(ResultSet rs) {
		try {
			GiantPlayer player = new GiantPlayer(UUID.fromString(rs.getString("uuid")));
			player.name = rs.getString("username");
			player.money = rs.getInt("giantcoins");
			player.rang = Rang.valueOf(rs.getString("prefixgroup").toUpperCase());
			player.firstJoin = rs.getLong("firstjoin");
			player.lastJoin = rs.getLong("lastjoin");
			player.tsuid = rs.getString("tsuid");
			player.banDuration = rs.getLong("banned");
			if (player.banDuration != 0) {
				player.banReason = rs.getString("banreason");
			}
			player.muteDuration = rs.getLong("muted");
			if (player.muteDuration != 0) {
				player.muteReason = rs.getString("mutereason");
			}
			player.nick = rs.getString("nickname");
			player.inserted = true;
			player.warShipTeamId = rs.getInt("warshipteam");
			if (rs.wasNull()) {
				player.warShipRang = null;
				player.warShipTeamId = 0;
			} else {
				player.warShipRang = WarShipRang.valueOf(rs.getString("warshiprang").toUpperCase());
			}
			player.playTime = rs.getLong("playtime");
			player.ip = rs.getString("ip");
			player.premiumTime = rs.getLong("premiumtime");
			return player;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private final UUID uuid;

	private String name;
	private String banReason, muteReason;
	private String tsuid, nick;
	private String ip;

	private Rang rang;

	private long firstJoin, lastJoin, playTime;
	private long banDuration, muteDuration;
	private long premiumTime;

	private int money;
	private int warShipTeamId;

	private WarShipTeam warShipTeam;
	private WarShipRang warShipRang;

	private boolean inserted;

	public GiantPlayer(UUID uuid, String username) {
		this.uuid = uuid;
		this.name = username;
		this.rang = Rang.DEFAULT;
		this.firstJoin = System.currentTimeMillis();
		this.lastJoin = System.currentTimeMillis();
		this.inserted = false;
	}

	private GiantPlayer(UUID uuid) {
		this.uuid = uuid;
	}

	public void update() {
		if (inserted) {
			try {
				PreparedStatement ps = ConnectionHolder.prepareStatement(
						"UPDATE Players SET username=?, giantcoins=?, prefixgroup=?, firstjoin=?, lastjoin=?, tsuid=?, banned=?, banreason=?, muted=?, mutereason=?, nickname=?, warshipteam=?, warshiprang=?, playtime=?, premiumtime=?, ip=? WHERE uuid=?");
				ps.setString(1, name);
				ps.setInt(2, money);
				ps.setString(3, rang.toString());
				ps.setLong(4, firstJoin);
				ps.setLong(5, lastJoin);
				ps.setString(6, tsuid);
				ps.setLong(7, banDuration);
				ps.setString(8, banReason);
				ps.setLong(9, muteDuration);
				ps.setString(10, muteReason);
				ps.setString(11, nick);
				if (hasWarShipTeam()) {
					ps.setInt(12, warShipTeamId);
					ps.setString(13, warShipRang.toString());
				} else {
					ps.setNull(12, Types.INTEGER);
					ps.setNull(13, Types.VARCHAR);
				}
				ps.setLong(14, playTime);
				ps.setLong(15, premiumTime);
				ps.setString(16, ip);

				ps.setString(17, uuid.toString());
				ConnectionHolder.executeUpdate(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				PreparedStatement ps = ConnectionHolder.prepareStatement(
						"INSERT INTO Players (username, giantcoins, prefixgroup, firstjoin, lastjoin, tsuid, banned, banreason, muted, mutereason, nickname, warshipteam, warshiprang, playtime, uuid, premiumtime, ip) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				ps.setString(1, name);
				ps.setInt(2, money);
				ps.setString(3, rang.toString());
				ps.setLong(4, firstJoin);
				ps.setLong(5, lastJoin);
				ps.setString(6, tsuid);
				ps.setLong(7, banDuration);
				ps.setString(8, banReason);
				ps.setLong(9, muteDuration);
				ps.setString(10, muteReason);
				ps.setString(11, nick);
				if (hasWarShipTeam()) {
					ps.setInt(12, warShipTeamId);
					ps.setString(13, warShipRang.toString());
				} else {
					ps.setNull(12, Types.INTEGER);
					ps.setNull(13, Types.VARCHAR);
				}
				ps.setLong(14, playTime);
				ps.setString(15, uuid.toString());
				ps.setLong(16, premiumTime);
				ps.setString(17, ip);
				ConnectionHolder.executeUpdate(ps);

				ps = ConnectionHolder.prepareStatement("INSERT INTO PlayerStats (uuid) VALUES (?)");
				ps.setString(1, uuid.toString());
				ConnectionHolder.executeUpdate(ps);

				ps = ConnectionHolder.prepareStatement("INSERT INTO Kits (uuid) VALUES (?)");
				ps.setString(1, uuid.toString());
				ConnectionHolder.executeUpdate(ps);
				inserted = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBanReason() {
		return banReason;
	}

	public void setBanReason(String banReason) {
		this.banReason = banReason;
	}

	public String getMuteReason() {
		return muteReason;
	}

	public void setMuteReason(String muteReason) {
		this.muteReason = muteReason;
	}

	public String getIp() {
		return ip;
	}

	public long getPremiumTime() {
		return premiumTime;
	}

	public void setIp(String IP) {
		ip = IP;
	}

	public void setPremiumTime(long time) {
		premiumTime = time;
	}

	public String getTsuid() {
		return tsuid;
	}

	public void setTsuid(String tsuid) {
		this.tsuid = tsuid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Rang getRang() {
		return rang;
	}

	public void setRang(Rang rang) {
		this.rang = rang;
	}

	public long getFirstJoin() {
		return firstJoin;
	}

	public void setFirstJoin(long firstJoin) {
		this.firstJoin = firstJoin;
	}

	public long getLastJoin() {
		return lastJoin;
	}

	public void setLastJoin(long lastJoin) {
		this.lastJoin = lastJoin;
	}

	public long getBanDuration() {
		return banDuration;
	}

	public void setBanDuration(long banDuration) {
		this.banDuration = banDuration;
	}

	public long getMuteDuration() {
		return muteDuration;
	}

	public void setMuteDuration(long muteDuration) {
		this.muteDuration = muteDuration;
	}

	public int getMoney() {
		return money;
	}

	public boolean setMoney(int money) {
		if (money < 0)
			return false;
		this.money = money;
		return true;
	}

	public boolean addMoney(int money) {
		if (this.money + money < 0)
			return false;
		this.money += money;
		return true;
	}

	public boolean isBanned() {
		return banReason != null;
	}

	public boolean isMuted() {
		return muteReason != null;
	}

	public boolean isNicked() {
		return nick != null && nick.isEmpty() == false;
	}

	// WARSHIP

	public int getWarShipTeamId() {
		return warShipTeamId;
	}

	public void setWarShipTeamId(int wstId) {
		this.warShipTeamId = wstId;
	}

	public WarShipTeam getWarShipTeam() {
		if (warShipTeam == null) {
			warShipTeam = WarShipTeam.getWarShipTeam(warShipTeamId);
		}
		return warShipTeam;
	}

	public WarShipRang getWarShipRang() {
		return warShipRang;
	}

	public void setWarShipRang(WarShipRang wsr) {
		this.warShipRang = wsr;
	}

	public boolean hasWarShipTeam() {
		return warShipTeamId != 0;
	}

	public long getPlayTime() {
		return playTime;
	}

	public void setPlayTime(long playTime) {
		this.playTime = playTime;
	}

	public void addPlayTime(long l) {
		playTime += l;
	}
}
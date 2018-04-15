package de.unknown.api;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConnectionHolder extends Plugin {

	private static Connection connection;
	private static Object lock = new Object();
	private final static String[] ARGS = new String[5];
	private ScheduledTask task;
	public static boolean restart;

	@Override
	public void onEnable() {
		super.onEnable();
		try {
			File file = new File(getDataFolder(), "config.yml");
			Configuration cfg;
			if (file.exists() == false) {
				file.getParentFile().mkdirs();
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				cfg.set("mysql.host", "localhost");
				cfg.set("mysql.database", "network");
				cfg.set("mysql.port", "3306");
				cfg.set("mysql.user", "root");
				cfg.set("mysql.password", "password");
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
			} else {
				cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			}
			ARGS[0] = cfg.getString("mysql.host", "localhost");
			ARGS[1] = cfg.getString("mysql.database", "network");
			ARGS[2] = cfg.getString("mysql.port", "3306");
			ARGS[3] = cfg.getString("mysql.user", "root");
			ARGS[4] = cfg.getString("mysql.password", "");
			connect(ARGS[0], ARGS[1], ARGS[2], ARGS[3], ARGS[4]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		task = ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				long time = (System.currentTimeMillis() / 1000) % 86400;
				if (time <= 1) {
					restart = true;
					ProxyServer.getInstance().broadcast("§7[Sea§bGiants§7] §6Das Netzwerk startet neu!");
					for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
						p.disconnect("");
					}
					BungeeCord.getInstance().stop();
					return;
				}
				if (time == 86399) { // einer Sekunde
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §6einer Sekunde §cneu!");
					return;
				}
				if (time >= 86390) { // 10 Sekunden
					ProxyServer.getInstance().broadcast(
							"§7[Sea§bGiants§7] §cDas Netzwerk startet in §6" + (86400 - time) + " Sekunden §cneu!");
					return;
				}
				if (time == 86380) { // 20 Sekunden
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §620 Sekunden §cneu!");
					return;
				}
				if (time == 86370) { // 30 Sekunden
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §630 Sekunden §cneu!");
					return;
				}
				if (time == 86340) { // 60 Sekunden
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §660 Sekunden §cneu!");
					return;
				}
				if (time == 86280) { // 2 Minuten
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §62 Minuten §cneu!");
					return;
				}
				if (time == 86220) { // 3 Minuten
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §63 Minuten §cneu!");
					return;
				}
				if (time == 86160) { // 4 Minuten
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §64 Minuten §cneu!");
					return;
				}
				if (time == 86100) { // 5 Minuten
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §65 Minuten §cneu!");
					return;
				}
				if (time == 85800) { // 10 Minuten
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §610 Minuten §cneu!");
					return;
				}
				if (time == 85200) { // 20 Minuten
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §620 Minuten §cneu!");
					return;
				}
				if (time == 84600) { // 30 Minuten
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §630 Minuten §cneu!");
					return;
				}
				if (time == 82800) { // 60 Minuten
					ProxyServer.getInstance()
							.broadcast("§7[Sea§bGiants§7] §cDas Netzwerk startet in §660 Minuten §cneu!");
					return;
				}
			}
		}, 0L, 1L, TimeUnit.SECONDS);
		
		ProxyServer.getInstance().registerChannel("BungeeCommands");
		ProxyServer.getInstance().getPluginManager().registerListener(this, new IncomingPluginChannelListener());		
	}

	@Override
	public void onDisable() {
		super.onDisable();
		task.cancel();
		close();
		if (restart) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						new ProcessBuilder("./start.sh").start();
					} catch (IOException e) {
						System.err.println("BungeeCord is unable to Restart!");
						System.err.println("BungeeCord is stopping...");
						e.printStackTrace();
					}
				}
			}, "RestartThread").start();
		}
	}

	public static void connect(String host, String database, String port, String user, String password) {
		synchronized (lock) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.err.println("[MySQL] §cTreiber funktionieren nicht!");
				return;
			}
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user="
						+ user + "&password=" + password);
				System.out.println("[MySQL] Verbunden zum angegebenen Server.");
			} catch (SQLException e) {
				System.err.println("[MySQL] Verbindung zum angegebenen Server fehlgeschlagen:");
				e.printStackTrace();
			}
		}
	}

	public static void close() {
		synchronized (lock) {
			try {
				if (connection == null || connection.isClosed()) {
					System.err.println("[MySQL] Verbindung exisitert nicht oder wurde bereits beendet!");
					return;
				}
				connection.close();
				System.out.println("[MySQL] Verbindung wurde erfolgreich beendet!");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("[MySQL] Verbindung konnte nicht beendet werden!");
			}
		}
	}

	public static PreparedStatement prepareStatement(String sql) throws SQLException {
		synchronized (lock) {
			if (connection == null || connection.isClosed())
				connect(ARGS[0], ARGS[1], ARGS[2], ARGS[3], ARGS[4]);
			return connection.prepareStatement(sql);
		}
	}

	public static ResultSet executeQuery(PreparedStatement ps) throws SQLException {
		synchronized (lock) {
			if (connection == null || connection.isClosed())
				connect(ARGS[0], ARGS[1], ARGS[2], ARGS[3], ARGS[4]);
			return ps.executeQuery();
		}
	}

	public static int executeUpdate(PreparedStatement ps) throws SQLException {
		synchronized (lock) {
			if (connection == null || connection.isClosed())
				connect(ARGS[0], ARGS[1], ARGS[2], ARGS[3], ARGS[4]);
			return ps.executeUpdate();
		}
	}

	public static Connection getConnection() throws SQLException {
		synchronized (lock) {
			if (connection == null || connection.isClosed())
				connect(ARGS[0], ARGS[1], ARGS[2], ARGS[3], ARGS[4]);
			return connection;
		}
	}

}

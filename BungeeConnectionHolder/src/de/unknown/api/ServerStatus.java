package de.unknown.api;

public enum ServerStatus {
	
	RESTART("§6�? Restarting �?"), OFFLINE("§4✘ Offline ✘"), INGAME("§b\u25B6 Ingame \u25C0"), ONLINE("§a\u2714 Online \u2714"), UNKNOWN(
			"§b? ? ?"), LOBBY("§a\u2714 Lobbyphase \u2714"), MAINTENANCE("§5✘ Maintenance ✘");
	
	private final String line;
	
	ServerStatus(String line) {
		this.line = line;
	}
	
	@Override
	public String toString() {
		return line;
	}
	
	
	
}

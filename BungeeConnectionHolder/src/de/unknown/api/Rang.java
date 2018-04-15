package de.unknown.api;

import java.util.ArrayList;

public enum Rang implements Comparable<Rang>{
	
	ADMIN(0, "§4Admin §8�? §4", "§6", "§4Admin §8�? §4"),
	HEADDEVELOPER(10, "§bHead-Developer §8�? §b", "§6", "§bHead §8�? §b"),
	HEADMODERATOR(20, "§cHead-Moderator §8�? §c", "§6", "§cHead §8�? §c"),
	HEADBUILDER(30, "§eHead-Architekt §8�? §e", "§6", "§eHead §8�? §e"),
	DEVELOPER(11, "§bDeveloper §8�? §b", "§r", "§bDev §8�? §b"),
	MODERATOR(21, "§cModerator §8�? §c", "§r", "§cMod §8�? §c"),
	SUPPORTER(22, "§9Supporter §8�? §9", "§r", "§9Sup §8�? §9"),
	BUILDER(31, "§eArchitekt §8�? §e", "§r", "§eArc §8�? §e"),
	YOUTUBER(44, "§5YouTuber §8�? §5", "§r", "§5YT §8�? §5"),
	PREMIUM(55, "§6", "§7", "§6"),
	DEFAULT(66, "§7", "§7", "§7");
	
	public final static Rang[] ADMINISTRATION = new Rang[] {ADMIN, HEADDEVELOPER, HEADMODERATOR, HEADBUILDER};
	public final static Rang[] DEVELOPING = new Rang[] {ADMIN, HEADDEVELOPER, HEADMODERATOR, HEADBUILDER, DEVELOPER};
	public final static Rang[] MODERATION = new Rang[] {ADMIN, HEADDEVELOPER, HEADMODERATOR, HEADBUILDER, DEVELOPER, MODERATOR};
	public final static Rang[] TEAM = new Rang[] {ADMIN, HEADDEVELOPER, HEADMODERATOR, HEADBUILDER, DEVELOPER, MODERATOR, SUPPORTER, BUILDER};
	public final static Rang[] YOUTUBE = new Rang[] {ADMIN, HEADDEVELOPER, HEADMODERATOR, HEADBUILDER, DEVELOPER, MODERATOR, SUPPORTER, BUILDER, YOUTUBER};
	
	private final int level;
	private String prefix, chatColor, tab;
	
	Rang(int level, String prefix, String chatcolor, String tab) {
		this.level = level;
		this.setPrefix(prefix);
		this.setChatColor(chatcolor);
		this.setTab(tab);
	}
	
	public boolean isDeveloping() {
		for(Rang r : DEVELOPING) {
			if(r == this) return true;
		}
		return false;
	}
	
	public static Rang[] getRangs(Rang rang) {
		ArrayList<Rang> rangs = new ArrayList<>();
		for(Rang r : values()) {
			if(r.level <= rang.level) rangs.add(r);
		}
		return rangs.toArray(new Rang[rangs.size()]);
	}

	public int getLevel() {
		return level;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getChatColor() {
		return chatColor;
	}

	public void setChatColor(String chatColor) {
		this.chatColor = chatColor;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}
	
	public boolean isAdministration() {
		for(Rang r : ADMINISTRATION) {
			if(r == this) return true;
		}
		return false;
	}
	
	public boolean isModeration() {
		for(Rang r : MODERATION) {
			if(r == this) return true;
		}
		return false;
	}
	
	public boolean isTeam() {
		for(Rang r : TEAM) {
			if(r == this) return true;
		}
		return false;
	}
	
	public boolean isPremium() {
		return this != DEFAULT;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}

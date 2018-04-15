package de.unknown.cmdframework.parent;

import de.unknown.api.Rang;

public abstract class AbstractCommand{
	
	private final String name;
	private String description;
	private String[] label;
	private String noPermissionsMessage;
	private String[][] tabComplete;
	private int max_args = -1;
	private int min_args = 0;
	private Rang[] permissions;
	
	public AbstractCommand(String name) {
		if(name == null) throw new NullPointerException();
		this.name = name;
	}
	public final String getName() {
		return name;
	}

	public final String getDescription() {
		return description == null ? "" : description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final String[] getLabel() {
		return label == null ? new String[0] : label;
	}

	public final void setLabel(String... label) {
		this.label = label;
	}

	public final String getNoPermissionsMessage() {
		return noPermissionsMessage == null ? "§cDu hast keine Berechtigungen!" : noPermissionsMessage;
	}

	public final void setNoPermissionsMessage(String noPermissionMessage) {
		this.noPermissionsMessage = noPermissionMessage;
	}

	public final String[][] getTabComplete() {
		return tabComplete == null ? new String[0][0] : tabComplete;
	}

	public final void setTabComplete(String[]... tabComplete) {
		this.tabComplete = tabComplete;
	}

	public final int getMaxArgs() {
		return max_args;
	}

	public final void setMaxArgs(int max_args) {
		this.max_args = max_args;
	}

	public final int getMinArgs() {
		return min_args;
	}

	public final void setMinArgs(int min_args) {
		this.min_args = min_args;
	}

	public final Rang[] getPermissions() {
		return permissions;
	}

	public final void setPermissions(Rang... permissions) {
		this.permissions = permissions;
	}

}

package de.unknown.cmdframework.command;

import java.util.ArrayList;

import de.unknown.api.GiantPlayer;
import de.unknown.api.Rang;
import de.unknown.api.WarShipTeam;
import de.unknown.cmdframework.JubeUtil;
import de.unknown.cmdframework.parent.AbstractCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

@SuppressWarnings("deprecation")
public abstract class Command extends AbstractCommand implements Comparable<Command> {

	private final Command parentCommand;
	protected ArrayList<Command> subcommands = new ArrayList<>();
	private String usage;

	public Command(String name, Command parent) {
		super(name.split(" ")[0]);
		this.parentCommand = parent;
	}

	public Command(String name) {
		this(name, null);
	}

	public abstract boolean execute(GiantPlayer gp, CommandSender cs, String label, String[] args);

	@Override
	public int compareTo(Command cmd) {
		return getFullCommand().compareToIgnoreCase(cmd.getFullCommand());
	}

	public void onCommand(GiantPlayer gp, CommandSender cs, String label, String[] args) {
		if (checkForSubCommands(gp, cs, label, args) == true)
			return;
		if (checkPermissions(gp, cs) == false) {
			cs.sendMessage(getNoPermissionsMessage());
			return;
		}
		if (checkArguments(args) == false) {
			cs.sendMessage("§c" + getUsage());
			return;
		}
		if (execute(gp, cs, label, args) == false)
			cs.sendMessage("§c" + getUsage());
	}

	public ArrayList<String> onTabComplete(GiantPlayer gp, CommandSender cs, String[] args, boolean space) {
		ArrayList<String> complete = new ArrayList<>();
		if (space && subcommands.size() > 0 && args.length == 0) {
			for (Command cmd : subcommands) {
				if (cmd.checkPermissions(gp, cs) == false)
					continue;
				complete.add(cmd.getName());
				for (String s : cmd.getLabel()) {
					complete.add(s);
				}
			}
			return complete;
		} else if (space == false && args.length == 1 && subcommands.size() > 0) {
			for (Command cmd : subcommands) {
				if (cmd.getName().toLowerCase().startsWith(args[0].toLowerCase())
						|| JubeUtil.startsArrayWith(args[0], getLabel())) {
					if (cmd.checkPermissions(gp, cs) == false)
						continue;
					complete.add(cmd.getName());
				}
			}
			return complete;
		} else if (args.length > 0 && subcommands.size() > 0) {
			for (Command cmd : subcommands) {
				if (cmd.getName().equalsIgnoreCase(args[0]) || JubeUtil.hasArrayString(args[0], cmd.getLabel())) {
					if (cmd.checkPermissions(gp, cs) == false)
						return complete;
					String[] args2 = new String[args.length - 1];
					System.arraycopy(args, 1, args2, 0, args2.length);
					return cmd.onTabComplete(gp, cs, args2, space);
				}
			}
			return complete;
		}
		if (checkPermissions(gp, cs) == false)
			return complete;
		if (args.length > getTabComplete().length || args.length == getTabComplete().length && space)
			return complete;
		int index = 0;
		if (args.length > 0 && args.length <= getTabComplete().length) {
			if (space && args.length != getTabComplete().length) {
				index = args.length;
			} else {
				index = args.length - 1;
			}
		}
		complete.addAll(replaceOnTabComplete(gp, getTabComplete()[index], args, space));
		return complete;
	}

	public boolean checkArguments(String[] args) {
		if (args.length < getMinArgs() || getMaxArgs() != -1 && args.length > getMaxArgs())
			return false;
		return true;
	}

	public boolean checkForSubCommands(GiantPlayer gp, CommandSender cs, String label, String[] args) {
		if (args.length == 0)
			return false;
		for (Command cmd : subcommands) {
			if (cmd.getName().toLowerCase().equals(args[0].toLowerCase())
					|| JubeUtil.hasArrayString(cmd.getName(), args)) {
				String[] args2 = new String[args.length - 1];
				System.arraycopy(args, 1, args2, 0, args2.length);
				cmd.onCommand(gp, cs, label, args2);
				return true;
			}
		}
		return false;
	}

	public boolean checkPermissions(GiantPlayer gp, CommandSender cs) {
		if (cs instanceof ConsoleCommandSender || getPermissions() == null || getPermissions().length == 0)
			return true;
		for (Rang permission : getPermissions()) {
			if (gp.getRang() == permission)
				return true;
		}
		return false;
	}

	public ArrayList<Command> getSubCommands() {
		return subcommands;
	}

	public void setSubCommands(ArrayList<Command> subcommands) {
		if (subcommands == null)
			throw new NullPointerException();
		this.subcommands = subcommands;
	}

	public void addSubCommand(Command subcmd) {
		if (subcmd == null)
			throw new NullPointerException();
		subcommands.add(subcmd);
	}

	public ArrayList<String> replaceOnTabComplete(GiantPlayer gp, String s, String[] args, boolean space) {
		ArrayList<String> complete = new ArrayList<>();
		if (s.equalsIgnoreCase("%player") || s.equalsIgnoreCase("%player%") ) {
			for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
				if (space) {
					complete.add(p.getName());
				} else if (args.length > 0) {
					if (p.getName().toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
						complete.add(p.getName());
					}
				}
			}
		} else if (s.equalsIgnoreCase("%server")) {
			for (String server : ProxyServer.getInstance().getServers().keySet()) {
				if (space) {
					complete.add(server);
				} else if (args.length > 0) {
					if (server.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
						complete.add(server);
					}
				}
			}
		} else if (s.equalsIgnoreCase("%team")) {
			for (WarShipTeam wst : WarShipTeam.getWarShipTeams()) {
				if (space) {
					complete.add(wst.getName());
				} else if (args.length > 0) {
					if (wst.getName().toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
						complete.add(wst.getName());
					}
				}
			}
		} else if (s.equalsIgnoreCase("%rang")) {
			for (Rang r : Rang.values()) {
				if (space) {
					complete.add(r.toString());
				} else if (args.length > 0) {
					if (r.toString().startsWith(args[args.length - 1].toLowerCase())) {
						complete.add(r.toString());
					}
				}
			}
		} else {
			if (space) {
				complete.add(s);
			} else if (args.length > 0) {
				if (s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
					complete.add(s);
				}
			}
		}
		return complete;
	}

	public ArrayList<String> replaceOnTabComplete(GiantPlayer gp, String[] strs, String[] args, boolean space) {
		ArrayList<String> complete = new ArrayList<>();
		for (String s : strs) {
			complete.addAll(replaceOnTabComplete(gp, s, args, space));
		}
		return complete;
	}

	public Command getParentCommand() {
		return parentCommand;
	}

	public boolean hasParentCommand() {
		return parentCommand != null;
	}

	public String getFullCommand() {
		if (hasParentCommand()) {
			return parentCommand.getFullCommand() + " " + getName();
		}
		return getName();
	}

	public String getUsage() {
		return "/" + getFullCommand() + " " + (usage == null ? "" : usage);
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

}

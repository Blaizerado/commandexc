package de.unknown.cmdframework.command;

import de.unknown.api.GiantPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.command.ConsoleCommandSender;

public abstract class ConsoleCommand extends Command {

	public ConsoleCommand(String name, Command parent) {
		super(name, parent);
	}

	public ConsoleCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(GiantPlayer gp, CommandSender cs, String label, String[] args) {
		return execute((ConsoleCommandSender) cs, label, args);
	}
	
	public abstract boolean execute(ConsoleCommandSender console, String label, String[] args);
	
	@Override
	public boolean checkPermissions(GiantPlayer gp, CommandSender cs) {
		return cs instanceof ConsoleCommandSender;
	}

}

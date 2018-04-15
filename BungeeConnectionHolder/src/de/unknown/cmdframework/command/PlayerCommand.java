package de.unknown.cmdframework.command;

import de.unknown.api.GiantPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class PlayerCommand extends Command {

	public PlayerCommand(String name, Command parent) {
		super(name, parent);
	}

	public PlayerCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(GiantPlayer gp, CommandSender cs, String label, String[] args) {
		return execute(gp, (ProxiedPlayer)cs, label, args);
	}
	
	public abstract boolean execute(GiantPlayer gp, ProxiedPlayer p, String label, String[] args);
	
	@Override
	public boolean checkPermissions(GiantPlayer gp, CommandSender cs) {
		if(cs instanceof ProxiedPlayer == false) return false;
		return super.checkPermissions(gp, cs);
	}

}

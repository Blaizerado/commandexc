package de.unknown.cmdframework;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import de.unknown.cmdframework.command.Command;

import java.util.Set;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class CommandFramework extends Plugin {
	
	private final HashMap<String, Command> commands;
	private final TabCompleter tabCompleter;
	private final CommandProcessor processor;

	
	public CommandFramework() {
		this.commands = new HashMap<>();
		this.tabCompleter = new TabCompleter(this);
		this.processor = new CommandProcessor(this);
	}
	
	public final Set<Entry<String, Command>> getCommandEntrySet() {
		return commands.entrySet();
	}
	
	public final HashMap<String, Command> getCommandMap()	{
		return commands;
	}
	
	public final Collection<Command> getCommands() {
		return commands.values();
	}
	
	public final void registerCommand(Command cmd) {
		commands.put(cmd.getName().toLowerCase(), cmd);
	}
	
	@Override
	public final void onEnable() {
		ProxyServer.getInstance().getPluginManager().registerListener(this, tabCompleter);
		ProxyServer.getInstance().getPluginManager().registerListener(this, processor);
		enable();
	}
	
	@Override
	public final void onDisable() {
		disable();
	}
	
	public abstract void enable();
	
	public abstract void disable();

}

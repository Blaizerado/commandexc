package de.unknown.cmdframework;

import java.util.List;

import de.unknown.api.GiantPlayer;
import de.unknown.cmdframework.command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class TabCompleter implements Listener {

	private final CommandFramework cmdframwork;

	public TabCompleter(CommandFramework commandFramework) {
		this.cmdframwork = commandFramework;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(TabCompleteEvent e) {
		if (e.getSender() instanceof ProxiedPlayer == false)
			return;
		GiantPlayer gp = GiantPlayer.getGiantPlayer(((ProxiedPlayer) e.getSender()).getUniqueId());
		String[] args = e.getCursor().split(" ");
		boolean space = e.getCursor().endsWith(" ");
		
		if (!e.getCursor().startsWith("/") || args.length == 0) {
			return;
		}
		
		if (args.length == 1 && space == false) {
			List<String> complete = e.getSuggestions();
			for (Command cmd : cmdframwork.getCommands()) {
				if (cmd.getName().toLowerCase().startsWith(args[0].substring(1).toLowerCase())
						|| JubeUtil.startsArrayWith(args[0].substring(1), cmd.getLabel())) {
					if (cmd.checkPermissions(gp, (CommandSender) e.getSender()) == true) {
						complete.add("/" + cmd.getName());
						for (String s : cmd.getLabel()) {
							complete.add("/" + s);
						}
					}
				}
			}
			return;
		}
		
		List<String> complete = e.getSuggestions();
		complete.clear();
		for (Command cmd : cmdframwork.getCommands()) {
			String command = "/" + cmd.getName();
			if (command.equalsIgnoreCase(args[0])
					|| JubeUtil.hasArrayString(args[0].substring(1, args[0].length()), cmd.getLabel())) {
				String[] args2 = new String[args.length - 1];
				System.arraycopy(args, 1, args2, 0, args2.length);				
				complete.addAll(cmd.onTabComplete(gp, (CommandSender) e.getSender(), args2, space));
				complete.forEach(System.out::println);
				return;
			}
		}
	}

}

package de.unknown.cmdframework;

import java.util.Map.Entry;

import de.unknown.api.GiantPlayer;
import de.unknown.cmdframework.command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class CommandProcessor implements Listener {

	private final CommandFramework cmdframework;
	public final static String[] ONLY_TEAM_COMMANDS = new String[] {};

	public CommandProcessor(CommandFramework cmdframework) {
		this.cmdframework = cmdframework;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(ChatEvent e) {
		if (e.getSender() instanceof CommandSender == false || e.isCommand() == false)
			return;
		try {
			GiantPlayer gp;
			if (e.getSender() instanceof ProxiedPlayer) {
				gp = GiantPlayer.getGiantPlayer(((ProxiedPlayer) e.getSender()).getUniqueId());
			} else {
				gp = null;
			}
			String lowered = e.getMessage().toLowerCase();
			if (gp.getRang().isTeam() == false && (lowered.startsWith("/pl ") || lowered.startsWith("/plugins ")
					|| lowered.startsWith("/? ") || lowered.startsWith("/help ") || lowered.startsWith("/about ")
					|| lowered.startsWith("/version ") || lowered.startsWith("/plugins ")
					|| lowered.startsWith("/ver "))) {

			}
			String[] args = e.getMessage().split(" ");
			args[0] = args[0].substring(1);
			String name = args[0].toLowerCase();
			for (Entry<String, Command> entry : cmdframework.getCommandEntrySet()) {
				if (name.equals(entry.getKey().toLowerCase()) == false
						&& JubeUtil.hasArrayString(name, entry.getValue().getLabel()) == false)
					continue;
				e.setCancelled(true);
				String[] args2 = new String[args.length - 1];
				System.arraycopy(args, 1, args2, 0, args2.length);
				entry.getValue().onCommand(gp, (CommandSender) e.getSender(), args[0], args2);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			((CommandSender) e.getSender())
					.sendMessage("§cEs ist ein unbekannter Fehler aufgetreten! Bitte wende dich an ein Team-Mitglied!");
		}
	}
}

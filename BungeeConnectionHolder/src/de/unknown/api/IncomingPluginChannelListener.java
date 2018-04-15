package de.unknown.api;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.unknown.cmdframework.CommandFramework;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/**
 * @author Butzlabben
 * @since 27.03.2018
 */
public class IncomingPluginChannelListener implements Listener {

	@EventHandler
	public void on(PluginMessageEvent e) {
		try {
			System.out.println(e.getTag());
			ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
			String subchannel = in.readUTF();
			System.out.println("Recieved Ping");
			System.out.println(subchannel);
			if (e.getTag().equalsIgnoreCase("BungeeCommands")) {
				
				String subserver = in.readUTF();
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("BungeeCommands");
				for (Plugin p : ProxyServer.getInstance().getPluginManager().getPlugins()) {
					if (p instanceof CommandFramework) {
						CommandFramework cm = (CommandFramework) p;
						for (String s : cm.getCommandMap().keySet()) {
							out.writeUTF("/" + s);
						}

					}
				}
				ServerInfo si = ProxyServer.getInstance().getServerInfo(subserver);
				si.sendData("BungeeCommands", out.toByteArray());
			}
		} catch (Exception e1) {
		}
	}
}

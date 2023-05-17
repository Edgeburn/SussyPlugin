package com.edgeburnmedia.sussy;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

class SussyPacketListener implements PluginMessageListener {

	public static final String SUSSY_PLAYER_PACKET_ID = "sussy:sussy_player";

	/**
	 * A method that will be thrown when a PluginMessageSource sends a plugin message on a
	 * registered channel.
	 *
	 * @param channel Channel that the message was sent through.
	 * @param player  Source of the message.
	 * @param message The raw message that was sent.
	 */
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (channel.equals(SUSSY_PLAYER_PACKET_ID)) {
			if (player.hasPermission("sussy.sussycommand")) {
				ByteArrayDataInput input = ByteStreams.newDataInput(message);
				String victimName = input.readUTF();
				Player victim = player.getServer().getPlayer(victimName);
				if (victim != null) {
					if (!victim.hasPermission("sussy.exempt")) {
						Sussy.sussyPlayer(victim, player);
					} else {
						player.sendMessage("§cThat player is exempt from being sussy!");
					}
				}
			}
		}
	}
}

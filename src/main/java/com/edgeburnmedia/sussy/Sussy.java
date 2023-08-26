package com.edgeburnmedia.sussy;

import me.NoChance.PvPManager.Managers.PlayerHandler;
import me.NoChance.PvPManager.PvPManager;
import me.NoChance.PvPManager.PvPlayer;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getOnlinePlayers;

public final class Sussy extends JavaPlugin {
	public static final int BSTATS_PLUGIN_ID = 14568;
	private static Sussy INSTANCE;
	public FileConfiguration config = getConfig();
	private SussyPacketListener sussyPacketListener;

	private static void showTitle(Player playerToTeleport, CommandSender sender) {
		String title;
		String subtitle;

		if (CommandSussy.showEjectorName) {
			subtitle = "They were ejected by " + sender.getName();
		} else {
			subtitle = "They were ejected";
		}

		title = playerToTeleport.getDisplayName() + " was the sussy Imposter!";

		if (CommandSussy.broadcastEjectToEveryone) {
			for (Player player : getOnlinePlayers()) { // send everyone on the server the message that the player in ejected was the sussy imposter
				player.sendTitle(title, subtitle, 1, 70, 10);
			}
		} else {
			playerToTeleport.sendTitle(title, subtitle, 1, 70, 10);
			((Player) sender).sendTitle(title, subtitle, 1, 70, 10);
		}

	}

	public static Sussy getInstance() {
		return INSTANCE;
	}

	/**
	 * Ejects a player into the void and blames a specific player for it.
	 *
	 * @param playerToTeleport The player to eject.
	 * @param ejector          The player who ejected the player.
	 */
	public static void sussyPlayer(Player playerToTeleport, CommandSender ejector) {
		Location locationToTeleportTo = getSussyLocation(playerToTeleport);

		if (!(playerToTeleport.getGameMode() == GameMode.ADVENTURE)) { // we don't want to change the player's gamemode to survival if they're in adventure because it could break things
			playerToTeleport.setGameMode(GameMode.SURVIVAL);
		}

		untagCombat(playerToTeleport);

		playerToTeleport.teleport(locationToTeleportTo);
		showTitle(playerToTeleport, ejector);

		getInstance().getLogger().info(ejector.getName() + " ejected " + playerToTeleport.getDisplayName());
	}

	private static Location getSussyLocation(Player playerToTeleport) {
		// Ensure that no matter what the world's minimum height is (should it be set via a datapack) it will always teleport 300 blocks below bedrock.
		double voidLocation = playerToTeleport.getWorld().getMinHeight() - 300.0;

		Location currentPlayerLocation = playerToTeleport.getLocation(); // get the current location of the player to be teleported
		Location locationToTeleportTo = new Location(playerToTeleport.getWorld(), currentPlayerLocation.getX(), voidLocation, currentPlayerLocation.getZ()); // set the location to which the player will be teleported, which is ~ -300 ~
		return locationToTeleportTo;
	}

	/**
	 * Ejects a player into the void and blames the server for it.
	 *
	 * @param playerToTeleport The player to eject.
	 */
	public static void sussyPlayer(Player playerToTeleport) {
		sussyPlayer(playerToTeleport, getInstance().getServer().getConsoleSender());
	}

	@Override
	public void onDisable() {
		sussyPacketListener = new SussyPacketListener();
		getServer().getMessenger().unregisterIncomingPluginChannel(this, SussyPacketListener.SUSSY_PLAYER_PACKET_ID,
			sussyPacketListener);
		// Plugin shutdown logic
	}

	@Override
	public void onEnable() {
		INSTANCE = this;
		this.getCommand("sussy").setExecutor(new CommandSussy(this));
		config.addDefault("showEjectorName", true);
		config.addDefault("broadcastEjectToEveryone", true);
		config.options().copyDefaults(true);
		saveConfig();
		sussyPacketListener = new SussyPacketListener();
		getServer().getMessenger().registerIncomingPluginChannel(this, SussyPacketListener.SUSSY_PLAYER_PACKET_ID, sussyPacketListener);

		Metrics metrics = new Metrics(this, BSTATS_PLUGIN_ID);
	}

	public boolean showEjectorName() {
		return config.getBoolean("showEjectorName", true);
	}

	public boolean broadcastEjectToEveryone() {
		return config.getBoolean("broadcastEjectToEveryone", true);
	}

	public static boolean supportsPackets() {
		return true;
	}

	static void untagCombat(Player player) {
		try {
			PvPManager pvpmanager = null;
			if (Bukkit.getPluginManager().isPluginEnabled("PvPManager")) {
				pvpmanager = (PvPManager) Bukkit.getPluginManager().getPlugin("PvPManager");
			}

			PlayerHandler playerHandler = pvpmanager.getPlayerHandler();

			PvPlayer pvplayer = playerHandler.get(player);

			pvplayer.unTag();
		} catch (NoClassDefFoundError e) {
			// ignore
		}
	}
}

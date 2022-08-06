package com.edgeburnmedia.sussy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class CommandSussy implements CommandExecutor {
	public static boolean showEjectorName;
	public static boolean broadcastEjectToEveryone;

	CommandSussy(Sussy plugin) {
		showEjectorName = plugin.showEjectorName();
		broadcastEjectToEveryone = plugin.broadcastEjectToEveryone();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player playerToTeleport;

		if (args.length == 0) {
			sender.sendMessage("§cYou must specify a player to eject!");
			return false;
		} else {
			playerToTeleport = getServer().getPlayer(args[0]);
		}

		if (playerToTeleport == null) {
			sender.sendMessage("§c\"" + args[0] + "\" is not online.");
			return true;
		} else if (playerToTeleport.hasPermission("sussy.exempt")) {
			sender.sendMessage("§c\"" + args[0] + "\" is exempt from being ejected.");
			return true;
		}
		sender.sendMessage(playerToTeleport.getDisplayName() + " was ejected!");
		Sussy.sussyPlayer(playerToTeleport, sender);
		return true;
	}

}

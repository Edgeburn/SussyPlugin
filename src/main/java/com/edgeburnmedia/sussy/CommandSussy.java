package com.edgeburnmedia.sussy;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.*;

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
        Location currentPlayerLocation;

        if (args.length == 0 && sender instanceof Player) { // command is being executed by a player with no arguments, so teleport themselves
            playerToTeleport = (Player) sender;
        } else if (args.length == 0 && !(sender instanceof Player)) { // the command is being run as console or in a command block and no player to teleport was specified
            sender.sendMessage("§cYou must either specify a player to eject or run this command as a player!");
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

        // Ensure that no matter what the world's minimum height is (should it be set via a datapack) it will always teleport 300 blocks below bedrock.
        double voidLocation = playerToTeleport.getWorld().getMinHeight() - 300.0;

        currentPlayerLocation = playerToTeleport.getLocation(); // get the current location of the player to be teleported
        Location locationToTeleportTo = new Location(playerToTeleport.getWorld(), currentPlayerLocation.getX(), voidLocation, currentPlayerLocation.getZ()); // set the location to which the player will be teleported, which is ~ -300 ~

        if (!(playerToTeleport.getGameMode() == GameMode.ADVENTURE)) { // we don't want to change the player's gamemode to survival if they're in adventure because it could break things
            playerToTeleport.setGameMode(GameMode.SURVIVAL);
        }

        playerToTeleport.teleport(locationToTeleportTo);
        showTitle(playerToTeleport, sender);
        sender.sendMessage(playerToTeleport.getDisplayName() + " was ejected!");
        getLogger().info(sender.getName() + " ejected " + playerToTeleport.getDisplayName());
        return true;
    }

    public void showTitle (Player playerToTeleport, CommandSender sender) {
        String title;
        String subtitle;

        if (showEjectorName) {
            subtitle = "They were ejected by " + sender.getName();
        } else {
            subtitle = "They were ejected";
        }

        title = playerToTeleport.getDisplayName() + " was the sussy Imposter!";

        if (broadcastEjectToEveryone) {
            for (Player player : getOnlinePlayers()) { // send everyone on the server the message that the player in ejected was the sussy imposter
                player.sendTitle(title, subtitle, 1, 70, 10);
            }
        } else {
            playerToTeleport.sendTitle(title, subtitle, 1, 70, 10);
            ((Player) sender).sendTitle(title, subtitle, 1, 70, 10);
        }

    }
}

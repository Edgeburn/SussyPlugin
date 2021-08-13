package com.edgeburnmedia.sussy;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.*;

public class CommandSussy implements CommandExecutor {

    public static final double VOID_LOCATION = -300d;
    public static boolean showEjectorName;

    CommandSussy(Sussy plugin) {
        showEjectorName = plugin.showEjectorName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player playerToTeleport;
        Location currentPlayerLocation;
        String subtitle;

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

        currentPlayerLocation = playerToTeleport.getLocation(); // get the current location of the player to be teleported
        Location locationToTeleportTo = new Location(playerToTeleport.getWorld(), currentPlayerLocation.getX(), VOID_LOCATION, currentPlayerLocation.getZ()); // set the location to which the player will be teleported, which is ~ -300 ~
        playerToTeleport.setGameMode(GameMode.SURVIVAL);
        playerToTeleport.teleport(locationToTeleportTo);
        if (showEjectorName) {
            subtitle = "They were ejected by " + sender.getName();
        } else {
            subtitle = "They were ejected";
        }

        for (Player player : getOnlinePlayers()) { // send everyone on the server the message that the player in ejected was the sussy imposter
            player.sendTitle(playerToTeleport.getDisplayName() + " was the sussy Imposter!", subtitle, 1, 70, 10);
        }

        sender.sendMessage(playerToTeleport.getDisplayName() + " was ejected!");
        getLogger().info(sender.getName() + " ejected " + playerToTeleport.getDisplayName());
        return true;
    }
}

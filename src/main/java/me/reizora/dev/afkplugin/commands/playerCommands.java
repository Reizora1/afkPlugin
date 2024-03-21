package me.reizora.dev.afkplugin.commands;

import me.reizora.dev.afkplugin.afkMonitor;
import me.reizora.dev.afkplugin.listeners.playerEvents;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class playerCommands implements CommandExecutor {
    public static final HashMap<Player, Location> playerSpawnLocations = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player){
            World world = player.getWorld();

            if (command.getName().equalsIgnoreCase("setspawn")) {
                Location getSpawn = player.getLocation();
                playerSpawnLocations.put(player, getSpawn);
                playerEvents.playerJoinLocation.remove(player);

                world.setSpawnLocation(getPlayerSpawn(player));
                player.sendMessage(ChatColor.GREEN+ "Spawn location set.");
            }
            else if (command.getName().equalsIgnoreCase("setAFKTimer")){
                if (sender.hasPermission("admin")){
                    if (args.length != 1){
                        sender.sendMessage(ChatColor.RED+ "Invalid command argument!");
                        sender.sendMessage(ChatColor.GRAY+ "/setafktimer <seconds>.");
                    }
                    else {
                        long afkTime = Long.parseLong(args[0]);
                        afkMonitor.defaultAfkTimer = afkTime * 1000L;
                        sender.sendMessage(ChatColor.GREEN+ "AFK timer has been updated to " +(afkTime+ " seconds."));
                    }
                }
                else{
                    sender.sendMessage(ChatColor.GRAY+ "You have no permission to execute that command!");
                }
            }
            else if (command.getName().equalsIgnoreCase("getAFKTimer")) {
                sender.sendMessage(ChatColor.GRAY+ "AFK Timer is currently set to " +(afkMonitor.defaultAfkTimer/1000L)+ " seconds.");
            }
        }

        return true;
    }
    public static Location getPlayerSpawn(Player player){
        return playerSpawnLocations.get(player);
    }
}
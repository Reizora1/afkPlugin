package me.reizora.dev.afkplugin.commands;

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
        Player player = (Player) sender;
        World world = player.getWorld();

        if (command.getName().equalsIgnoreCase("setspawn")) {
            Location getSpawn = player.getLocation();
            playerSpawnLocations.put(player, getSpawn);

            world.setSpawnLocation(getPlayerSpawn(player));
            player.sendMessage(ChatColor.GREEN + "Spawn location set.");
        }

        return true;
    }
    public static Location getPlayerSpawn(Player player){
        return playerSpawnLocations.get(player);
    }
}
package me.reizora.dev.afkplugin;

import me.reizora.dev.afkplugin.commands.playerCommands;
import me.reizora.dev.afkplugin.listeners.playerEvents;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class afkMonitor {
    JavaPlugin plugin;
    World world;
    public afkMonitor(JavaPlugin plugin){
        this.plugin = plugin;
    }
    public static long defaultAfkTimer = 10000L;                                                                        // 1000L = 1sec.
    private static final HashMap<Player, Long> lastRecordedMovement = new HashMap<>();                                  // HashMap to store player object as keys and the passed value; System.currentTimeMillis() as its value.
    private static final HashMap<Player, Boolean> isPlayerAFK = new HashMap<>();
    private static final HashMap<Player, Long> lastRewardReleasedTime = new HashMap<>();
    public static void playerJoined(Player player){
        lastRecordedMovement.put(player, System.currentTimeMillis());                                                   // Immediately fetches the system time upon playerJoin and store it in the lastRecordedMovement hashmap.
        lastRewardReleasedTime.put(player, System.currentTimeMillis());                                                 // Initializes the hashmap for the periodic rewards every x interval in AFK status.
        isPlayerAFK.put(player, false);
    }
    public static void playerLeft(Player player){                                                                       // Removes an instance of a players data from the hashmaps.
        isPlayerAFK.remove(player);
        lastRecordedMovement.remove(player);
        lastRewardReleasedTime.remove(player);
        playerCommands.playerSpawnLocations.remove(player);
        playerEvents.playerLastLocation.remove(player);
        playerEvents.playerJoinLocation.remove(player);
    }
    public static void playerMoved(Player player){                                                                      // Called in the onPlayerMove() in the playerEvents.java. It is the logic for determining whenever a player gets out of an AFK status.
        if (isPlayerAFK.get(player)){
            player.sendMessage(ChatColor.GREEN+ "You are no longer AFK!");
            isPlayerAFK.put(player, false);
            teleportToLastLocation(player);
        }
        lastRecordedMovement.put(player, System.currentTimeMillis());                                                   // THE UPDATE LINE. This updates the lastRecordedMovement hashmap to store the new currentTimeMillis from the system when the player moves.
                                                                                                                        // The update is necessary for the wasAFK variable to evaluate to either true or false.
    }

    public void startIdleCheckTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long currentTime = System.currentTimeMillis();

            for (Player player : Bukkit.getOnlinePlayers()) {
                long lastMoveTime = lastRecordedMovement.get(player);

                if (currentTime - lastMoveTime >= defaultAfkTimer) {
                    isPlayerAFK.put(player, true);
                    player.sendMessage("The player has been afk for " +(System.currentTimeMillis() - lastMoveTime) / 1000L +" seconds.");
                    System.out.println("The player has been afk for " +(System.currentTimeMillis() - lastMoveTime) / 1000L +" seconds.");
                }
                teleportToSpawn(player, isPlayerAFK.get(player));
            }
        }, 0L, 20);                                                                                                // real-time checking of afk status in 1sec interval.
    }

    private void teleportToSpawn(Player player, Boolean isAFK) {
        long currentTime = System.currentTimeMillis();
        world = player.getWorld();

        if(playerCommands.getPlayerSpawn(player) != null){
            world.setSpawnLocation(playerCommands.getPlayerSpawn(player));
        }
        else {
            world.setSpawnLocation(playerEvents.playerJoinLocation.get(player));
        }

        Location playerLocation = player.getLocation();
        Location spawnLocation = world.getSpawnLocation();

        if (isAFK && !playerLocation.equals(spawnLocation)){
            player.teleport(spawnLocation);
            lastRecordedMovement.put(player, System.currentTimeMillis()); //note of removal.
            player.sendMessage(ChatColor.YELLOW + "You have been automatically teleported to spawn due to inactivity.");
            player.sendMessage("You are now AFK!");
        }
        else if (isAFK && currentTime - lastRewardReleasedTime.get(player) >= defaultAfkTimer) {
            player.getInventory().addItem(new ItemStack(Material.DIAMOND));
            player.sendMessage("You received a reward!");
            lastRewardReleasedTime.put(player, currentTime);
        }
    }
    private static void teleportToLastLocation(Player player){
        World world = player.getWorld();
        world.setSpawnLocation(playerEvents.playerLastLocation.get(player));
        Location spawnLocation = world.getSpawnLocation();

        player.teleport(spawnLocation);
        player.sendMessage(ChatColor.YELLOW + "Returning to last recorded position...");
    }
}

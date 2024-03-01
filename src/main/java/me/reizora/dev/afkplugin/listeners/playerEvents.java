package me.reizora.dev.afkplugin.listeners;

import me.reizora.dev.afkplugin.afkMonitor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.HashMap;

public class playerEvents implements Listener {
    public static final HashMap<Player, Location> playerJoinLocation = new HashMap<>();
    public static final HashMap<Player, Location> playerLastLocation = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerJoinLocation.put(player, player.getLocation());

        afkMonitor.playerJoined(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        afkMonitor.playerLeft(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        afkMonitor.playerMoved(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        playerLastLocation.put(player, event.getFrom());
    }
}
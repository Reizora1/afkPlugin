package me.reizora.dev.afkplugin;

import me.reizora.dev.afkplugin.commands.playerCommands;
import me.reizora.dev.afkplugin.listeners.playerEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class AfkPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        playerEvents player = new playerEvents();
        afkMonitor afk = new afkMonitor(this);

        getServer().getPluginManager().registerEvents(player, this);
        getCommand("setspawn").setExecutor(new playerCommands());

        afk.startIdleCheckTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

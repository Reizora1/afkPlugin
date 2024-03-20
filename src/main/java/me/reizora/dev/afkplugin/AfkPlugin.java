package me.reizora.dev.afkplugin;

import me.reizora.dev.afkplugin.commands.playerCommands;
import me.reizora.dev.afkplugin.listeners.playerEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class AfkPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("PLUGIN HAS STARTED!!!!!!!!!!!!!");
        playerEvents player = new playerEvents();
        afkMonitor afk = new afkMonitor(this);

        getServer().getPluginManager().registerEvents(player, this);
        getCommand("setspawn").setExecutor(new playerCommands());
        getCommand("setAFKTimer").setExecutor(new playerCommands());
        getCommand("getAFKTimer").setExecutor(new playerCommands());

        afk.startIdleCheckTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

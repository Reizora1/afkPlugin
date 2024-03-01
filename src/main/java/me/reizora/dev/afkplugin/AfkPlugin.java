package me.reizora.dev.afkplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class AfkPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        afkMonitor afk = new afkMonitor(this);
        afk.startIdleCheckTask();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package com.edgeburnmedia.sussy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Sussy extends JavaPlugin {
    public FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        this.getCommand("sussy").setExecutor(new CommandSussy(this));
        config.addDefault("showEjectorName", true);
        config.addDefault("broadcastEjectToEveryone", true);
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean showEjectorName() {
        return config.getBoolean("showEjectorName");
    }
    public boolean broadcastEjectToEveryone() { return config.getBoolean("broadcastEjectToEveryone"); }

}

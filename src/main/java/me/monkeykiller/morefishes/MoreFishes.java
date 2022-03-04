package me.monkeykiller.morefishes;

import me.monkeykiller.morefishes.commands.AppraiseCommand;
import me.monkeykiller.morefishes.commands.MoreFishesCommand;
import me.monkeykiller.morefishes.listeners.Events;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MoreFishes extends JavaPlugin {
    private static final String prefix = "&aMoreFishes &8>&7 ";
    private static JavaPlugin plugin;
    private static FileConfiguration config;

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    protected static FileConfiguration getFileConfig() {
        return config;
    }

    public static void loadConfig() {
        Quality.loadFromConfig();
        CustomFish.loadFromConfig();
    }

    public static void registerCommands() {
        MoreFishesCommand.INSTANCE.register();
        AppraiseCommand.INSTANCE.register();
    }

    @Override
    public void onEnable() {
        plugin = this;
        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
        registerCommands();
        loadConfig();
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Utils.log(prefix + "Plugin enabled.");
        Utils.log("&aLoaded", CustomFish.getRegistry().size() + "", "Custom Fishes");
    }

    @Override
    public void onDisable() {
        Utils.log(prefix + "Plugin disabled.");
    }
}

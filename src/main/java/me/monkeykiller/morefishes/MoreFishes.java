package me.monkeykiller.morefishes;

import me.monkeykiller.morefishes.commands.AppraiseCommand;
import me.monkeykiller.morefishes.commands.FishRankCommand;
import me.monkeykiller.morefishes.commands.MoreFishesCommand;
import me.monkeykiller.morefishes.gui.CustomHolder;
import me.monkeykiller.morefishes.listeners.Events;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MoreFishes extends JavaPlugin {
    private static final String prefix = "&aMoreFishes &8>&7 ";
    private static JavaPlugin plugin;
    private static FileConfiguration config;
    private static ConfigFile heaviestFishes;

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static FileConfiguration getFileConfig() {
        return config;
    }

    public static ConfigFile getHeaviestFishes() {
        return heaviestFishes;
    }

    public static void loadConfig() {
        Quality.loadFromConfig();
        CustomFish.loadFromConfig();
    }

    public static void registerCommands() {
        MoreFishesCommand.INSTANCE.register();
        AppraiseCommand.INSTANCE.register();
        FishRankCommand.INSTANCE.register();
    }

    @Override
    public void onEnable() {
        plugin = this;
        config = getConfig();
        config.options().copyDefaults(true);
        heaviestFishes = new ConfigFile(getDataFolder(), "heaviest_fishes.yml", true);
        saveDefaultConfig();
        registerCommands();
        loadConfig();
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Utils.log(prefix + "Plugin enabled.");
        Utils.log("&aLoaded", CustomFish.getRegistry().size() + "", "Custom Fishes");
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof CustomHolder holder && holder.getId().equals("fish_rank"))
                player.closeInventory();
        Utils.log(prefix + "Plugin disabled.");
    }
}

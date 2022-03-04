package me.monkeykiller.morefishes.commands;

import me.monkeykiller.morefishes.MoreFishes;
import org.apache.commons.lang.Validate;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCommand implements TabExecutor {
    public final String command;

    public BaseCommand(@NotNull String command) {
        this.command = command;
    }

    public void register() {
        PluginCommand cmd = MoreFishes.getPlugin().getCommand(command);
        Validate.notNull(cmd, "Command " + command + " not found");
        cmd.setTabCompleter(this);
        cmd.setExecutor(this);
    }
}

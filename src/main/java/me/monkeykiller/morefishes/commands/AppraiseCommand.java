package me.monkeykiller.morefishes.commands;

import me.monkeykiller.morefishes.Utils;
import me.monkeykiller.morefishes.gui.AppraiseFishGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AppraiseCommand extends BaseCommand {
    public static final AppraiseCommand INSTANCE = new AppraiseCommand();

    public AppraiseCommand() {
        super("appraise");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("morefishes.command.appraise")) {
            sender.sendMessage(Utils.colorize("&cYou don't have permission to do this"));
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Utils.colorize("&cYou are not a player!"));
            return true;
        }
        AppraiseFishGUI.open(player);
        sender.sendMessage(Utils.colorize("&aOpening apraising gui"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}

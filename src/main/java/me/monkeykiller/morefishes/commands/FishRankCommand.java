package me.monkeykiller.morefishes.commands;

import me.monkeykiller.morefishes.Utils;
import me.monkeykiller.morefishes.gui.FishRankGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FishRankCommand extends BaseCommand {
    public static final FishRankCommand INSTANCE = new FishRankCommand();

    public FishRankCommand() {
        super("fishrank");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Utils.colorize("&cYou're not a player"));
            return true;
        }
        FishRankGUI.openPage(player);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}

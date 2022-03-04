package me.monkeykiller.morefishes.commands;

import me.monkeykiller.morefishes.CustomFish;
import me.monkeykiller.morefishes.ItemUtils;
import me.monkeykiller.morefishes.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
        PlayerInventory inv = player.getInventory();
        ItemStack item = null, mainHand = inv.getItemInMainHand(), offHand = inv.getItemInOffHand();
        if (!ItemUtils.isAirOrNull(mainHand) && CustomFish.getFishByItem(mainHand) != null)
            item = mainHand;
        else if (!ItemUtils.isAirOrNull(offHand) && CustomFish.getFishByItem(offHand) != null)
            item = offHand;
        if (ItemUtils.isAirOrNull(item)) {
            sender.sendMessage(Utils.colorize("&cYou don't have any custom fish in your hands"));
            return true;
        }
        CustomFish fish = CustomFish.getFishByItem(item);
        assert fish != null;
        if (ItemUtils.getFishQuality(item) != null) {
            sender.sendMessage(Utils.colorize("&cThis fish was already evaluated"));
            return true;
        }
        if (item.getAmount() > 1) {
            sender.sendMessage(Utils.colorize("&cYou have more than one fish in the stack"));
            return true;
        }
        ItemUtils.setFishQuality(item, fish.getRandomQuality().getId());
        fish.updateLore(item);
        sender.sendMessage(Utils.colorize("&aFish appraised successfully"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}

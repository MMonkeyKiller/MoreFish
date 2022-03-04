package me.monkeykiller.morefishes.commands;

import me.monkeykiller.morefishes.CustomFish;
import me.monkeykiller.morefishes.ItemUtils;
import me.monkeykiller.morefishes.MoreFishesConfig;
import me.monkeykiller.morefishes.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoreFishesCommand extends BaseCommand {
    public static final BaseCommand INSTANCE = new MoreFishesCommand();

    public MoreFishesCommand() {
        super("morefishes");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return false;
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("morefishes.command.give")) {
                sender.sendMessage(Utils.colorize("&cYou don't have permission to do this"));
                return true;
            }
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Utils.colorize("&cYou are not a player!"));
                return true;
            }
            if (args.length < 2) return false;
            CustomFish fish = CustomFish.getFishById(args[1]);
            if (fish == null) {
                sender.sendMessage(Utils.colorize("&cFish with id ", args[1], " not found."));
                return true;
            }
            int count = 1;
            Integer weight = null;
            if (args.length >= 3) try {
                count = Integer.parseUnsignedInt(args[2]);

                if (args.length >= 4) {
                    weight = Integer.parseUnsignedInt(args[3]);
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(Utils.colorize("&cError: " + e.getMessage()));
                return true;
            }

            for (int i = 0; i < count; i++) {
                ItemStack item = fish.createItem(weight == null ? fish.getRandomWeight() : weight);
                ItemUtils.addItem(player, item);
            }

            Component[] with = new Component[]{
                    Component.text(count),
                    Utils.format(fish.getDisplayName()),
                    player.displayName().hoverEvent(player)
            };
            sender.sendMessage(Component.translatable("commands.give.success.single", with));
            return true;
        } else if (args[0].equalsIgnoreCase("appraise")) {
            return AppraiseCommand.INSTANCE.onCommand(sender, command, label, args);
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("morefishes.command.reload")) {
                sender.sendMessage(Utils.colorize("&cYou don't have permission to do this"));
                return true;
            }
            MoreFishesConfig.reload();
            sender.sendMessage(Utils.colorize("&aConfig reloaded successfully"));
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) return List.of("give", "appraise", "reload");
        if (args.length == 2 && args[0].equalsIgnoreCase("give"))
            return CustomFish.getRegistry().stream().map(CustomFish::getId).toList();
        return List.of();
    }
}

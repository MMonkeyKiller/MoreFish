package me.monkeykiller.morefishes.gui;

import me.monkeykiller.morefishes.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppraiseFishGUI extends CustomHolder {
    public AppraiseFishGUI() {
        super("appraise_fish");
    }

    public static void open(@NotNull Player player) {
        player.openInventory(new AppraiseFishGUI().getInventory());
    }

    public static ItemStack getAppraiseBtn(@NotNull Inventory inv) {
        ItemStack fish = inv.getItem(13), item;
        boolean noItem = ItemUtils.isAirOrNull(fish),
                noFish = noItem || CustomFish.getFishByItem(fish) == null,
                alreadyAppraised = noItem || ItemUtils.getFishQuality(fish) != null;
        if (noItem || noFish || alreadyAppraised) {
            item = new ItemStack(Material.BARRIER);
            item.editMeta(meta -> {
                meta.displayName(Utils.format("&cNo fish to appraise").decoration(TextDecoration.ITALIC, false));
                List<String> lore = noItem ? List.of(
                        "&7No item to appraise.",
                        "&7Please put an item."
                ) : noFish ? List.of(
                        "&7The item is not a fish.",
                        "&7Unable to appraise"
                ) : List.of(
                        "&7The fish is already appraised.",
                        "&7Unable to appraise"
                );
                meta.lore(lore.stream().map(l -> Utils.format(l).decoration(TextDecoration.ITALIC, false).asComponent()).toList());
            });
        } else {
            item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            item.editMeta(meta -> {
                meta.displayName(Utils.format("&aAppraise Fish").decoration(TextDecoration.ITALIC, false));
                meta.lore(List.of(Utils.format("&7Click to appraise the fish").decoration(TextDecoration.ITALIC, false).asComponent()));
            });
        }
        return item;
    }

    public static ItemStack appraiseFish(@NotNull ItemStack item) {
        CustomFish fish = CustomFish.getFishByItem(item);
        if (fish == null) return item;
        ItemUtils.setFishQuality(item, fish.getRandomQuality().getId());
        fish.updateLore(item);
        return item;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 9 * 3, Utils.format(MoreFishesConfig.getAppraiseFishTitle()));
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        item.editMeta(meta -> meta.displayName(Component.text(" ")));
        List.of(3, 4, 5, 12, 14, 21, 23).forEach(i -> inv.setItem(i, item));
        inv.setItem(22, getAppraiseBtn(inv));
        return inv;
    }
}

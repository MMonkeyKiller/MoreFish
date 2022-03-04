package me.monkeykiller.morefishes;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.persistence.PersistentDataType.INTEGER;
import static org.bukkit.persistence.PersistentDataType.STRING;

public class ItemUtils {
    private static final NamespacedKey
            FISH_ID = Utils.key("fish_id"),
            FISH_WEIGHT = Utils.key("fish_weight"),
            FISH_QUAlITY = Utils.key("fish_quality");

    public static ItemStack setFishId(@NotNull ItemStack item, @NotNull String id) {
        item.editMeta(meta -> meta.getPersistentDataContainer().set(FISH_ID, STRING, id));
        return item;
    }

    public static String getFishId(@NotNull ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(FISH_ID, STRING);
    }

    public static ItemStack setFishWeight(@NotNull ItemStack item, int weight) {
        item.editMeta(meta -> meta.getPersistentDataContainer().set(FISH_WEIGHT, INTEGER, weight));
        return item;
    }

    public static Integer getFishWeight(@NotNull ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(FISH_WEIGHT, INTEGER);
    }

    public static ItemStack setFishQuality(@NotNull ItemStack item, @NotNull String qualityId) {
        item.editMeta(meta -> meta.getPersistentDataContainer().set(FISH_QUAlITY, STRING, qualityId));
        return item;
    }

    public static String getFishQuality(@NotNull ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(FISH_QUAlITY, STRING);
    }

    public static boolean isAirOrNull(ItemStack item) {
        return item == null || item.getType().isAir();
    }

    public static void addItem(@NotNull Player player, @NotNull ItemStack... items) {
        player.getInventory().addItem(items).values().forEach(item ->
                player.getWorld().dropItem(player.getLocation(), item));
    }
}

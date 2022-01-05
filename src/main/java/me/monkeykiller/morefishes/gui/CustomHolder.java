package me.monkeykiller.morefishes.gui;

import me.monkeykiller.morefishes.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class CustomHolder implements InventoryHolder {
    private final String id;
    public CustomHolder(@NotNull String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

    public static boolean is(@Nullable InventoryHolder holder, @Nullable String id) {
        return holder instanceof CustomHolder cholder && (id == null || id.equals(cholder.getId()));
    }

    public static void dropInput(@NotNull Inventory inv) {
        if (!is(inv.getHolder(), "appraise_fish")) return;
        ItemStack item = inv.getItem(13);
        if (!ItemUtils.isAirOrNull(item)) ItemUtils.addItem((Player) inv.getViewers().get(0), item);
    }
}

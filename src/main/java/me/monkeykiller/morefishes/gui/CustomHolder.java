package me.monkeykiller.morefishes.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

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
}

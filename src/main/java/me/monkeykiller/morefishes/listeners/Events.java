package me.monkeykiller.morefishes.listeners;

import me.monkeykiller.morefishes.*;
import me.monkeykiller.morefishes.gui.AppraiseFishGUI;
import me.monkeykiller.morefishes.gui.CustomHolder;
import me.monkeykiller.morefishes.gui.FishRankGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Events implements Listener {
    private static final List<Material> FISH_TYPES = List.of(Material.COD,
            Material.SALMON, Material.PUFFERFISH, Material.TROPICAL_FISH);

    @EventHandler
    private void onFish(PlayerFishEvent event) {
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) && event.getCaught() instanceof Item entityItem) {
            // Checking if the item to replace is a fish (configurable)
            if (MoreFishesConfig.getAppendToVanillaFishLoot() && !FISH_TYPES.contains(entityItem.getItemStack().getType()))
                return;
            // Chance to obtain a custom fish (configurable)
            if (MoreFishesConfig.getCustomFishLootWeight() > Math.random()) {
                List<CustomFish> fishes = CustomFish.getRegistry(), lootFishes = new ArrayList<>();
                if (fishes.size() == 0) return;
                // Adding the fishes on another list x times based on their loot weight
                for (CustomFish fish : fishes)
                    for (int i = 0; i < fish.getLootWeight(); i++) lootFishes.add(fish);

                // Getting a random fish based on probabilities
                CustomFish fish = lootFishes.get(new Random().nextInt(lootFishes.size()));
                int weight = fish.getRandomWeight();
                entityItem.setItemStack(fish.createItem(weight));

                FishStorage.Fish saved = FishStorage.get(event.getPlayer());
                if (saved == null || saved.getWeight() < weight)
                    FishStorage.set(new FishStorage.Fish(event.getPlayer().getUniqueId(), fish.getId(), weight));
            }
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!CustomHolder.is(holder, null))
            return;
        event.setCancelled(true);
        Player player = (Player) event.getView().getPlayer();
        if (CustomHolder.is(holder, "fish_rank")) {
            switch (event.getSlot()) {
                case 0 -> FishRankGUI.openPage(player, false);
                case 8 -> FishRankGUI.openPage(player, true);
            }
        } else if (CustomHolder.is(holder, "appraise_fish")) {
            Inventory inv = event.getView().getTopInventory();
            ItemStack item = inv.getItem(13), clicked = event.getCurrentItem();
            /* Handling input */
            if (event.getClickedInventory() != inv) {
                if (event.getClick().isShiftClick()) {
                    if (ItemUtils.isAirOrNull(item) && !ItemUtils.isAirOrNull(clicked)) {
                        inv.setItem(13, clicked.asOne());
                        clicked.subtract();
                    }
                } else event.setCancelled(false);
            } else if (event.getSlot() == 13) {
                if (event.isShiftClick() || ItemUtils.isAirOrNull(event.getCursor()))
                    event.setCancelled(false);
                else if (!ItemUtils.isAirOrNull(clicked = event.getCursor())) {
                    if (ItemUtils.isAirOrNull(item)) {
                        inv.setItem(13, clicked.asOne());
                        clicked.subtract();
                    } else if (clicked.getAmount() == 1) {
                        event.getWhoClicked().setItemOnCursor(item);
                        inv.setItem(13, clicked.asOne());
                    }
                }
                /* Handling appraise button */
            } else if (event.getSlot() == 22 && !ItemUtils.isAirOrNull(item) && CustomFish.getFishByItem(item) != null && ItemUtils.getFishQuality(item) == null) {
                inv.setItem(13, AppraiseFishGUI.appraiseFish(item));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
            Bukkit.getScheduler().runTaskLater(MoreFishes.getPlugin(), () -> inv.setItem(22, AppraiseFishGUI.getAppraiseBtn(inv)), 1);
        }
    }

    @EventHandler
    private void onInventoryDrag(InventoryDragEvent event) {
        if (CustomHolder.is(event.getInventory().getHolder(), null))
            event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        CustomHolder.dropInput(event.getView().getTopInventory());
    }
}

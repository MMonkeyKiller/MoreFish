package me.monkeykiller.morefishes.listeners;

import me.monkeykiller.morefishes.CustomFish;
import me.monkeykiller.morefishes.FishStorage;
import me.monkeykiller.morefishes.MoreFishesConfig;
import me.monkeykiller.morefishes.gui.CustomHolder;
import me.monkeykiller.morefishes.gui.FishRankGUI;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        if (!(event.getInventory().getHolder() instanceof CustomHolder holder && Objects.equals(holder.getId(), "fish_rank")))
            return;
        event.setCancelled(true);
        Player player = (Player) event.getView().getPlayer();
        switch (event.getSlot()) {
            case 0 -> FishRankGUI.openPage(player, false);
            case 8 -> FishRankGUI.openPage(player, true);
        }
    }
}

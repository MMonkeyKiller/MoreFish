package me.monkeykiller.morefishes.listeners;

import me.monkeykiller.morefishes.CustomFish;
import me.monkeykiller.morefishes.MoreFishesConfig;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Events implements Listener {
    private static final List<Material> FISH_TYPES = List.of(Material.COD,
            Material.SALMON, Material.PUFFERFISH, Material.TROPICAL_FISH);

    @EventHandler
    private void onFish(PlayerFishEvent event) {
        if (event.getCaught() instanceof Item entityItem) {
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
                entityItem.setItemStack(fish.createItem());
            }
        }
    }
}

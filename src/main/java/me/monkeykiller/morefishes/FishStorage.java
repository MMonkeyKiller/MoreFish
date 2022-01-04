package me.monkeykiller.morefishes;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FishStorage {
    public static Fish get(@NotNull Player player) {
        return get(player.getUniqueId());
    }

    public static Fish get(@NotNull UUID uuid) {
        MemorySection deserialize = (MemorySection) getStorage().get(uuid.toString());
        if (deserialize == null) return null;
        String id = deserialize.getString("id");
        int weight = deserialize.getInt("weight");
        Validate.notNull(id, "Error parsing " + uuid + ".id in heaviest_fishes.yml. No id found");
        return new Fish(uuid, id, weight);
    }

    public static void set(@NotNull Fish fish) {
        String base = fish.getOwner().toString();
        getStorage().set(base + ".id", fish.getId());
        getStorage().set(base + ".weight", fish.getWeight());
        MoreFishes.getHeaviestFishes().save();
    }

    public static Fish getHeaviestOfTheServer() {
        List<Fish> results = getAll();
        return results.size() > 0 ? results.get(results.size() - 1) : null;
    }

    public static List<Fish> getAll() {
        return getStorage().getKeys(false).stream()
                .map(key -> get(UUID.fromString(key)))
                .sorted(Comparator.comparing(fish -> fish != null ? fish.getWeight() : 0))
                .toList();
    }

    private static FileConfiguration getStorage() {
        return MoreFishes.getHeaviestFishes().getConfig();
    }

    public static class Fish {
        private final UUID owner;
        private final String id;
        private final int weight;

        public Fish(@NotNull UUID owner, @NotNull String id, int weight) {
            this.owner = owner;
            this.id = id;
            this.weight = weight;
        }

        public UUID getOwner() {
            return owner;
        }

        public String getId() {
            return id;
        }

        public int getWeight() {
            return weight;
        }

        public ItemStack genItem() {
            return getFish() == null ? null : getFish().createItem(weight);
        }

        public CustomFish getFish() {
            return CustomFish.getFishById(id);
        }
    }
}

package me.monkeykiller.morefishes;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MoreFishesConfig {
    private static final FileConfiguration config = MoreFishes.getFileConfig();

    public static boolean getAppendToVanillaFishLoot() {
        return config.getBoolean("append_to_vanilla_fish_loot");
    }

    public static void setAppendToVanillaFishLoot(boolean bool) {
        config.set("append_to_vanilla_fish_loot", bool);
    }

    public static double getCustomFishLootWeight() {
        return config.getDouble("custom_fish_loot_weight");
    }

    public static void setCustomFishLootWeight(double lootWeight) {
        config.set("custom_fish_loot_weight", lootWeight);
    }

    public static String getWeightMeasure() {
        return config.getString("weight_measure", "oz");
    }

    public static void setWeightMeasure(@NotNull String measure) {
        config.set("weight_measure", measure);
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getSerializedCustomFishes() {
        return (List<Map<String, Object>>) config.getList("fishes");
    }

    public static List<CustomFish> getCustomFishes() {
        return getSerializedCustomFishes().stream().map(CustomFish::deserialize)
                .filter(Objects::nonNull).toList();
    }

    public static void reload() {
        MoreFishes.getPlugin().reloadConfig();
    }

}

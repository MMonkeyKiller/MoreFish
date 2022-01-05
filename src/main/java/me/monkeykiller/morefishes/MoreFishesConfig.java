package me.monkeykiller.morefishes;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MoreFishesConfig {
    private static final FileConfiguration config = MoreFishes.getFileConfig();

    public static boolean getAppendToVanillaFishLoot() {
        return config.getBoolean("append_to_vanilla_fish_loot");
    }

    public static double getCustomFishLootWeight() {
        return config.getDouble("custom_fish_loot_weight");
    }

    public static String getWeightMeasure() {
        return config.getString("weight_measure", "oz");
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getSerializedQualities() {
        return (List<Map<String, Object>>) config.getList("qualities");
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getSerializedCustomFishes() {
        return (List<Map<String, Object>>) config.getList("fishes");
    }

    public static List<CustomFish> getCustomFishes() {
        return getSerializedCustomFishes().stream().map(CustomFish::deserialize)
                .filter(Objects::nonNull).toList();
    }

    public static List<Quality> getQualities() {
        return getSerializedQualities().stream().map(Quality::deserialize).toList();
    }

    public static String getFishRankTitle() {
        return config.getString("gui_titles.fish_rank");
    }

    public static String getAppraiseFishTitle() {
        return config.getString("gui_titles.appraise_fish");
    }

    public static void reload() {
        MoreFishes.getPlugin().reloadConfig();
        MoreFishes.loadConfig();
    }

}

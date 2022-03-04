package me.monkeykiller.morefishes;

import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomFish {

    private static final List<CustomFish> REGISTRY = new ArrayList<>();

    private final String id;
    private final String displayName;
    private final List<String> lore;
    private final Integer modelData;
    private final Material material;
    private final int lootWeight;
    private final int minWeight, maxWeight;
    private final Map<String, Integer> customQualities;

    public CustomFish(@NotNull String id, @NotNull String displayName, @NotNull List<String> lore, Integer modelData, @NotNull Material material, int lootWeight, Map<String, Integer> customQualities, int minWeight, int maxWeight) {
        Validate.isTrue(getFishById(id) == null, String.format("Error registering custom fish: Fish with id %s already exists!", id));
        this.id = id;
        this.displayName = displayName;
        this.modelData = modelData;
        this.lore = lore;
        this.material = material;
        this.lootWeight = lootWeight;
        this.customQualities = customQualities;

        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    @SuppressWarnings("unchecked")
    public static CustomFish deserialize(@NotNull Map<String, Object> deserialize) {
        try {
            String id = (String) deserialize.get("id");
            String displayName = (String) deserialize.get("display_name");
            List<String> lore = deserialize.get("lore") instanceof String ? List.of((String) deserialize.get("lore")) : (List<String>) deserialize.get("lore");
            Integer modelData = (Integer) deserialize.getOrDefault("model_data", null);
            String materialName = ((String) deserialize.get("material")).toUpperCase();
            Material material = Material.matchMaterial(materialName);
            Validate.notNull(material, String.format("%s is not a valid material", deserialize.get("material")));

            int lootWeight = (int) deserialize.getOrDefault("loot_weight", 1);

            HashMap<String, Integer> fishWeight = (HashMap<String, Integer>) deserialize.getOrDefault("fish_weight", new HashMap<String, Integer>());
            Map<String, Integer> customQualities = null;
            if (deserialize.containsKey("qualities")) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) deserialize.get("qualities");
                customQualities = new HashMap<>();
                for (Map<String, Object> quality : list) {
                    if (Quality.getQualityById((String) quality.get("id")) == null) {
                        Utils.log("&c[" + id + "] Quality with id " + (String) quality.get("id") + " not found. Skipping it...");
                        continue;
                    }
                    customQualities.put((String) quality.get("id"), (Integer) quality.get("weight"));
                }
            }
            int minWeight = fishWeight.getOrDefault("min", 1);
            int maxWeight = fishWeight.getOrDefault("max", 10);

            Validate.isTrue(minWeight >= 0, "fish_weight.min can't be negative");
            Validate.isTrue(maxWeight >= 0, "fish_weight.max can't be negative");
            Validate.isTrue(minWeight <= maxWeight, "fish_weight.min can't be higher than fish_weight.max");

            return new CustomFish(id, displayName, lore, modelData, material, lootWeight, customQualities, minWeight, maxWeight);
        } catch (Exception e) {
            Utils.log("&cError while custom fish deserialization");
            e.printStackTrace();
        }
        return null;
    }

    public static void loadFromConfig() {
        REGISTRY.clear();
        REGISTRY.addAll(MoreFishesConfig.getCustomFishes());
    }

    public static List<CustomFish> getRegistry() {
        return Collections.unmodifiableList(REGISTRY);
    }

    public static CustomFish getFishByItem(@NotNull ItemStack item) {
        String fishId = ItemUtils.getFishId(item);
        if (fishId == null) return null;
        return getFishById(fishId);
    }

    public static CustomFish getFishById(@NotNull String id) {
        for (CustomFish fish : REGISTRY)
            if (fish.id.equalsIgnoreCase(id)) return fish;
        return null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Quality getRandomQuality() {
        Map<String, Integer> options = new HashMap<>();
        Quality.getRegistry().forEach(q -> options.put(q.getId(), q.getWeight()));
        if (customQualities != null) options.putAll(customQualities);
        List<String> lootQuality = new ArrayList<>();
        for (String id : options.keySet())
            for (int i = 0; i < options.get(id); i++)
                lootQuality.add(id);
        return Quality.getQualityById(lootQuality.get(new Random().nextInt(lootQuality.size())));
    }

    public int getRandomWeight() {
        return minWeight + new Random().nextInt(maxWeight - minWeight + 1);
    }

    public int getLootWeight() {
        return lootWeight;
    }

    public String getId() {
        return id;
    }

    public ItemStack createItem() {
        return createItem(getRandomWeight());
    }

    public ItemStack createItem(int weight) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(Utils.format(displayName).decoration(TextDecoration.ITALIC, false));
            meta.setCustomModelData(modelData);
        });

        ItemUtils.setFishWeight(item, weight);
        ItemUtils.setFishId(item, id);
        updateLore(item);
        return item;
    }

    public void updateLore(@NotNull ItemStack item) {
        item.editMeta(meta -> {
            Integer weight = ItemUtils.getFishWeight(item);
            if (weight == null) {
                weight = getRandomWeight();
                ItemUtils.setFishWeight(item, weight);
            }
            String weightMeasure = MoreFishesConfig.getWeightMeasure();

            List<String> rawLore = new ArrayList<>(this.lore);
            if (rawLore.size() > 0) rawLore.add("");
            String qualityId = ItemUtils.getFishQuality(item);
            if (qualityId != null) {
                Quality quality = Quality.getQualityById(qualityId);
                if (quality != null) rawLore.add(quality.getDisplay());
            }
            rawLore.add("&8Weight: &7" + weight + weightMeasure);

            meta.lore(rawLore.stream().map(line ->
                    Utils.format(line).decoration(TextDecoration.ITALIC, false).asComponent()).toList());
        });
    }

    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("id", id);
        serialized.put("display_name", displayName);
        serialized.put("lore", lore);
        if (modelData != null) serialized.put("model_data", modelData);
        serialized.put("material", material);
        serialized.put("loot_weight", lootWeight);
        return serialized;
    }
}

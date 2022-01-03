package me.monkeykiller.morefishes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomFish {

    private static final List<CustomFish> REGISTRY = new ArrayList<>();

    private final String id, displayName;
    private final List<String> lore;
    private final Material material;
    private final int lootWeight;

    private final int minWeight, maxWeight;

    public CustomFish(@NotNull String id, @NotNull String displayName, @NotNull List<String> lore, @NotNull Material material, int lootWeight, int minWeight, int maxWeight) {
        Validate.isTrue(getFishById(id) == null, String.format("Error registering custom fish: Fish with id %s already exists!", id));
        this.id = id;
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.lootWeight = lootWeight;

        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    @SuppressWarnings("unchecked")
    public static CustomFish deserialize(@NotNull Map<String, Object> deserialize) {
        try {
            String id = (String) deserialize.get("id");
            String displayName = (String) deserialize.get("display_name");
            List<String> lore = deserialize.get("lore") instanceof String ? List.of((String) deserialize.get("lore")) : (List<String>) deserialize.get("lore");
            String materialName = ((String) deserialize.get("material")).toUpperCase();
            Material material = Material.matchMaterial(materialName);
            Validate.notNull(material, String.format("%s is not a valid material", deserialize.get("material")));

            int lootWeight = (int) deserialize.getOrDefault("loot_weight", 1);

            HashMap<String, Integer> fishWeight = (HashMap<String, Integer>) deserialize.getOrDefault("fish_weight", new HashMap<String, Integer>());
            int minWeight = fishWeight.getOrDefault("min", 1);
            int maxWeight = fishWeight.getOrDefault("max", 10);

            Validate.isTrue(minWeight >= 0, "fish_weight.min can't be negative");
            Validate.isTrue(maxWeight >= 0, "fish_weight.max can't be negative");
            Validate.isTrue(minWeight <= maxWeight, "fish_weight.min can't be higher than fish_weight.max");

            return new CustomFish(id, displayName, lore, material, lootWeight, minWeight, maxWeight);
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

    public int getRandomWeight() {
        return minWeight + new Random().nextInt(maxWeight - minWeight + 1);
    }

    public int getLootWeight() {
        return lootWeight;
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(material);
        int weight = getRandomWeight();
        item.editMeta(meta -> {
            meta.displayName(Utils.format(displayName).decoration(TextDecoration.ITALIC, false));
            List<Component> componentLore = new ArrayList<>(lore.stream().map(line -> Utils.format(line).decoration(TextDecoration.ITALIC, false).asComponent()).toList());
            componentLore.add(Utils.format("&7Weight: " + weight, MoreFishesConfig.getWeightMeasure()).decoration(TextDecoration.ITALIC, false));
            meta.lore(componentLore);
        });
        ItemUtils.setFishWeight(item, weight);
        return ItemUtils.setFishId(item, id);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("id", id);
        serialized.put("display_name", displayName);
        serialized.put("lore", lore);
        serialized.put("material", material);
        serialized.put("loot_weight", lootWeight);
        return serialized;
    }
}

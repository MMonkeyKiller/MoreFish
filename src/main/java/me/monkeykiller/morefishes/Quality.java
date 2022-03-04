package me.monkeykiller.morefishes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Quality {

    private static final List<Quality> REGISTRY = new ArrayList<>();

    private String id, display;
    private int weight;

    public Quality(@NotNull String id, @NotNull String display, int weight) {
        this.id = id;
        this.display = display;
        this.weight = weight;
    }

    public static void loadFromConfig() {
        REGISTRY.clear();
        REGISTRY.addAll(MoreFishesConfig.getQualities());
    }

    public static List<Quality> getRegistry() {
        return Collections.unmodifiableList(REGISTRY);
    }

    public static Quality getQualityById(@NotNull String id) {
        for (Quality quality : REGISTRY)
            if (quality.getId().equalsIgnoreCase(id)) return quality;
        return null;
    }

    public static Quality deserialize(@NotNull Map<String, Object> deserialize) {
        String id = (String) deserialize.get("id");
        String display = (String) deserialize.get("display");
        int weight = (int) deserialize.getOrDefault("weight", 1);

        return new Quality(id, display, weight);
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

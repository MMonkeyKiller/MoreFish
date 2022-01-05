package me.monkeykiller.morefishes.gui;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.monkeykiller.morefishes.FishStorage;
import me.monkeykiller.morefishes.MoreFishesConfig;
import me.monkeykiller.morefishes.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FishRankGUI {
    private static final int FISHES_PER_PAGE = 9 * 5;
    private static final Map<UUID, Integer> pages = new HashMap<>();

    public static int getTotalPages() {
        int fishSize = FishStorage.getAll().size();
        int pages = fishSize / FISHES_PER_PAGE;
        if (fishSize % FISHES_PER_PAGE != 0) pages++;
        return pages;
    }

    public static void openPage(@NotNull Player player, boolean next) {
        pages.put(player.getUniqueId(), pages.getOrDefault(player.getUniqueId(), 0) + (next ? 1 : -1));
        openPage(player);
    }

    public static void openPage(@NotNull Player player) {
        openPage(player, pages.getOrDefault(player.getUniqueId(), 0));
    }

    public static void openPage(@NotNull Player player, int page) {
        page = Math.max(0, Math.min(getTotalPages() - 1, page));
        List<FishStorage.Fish> fishes = FishStorage.getAll();
        fishes = fishes.subList(page, Math.min(page + FISHES_PER_PAGE, fishes.size()));

        Inventory inv = Bukkit.createInventory(new CustomHolder("fish_rank"), 9 * 6, Utils.format(MoreFishesConfig.getFishRankTitle() + " - Page " + (page + 1) + "/" + getTotalPages()));
        for (int i = 0; i < 9; i++) {
            ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            item.editMeta(meta -> meta.displayName(Component.text(" ").asComponent()));
            inv.setItem(i, item);
        }
        inv.setItem(0, getBackBtn());
        inv.setItem(8, getNextBtn());

        ItemStack heaviestOfServer;
        FishStorage.Fish serverFish = FishStorage.getHeaviestOfTheServer();
        if (serverFish != null) {
            heaviestOfServer = serverFish.genItem();
            List<Component> lore = heaviestOfServer.lore();
            if (lore != null) lore.addAll(genInfo(serverFish));
            heaviestOfServer.lore(lore);
        } else {
            heaviestOfServer = new ItemStack(Material.BARRIER);
            heaviestOfServer.editMeta(meta ->
                    meta.displayName(Utils.format("&cNo fish found").decoration(TextDecoration.ITALIC, false)));
        }

        inv.setItem(4, heaviestOfServer);

        for (int i = 0; i < fishes.size(); i++) {
            FishStorage.Fish fish = fishes.get(i);
            ItemStack item = fish.genItem();

            List<Component> lore = item.lore();
            if (lore != null) lore.addAll(genInfo(fish));
            item.lore(lore);
            inv.setItem(9 + i, item);
        }
        player.openInventory(inv);
    }

    private static List<Component> genInfo(@NotNull FishStorage.Fish fish) {
        Player playerOwner = Bukkit.getPlayer(fish.getOwner());
        Component owner = playerOwner != null ? playerOwner.displayName().color(NamedTextColor.GRAY) :
                Utils.format("&7unknown").decoration(TextDecoration.ITALIC, false);
        FishStorage.Fish heaviestOfServer = FishStorage.getHeaviestOfTheServer();
        List<Component> info = new ArrayList<>(List.of(
                Component.text().asComponent(),
                Utils.format("&aCaught by: ").append(owner)
        ));
        if (heaviestOfServer != null && fish.getOwner().equals(heaviestOfServer.getOwner()))
            info.add(Utils.format("&6★ Top #1 of the server"));
        return info.stream().map(c -> c.decoration(TextDecoration.ITALIC, false)).toList();
    }

    private static ItemStack getBackBtn() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        item.editMeta(meta -> {
            if (!(meta instanceof SkullMeta skullMeta)) return;
            PlayerProfile profile = Bukkit.createProfile(UUID.fromString("e8627b92-1dcb-4733-810c-a2b47833c451"), "back");
            profile.setProperty(new ProfileProperty("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1MmUyYjkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OTc3MzRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0="));
            skullMeta.setPlayerProfile(profile);

            meta.displayName(Utils.format("&aBack").decoration(TextDecoration.ITALIC, false));
        });
        return item;
    }

    private static ItemStack getNextBtn() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        item.editMeta(meta -> {
            if (!(meta instanceof SkullMeta skullMeta)) return;
            PlayerProfile profile = Bukkit.createProfile(UUID.fromString("74d11297-0fd9-4d43-91d9-8e5216a63efa"), "next");
            profile.setProperty(new ProfileProperty("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjhmNjgxZGFhZDhiZjQzNmNhZThkYTNmZTgxMzFmNjJhMTYyYWI4MWFmNjM5YzNlMDY0NGFhNmFiYWMyZiJ9fX0="));
            skullMeta.setPlayerProfile(profile);

            meta.displayName(Utils.format("&aNext").decoration(TextDecoration.ITALIC, false));
        });
        return item;
    }
}

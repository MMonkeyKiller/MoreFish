package me.monkeykiller.morefishes;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static void log(@NotNull String... text) {
        Bukkit.getLogger().log(Level.INFO, colorize(text));
    }

    public static NamespacedKey key(@NotNull String key) {
        return new NamespacedKey(MoreFishes.getPlugin(), key);
    }

    public static String colorize(@NotNull String... text) {
        String input = String.join(" ", text);
        Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String color = input.substring(matcher.start(), matcher.end());
            input = input.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
        }

        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static TextComponent format(@NotNull String... text) {
        return toComponent(colorize(text));
    }

    public static TextComponent toComponent(@NotNull String... text) {
        return LegacyComponentSerializer.legacySection().deserialize(String.join(" ", text));
    }
}

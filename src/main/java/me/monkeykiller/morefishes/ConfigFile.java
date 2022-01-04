package me.monkeykiller.morefishes;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SuppressWarnings("unused")
public class ConfigFile {

    private final File folder;
    private final String file;
    private FileConfiguration config;
    private File configFile;

    public ConfigFile(@NotNull String file) {
        this(MoreFishes.getPlugin().getDataFolder(), file);
    }

    public ConfigFile(@NotNull File folder, @NotNull String file) {
        this(folder, file, false);
    }

    public ConfigFile(@NotNull File folder, @NotNull String file, boolean copyDefaults) {
        this.folder = folder;
        this.file = file;
        if (copyDefaults) this.register();
    }

    public FileConfiguration getConfig() {
        if (config == null) reload();
        return config;
    }

    public void reload() {
        if (config == null) configFile = new File(folder, file);
        config = YamlConfiguration.loadConfiguration(configFile);
        assert MoreFishes.getPlugin().getResource(file) != null;
        Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(MoreFishes.getPlugin().getResource(file)), StandardCharsets.UTF_8);
        config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
    }

    public void save() {
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        configFile = new File(folder, file);
        if (!configFile.exists()) {
            getConfig().options().copyDefaults(true);
            save();
        }
    }
}

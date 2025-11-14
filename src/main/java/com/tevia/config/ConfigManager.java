package com.tevia.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages loading and saving of configuration files.
 */
public class ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE_NAME = "tevia-config.json";

    private final Path configPath;
    private final Gson gson;
    private TeviaConfig config;

    public ConfigManager() {
        this.configPath = FabricLoader.getInstance()
                .getConfigDir()
                .resolve(CONFIG_FILE_NAME);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Load configuration from file, or create default if not exists.
     */
    public void loadConfig() {
        try {
            if (Files.exists(configPath)) {
                String json = Files.readString(configPath);
                config = gson.fromJson(json, TeviaConfig.class);
                LOGGER.info("Configuration loaded from {}", configPath);
            } else {
                config = new TeviaConfig();
                saveConfig();
                LOGGER.info("Default configuration created at {}", configPath);
            }

            // Validate configuration
            config.validate();

        } catch (IOException e) {
            LOGGER.error("Failed to load configuration", e);
            config = new TeviaConfig();
        } catch (IllegalStateException e) {
            LOGGER.error("Invalid configuration: {}", e.getMessage());
            config = new TeviaConfig();
        }
    }

    /**
     * Save current configuration to file.
     */
    public void saveConfig() {
        try {
            String json = gson.toJson(config);
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, json);
            LOGGER.info("Configuration saved to {}", configPath);
        } catch (IOException e) {
            LOGGER.error("Failed to save configuration", e);
        }
    }

    /**
     * Reload configuration from file.
     */
    public void reloadConfig() {
        loadConfig();
    }

    /**
     * Get the current configuration.
     */
    public TeviaConfig getConfig() {
        return config;
    }

    /**
     * Update configuration and save to file.
     */
    public void updateConfig(TeviaConfig newConfig) {
        this.config = newConfig;
        saveConfig();
    }

    /**
     * Get the configuration file path.
     */
    public Path getConfigPath() {
        return configPath;
    }
}

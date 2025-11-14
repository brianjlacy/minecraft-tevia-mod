package com.tevia;

import com.tevia.commands.CommandRegistry;
import com.tevia.config.ConfigManager;
import com.tevia.llm.LLMCoordinator;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for the Tevia LLM Automation Mod.
 * This mod enables Large Language Models to fully automate a Minecraft player character.
 */
public class TeviaAutomationMod implements ClientModInitializer {

    public static final String MOD_ID = "tevia";
    public static final String MOD_NAME = "Tevia - LLM Automation Mod";
    public static final String VERSION = "1.0.0";

    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    private static TeviaAutomationMod instance;
    private ConfigManager configManager;
    private LLMCoordinator llmCoordinator;
    private boolean isRunning = false;

    @Override
    public void onInitializeClient() {
        instance = this;

        LOGGER.info("Initializing {} v{}", MOD_NAME, VERSION);

        try {
            // Initialize configuration
            configManager = new ConfigManager();
            configManager.loadConfig();
            LOGGER.info("Configuration loaded successfully");

            // Initialize LLM coordinator
            llmCoordinator = new LLMCoordinator(configManager);
            LOGGER.info("LLM Coordinator initialized");

            // Register commands
            CommandRegistry.registerCommands();
            LOGGER.info("Commands registered");

            // Register tick event for main decision loop
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (isRunning && client.player != null) {
                    llmCoordinator.tick(client);
                }
            });

            LOGGER.info("{} initialized successfully", MOD_NAME);

        } catch (Exception e) {
            LOGGER.error("Failed to initialize {}", MOD_NAME, e);
        }
    }

    /**
     * Start the LLM automation.
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            llmCoordinator.start();
            LOGGER.info("LLM automation started");
        }
    }

    /**
     * Stop the LLM automation.
     */
    public void stop() {
        if (isRunning) {
            isRunning = false;
            llmCoordinator.stop();
            LOGGER.info("LLM automation stopped");
        }
    }

    /**
     * Check if automation is currently running.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Get the singleton instance of the mod.
     */
    public static TeviaAutomationMod getInstance() {
        return instance;
    }

    /**
     * Get the configuration manager.
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Get the LLM coordinator.
     */
    public LLMCoordinator getLLMCoordinator() {
        return llmCoordinator;
    }

    /**
     * Get the mod logger.
     */
    public static Logger getLogger() {
        return LOGGER;
    }
}

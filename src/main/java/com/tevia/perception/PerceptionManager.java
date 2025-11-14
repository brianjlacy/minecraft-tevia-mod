package com.tevia.perception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tevia.config.TeviaConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages all perception systems and collects world state information.
 */
public class PerceptionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerceptionManager.class);

    private final TeviaConfig config;
    private final Gson gson;
    private final PlayerStateCollector playerStateCollector;
    private final InventoryAnalyzer inventoryAnalyzer;
    private final EntityDetector entityDetector;
    private final BlockScanner blockScanner;
    private final EnvironmentSensor environmentSensor;
    private final EventTracker eventTracker;
    private final ScreenshotCapture screenshotCapture;

    private int tickCounter = 0;

    public PerceptionManager(TeviaConfig config) {
        this.config = config;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.playerStateCollector = new PlayerStateCollector();
        this.inventoryAnalyzer = new InventoryAnalyzer();
        this.entityDetector = new EntityDetector(config.getPerceptionRadius());
        this.blockScanner = new BlockScanner(config.getPerceptionRadius());
        this.environmentSensor = new EnvironmentSensor();
        this.eventTracker = new EventTracker();
        this.screenshotCapture = new ScreenshotCapture();
    }

    /**
     * Collect the current world state.
     *
     * @param client The Minecraft client instance
     * @return WorldState object containing all perception data
     */
    public WorldState collectWorldState(MinecraftClient client) {
        ClientPlayerEntity player = client.player;

        if (player == null || client.world == null) {
            LOGGER.warn("Cannot collect world state: player or world is null");
            return null;
        }

        WorldState worldState = new WorldState();

        try {
            // Collect player state
            worldState.setPlayer(playerStateCollector.collect(player));

            // Collect inventory state
            worldState.setInventory(inventoryAnalyzer.analyze(player));

            // Collect nearby entities
            if (config.isEnableEntityDetection()) {
                worldState.setNearbyEntities(entityDetector.detect(client.world, player));
            }

            // Scan surrounding blocks
            if (config.isEnableBlockScanning()) {
                worldState.setSurroundingBlocks(blockScanner.scan(client.world, player));
            }

            // Sense environment
            if (config.isEnableEnvironmentSensing()) {
                worldState.setEnvironment(environmentSensor.sense(client.world, player));
            }

            // Collect recent events
            worldState.setRecentEvents(eventTracker.getRecentEvents());

            // Capture screenshot (if enabled and it's time)
            if (config.isEnableVisualPerception() && shouldCaptureScreenshot()) {
                String screenshot = screenshotCapture.capture(client);
                worldState.setScreenshotBase64(screenshot);
            }

            tickCounter++;

        } catch (Exception e) {
            LOGGER.error("Error collecting world state", e);
        }

        return worldState;
    }

    /**
     * Convert world state to JSON string for LLM consumption.
     */
    public String toJson(WorldState worldState) {
        return gson.toJson(worldState);
    }

    /**
     * Check if it's time to capture a screenshot based on config interval.
     */
    private boolean shouldCaptureScreenshot() {
        return tickCounter % config.getVisualCaptureIntervalTicks() == 0;
    }

    /**
     * Get the event tracker for recording events.
     */
    public EventTracker getEventTracker() {
        return eventTracker;
    }

    /**
     * Reset perception state.
     */
    public void reset() {
        eventTracker.clearEvents();
        tickCounter = 0;
    }
}

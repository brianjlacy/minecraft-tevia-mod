package com.tevia.config;

/**
 * Configuration data model for the Tevia mod.
 * Contains all user-configurable settings.
 */
public class TeviaConfig {

    // API Settings
    private String apiProvider = "anthropic"; // "anthropic" or "replicate"
    private String anthropicApiKey = "";
    private String anthropicModel = "claude-3-5-sonnet-20241022";
    private String replicateApiKey = "";
    private String replicateModel = "meta/llama-2-70b-chat";

    // Decision Loop Settings
    private double decisionFrequencyHz = 0.5; // 0.5 = once every 2 seconds
    private int maxContextMessages = 20; // Max conversation history to keep

    // Perception Settings
    private boolean enableVisualPerception = false;
    private int visualCaptureIntervalTicks = 100; // Capture every 5 seconds (at 20 TPS)
    private int perceptionRadius = 16; // Block radius for scanning
    private boolean enableEntityDetection = true;
    private boolean enableBlockScanning = true;
    private boolean enableEnvironmentSensing = true;

    // Action Settings
    private double maxMovementSpeed = 1.0; // Multiplier for movement speed
    private boolean enableCombat = true;
    private boolean enableInventoryManagement = true;
    private boolean enableBlockInteraction = true;
    private boolean enableChatting = true;
    private int actionQueueSize = 10; // Max queued actions

    // Safety Settings
    private boolean requireConfirmationForDangerousActions = true;
    private int maxActionsPerTick = 3;
    private boolean enableBoundaryRestrictions = false;
    private int boundaryRadius = 100; // Max distance from spawn

    // Character Profile
    private String characterProfilePath = "profiles/explorer.json";
    private boolean autoReloadProfile = true;

    // Logging
    private String logLevel = "INFO"; // DEBUG, INFO, WARN, ERROR
    private boolean logLLMPrompts = true;
    private boolean logLLMResponses = true;
    private boolean logActions = true;

    // Performance
    private int apiTimeoutSeconds = 30;
    private int maxRetries = 3;
    private boolean enableRateLimiting = true;

    // Getters and Setters

    public String getApiProvider() {
        return apiProvider;
    }

    public void setApiProvider(String apiProvider) {
        this.apiProvider = apiProvider;
    }

    public String getAnthropicApiKey() {
        return anthropicApiKey;
    }

    public void setAnthropicApiKey(String anthropicApiKey) {
        this.anthropicApiKey = anthropicApiKey;
    }

    public String getAnthropicModel() {
        return anthropicModel;
    }

    public void setAnthropicModel(String anthropicModel) {
        this.anthropicModel = anthropicModel;
    }

    public String getReplicateApiKey() {
        return replicateApiKey;
    }

    public void setReplicateApiKey(String replicateApiKey) {
        this.replicateApiKey = replicateApiKey;
    }

    public String getReplicateModel() {
        return replicateModel;
    }

    public void setReplicateModel(String replicateModel) {
        this.replicateModel = replicateModel;
    }

    public double getDecisionFrequencyHz() {
        return decisionFrequencyHz;
    }

    public void setDecisionFrequencyHz(double decisionFrequencyHz) {
        this.decisionFrequencyHz = decisionFrequencyHz;
    }

    public int getMaxContextMessages() {
        return maxContextMessages;
    }

    public void setMaxContextMessages(int maxContextMessages) {
        this.maxContextMessages = maxContextMessages;
    }

    public boolean isEnableVisualPerception() {
        return enableVisualPerception;
    }

    public void setEnableVisualPerception(boolean enableVisualPerception) {
        this.enableVisualPerception = enableVisualPerception;
    }

    public int getVisualCaptureIntervalTicks() {
        return visualCaptureIntervalTicks;
    }

    public void setVisualCaptureIntervalTicks(int visualCaptureIntervalTicks) {
        this.visualCaptureIntervalTicks = visualCaptureIntervalTicks;
    }

    public int getPerceptionRadius() {
        return perceptionRadius;
    }

    public void setPerceptionRadius(int perceptionRadius) {
        this.perceptionRadius = perceptionRadius;
    }

    public boolean isEnableEntityDetection() {
        return enableEntityDetection;
    }

    public void setEnableEntityDetection(boolean enableEntityDetection) {
        this.enableEntityDetection = enableEntityDetection;
    }

    public boolean isEnableBlockScanning() {
        return enableBlockScanning;
    }

    public void setEnableBlockScanning(boolean enableBlockScanning) {
        this.enableBlockScanning = enableBlockScanning;
    }

    public boolean isEnableEnvironmentSensing() {
        return enableEnvironmentSensing;
    }

    public void setEnableEnvironmentSensing(boolean enableEnvironmentSensing) {
        this.enableEnvironmentSensing = enableEnvironmentSensing;
    }

    public double getMaxMovementSpeed() {
        return maxMovementSpeed;
    }

    public void setMaxMovementSpeed(double maxMovementSpeed) {
        this.maxMovementSpeed = maxMovementSpeed;
    }

    public boolean isEnableCombat() {
        return enableCombat;
    }

    public void setEnableCombat(boolean enableCombat) {
        this.enableCombat = enableCombat;
    }

    public boolean isEnableInventoryManagement() {
        return enableInventoryManagement;
    }

    public void setEnableInventoryManagement(boolean enableInventoryManagement) {
        this.enableInventoryManagement = enableInventoryManagement;
    }

    public boolean isEnableBlockInteraction() {
        return enableBlockInteraction;
    }

    public void setEnableBlockInteraction(boolean enableBlockInteraction) {
        this.enableBlockInteraction = enableBlockInteraction;
    }

    public boolean isEnableChatting() {
        return enableChatting;
    }

    public void setEnableChatting(boolean enableChatting) {
        this.enableChatting = enableChatting;
    }

    public int getActionQueueSize() {
        return actionQueueSize;
    }

    public void setActionQueueSize(int actionQueueSize) {
        this.actionQueueSize = actionQueueSize;
    }

    public boolean isRequireConfirmationForDangerousActions() {
        return requireConfirmationForDangerousActions;
    }

    public void setRequireConfirmationForDangerousActions(boolean requireConfirmationForDangerousActions) {
        this.requireConfirmationForDangerousActions = requireConfirmationForDangerousActions;
    }

    public int getMaxActionsPerTick() {
        return maxActionsPerTick;
    }

    public void setMaxActionsPerTick(int maxActionsPerTick) {
        this.maxActionsPerTick = maxActionsPerTick;
    }

    public boolean isEnableBoundaryRestrictions() {
        return enableBoundaryRestrictions;
    }

    public void setEnableBoundaryRestrictions(boolean enableBoundaryRestrictions) {
        this.enableBoundaryRestrictions = enableBoundaryRestrictions;
    }

    public int getBoundaryRadius() {
        return boundaryRadius;
    }

    public void setBoundaryRadius(int boundaryRadius) {
        this.boundaryRadius = boundaryRadius;
    }

    public String getCharacterProfilePath() {
        return characterProfilePath;
    }

    public void setCharacterProfilePath(String characterProfilePath) {
        this.characterProfilePath = characterProfilePath;
    }

    public boolean isAutoReloadProfile() {
        return autoReloadProfile;
    }

    public void setAutoReloadProfile(boolean autoReloadProfile) {
        this.autoReloadProfile = autoReloadProfile;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public boolean isLogLLMPrompts() {
        return logLLMPrompts;
    }

    public void setLogLLMPrompts(boolean logLLMPrompts) {
        this.logLLMPrompts = logLLMPrompts;
    }

    public boolean isLogLLMResponses() {
        return logLLMResponses;
    }

    public void setLogLLMResponses(boolean logLLMResponses) {
        this.logLLMResponses = logLLMResponses;
    }

    public boolean isLogActions() {
        return logActions;
    }

    public void setLogActions(boolean logActions) {
        this.logActions = logActions;
    }

    public int getApiTimeoutSeconds() {
        return apiTimeoutSeconds;
    }

    public void setApiTimeoutSeconds(int apiTimeoutSeconds) {
        this.apiTimeoutSeconds = apiTimeoutSeconds;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public boolean isEnableRateLimiting() {
        return enableRateLimiting;
    }

    public void setEnableRateLimiting(boolean enableRateLimiting) {
        this.enableRateLimiting = enableRateLimiting;
    }

    /**
     * Validate the configuration.
     * @throws IllegalStateException if configuration is invalid
     */
    public void validate() {
        if (apiProvider == null || (!apiProvider.equals("anthropic") && !apiProvider.equals("replicate"))) {
            throw new IllegalStateException("API provider must be 'anthropic' or 'replicate'");
        }

        if (apiProvider.equals("anthropic") && (anthropicApiKey == null || anthropicApiKey.isEmpty())) {
            throw new IllegalStateException("Anthropic API key is required when using Anthropic as provider");
        }

        if (apiProvider.equals("replicate") && (replicateApiKey == null || replicateApiKey.isEmpty())) {
            throw new IllegalStateException("Replicate API key is required when using Replicate as provider");
        }

        if (decisionFrequencyHz <= 0 || decisionFrequencyHz > 10) {
            throw new IllegalStateException("Decision frequency must be between 0 and 10 Hz");
        }

        if (perceptionRadius < 1 || perceptionRadius > 64) {
            throw new IllegalStateException("Perception radius must be between 1 and 64 blocks");
        }

        if (characterProfilePath == null || characterProfilePath.isEmpty()) {
            throw new IllegalStateException("Character profile path is required");
        }
    }
}

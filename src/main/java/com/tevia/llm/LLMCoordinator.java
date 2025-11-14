package com.tevia.llm;

import com.tevia.action.ActionExecutor;
import com.tevia.action.models.Action;
import com.tevia.api.AnthropicClient;
import com.tevia.api.LLMProvider;
import com.tevia.api.ReplicateClient;
import com.tevia.api.models.LLMRequest;
import com.tevia.api.models.LLMResponse;
import com.tevia.config.ConfigManager;
import com.tevia.config.TeviaConfig;
import com.tevia.perception.PerceptionManager;
import com.tevia.perception.WorldState;
import com.tevia.profile.CharacterProfile;
import com.tevia.profile.ProfileManager;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Coordinates the LLM decision-making loop.
 * This is the main "brain" that perceives, thinks, and acts.
 */
public class LLMCoordinator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LLMCoordinator.class);

    private final ConfigManager configManager;
    private final PerceptionManager perceptionManager;
    private final ActionExecutor actionExecutor;
    private final PromptBuilder promptBuilder;
    private final ResponseParser responseParser;
    private final ContextManager contextManager;
    private final ProfileManager profileManager;

    private LLMProvider llmProvider;
    private CharacterProfile currentProfile;
    private int ticksSinceLastDecision = 0;
    private int decisionInterval;
    private boolean isActive = false;

    public LLMCoordinator(ConfigManager configManager) {
        this.configManager = configManager;

        TeviaConfig config = configManager.getConfig();
        this.perceptionManager = new PerceptionManager(config);
        this.actionExecutor = new ActionExecutor(config);
        this.promptBuilder = new PromptBuilder();
        this.responseParser = new ResponseParser();
        this.contextManager = new ContextManager(config.getMaxContextMessages());
        this.profileManager = new ProfileManager();

        // Initialize LLM provider
        initializeLLMProvider(config);

        // Calculate decision interval from frequency
        this.decisionInterval = (int) (20 / config.getDecisionFrequencyHz()); // 20 TPS

        // Load character profile
        try {
            String profilePath = config.getCharacterProfilePath();
            this.currentProfile = profileManager.loadProfile(profilePath);
            LOGGER.info("Character profile loaded: {}", currentProfile.getName());
        } catch (Exception e) {
            LOGGER.error("Failed to load character profile", e);
            this.currentProfile = profileManager.createDefaultProfile();
        }
    }

    /**
     * Initialize the LLM provider based on config.
     */
    private void initializeLLMProvider(TeviaConfig config) {
        String provider = config.getApiProvider();

        if ("anthropic".equals(provider)) {
            this.llmProvider = new AnthropicClient(
                    config.getAnthropicApiKey(),
                    config.getAnthropicModel(),
                    config.getApiTimeoutSeconds()
            );
        } else if ("replicate".equals(provider)) {
            this.llmProvider = new ReplicateClient(
                    config.getReplicateApiKey(),
                    config.getReplicateModel(),
                    config.getApiTimeoutSeconds()
            );
        } else {
            throw new IllegalStateException("Invalid API provider: " + provider);
        }

        if (!llmProvider.isConfigured()) {
            throw new IllegalStateException("LLM provider is not properly configured");
        }

        LOGGER.info("LLM Provider initialized: {} ({})",
                llmProvider.getProviderName(), llmProvider.getModel());
    }

    /**
     * Main tick method called every game tick.
     */
    public void tick(MinecraftClient client) {
        if (!isActive || client.player == null) {
            return;
        }

        // Execute queued actions
        actionExecutor.tick(client);

        // Check if it's time to make a new decision
        ticksSinceLastDecision++;
        if (ticksSinceLastDecision >= decisionInterval) {
            ticksSinceLastDecision = 0;
            makeDecision(client);
        }
    }

    /**
     * Make an LLM-driven decision.
     */
    private void makeDecision(MinecraftClient client) {
        try {
            // 1. Collect world state
            WorldState worldState = perceptionManager.collectWorldState(client);
            if (worldState == null) {
                LOGGER.warn("Failed to collect world state, skipping decision");
                return;
            }

            // 2. Build prompt
            String worldStateJson = perceptionManager.toJson(worldState);
            LLMRequest request = promptBuilder.buildRequest(
                    currentProfile,
                    worldStateJson,
                    contextManager.getConversationHistory()
            );

            // 3. Call LLM
            TeviaConfig config = configManager.getConfig();
            if (config.isLogLLMPrompts()) {
                LOGGER.debug("Sending prompt to LLM");
            }

            LLMResponse response = llmProvider.sendRequest(request);

            if (config.isLogLLMResponses()) {
                LOGGER.debug("Received response from LLM: {}", response.getContent());
            }

            // 4. Parse response into actions
            List<Action> actions = responseParser.parse(response.getContent());

            if (actions.isEmpty()) {
                LOGGER.warn("No valid actions parsed from LLM response");
                return;
            }

            // 5. Queue actions
            for (Action action : actions) {
                boolean queued = actionExecutor.queueAction(action);
                if (!queued) {
                    LOGGER.warn("Failed to queue action: {}", action.getDescription());
                }
            }

            // 6. Update conversation history
            contextManager.addUserMessage(worldStateJson);
            contextManager.addAssistantMessage(response.getContent());

            LOGGER.info("Decision made: {} actions queued", actions.size());

        } catch (Exception e) {
            LOGGER.error("Error during LLM decision making", e);
        }
    }

    /**
     * Start the LLM coordinator.
     */
    public void start() {
        isActive = true;
        ticksSinceLastDecision = 0;
        LOGGER.info("LLM Coordinator started");
    }

    /**
     * Stop the LLM coordinator.
     */
    public void stop() {
        isActive = false;
        actionExecutor.clearActions();
        LOGGER.info("LLM Coordinator stopped");
    }

    /**
     * Reload the character profile.
     */
    public void reloadProfile() {
        try {
            TeviaConfig config = configManager.getConfig();
            String profilePath = config.getCharacterProfilePath();
            this.currentProfile = profileManager.loadProfile(profilePath);
            contextManager.clear();
            LOGGER.info("Character profile reloaded: {}", currentProfile.getName());
        } catch (Exception e) {
            LOGGER.error("Failed to reload character profile", e);
        }
    }

    /**
     * Get the perception manager for event recording.
     */
    public PerceptionManager getPerceptionManager() {
        return perceptionManager;
    }

    /**
     * Get the action executor.
     */
    public ActionExecutor getActionExecutor() {
        return actionExecutor;
    }
}

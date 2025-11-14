package com.tevia.llm;

import com.tevia.api.models.LLMRequest;
import com.tevia.profile.CharacterProfile;

import java.util.List;

/**
 * Builds prompts for the LLM based on world state and character profile.
 */
public class PromptBuilder {

    private static final String SYSTEM_PROMPT_TEMPLATE =
            "You are controlling a Minecraft character named %s.\n\n" +
            "CHARACTER PROFILE:\n%s\n\n" +
            "INSTRUCTIONS:\n" +
            "You will receive the current world state as JSON. Based on this information and your character's " +
            "personality, goals, and knowledge, decide what actions to take.\n\n" +
            "You must respond with a JSON array of actions. Each action has a 'type' and relevant parameters.\n\n" +
            "Available action types:\n" +
            "- {\"type\": \"move_forward\", \"ticks\": 20} - Move forward for N ticks (20 ticks = 1 second)\n" +
            "- {\"type\": \"move_backward\", \"ticks\": 20}\n" +
            "- {\"type\": \"strafe_left\", \"ticks\": 20}\n" +
            "- {\"type\": \"strafe_right\", \"ticks\": 20}\n" +
            "- {\"type\": \"jump\"}\n" +
            "- {\"type\": \"sprint\"} - Toggle sprint\n" +
            "- {\"type\": \"sneak\"} - Toggle sneak\n" +
            "- {\"type\": \"look\", \"yaw\": 90.0, \"pitch\": 0.0} - Look in direction (yaw: -180 to 180, pitch: -90 to 90)\n" +
            "- {\"type\": \"attack\"} - Attack entity in crosshair\n" +
            "- {\"type\": \"use_item\"} - Use item in hand (eat, drink, etc.)\n" +
            "- {\"type\": \"select_slot\", \"slot\": 0} - Select hotbar slot (0-8)\n" +
            "- {\"type\": \"mine_block\", \"x\": 10, \"y\": 64, \"z\": 10} - Mine block at position\n" +
            "- {\"type\": \"place_block\", \"block\": \"dirt\", \"x\": 10, \"y\": 64, \"z\": 10} - Place block\n" +
            "- {\"type\": \"use_block\", \"x\": 10, \"y\": 64, \"z\": 10} - Use block (door, chest, etc.)\n" +
            "- {\"type\": \"chat\", \"message\": \"Hello!\"} - Send chat message\n" +
            "- {\"type\": \"wait\"} - Do nothing this cycle\n\n" +
            "RESPONSE FORMAT:\n" +
            "Your entire response must be a valid JSON array, nothing else. Example:\n" +
            "[{\"type\": \"look\", \"yaw\": 45, \"pitch\": 0}, {\"type\": \"move_forward\", \"ticks\": 20}]\n\n" +
            "Think carefully about your character's personality and goals when deciding actions. " +
            "Stay in character and make decisions that align with your profile.";

    /**
     * Build an LLM request with system prompt, character profile, and world state.
     */
    public LLMRequest buildRequest(CharacterProfile profile, String worldStateJson,
                                   List<LLMRequest.Message> conversationHistory) {
        LLMRequest request = new LLMRequest();

        // Build system prompt
        String systemPrompt = buildSystemPrompt(profile);
        request.setSystem(systemPrompt);

        // Add conversation history
        if (conversationHistory != null && !conversationHistory.isEmpty()) {
            request.setMessages(conversationHistory);
        }

        // Add current world state
        String userPrompt = "Current world state:\n\n" + worldStateJson +
                           "\n\nWhat actions do you want to take? Respond with JSON array only.";
        request.addMessage("user", userPrompt);

        // Set parameters
        request.setMaxTokens(2048);
        request.setTemperature(0.7);

        return request;
    }

    /**
     * Build the system prompt from character profile.
     */
    private String buildSystemPrompt(CharacterProfile profile) {
        StringBuilder profileText = new StringBuilder();

        profileText.append("Name: ").append(profile.getName()).append("\n");

        if (profile.getPersonality() != null && !profile.getPersonality().isEmpty()) {
            profileText.append("Personality: ").append(profile.getPersonality()).append("\n");
        }

        if (profile.getBackground() != null && !profile.getBackground().isEmpty()) {
            profileText.append("Background: ").append(profile.getBackground()).append("\n");
        }

        if (profile.getGoals() != null && !profile.getGoals().isEmpty()) {
            profileText.append("Goals: ").append(String.join(", ", profile.getGoals())).append("\n");
        }

        if (profile.getKnowledgeLevel() != null && !profile.getKnowledgeLevel().isEmpty()) {
            profileText.append("Minecraft Knowledge: ").append(profile.getKnowledgeLevel()).append("\n");
        }

        if (profile.getBehavioralTraits() != null && !profile.getBehavioralTraits().isEmpty()) {
            profileText.append("Traits: ").append(String.join(", ", profile.getBehavioralTraits())).append("\n");
        }

        if (profile.getSpecialInstructions() != null && !profile.getSpecialInstructions().isEmpty()) {
            profileText.append("\nSpecial Instructions:\n").append(profile.getSpecialInstructions()).append("\n");
        }

        return String.format(SYSTEM_PROMPT_TEMPLATE, profile.getName(), profileText.toString());
    }
}

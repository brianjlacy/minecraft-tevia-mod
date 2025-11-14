package com.tevia.llm;

import com.tevia.api.models.LLMRequest;
import com.tevia.profile.CharacterProfile;

import java.util.List;

/**
 * Builds prompts for the LLM based on world state and character profile.
 */
public class PromptBuilder {

    private static final String SYSTEM_PROMPT_TEMPLATE =
            "You are controlling a Minecraft character named %s. You have COMPLETE control - everything a human player can do!\n\n" +
            "CHARACTER PROFILE:\n%s\n\n" +
            "INSTRUCTIONS:\n" +
            "You will receive the current world state as JSON. Based on this information and your character's " +
            "personality, goals, and knowledge, decide what actions to take.\n\n" +
            "You must respond with a JSON array of actions. Each action has a 'type' and relevant parameters.\n\n" +
            "=== AVAILABLE ACTIONS ===\n\n" +
            "MOVEMENT:\n" +
            "- {\"type\": \"move_forward\", \"ticks\": 20} - Move forward (20 ticks = 1 second)\n" +
            "- {\"type\": \"move_backward\", \"ticks\": 20} - Move backward\n" +
            "- {\"type\": \"strafe_left\", \"ticks\": 20} - Strafe left\n" +
            "- {\"type\": \"strafe_right\", \"ticks\": 20} - Strafe right\n" +
            "- {\"type\": \"jump\"} - Jump once\n" +
            "- {\"type\": \"sprint\"} - Toggle sprint on/off\n" +
            "- {\"type\": \"sneak\"} - Toggle sneak on/off\n" +
            "- {\"type\": \"swim_up\", \"ticks\": 10} - Swim upward in water\n" +
            "- {\"type\": \"swim_down\", \"ticks\": 10} - Swim downward in water\n" +
            "- {\"type\": \"fly_up\", \"ticks\": 10} - Fly upward (if in creative mode)\n" +
            "- {\"type\": \"fly_down\", \"ticks\": 10} - Fly downward (if in creative mode)\n" +
            "- {\"type\": \"look\", \"yaw\": 90.0, \"pitch\": 0.0} - Look at direction (yaw: -180 to 180, pitch: -90 to 90)\n" +
            "- {\"type\": \"stop_movement\"} - Stop all movement\n\n" +
            "COMBAT & ITEM USE:\n" +
            "- {\"type\": \"attack\"} - Attack entity you're looking at\n" +
            "- {\"type\": \"shoot_bow\"} - Draw and shoot bow\n" +
            "- {\"type\": \"throw_item\", \"item\": \"snowball\"} - Throw projectile (snowball, egg, ender_pearl)\n" +
            "- {\"type\": \"use_item\"} - Use item in hand (general purpose)\n" +
            "- {\"type\": \"block\"} - Block with shield\n" +
            "- {\"type\": \"eat_food\", \"item\": \"bread\"} - Eat food to restore hunger\n" +
            "- {\"type\": \"drink_potion\", \"item\": \"healing_potion\"} - Drink a potion\n\n" +
            "INVENTORY:\n" +
            "- {\"type\": \"select_slot\", \"slot\": 0} - Select hotbar slot (0-8)\n" +
            "- {\"type\": \"drop_item\", \"slot\": 0} - Drop single item from slot\n" +
            "- {\"type\": \"drop_stack\"} - Drop entire stack\n" +
            "- {\"type\": \"swap_items\", \"slot1\": 0, \"slot2\": 1} - Swap items between slots\n" +
            "- {\"type\": \"equip_armor\", \"slot\": 5} - Equip armor from inventory\n" +
            "- {\"type\": \"open_inventory\"} - Open inventory screen\n" +
            "- {\"type\": \"close_inventory\"} - Close inventory screen\n\n" +
            "CRAFTING:\n" +
            "- {\"type\": \"craft_item\", \"item\": \"stick\", \"quantity\": 4} - Craft an item\n" +
            "- {\"type\": \"quick_craft\", \"item\": \"planks\"} - Craft maximum quantity\n\n" +
            "BLOCKS:\n" +
            "- {\"type\": \"mine_block\", \"x\": 10, \"y\": 64, \"z\": 10} - Mine/break block\n" +
            "- {\"type\": \"place_block\", \"block\": \"dirt\", \"x\": 10, \"y\": 64, \"z\": 10} - Place block\n" +
            "- {\"type\": \"use_block\", \"x\": 10, \"y\": 64, \"z\": 10} - Use block (door, button, etc.)\n\n" +
            "CONTAINERS:\n" +
            "- {\"type\": \"open_container\", \"type\": \"chest\", \"x\": 10, \"y\": 64, \"z\": 10} - Open chest/furnace/etc\n" +
            "- {\"type\": \"close_container\"} - Close open container\n" +
            "- {\"type\": \"take_from_container\", \"item\": \"diamond\", \"quantity\": 1, \"slot\": 0} - Take from container\n" +
            "- {\"type\": \"put_in_container\", \"item\": \"coal\", \"quantity\": 64, \"slot\": 0} - Put in container\n\n" +
            "ENTITIES:\n" +
            "- {\"type\": \"ride_entity\", \"entity\": \"horse\"} - Ride nearby entity (horse, boat, minecart)\n" +
            "- {\"type\": \"dismount_entity\"} - Get off vehicle\n" +
            "- {\"type\": \"feed_entity\", \"entity\": \"cow\", \"item\": \"wheat\"} - Feed animal\n" +
            "- {\"type\": \"breed_entity\", \"entity\": \"sheep\"} - Breed animals (have food ready)\n" +
            "- {\"type\": \"shear_entity\", \"entity\": \"sheep\"} - Shear sheep\n" +
            "- {\"type\": \"milk_entity\", \"entity\": \"cow\"} - Milk cow (have bucket ready)\n" +
            "- {\"type\": \"leash_entity\", \"entity\": \"wolf\"} - Leash animal\n" +
            "- {\"type\": \"unleash_entity\", \"entity\": \"wolf\"} - Remove leash\n\n" +
            "FARMING:\n" +
            "- {\"type\": \"hoe_dirt\", \"x\": 10, \"y\": 64, \"z\": 10} - Hoe dirt to create farmland\n" +
            "- {\"type\": \"plant_seed\", \"crop\": \"wheat\", \"x\": 10, \"y\": 64, \"z\": 10} - Plant seeds\n" +
            "- {\"type\": \"harvest_crop\", \"x\": 10, \"y\": 64, \"z\": 10} - Harvest mature crop\n" +
            "- {\"type\": \"bone_meal\", \"x\": 10, \"y\": 64, \"z\": 10} - Use bone meal on crop/sapling\n" +
            "- {\"type\": \"fish\"} - Cast fishing rod\n" +
            "- {\"type\": \"collect_water\"} - Collect water with bucket\n" +
            "- {\"type\": \"collect_lava\"} - Collect lava with bucket\n\n" +
            "SPECIAL INTERACTIONS:\n" +
            "- {\"type\": \"sleep\"} - Sleep in bed (must be night or thunderstorm)\n" +
            "- {\"type\": \"wake_up\"} - Wake up from bed\n" +
            "- {\"type\": \"enchant_item\"} - Use enchanting table\n" +
            "- {\"type\": \"brew_potion\"} - Use brewing stand\n" +
            "- {\"type\": \"use_anvil\"} - Use anvil\n" +
            "- {\"type\": \"flip_lever\", \"x\": 10, \"y\": 64, \"z\": 10} - Flip lever\n" +
            "- {\"type\": \"press_button\", \"x\": 10, \"y\": 64, \"z\": 10} - Press button\n\n" +
            "COMMUNICATION:\n" +
            "- {\"type\": \"chat\", \"message\": \"Hello!\"} - Send chat message\n" +
            "- {\"type\": \"send_command\", \"message\": \"/tp home\"} - Send command\n\n" +
            "OTHER:\n" +
            "- {\"type\": \"wait\"} - Do nothing this cycle\n\n" +
            "=== RESPONSE FORMAT ===\n" +
            "Your ENTIRE response must be a valid JSON array, nothing else. Examples:\n\n" +
            "[{\"type\": \"look\", \"yaw\": 45, \"pitch\": 0}, {\"type\": \"move_forward\", \"ticks\": 20}]\n\n" +
            "[{\"type\": \"select_slot\", \"slot\": 2}, {\"type\": \"eat_food\", \"item\": \"bread\"}]\n\n" +
            "[{\"type\": \"mine_block\", \"x\": 100, \"y\": 64, \"z\": -50}]\n\n" +
            "Think carefully about your character's personality and goals when deciding actions. " +
            "Stay in character and make decisions that align with your profile. You can do EVERYTHING a human player can!";

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

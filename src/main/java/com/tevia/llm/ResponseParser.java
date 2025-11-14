package com.tevia.llm;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tevia.action.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses LLM responses into executable actions.
 */
public class ResponseParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseParser.class);
    private final Gson gson;

    public ResponseParser() {
        this.gson = new Gson();
    }

    /**
     * Parse LLM response content into a list of actions.
     */
    public List<Action> parse(String responseContent) {
        List<Action> actions = new ArrayList<>();

        if (responseContent == null || responseContent.trim().isEmpty()) {
            LOGGER.warn("Empty response content");
            return actions;
        }

        try {
            // Extract JSON from response (might have explanation text before/after)
            String jsonContent = extractJson(responseContent);

            if (jsonContent == null) {
                LOGGER.warn("No JSON found in response");
                return actions;
            }

            // Parse JSON array
            JsonArray jsonArray = gson.fromJson(jsonContent, JsonArray.class);

            for (JsonElement element : jsonArray) {
                if (!element.isJsonObject()) {
                    continue;
                }

                JsonObject actionObj = element.getAsJsonObject();
                Action action = parseAction(actionObj);

                if (action != null) {
                    actions.add(action);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to parse response: {}", responseContent, e);
        }

        return actions;
    }

    /**
     * Extract JSON array from response text.
     */
    private String extractJson(String text) {
        // Find first '[' and last ']'
        int start = text.indexOf('[');
        int end = text.lastIndexOf(']');

        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }

        return null;
    }

    /**
     * Parse a single action from JSON object.
     */
    private Action parseAction(JsonObject obj) {
        if (!obj.has("type")) {
            LOGGER.warn("Action missing 'type' field");
            return null;
        }

        String type = obj.get("type").getAsString().toLowerCase();

        try {
            switch (type) {
                // Movement actions
                case "move_forward":
                    return MovementAction.forward(getInt(obj, "ticks", 20));

                case "move_backward":
                    return MovementAction.backward(getInt(obj, "ticks", 20));

                case "strafe_left":
                    return MovementAction.strafeLeft(getInt(obj, "ticks", 20));

                case "strafe_right":
                    return MovementAction.strafeRight(getInt(obj, "ticks", 20));

                case "jump":
                    return MovementAction.jump();

                case "sprint":
                    return new MovementAction(Action.ActionType.SPRINT);

                case "sneak":
                    return new MovementAction(Action.ActionType.SNEAK);

                case "look":
                    float yaw = getFloat(obj, "yaw", 0);
                    float pitch = getFloat(obj, "pitch", 0);
                    return MovementAction.look(yaw, pitch);

                // Combat actions
                case "attack":
                    return CombatAction.attack();

                case "use_item":
                    return CombatAction.useItem();

                // Inventory actions
                case "select_slot":
                    return InventoryAction.selectSlot(getInt(obj, "slot", 0));

                case "drop_item":
                    return InventoryAction.dropItem(getInt(obj, "slot", 0));

                // Block actions
                case "mine_block":
                    return BlockAction.mine(
                            getInt(obj, "x", 0),
                            getInt(obj, "y", 0),
                            getInt(obj, "z", 0)
                    );

                case "place_block":
                    return BlockAction.place(
                            getString(obj, "block", ""),
                            getInt(obj, "x", 0),
                            getInt(obj, "y", 0),
                            getInt(obj, "z", 0)
                    );

                case "use_block":
                    return BlockAction.use(
                            getInt(obj, "x", 0),
                            getInt(obj, "y", 0),
                            getInt(obj, "z", 0)
                    );

                // Chat actions
                case "chat":
                    return ChatAction.send(getString(obj, "message", ""));

                // Wait (do nothing)
                case "wait":
                    return null; // Skip wait actions

                default:
                    LOGGER.warn("Unknown action type: {}", type);
                    return null;
            }

        } catch (Exception e) {
            LOGGER.error("Error parsing action: {}", obj, e);
            return null;
        }
    }

    // Helper methods to safely extract values from JSON

    private int getInt(JsonObject obj, String key, int defaultValue) {
        if (obj.has(key)) {
            return obj.get(key).getAsInt();
        }
        return defaultValue;
    }

    private float getFloat(JsonObject obj, String key, float defaultValue) {
        if (obj.has(key)) {
            return obj.get(key).getAsFloat();
        }
        return defaultValue;
    }

    private String getString(JsonObject obj, String key, String defaultValue) {
        if (obj.has(key)) {
            return obj.get(key).getAsString();
        }
        return defaultValue;
    }
}

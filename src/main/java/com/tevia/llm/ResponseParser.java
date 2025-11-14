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

                case "stop_movement":
                    return new MovementAction(Action.ActionType.STOP_MOVEMENT);

                case "swim_up":
                    MovementAction swimUp = new MovementAction(Action.ActionType.SWIM_UP);
                    swimUp.setDurationTicks(getInt(obj, "ticks", 10));
                    return swimUp;

                case "swim_down":
                    MovementAction swimDown = new MovementAction(Action.ActionType.SWIM_DOWN);
                    swimDown.setDurationTicks(getInt(obj, "ticks", 10));
                    return swimDown;

                case "fly_up":
                    MovementAction flyUp = new MovementAction(Action.ActionType.FLY_UP);
                    flyUp.setDurationTicks(getInt(obj, "ticks", 10));
                    return flyUp;

                case "fly_down":
                    MovementAction flyDown = new MovementAction(Action.ActionType.FLY_DOWN);
                    flyDown.setDurationTicks(getInt(obj, "ticks", 10));
                    return flyDown;

                // Combat actions
                case "attack":
                    return CombatAction.attack();

                case "use_item":
                    return CombatAction.useItem();

                // Special actions
                case "eat_food":
                    return SpecialAction.eat(getString(obj, "item", ""));

                case "drink_potion":
                    SpecialAction drinkPotion = new SpecialAction(Action.ActionType.DRINK_POTION);
                    drinkPotion.setTargetItem(getString(obj, "item", ""));
                    return drinkPotion;

                case "shoot_bow":
                    return SpecialAction.shootBow();

                case "throw_item":
                    return SpecialAction.throwItem(getString(obj, "item", ""));

                case "sleep":
                    return SpecialAction.sleep();

                case "wake_up":
                    return new SpecialAction(Action.ActionType.WAKE_UP);

                case "flip_lever":
                case "press_button":
                    SpecialAction mechanism = new SpecialAction(
                            type.equals("flip_lever") ? Action.ActionType.FLIP_LEVER : Action.ActionType.PRESS_BUTTON
                    );
                    mechanism.setX(getInt(obj, "x", 0));
                    mechanism.setY(getInt(obj, "y", 0));
                    mechanism.setZ(getInt(obj, "z", 0));
                    return mechanism;

                case "enchant_item":
                    return new SpecialAction(Action.ActionType.ENCHANT_ITEM);

                case "brew_potion":
                    return new SpecialAction(Action.ActionType.BREW_POTION);

                case "use_anvil":
                    return new SpecialAction(Action.ActionType.USE_ANVIL);

                // Inventory actions
                case "select_slot":
                    return InventoryAction.selectSlot(getInt(obj, "slot", 0));

                case "drop_item":
                    return InventoryAction.dropItem(getInt(obj, "slot", 0));

                case "drop_stack":
                    return new InventoryAction(Action.ActionType.DROP_STACK);

                case "swap_items":
                    InventoryAction swap = new InventoryAction(Action.ActionType.SWAP_ITEMS);
                    swap.setSlot(getInt(obj, "slot1", 0));
                    swap.setTargetSlot(getInt(obj, "slot2", 1));
                    return swap;

                case "equip_armor":
                    InventoryAction equip = new InventoryAction(Action.ActionType.EQUIP_ARMOR);
                    equip.setSlot(getInt(obj, "slot", 0));
                    return equip;

                // Crafting actions
                case "craft_item":
                    return CraftingAction.craft(
                            getString(obj, "item", ""),
                            getInt(obj, "quantity", 1)
                    );

                case "quick_craft":
                    CraftingAction quickCraft = new CraftingAction(Action.ActionType.QUICK_CRAFT);
                    quickCraft.setItemToCraft(getString(obj, "item", ""));
                    return quickCraft;

                case "open_inventory":
                    return new CraftingAction(Action.ActionType.OPEN_INVENTORY);

                case "close_inventory":
                    return new CraftingAction(Action.ActionType.CLOSE_INVENTORY);

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

                // Container actions
                case "open_container":
                    return ContainerAction.open(
                            getString(obj, "type", "chest"),
                            getInt(obj, "x", 0),
                            getInt(obj, "y", 0),
                            getInt(obj, "z", 0)
                    );

                case "close_container":
                    return new ContainerAction(Action.ActionType.CLOSE_CONTAINER);

                case "take_from_container":
                    return ContainerAction.takeItem(
                            getString(obj, "item", ""),
                            getInt(obj, "quantity", 1),
                            getInt(obj, "slot", 0)
                    );

                case "put_in_container":
                    ContainerAction putIn = new ContainerAction(Action.ActionType.PUT_IN_CONTAINER);
                    putIn.setItemName(getString(obj, "item", ""));
                    putIn.setQuantity(getInt(obj, "quantity", 1));
                    putIn.setSlot(getInt(obj, "slot", 0));
                    return putIn;

                // Entity interaction actions
                case "ride_entity":
                    return EntityInteractionAction.ride(getString(obj, "entity", ""));

                case "dismount_entity":
                    return EntityInteractionAction.dismount();

                case "feed_entity":
                    return EntityInteractionAction.feed(
                            getString(obj, "entity", ""),
                            getString(obj, "item", "")
                    );

                case "breed_entity":
                    EntityInteractionAction breed = new EntityInteractionAction(Action.ActionType.BREED_ENTITY);
                    breed.setTargetEntity(getString(obj, "entity", ""));
                    return breed;

                case "shear_entity":
                    EntityInteractionAction shear = new EntityInteractionAction(Action.ActionType.SHEAR_ENTITY);
                    shear.setTargetEntity(getString(obj, "entity", ""));
                    return shear;

                case "milk_entity":
                    EntityInteractionAction milk = new EntityInteractionAction(Action.ActionType.MILK_ENTITY);
                    milk.setTargetEntity(getString(obj, "entity", ""));
                    return milk;

                case "leash_entity":
                    EntityInteractionAction leash = new EntityInteractionAction(Action.ActionType.LEASH_ENTITY);
                    leash.setTargetEntity(getString(obj, "entity", ""));
                    return leash;

                case "unleash_entity":
                    EntityInteractionAction unleash = new EntityInteractionAction(Action.ActionType.UNLEASH_ENTITY);
                    unleash.setTargetEntity(getString(obj, "entity", ""));
                    return unleash;

                // Farming actions
                case "hoe_dirt":
                    return FarmingAction.hoe(
                            getInt(obj, "x", 0),
                            getInt(obj, "y", 0),
                            getInt(obj, "z", 0)
                    );

                case "plant_seed":
                    return FarmingAction.plant(
                            getString(obj, "crop", "wheat"),
                            getInt(obj, "x", 0),
                            getInt(obj, "y", 0),
                            getInt(obj, "z", 0)
                    );

                case "harvest_crop":
                    FarmingAction harvest = new FarmingAction(Action.ActionType.HARVEST_CROP);
                    harvest.setX(getInt(obj, "x", 0));
                    harvest.setY(getInt(obj, "y", 0));
                    harvest.setZ(getInt(obj, "z", 0));
                    return harvest;

                case "bone_meal":
                    FarmingAction boneMeal = new FarmingAction(Action.ActionType.BONE_MEAL);
                    boneMeal.setX(getInt(obj, "x", 0));
                    boneMeal.setY(getInt(obj, "y", 0));
                    boneMeal.setZ(getInt(obj, "z", 0));
                    return boneMeal;

                case "fish":
                    return FarmingAction.fish();

                case "collect_water":
                    return new FarmingAction(Action.ActionType.COLLECT_WATER);

                case "collect_lava":
                    return new FarmingAction(Action.ActionType.COLLECT_LAVA);

                // Chat actions
                case "chat":
                    return ChatAction.send(getString(obj, "message", ""));

                case "send_command":
                    ChatAction command = new ChatAction();
                    command.setMessage(getString(obj, "message", ""));
                    return command;

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

package com.tevia.action.models;

/**
 * Base class for all actions that the AI can take.
 */
public abstract class Action {

    private final ActionType type;
    private boolean completed = false;
    private String failureReason;

    public Action(ActionType type) {
        this.type = type;
    }

    public ActionType getType() {
        return type;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public boolean hasFailed() {
        return failureReason != null;
    }

    /**
     * Get a human-readable description of this action.
     */
    public abstract String getDescription();

    public enum ActionType {
        // Movement
        MOVE_FORWARD,
        MOVE_BACKWARD,
        STRAFE_LEFT,
        STRAFE_RIGHT,
        JUMP,
        SPRINT,
        SNEAK,
        LOOK,
        STOP_MOVEMENT,
        SWIM_UP,
        SWIM_DOWN,
        FLY_UP,
        FLY_DOWN,

        // Combat & Item Use
        ATTACK,
        USE_ITEM,
        BLOCK,
        SHOOT_BOW,
        THROW_ITEM,
        EAT_FOOD,
        DRINK_POTION,

        // Inventory Management
        SELECT_HOTBAR_SLOT,
        SWAP_ITEMS,
        DROP_ITEM,
        DROP_STACK,
        EQUIP_ARMOR,
        OPEN_INVENTORY,
        CLOSE_INVENTORY,
        CRAFT_ITEM,
        QUICK_CRAFT,

        // Block Interaction
        MINE_BLOCK,
        PLACE_BLOCK,
        USE_BLOCK,
        OPEN_CONTAINER,
        CLOSE_CONTAINER,
        TAKE_FROM_CONTAINER,
        PUT_IN_CONTAINER,

        // Entity Interaction
        RIDE_ENTITY,
        DISMOUNT_ENTITY,
        FEED_ENTITY,
        BREED_ENTITY,
        SHEAR_ENTITY,
        MILK_ENTITY,
        LEASH_ENTITY,
        UNLEASH_ENTITY,

        // Trading & Villagers
        OPEN_TRADING,
        SELECT_TRADE,
        ACCEPT_TRADE,
        CLOSE_TRADING,

        // Farming & Tools
        HOE_DIRT,
        PLANT_SEED,
        HARVEST_CROP,
        BONE_MEAL,
        SHEAR_SHEEP,
        FISH,
        COLLECT_WATER,
        COLLECT_LAVA,

        // Sleeping & Beds
        SLEEP,
        WAKE_UP,

        // Special Interactions
        ENCHANT_ITEM,
        BREW_POTION,
        USE_ANVIL,
        USE_GRINDSTONE,
        USE_SMITHING_TABLE,
        USE_STONECUTTER,
        USE_LOOM,
        USE_CARTOGRAPHY_TABLE,
        WRITE_BOOK,
        SIGN_BOOK,
        EDIT_SIGN,

        // Redstone & Mechanisms
        FLIP_LEVER,
        PRESS_BUTTON,
        PULL_TRIPWIRE,
        SET_REPEATER_DELAY,
        SET_COMPARATOR_MODE,

        // Advanced
        USE_ENDER_PEARL,
        USE_ENDER_CHEST,
        PLACE_ENTITY,
        BREAK_ITEM,

        // Chat
        SEND_CHAT,
        SEND_COMMAND,

        // Composite
        WAIT,
        SEQUENCE
    }
}

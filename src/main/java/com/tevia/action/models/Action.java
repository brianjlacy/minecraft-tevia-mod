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

        // Combat
        ATTACK,
        USE_ITEM,
        BLOCK,

        // Inventory
        SELECT_HOTBAR_SLOT,
        SWAP_ITEMS,
        DROP_ITEM,
        EQUIP_ARMOR,

        // Block interaction
        MINE_BLOCK,
        PLACE_BLOCK,
        USE_BLOCK,

        // Chat
        SEND_CHAT,

        // Composite
        WAIT,
        SEQUENCE
    }
}

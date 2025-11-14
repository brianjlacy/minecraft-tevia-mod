package com.tevia.action;

import com.tevia.action.models.*;
import com.tevia.config.TeviaConfig;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main action executor that dispatches actions to appropriate controllers.
 */
public class ActionExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionExecutor.class);

    private final TeviaConfig config;
    private final ActionQueue actionQueue;
    private final MovementController movementController;
    private final CombatController combatController;
    private final InventoryController inventoryController;
    private final BlockController blockController;
    private final ChatController chatController;

    public ActionExecutor(TeviaConfig config) {
        this.config = config;
        this.actionQueue = new ActionQueue(config.getActionQueueSize());
        this.movementController = new MovementController();
        this.combatController = new CombatController();
        this.inventoryController = new InventoryController();
        this.blockController = new BlockController();
        this.chatController = new ChatController();
    }

    /**
     * Queue an action for execution.
     */
    public boolean queueAction(Action action) {
        if (!validateAction(action)) {
            LOGGER.warn("Action validation failed: {}", action.getDescription());
            return false;
        }

        return actionQueue.enqueue(action);
    }

    /**
     * Execute queued actions (called every tick).
     */
    public void tick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        // Execute current action
        Action currentAction = actionQueue.getCurrentAction();
        if (currentAction != null && !currentAction.isCompleted()) {
            continueExecutingAction(client, currentAction);
            return;
        }

        // Start next action if available
        if (!actionQueue.isEmpty()) {
            Action nextAction = actionQueue.dequeue();
            actionQueue.setCurrentAction(nextAction);
            startExecutingAction(client, nextAction);
        }
    }

    /**
     * Start executing a new action.
     */
    private void startExecutingAction(MinecraftClient client, Action action) {
        try {
            if (config.isLogActions()) {
                LOGGER.info("Executing action: {}", action.getDescription());
            }

            boolean success = dispatchAction(client, action);

            if (!success) {
                action.setFailureReason("Action dispatch failed");
                action.setCompleted(true);
            }

        } catch (Exception e) {
            LOGGER.error("Error executing action: {}", action.getDescription(), e);
            action.setFailureReason("Exception: " + e.getMessage());
            action.setCompleted(true);
        }
    }

    /**
     * Continue executing a multi-tick action.
     */
    private void continueExecutingAction(MinecraftClient client, Action action) {
        try {
            dispatchAction(client, action);
        } catch (Exception e) {
            LOGGER.error("Error continuing action: {}", action.getDescription(), e);
            action.setFailureReason("Exception: " + e.getMessage());
            action.setCompleted(true);
        }
    }

    /**
     * Dispatch action to appropriate controller.
     */
    private boolean dispatchAction(MinecraftClient client, Action action) {
        switch (action.getType()) {
            // Movement
            case MOVE_FORWARD:
            case MOVE_BACKWARD:
            case STRAFE_LEFT:
            case STRAFE_RIGHT:
            case JUMP:
            case SPRINT:
            case SNEAK:
            case LOOK:
            case STOP_MOVEMENT:
                return movementController.execute(client, (MovementAction) action);

            // Combat
            case ATTACK:
            case USE_ITEM:
            case BLOCK:
                if (!config.isEnableCombat()) {
                    action.setFailureReason("Combat is disabled in config");
                    action.setCompleted(true);
                    return false;
                }
                return combatController.execute(client, (CombatAction) action);

            // Inventory
            case SELECT_HOTBAR_SLOT:
            case SWAP_ITEMS:
            case DROP_ITEM:
            case EQUIP_ARMOR:
                if (!config.isEnableInventoryManagement()) {
                    action.setFailureReason("Inventory management is disabled in config");
                    action.setCompleted(true);
                    return false;
                }
                return inventoryController.execute(client, (InventoryAction) action);

            // Block interaction
            case MINE_BLOCK:
            case PLACE_BLOCK:
            case USE_BLOCK:
                if (!config.isEnableBlockInteraction()) {
                    action.setFailureReason("Block interaction is disabled in config");
                    action.setCompleted(true);
                    return false;
                }
                return blockController.execute(client, (BlockAction) action);

            // Chat
            case SEND_CHAT:
                if (!config.isEnableChatting()) {
                    action.setFailureReason("Chat is disabled in config");
                    action.setCompleted(true);
                    return false;
                }
                return chatController.execute(client, (ChatAction) action);

            default:
                LOGGER.warn("Unknown action type: {}", action.getType());
                action.setFailureReason("Unknown action type");
                action.setCompleted(true);
                return false;
        }
    }

    /**
     * Validate an action before queuing.
     */
    private boolean validateAction(Action action) {
        if (action == null) {
            return false;
        }

        // Add validation logic here (e.g., check bounds, required fields)
        return true;
    }

    /**
     * Get the action queue.
     */
    public ActionQueue getActionQueue() {
        return actionQueue;
    }

    /**
     * Clear all queued actions.
     */
    public void clearActions() {
        actionQueue.clear();
        movementController.reset();
    }
}

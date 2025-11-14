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
    private final EntityInteractionController entityInteractionController;
    private final CraftingController craftingController;
    private final ContainerController containerController;
    private final FarmingController farmingController;
    private final SpecialInteractionController specialInteractionController;

    public ActionExecutor(TeviaConfig config) {
        this.config = config;
        this.actionQueue = new ActionQueue(config.getActionQueueSize());
        this.movementController = new MovementController();
        this.combatController = new CombatController();
        this.inventoryController = new InventoryController();
        this.blockController = new BlockController();
        this.chatController = new ChatController();
        this.entityInteractionController = new EntityInteractionController();
        this.craftingController = new CraftingController();
        this.containerController = new ContainerController();
        this.farmingController = new FarmingController();
        this.specialInteractionController = new SpecialInteractionController();
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
            case SWIM_UP:
            case SWIM_DOWN:
            case FLY_UP:
            case FLY_DOWN:
                return movementController.execute(client, (MovementAction) action);

            // Combat & Item Use
            case ATTACK:
            case USE_ITEM:
            case BLOCK:
                if (!config.isEnableCombat()) {
                    action.setFailureReason("Combat is disabled in config");
                    action.setCompleted(true);
                    return false;
                }
                return combatController.execute(client, (CombatAction) action);

            // Special actions (eating, drinking, shooting, throwing)
            case EAT_FOOD:
            case DRINK_POTION:
            case SHOOT_BOW:
            case THROW_ITEM:
            case SLEEP:
            case WAKE_UP:
            case FLIP_LEVER:
            case PRESS_BUTTON:
            case ENCHANT_ITEM:
            case BREW_POTION:
            case USE_ANVIL:
            case USE_GRINDSTONE:
            case USE_SMITHING_TABLE:
            case USE_STONECUTTER:
            case USE_LOOM:
            case USE_CARTOGRAPHY_TABLE:
            case WRITE_BOOK:
            case SIGN_BOOK:
            case EDIT_SIGN:
            case SET_REPEATER_DELAY:
            case SET_COMPARATOR_MODE:
            case USE_ENDER_PEARL:
            case USE_ENDER_CHEST:
            case PLACE_ENTITY:
            case BREAK_ITEM:
                return specialInteractionController.execute(client, (SpecialAction) action);

            // Inventory management
            case SELECT_HOTBAR_SLOT:
            case SWAP_ITEMS:
            case DROP_ITEM:
            case DROP_STACK:
            case EQUIP_ARMOR:
                if (!config.isEnableInventoryManagement()) {
                    action.setFailureReason("Inventory management is disabled in config");
                    action.setCompleted(true);
                    return false;
                }
                return inventoryController.execute(client, (InventoryAction) action);

            // Crafting
            case CRAFT_ITEM:
            case QUICK_CRAFT:
            case OPEN_INVENTORY:
            case CLOSE_INVENTORY:
                if (!config.isEnableInventoryManagement()) {
                    action.setFailureReason("Inventory management is disabled in config");
                    action.setCompleted(true);
                    return false;
                }
                return craftingController.execute(client, (CraftingAction) action);

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

            // Container interactions
            case OPEN_CONTAINER:
            case CLOSE_CONTAINER:
            case TAKE_FROM_CONTAINER:
            case PUT_IN_CONTAINER:
                if (!config.isEnableBlockInteraction()) {
                    action.setFailureReason("Block interaction is disabled in config");
                    action.setCompleted(true);
                    return false;
                }
                return containerController.execute(client, (ContainerAction) action);

            // Entity interactions
            case RIDE_ENTITY:
            case DISMOUNT_ENTITY:
            case FEED_ENTITY:
            case BREED_ENTITY:
            case SHEAR_ENTITY:
            case MILK_ENTITY:
            case LEASH_ENTITY:
            case UNLEASH_ENTITY:
                return entityInteractionController.execute(client, (EntityInteractionAction) action);

            // Farming & tools
            case HOE_DIRT:
            case PLANT_SEED:
            case HARVEST_CROP:
            case BONE_MEAL:
            case SHEAR_SHEEP:
            case FISH:
            case COLLECT_WATER:
            case COLLECT_LAVA:
                return farmingController.execute(client, (FarmingAction) action);

            // Chat
            case SEND_CHAT:
            case SEND_COMMAND:
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

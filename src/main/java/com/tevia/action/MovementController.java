package com.tevia.action;

import com.tevia.action.models.MovementAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;

/**
 * Controls player movement actions.
 */
public class MovementController {

    private int movementTicksRemaining = 0;

    /**
     * Execute a movement action.
     */
    public boolean execute(MinecraftClient client, MovementAction action) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            action.setCompleted(true);
            return false;
        }

        GameOptions options = client.options;

        switch (action.getType()) {
            case MOVE_FORWARD:
                return handleMovement(action, () -> options.forwardKey.setPressed(true));

            case MOVE_BACKWARD:
                return handleMovement(action, () -> options.backKey.setPressed(true));

            case STRAFE_LEFT:
                return handleMovement(action, () -> options.leftKey.setPressed(true));

            case STRAFE_RIGHT:
                return handleMovement(action, () -> options.rightKey.setPressed(true));

            case JUMP:
                options.jumpKey.setPressed(true);
                action.setCompleted(true);
                return true;

            case SPRINT:
                player.setSprinting(!player.isSprinting());
                action.setCompleted(true);
                return true;

            case SNEAK:
                options.sneakKey.setPressed(!options.sneakKey.isPressed());
                action.setCompleted(true);
                return true;

            case LOOK:
                player.setYaw(action.getYaw());
                player.setPitch(action.getPitch());
                action.setCompleted(true);
                return true;

            case STOP_MOVEMENT:
                stopAllMovement(options);
                action.setCompleted(true);
                return true;

            default:
                action.setCompleted(true);
                return false;
        }
    }

    /**
     * Handle timed movement actions.
     */
    private boolean handleMovement(MovementAction action, Runnable pressKey) {
        if (movementTicksRemaining == 0) {
            // Start movement
            movementTicksRemaining = action.getDurationTicks();
            pressKey.run();
        } else {
            // Continue movement
            movementTicksRemaining--;
            pressKey.run();

            if (movementTicksRemaining <= 0) {
                action.setCompleted(true);
            }
        }
        return true;
    }

    /**
     * Stop all movement inputs.
     */
    private void stopAllMovement(GameOptions options) {
        options.forwardKey.setPressed(false);
        options.backKey.setPressed(false);
        options.leftKey.setPressed(false);
        options.rightKey.setPressed(false);
        options.jumpKey.setPressed(false);
        options.sneakKey.setPressed(false);
    }

    /**
     * Reset movement state.
     */
    public void reset() {
        movementTicksRemaining = 0;
    }
}

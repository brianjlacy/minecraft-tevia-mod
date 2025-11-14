package com.tevia.action;

import com.tevia.action.models.CombatAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.HitResult;

/**
 * Controls combat actions.
 */
public class CombatController {

    /**
     * Execute a combat action.
     */
    public boolean execute(MinecraftClient client, CombatAction action) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            action.setCompleted(true);
            return false;
        }

        switch (action.getType()) {
            case ATTACK:
                return handleAttack(client, action);

            case USE_ITEM:
                return handleUseItem(client, action);

            case BLOCK:
                return handleBlock(client, action);

            default:
                action.setCompleted(true);
                return false;
        }
    }

    /**
     * Handle attack action.
     */
    private boolean handleAttack(MinecraftClient client, CombatAction action) {
        // Perform attack
        if (client.crosshairTarget != null && client.crosshairTarget.getType() != HitResult.Type.MISS) {
            client.options.attackKey.setPressed(true);

            // Attack for one tick, then release
            if (!action.isContinuous()) {
                action.setCompleted(true);
            }
        } else {
            // No target
            action.setFailureReason("No target in crosshair");
            action.setCompleted(true);
            return false;
        }

        return true;
    }

    /**
     * Handle use item action (eat, drink, use tool, etc.).
     */
    private boolean handleUseItem(MinecraftClient client, CombatAction action) {
        client.options.useKey.setPressed(true);

        // Use for one tick
        action.setCompleted(true);
        return true;
    }

    /**
     * Handle block with shield.
     */
    private boolean handleBlock(MinecraftClient client, CombatAction action) {
        ClientPlayerEntity player = client.player;

        // Check if player has a shield
        if (player.getOffHandStack().isEmpty() || !player.getOffHandStack().getItem().toString().contains("shield")) {
            action.setFailureReason("No shield equipped");
            action.setCompleted(true);
            return false;
        }

        client.options.useKey.setPressed(true);

        // Hold block
        if (!action.isContinuous()) {
            action.setCompleted(true);
        }

        return true;
    }
}

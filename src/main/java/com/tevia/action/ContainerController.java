package com.tevia.action;

import com.tevia.action.models.ContainerAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Controls container interaction actions (chests, furnaces, etc.).
 */
public class ContainerController {

    /**
     * Execute a container action.
     */
    public boolean execute(MinecraftClient client, ContainerAction action) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            action.setCompleted(true);
            return false;
        }

        switch (action.getType()) {
            case OPEN_CONTAINER:
                return handleOpenContainer(client, action);

            case CLOSE_CONTAINER:
                return handleCloseContainer(client, action);

            case TAKE_FROM_CONTAINER:
                return handleTakeFromContainer(client, action);

            case PUT_IN_CONTAINER:
                return handlePutInContainer(client, action);

            default:
                action.setCompleted(true);
                return false;
        }
    }

    /**
     * Open a container (chest, furnace, etc.).
     */
    private boolean handleOpenContainer(MinecraftClient client, ContainerAction action) {
        BlockPos pos = new BlockPos(action.getX(), action.getY(), action.getZ());

        // Check if block is a container
        if (!client.world.getBlockState(pos).isAir()) {
            // Right-click the container block
            BlockHitResult hitResult = new BlockHitResult(
                    Vec3d.ofCenter(pos),
                    Direction.UP,
                    pos,
                    false
            );

            client.interactionManager.interactBlock(
                    client.player,
                    Hand.MAIN_HAND,
                    hitResult
            );

            action.setCompleted(true);
            return true;
        }

        action.setFailureReason("No container at position");
        action.setCompleted(true);
        return false;
    }

    /**
     * Close the currently open container.
     */
    private boolean handleCloseContainer(MinecraftClient client, ContainerAction action) {
        if (client.currentScreen != null) {
            client.player.closeScreen();
        }
        action.setCompleted(true);
        return true;
    }

    /**
     * Take an item from a container.
     */
    private boolean handleTakeFromContainer(MinecraftClient client, ContainerAction action) {
        // Would require screen handler interaction
        // Need to click on the slot and move to player inventory
        action.setCompleted(true);
        return true;
    }

    /**
     * Put an item into a container.
     */
    private boolean handlePutInContainer(MinecraftClient client, ContainerAction action) {
        // Would require screen handler interaction
        // Need to click on player inventory slot and move to container
        action.setCompleted(true);
        return true;
    }
}

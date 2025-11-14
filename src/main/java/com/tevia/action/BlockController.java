package com.tevia.action;

import com.tevia.action.models.BlockAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Controls block interaction actions.
 */
public class BlockController {

    /**
     * Execute a block action.
     */
    public boolean execute(MinecraftClient client, BlockAction action) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            action.setCompleted(true);
            return false;
        }

        switch (action.getType()) {
            case MINE_BLOCK:
                return handleMineBlock(client, action);

            case PLACE_BLOCK:
                return handlePlaceBlock(client, action);

            case USE_BLOCK:
                return handleUseBlock(client, action);

            default:
                action.setCompleted(true);
                return false;
        }
    }

    /**
     * Mine a block.
     */
    private boolean handleMineBlock(MinecraftClient client, BlockAction action) {
        BlockPos pos = new BlockPos(action.getX(), action.getY(), action.getZ());

        // Check if block exists
        if (client.world.getBlockState(pos).isAir()) {
            action.setFailureReason("No block at position");
            action.setCompleted(true);
            return false;
        }

        // Start breaking block
        client.options.attackKey.setPressed(true);

        // For now, complete immediately (actual mining would take time)
        action.setCompleted(true);
        return true;
    }

    /**
     * Place a block.
     */
    private boolean handlePlaceBlock(MinecraftClient client, BlockAction action) {
        ClientPlayerEntity player = client.player;
        BlockPos pos = new BlockPos(action.getX(), action.getY(), action.getZ());

        // Check if position is valid
        if (!client.world.getBlockState(pos).isAir()) {
            action.setFailureReason("Position is not empty");
            action.setCompleted(true);
            return false;
        }

        // Place block (simplified - would need to check for correct item, etc.)
        Direction direction = Direction.UP;
        BlockHitResult hitResult = new BlockHitResult(
                Vec3d.ofCenter(pos),
                direction,
                pos,
                false
        );

        client.interactionManager.interactBlock(player, Hand.MAIN_HAND, hitResult);

        action.setCompleted(true);
        return true;
    }

    /**
     * Use/interact with a block (door, button, chest, etc.).
     */
    private boolean handleUseBlock(MinecraftClient client, BlockAction action) {
        ClientPlayerEntity player = client.player;
        BlockPos pos = new BlockPos(action.getX(), action.getY(), action.getZ());

        // Check if block exists
        if (client.world.getBlockState(pos).isAir()) {
            action.setFailureReason("No block at position");
            action.setCompleted(true);
            return false;
        }

        // Interact with block
        Direction direction = Direction.UP;
        BlockHitResult hitResult = new BlockHitResult(
                Vec3d.ofCenter(pos),
                direction,
                pos,
                false
        );

        client.interactionManager.interactBlock(player, Hand.MAIN_HAND, hitResult);

        action.setCompleted(true);
        return true;
    }
}

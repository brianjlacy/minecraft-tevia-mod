package com.tevia.action;

import com.tevia.action.models.FarmingAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Controls farming and tool usage actions.
 */
public class FarmingController {

    /**
     * Execute a farming action.
     */
    public boolean execute(MinecraftClient client, FarmingAction action) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            action.setCompleted(true);
            return false;
        }

        switch (action.getType()) {
            case HOE_DIRT:
                return handleHoe(client, action);

            case PLANT_SEED:
                return handlePlant(client, action);

            case HARVEST_CROP:
                return handleHarvest(client, action);

            case BONE_MEAL:
                return handleBoneMeal(client, action);

            case FISH:
                return handleFish(client, action);

            case COLLECT_WATER:
            case COLLECT_LAVA:
                return handleCollectLiquid(client, action);

            default:
                action.setCompleted(true);
                return false;
        }
    }

    /**
     * Use hoe on dirt to create farmland.
     */
    private boolean handleHoe(MinecraftClient client, FarmingAction action) {
        BlockPos pos = new BlockPos(action.getX(), action.getY(), action.getZ());

        BlockHitResult hitResult = new BlockHitResult(
                Vec3d.ofCenter(pos),
                Direction.UP,
                pos,
                false
        );

        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
        action.setCompleted(true);
        return true;
    }

    /**
     * Plant seeds on farmland.
     */
    private boolean handlePlant(MinecraftClient client, FarmingAction action) {
        BlockPos pos = new BlockPos(action.getX(), action.getY(), action.getZ());

        BlockHitResult hitResult = new BlockHitResult(
                Vec3d.ofCenter(pos),
                Direction.UP,
                pos,
                false
        );

        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
        action.setCompleted(true);
        return true;
    }

    /**
     * Harvest a crop.
     */
    private boolean handleHarvest(MinecraftClient client, FarmingAction action) {
        BlockPos pos = new BlockPos(action.getX(), action.getY(), action.getZ());

        // Break the crop block
        client.interactionManager.attackBlock(pos, Direction.UP);
        action.setCompleted(true);
        return true;
    }

    /**
     * Use bone meal on a crop or sapling.
     */
    private boolean handleBoneMeal(MinecraftClient client, FarmingAction action) {
        BlockPos pos = new BlockPos(action.getX(), action.getY(), action.getZ());

        BlockHitResult hitResult = new BlockHitResult(
                Vec3d.ofCenter(pos),
                Direction.UP,
                pos,
                false
        );

        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
        action.setCompleted(true);
        return true;
    }

    /**
     * Use fishing rod.
     */
    private boolean handleFish(MinecraftClient client, FarmingAction action) {
        // Use item (fishing rod)
        client.options.useKey.setPressed(true);
        action.setCompleted(true);
        return true;
    }

    /**
     * Collect water or lava with bucket.
     */
    private boolean handleCollectLiquid(MinecraftClient client, FarmingAction action) {
        // Right-click on liquid with bucket
        client.options.useKey.setPressed(true);
        action.setCompleted(true);
        return true;
    }
}

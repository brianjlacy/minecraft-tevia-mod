package com.tevia.perception;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Scans surrounding blocks in the world.
 */
public class BlockScanner {

    private final int radius;

    public BlockScanner(int radius) {
        this.radius = Math.min(radius, 16); // Cap at 16 to avoid performance issues
    }

    /**
     * Scan surrounding blocks.
     */
    public Map<String, WorldState.BlockInfo> scan(World world, ClientPlayerEntity player) {
        Map<String, WorldState.BlockInfo> blocks = new HashMap<>();

        BlockPos playerPos = player.getBlockPos();

        // Scan important nearby positions
        scanRelativeBlocks(world, playerPos, blocks);

        // Scan a small radius for important blocks
        scanForImportantBlocks(world, playerPos, blocks);

        return blocks;
    }

    /**
     * Scan blocks at key relative positions (front, above, below, etc.).
     */
    private void scanRelativeBlocks(World world, BlockPos playerPos, Map<String, WorldState.BlockInfo> blocks) {
        // Below
        addBlock(world, playerPos.down(), "below", blocks);
        addBlock(world, playerPos.down(2), "below_2", blocks);

        // Above
        addBlock(world, playerPos.up(), "above", blocks);
        addBlock(world, playerPos.up(2), "above_2", blocks);

        // Cardinal directions
        for (Direction dir : Direction.Type.HORIZONTAL) {
            String dirName = dir.getName();
            addBlock(world, playerPos.offset(dir), dirName, blocks);
            addBlock(world, playerPos.offset(dir, 2), dirName + "_2", blocks);
        }
    }

    /**
     * Scan a small radius for important blocks (ores, chests, etc.).
     */
    private void scanForImportantBlocks(World world, BlockPos center, Map<String, WorldState.BlockInfo> blocks) {
        int scanRadius = Math.min(radius, 8); // Limit to 8 blocks for performance

        for (int x = -scanRadius; x <= scanRadius; x++) {
            for (int y = -scanRadius; y <= scanRadius; y++) {
                for (int z = -scanRadius; z <= scanRadius; z++) {
                    BlockPos pos = center.add(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    // Only include important blocks
                    if (isImportantBlock(block)) {
                        String key = String.format("important_%d_%d_%d", x, y, z);
                        String blockName = block.toString();
                        blocks.put(key, new WorldState.BlockInfo(
                                blockName,
                                pos.getX(), pos.getY(), pos.getZ(),
                                String.format("offset(%d,%d,%d)", x, y, z)
                        ));
                    }
                }
            }
        }

        // Limit to 30 blocks total
        if (blocks.size() > 30) {
            // Keep only the first 30 (this is a simple approach)
            Map<String, WorldState.BlockInfo> limited = new HashMap<>();
            int count = 0;
            for (Map.Entry<String, WorldState.BlockInfo> entry : blocks.entrySet()) {
                if (count++ >= 30) break;
                limited.put(entry.getKey(), entry.getValue());
            }
            blocks.clear();
            blocks.putAll(limited);
        }
    }

    /**
     * Add a block to the map if it's not air.
     */
    private void addBlock(World world, BlockPos pos, String position, Map<String, WorldState.BlockInfo> blocks) {
        BlockState state = world.getBlockState(pos);
        if (!state.isAir()) {
            String blockName = state.getBlock().toString();
            blocks.put(position, new WorldState.BlockInfo(
                    blockName,
                    pos.getX(), pos.getY(), pos.getZ(),
                    position
            ));
        }
    }

    /**
     * Check if a block is important (ores, chests, spawners, etc.).
     */
    private boolean isImportantBlock(Block block) {
        String blockName = block.toString().toLowerCase();
        return blockName.contains("ore") ||
                blockName.contains("chest") ||
                blockName.contains("spawner") ||
                blockName.contains("portal") ||
                blockName.contains("furnace") ||
                blockName.contains("crafting") ||
                blockName.contains("enchanting") ||
                blockName.contains("anvil") ||
                blockName.contains("bed") ||
                blockName.contains("door") ||
                blockName.contains("gate") ||
                blockName.contains("tnt") ||
                blockName.contains("diamond") ||
                blockName.contains("emerald");
    }
}

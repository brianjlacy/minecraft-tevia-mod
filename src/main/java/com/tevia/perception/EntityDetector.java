package com.tevia.perception;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects nearby entities in the world.
 */
public class EntityDetector {

    private final int radius;

    public EntityDetector(int radius) {
        this.radius = radius;
    }

    /**
     * Detect all nearby entities.
     */
    public List<WorldState.EntityInfo> detect(World world, ClientPlayerEntity player) {
        List<WorldState.EntityInfo> entities = new ArrayList<>();

        for (Entity entity : world.getEntities()) {
            // Skip the player themselves
            if (entity.equals(player)) {
                continue;
            }

            double distance = player.distanceTo(entity);

            // Only include entities within radius
            if (distance <= radius) {
                WorldState.EntityInfo info = createEntityInfo(entity, distance);
                if (info != null) {
                    entities.add(info);
                }
            }
        }

        // Sort by distance (closest first)
        entities.sort((a, b) -> Double.compare(a.getDistance(), b.getDistance()));

        // Limit to 20 closest entities to avoid overwhelming the LLM
        if (entities.size() > 20) {
            entities = entities.subList(0, 20);
        }

        return entities;
    }

    /**
     * Create EntityInfo from a Minecraft entity.
     */
    private WorldState.EntityInfo createEntityInfo(Entity entity, double distance) {
        WorldState.EntityInfo info = new WorldState.EntityInfo();

        info.setType(entity.getType().toString());
        info.setDistance(Math.round(distance * 100.0) / 100.0); // Round to 2 decimals
        info.setX(entity.getX());
        info.setY(entity.getY());
        info.setZ(entity.getZ());

        // Set name if available
        if (entity.hasCustomName()) {
            info.setName(entity.getCustomName().getString());
        } else if (entity instanceof PlayerEntity) {
            info.setName(((PlayerEntity) entity).getGameProfile().getName());
        } else {
            info.setName(entity.getName().getString());
        }

        // Get health if entity is living
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            info.setHealth(living.getHealth());
        }

        // Check if hostile
        info.setHostile(entity instanceof HostileEntity);

        return info;
    }
}

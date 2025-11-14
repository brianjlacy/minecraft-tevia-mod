package com.tevia.action;

import com.tevia.action.models.EntityInteractionAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Hand;

/**
 * Controls entity interaction actions (riding, feeding, breeding, etc.).
 */
public class EntityInteractionController {

    /**
     * Execute an entity interaction action.
     */
    public boolean execute(MinecraftClient client, EntityInteractionAction action) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            action.setCompleted(true);
            return false;
        }

        switch (action.getType()) {
            case RIDE_ENTITY:
                return handleRide(client, action);

            case DISMOUNT_ENTITY:
                return handleDismount(player, action);

            case FEED_ENTITY:
                return handleFeed(client, action);

            case BREED_ENTITY:
                return handleBreed(client, action);

            case SHEAR_ENTITY:
                return handleShear(client, action);

            case MILK_ENTITY:
                return handleMilk(client, action);

            case LEASH_ENTITY:
            case UNLEASH_ENTITY:
                return handleLeash(client, action);

            default:
                action.setCompleted(true);
                return false;
        }
    }

    /**
     * Ride an entity (horse, boat, minecart, etc.).
     */
    private boolean handleRide(MinecraftClient client, EntityInteractionAction action) {
        // Find nearby rideable entity
        Entity nearestRideable = findNearbyEntity(client, action.getTargetEntity(), 5.0);

        if (nearestRideable != null && !client.player.hasVehicle()) {
            // Right-click on entity to ride
            client.interactionManager.interactEntity(client.player, nearestRideable, Hand.MAIN_HAND);
            action.setCompleted(true);
            return true;
        }

        action.setFailureReason("No rideable entity nearby");
        action.setCompleted(true);
        return false;
    }

    /**
     * Dismount from current vehicle.
     */
    private boolean handleDismount(ClientPlayerEntity player, EntityInteractionAction action) {
        if (player.hasVehicle()) {
            player.stopRiding();
            action.setCompleted(true);
            return true;
        }

        action.setFailureReason("Not riding anything");
        action.setCompleted(true);
        return false;
    }

    /**
     * Feed an animal.
     */
    private boolean handleFeed(MinecraftClient client, EntityInteractionAction action) {
        Entity animal = findNearbyEntity(client, action.getTargetEntity(), 5.0);

        if (animal instanceof AnimalEntity) {
            // Right-click with food item
            client.interactionManager.interactEntity(client.player, animal, Hand.MAIN_HAND);
            action.setCompleted(true);
            return true;
        }

        action.setFailureReason("No feedable animal nearby");
        action.setCompleted(true);
        return false;
    }

    /**
     * Breed animals.
     */
    private boolean handleBreed(MinecraftClient client, EntityInteractionAction action) {
        // Similar to feeding - right-click two animals with breeding item
        return handleFeed(client, action);
    }

    /**
     * Shear a sheep or similar entity.
     */
    private boolean handleShear(MinecraftClient client, EntityInteractionAction action) {
        Entity entity = findNearbyEntity(client, action.getTargetEntity(), 5.0);

        if (entity != null) {
            client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
            action.setCompleted(true);
            return true;
        }

        action.setFailureReason("No shearable entity nearby");
        action.setCompleted(true);
        return false;
    }

    /**
     * Milk a cow or mooshroom.
     */
    private boolean handleMilk(MinecraftClient client, EntityInteractionAction action) {
        Entity entity = findNearbyEntity(client, action.getTargetEntity(), 5.0);

        if (entity != null) {
            client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
            action.setCompleted(true);
            return true;
        }

        action.setFailureReason("No milkable entity nearby");
        action.setCompleted(true);
        return false;
    }

    /**
     * Leash or unleash an entity.
     */
    private boolean handleLeash(MinecraftClient client, EntityInteractionAction action) {
        Entity entity = findNearbyEntity(client, action.getTargetEntity(), 5.0);

        if (entity != null) {
            client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
            action.setCompleted(true);
            return true;
        }

        action.setFailureReason("No entity nearby to leash");
        action.setCompleted(true);
        return false;
    }

    /**
     * Find a nearby entity by type.
     */
    private Entity findNearbyEntity(MinecraftClient client, String entityType, double maxDistance) {
        if (client.world == null || client.player == null) {
            return null;
        }

        Entity nearest = null;
        double nearestDistance = maxDistance;

        for (Entity entity : client.world.getEntities()) {
            if (entity.equals(client.player)) {
                continue;
            }

            double distance = client.player.distanceTo(entity);
            if (distance < nearestDistance) {
                String type = entity.getType().toString();
                if (entityType == null || type.contains(entityType.toLowerCase())) {
                    nearest = entity;
                    nearestDistance = distance;
                }
            }
        }

        return nearest;
    }
}

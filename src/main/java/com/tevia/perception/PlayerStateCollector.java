package com.tevia.perception;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects information about the player's current state.
 */
public class PlayerStateCollector {

    /**
     * Collect player state information.
     */
    public WorldState.PlayerState collect(ClientPlayerEntity player) {
        WorldState.PlayerState state = new WorldState.PlayerState();

        // Position
        state.setX(player.getX());
        state.setY(player.getY());
        state.setZ(player.getZ());

        // Rotation
        state.setYaw(player.getYaw());
        state.setPitch(player.getPitch());

        // Health and hunger
        state.setHealth(player.getHealth());
        state.setFoodLevel(player.getHungerManager().getFoodLevel());
        state.setSaturation(player.getHungerManager().getSaturationLevel());

        // Experience
        state.setXpLevel(player.experienceLevel);

        // Movement state
        state.setOnGround(player.isOnGround());
        state.setInWater(player.isInWater() || player.isSubmergedInWater());
        state.setInLava(player.isInLava());
        state.setSprinting(player.isSprinting());
        state.setSneaking(player.isSneaking());

        // Active effects
        List<String> effects = new ArrayList<>();
        for (StatusEffectInstance effect : player.getStatusEffects()) {
            String effectName = effect.getEffectType().getName().getString();
            int duration = effect.getDuration() / 20; // Convert ticks to seconds
            int amplifier = effect.getAmplifier() + 1;
            effects.add(String.format("%s %d (%ds)", effectName, amplifier, duration));
        }
        state.setActiveEffects(effects);

        return state;
    }
}

package com.tevia.perception;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Senses environmental conditions.
 */
public class EnvironmentSensor {

    /**
     * Sense environment information.
     */
    public WorldState.EnvironmentInfo sense(World world, ClientPlayerEntity player) {
        WorldState.EnvironmentInfo info = new WorldState.EnvironmentInfo();

        BlockPos pos = player.getBlockPos();

        // Biome
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        info.setBiome(biomeEntry.getKey().map(key -> key.getValue().toString()).orElse("unknown"));

        // Weather
        if (world.isRaining()) {
            info.setWeather(world.isThundering() ? "thunderstorm" : "rain");
        } else {
            info.setWeather("clear");
        }

        // Time of day
        long worldTime = world.getTimeOfDay() % 24000;
        info.setWorldTime(worldTime);
        info.setTimeOfDay(getTimeOfDayString(worldTime));

        // Light level
        info.setLightLevel(world.getLightLevel(pos));

        // Dimension
        info.setDimension(world.getRegistryKey().getValue().toString());

        return info;
    }

    /**
     * Convert world time to human-readable time of day.
     */
    private String getTimeOfDayString(long time) {
        if (time < 1000) return "morning";
        else if (time < 6000) return "day";
        else if (time < 12000) return "noon";
        else if (time < 13000) return "afternoon";
        else if (time < 18000) return "evening";
        else if (time < 19000) return "dusk";
        else return "night";
    }
}

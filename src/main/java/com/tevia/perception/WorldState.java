package com.tevia.perception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the current state of the world as perceived by the AI.
 * This data structure is serialized to JSON and sent to the LLM.
 */
public class WorldState {

    // Player state
    private PlayerState player;

    // Inventory state
    private InventoryState inventory;

    // Nearby entities
    private List<EntityInfo> nearbyEntities;

    // Surrounding blocks
    private Map<String, BlockInfo> surroundingBlocks;

    // Environment
    private EnvironmentInfo environment;

    // Recent events
    private List<String> recentEvents;

    // Visual perception (optional)
    private String screenshotBase64;

    public WorldState() {
        this.nearbyEntities = new ArrayList<>();
        this.surroundingBlocks = new HashMap<>();
        this.recentEvents = new ArrayList<>();
    }

    // Getters and Setters

    public PlayerState getPlayer() {
        return player;
    }

    public void setPlayer(PlayerState player) {
        this.player = player;
    }

    public InventoryState getInventory() {
        return inventory;
    }

    public void setInventory(InventoryState inventory) {
        this.inventory = inventory;
    }

    public List<EntityInfo> getNearbyEntities() {
        return nearbyEntities;
    }

    public void setNearbyEntities(List<EntityInfo> nearbyEntities) {
        this.nearbyEntities = nearbyEntities;
    }

    public Map<String, BlockInfo> getSurroundingBlocks() {
        return surroundingBlocks;
    }

    public void setSurroundingBlocks(Map<String, BlockInfo> surroundingBlocks) {
        this.surroundingBlocks = surroundingBlocks;
    }

    public EnvironmentInfo getEnvironment() {
        return environment;
    }

    public void setEnvironment(EnvironmentInfo environment) {
        this.environment = environment;
    }

    public List<String> getRecentEvents() {
        return recentEvents;
    }

    public void setRecentEvents(List<String> recentEvents) {
        this.recentEvents = recentEvents;
    }

    public String getScreenshotBase64() {
        return screenshotBase64;
    }

    public void setScreenshotBase64(String screenshotBase64) {
        this.screenshotBase64 = screenshotBase64;
    }

    // Inner classes for structured data

    public static class PlayerState {
        private double x, y, z;
        private float yaw, pitch;
        private float health;
        private int foodLevel;
        private float saturation;
        private int xpLevel;
        private boolean isOnGround;
        private boolean isInWater;
        private boolean isInLava;
        private boolean isSprinting;
        private boolean isSneaking;
        private List<String> activeEffects;

        public PlayerState() {
            this.activeEffects = new ArrayList<>();
        }

        // Getters and setters
        public double getX() { return x; }
        public void setX(double x) { this.x = x; }
        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
        public double getZ() { return z; }
        public void setZ(double z) { this.z = z; }
        public float getYaw() { return yaw; }
        public void setYaw(float yaw) { this.yaw = yaw; }
        public float getPitch() { return pitch; }
        public void setPitch(float pitch) { this.pitch = pitch; }
        public float getHealth() { return health; }
        public void setHealth(float health) { this.health = health; }
        public int getFoodLevel() { return foodLevel; }
        public void setFoodLevel(int foodLevel) { this.foodLevel = foodLevel; }
        public float getSaturation() { return saturation; }
        public void setSaturation(float saturation) { this.saturation = saturation; }
        public int getXpLevel() { return xpLevel; }
        public void setXpLevel(int xpLevel) { this.xpLevel = xpLevel; }
        public boolean isOnGround() { return isOnGround; }
        public void setOnGround(boolean onGround) { isOnGround = onGround; }
        public boolean isInWater() { return isInWater; }
        public void setInWater(boolean inWater) { isInWater = inWater; }
        public boolean isInLava() { return isInLava; }
        public void setInLava(boolean inLava) { isInLava = inLava; }
        public boolean isSprinting() { return isSprinting; }
        public void setSprinting(boolean sprinting) { isSprinting = sprinting; }
        public boolean isSneaking() { return isSneaking; }
        public void setSneaking(boolean sneaking) { isSneaking = sneaking; }
        public List<String> getActiveEffects() { return activeEffects; }
        public void setActiveEffects(List<String> activeEffects) { this.activeEffects = activeEffects; }
    }

    public static class InventoryState {
        private List<ItemStack> inventory;
        private List<ItemStack> hotbar;
        private int selectedSlot;
        private ItemStack mainHand;
        private ItemStack offHand;
        private List<ItemStack> armor;

        public InventoryState() {
            this.inventory = new ArrayList<>();
            this.hotbar = new ArrayList<>();
            this.armor = new ArrayList<>();
        }

        public List<ItemStack> getInventory() { return inventory; }
        public void setInventory(List<ItemStack> inventory) { this.inventory = inventory; }
        public List<ItemStack> getHotbar() { return hotbar; }
        public void setHotbar(List<ItemStack> hotbar) { this.hotbar = hotbar; }
        public int getSelectedSlot() { return selectedSlot; }
        public void setSelectedSlot(int selectedSlot) { this.selectedSlot = selectedSlot; }
        public ItemStack getMainHand() { return mainHand; }
        public void setMainHand(ItemStack mainHand) { this.mainHand = mainHand; }
        public ItemStack getOffHand() { return offHand; }
        public void setOffHand(ItemStack offHand) { this.offHand = offHand; }
        public List<ItemStack> getArmor() { return armor; }
        public void setArmor(List<ItemStack> armor) { this.armor = armor; }
    }

    public static class ItemStack {
        private String item;
        private int count;
        private int slot;

        public ItemStack(String item, int count, int slot) {
            this.item = item;
            this.count = count;
            this.slot = slot;
        }

        public String getItem() { return item; }
        public void setItem(String item) { this.item = item; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public int getSlot() { return slot; }
        public void setSlot(int slot) { this.slot = slot; }
    }

    public static class EntityInfo {
        private String type;
        private String name;
        private double distance;
        private double x, y, z;
        private float health;
        private boolean isHostile;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getDistance() { return distance; }
        public void setDistance(double distance) { this.distance = distance; }
        public double getX() { return x; }
        public void setX(double x) { this.x = x; }
        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
        public double getZ() { return z; }
        public void setZ(double z) { this.z = z; }
        public float getHealth() { return health; }
        public void setHealth(float health) { this.health = health; }
        public boolean isHostile() { return isHostile; }
        public void setHostile(boolean hostile) { isHostile = hostile; }
    }

    public static class BlockInfo {
        private String type;
        private int x, y, z;
        private String relativePosition; // e.g., "front", "above", "below"

        public BlockInfo(String type, int x, int y, int z, String relativePosition) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.z = z;
            this.relativePosition = relativePosition;
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public int getX() { return x; }
        public void setX(int x) { this.x = x; }
        public int getY() { return y; }
        public void setY(int y) { this.y = y; }
        public int getZ() { return z; }
        public void setZ(int z) { this.z = z; }
        public String getRelativePosition() { return relativePosition; }
        public void setRelativePosition(String relativePosition) { this.relativePosition = relativePosition; }
    }

    public static class EnvironmentInfo {
        private String biome;
        private String weather;
        private String timeOfDay;
        private long worldTime;
        private int lightLevel;
        private String dimension;

        public String getBiome() { return biome; }
        public void setBiome(String biome) { this.biome = biome; }
        public String getWeather() { return weather; }
        public void setWeather(String weather) { this.weather = weather; }
        public String getTimeOfDay() { return timeOfDay; }
        public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }
        public long getWorldTime() { return worldTime; }
        public void setWorldTime(long worldTime) { this.worldTime = worldTime; }
        public int getLightLevel() { return lightLevel; }
        public void setLightLevel(int lightLevel) { this.lightLevel = lightLevel; }
        public String getDimension() { return dimension; }
        public void setDimension(String dimension) { this.dimension = dimension; }
    }
}

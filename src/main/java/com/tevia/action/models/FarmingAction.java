package com.tevia.action.models;

/**
 * Actions for farming and tool usage.
 */
public class FarmingAction extends Action {

    private int x, y, z;
    private String cropType;
    private String toolType;

    public FarmingAction(ActionType type) {
        super(type);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    @Override
    public String getDescription() {
        switch (getType()) {
            case HOE_DIRT:
                return String.format("Hoe dirt at (%d, %d, %d)", x, y, z);
            case PLANT_SEED:
                return String.format("Plant %s at (%d, %d, %d)", cropType, x, y, z);
            case HARVEST_CROP:
                return String.format("Harvest crop at (%d, %d, %d)", x, y, z);
            case BONE_MEAL:
                return String.format("Use bone meal at (%d, %d, %d)", x, y, z);
            case FISH:
                return "Cast fishing rod";
            case COLLECT_WATER:
                return "Collect water with bucket";
            case COLLECT_LAVA:
                return "Collect lava with bucket";
            default:
                return "Farming action";
        }
    }

    public static FarmingAction hoe(int x, int y, int z) {
        FarmingAction action = new FarmingAction(ActionType.HOE_DIRT);
        action.setX(x);
        action.setY(y);
        action.setZ(z);
        return action;
    }

    public static FarmingAction plant(String crop, int x, int y, int z) {
        FarmingAction action = new FarmingAction(ActionType.PLANT_SEED);
        action.setCropType(crop);
        action.setX(x);
        action.setY(y);
        action.setZ(z);
        return action;
    }

    public static FarmingAction fish() {
        return new FarmingAction(ActionType.FISH);
    }
}

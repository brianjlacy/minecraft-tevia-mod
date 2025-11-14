package com.tevia.action.models;

/**
 * Block interaction actions.
 */
public class BlockAction extends Action {

    private int x, y, z;
    private String blockType;
    private String direction;

    public BlockAction(ActionType type) {
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

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String getDescription() {
        switch (getType()) {
            case MINE_BLOCK:
                return String.format("Mine block at (%d, %d, %d)", x, y, z);
            case PLACE_BLOCK:
                return String.format("Place %s at (%d, %d, %d)", blockType, x, y, z);
            case USE_BLOCK:
                return String.format("Use block at (%d, %d, %d)", x, y, z);
            default:
                return getType().toString();
        }
    }

    public static BlockAction mine(int x, int y, int z) {
        BlockAction action = new BlockAction(ActionType.MINE_BLOCK);
        action.setX(x);
        action.setY(y);
        action.setZ(z);
        return action;
    }

    public static BlockAction place(String blockType, int x, int y, int z) {
        BlockAction action = new BlockAction(ActionType.PLACE_BLOCK);
        action.setBlockType(blockType);
        action.setX(x);
        action.setY(y);
        action.setZ(z);
        return action;
    }

    public static BlockAction use(int x, int y, int z) {
        BlockAction action = new BlockAction(ActionType.USE_BLOCK);
        action.setX(x);
        action.setY(y);
        action.setZ(z);
        return action;
    }
}

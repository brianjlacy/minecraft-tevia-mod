package com.tevia.action.models;

/**
 * Actions for container interactions (chests, furnaces, etc.).
 */
public class ContainerAction extends Action {

    private int x, y, z;
    private String containerType;
    private int slot;
    private String itemName;
    private int quantity;

    public ContainerAction(ActionType type) {
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

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getDescription() {
        switch (getType()) {
            case OPEN_CONTAINER:
                return String.format("Open %s at (%d, %d, %d)", containerType, x, y, z);
            case CLOSE_CONTAINER:
                return "Close container";
            case TAKE_FROM_CONTAINER:
                return String.format("Take %dx %s from slot %d", quantity, itemName, slot);
            case PUT_IN_CONTAINER:
                return String.format("Put %dx %s in slot %d", quantity, itemName, slot);
            default:
                return "Container interaction";
        }
    }

    public static ContainerAction open(String type, int x, int y, int z) {
        ContainerAction action = new ContainerAction(ActionType.OPEN_CONTAINER);
        action.setContainerType(type);
        action.setX(x);
        action.setY(y);
        action.setZ(z);
        return action;
    }

    public static ContainerAction takeItem(String item, int quantity, int slot) {
        ContainerAction action = new ContainerAction(ActionType.TAKE_FROM_CONTAINER);
        action.setItemName(item);
        action.setQuantity(quantity);
        action.setSlot(slot);
        return action;
    }
}

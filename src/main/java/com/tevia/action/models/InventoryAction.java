package com.tevia.action.models;

/**
 * Inventory-related actions.
 */
public class InventoryAction extends Action {

    private int slot;
    private int targetSlot;
    private String itemName;

    public InventoryAction(ActionType type) {
        super(type);
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getTargetSlot() {
        return targetSlot;
    }

    public void setTargetSlot(int targetSlot) {
        this.targetSlot = targetSlot;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String getDescription() {
        switch (getType()) {
            case SELECT_HOTBAR_SLOT:
                return "Select hotbar slot " + slot;
            case SWAP_ITEMS:
                return "Swap items between slots " + slot + " and " + targetSlot;
            case DROP_ITEM:
                return itemName != null ? "Drop " + itemName : "Drop item in slot " + slot;
            case EQUIP_ARMOR:
                return "Equip armor from slot " + slot;
            default:
                return getType().toString();
        }
    }

    public static InventoryAction selectSlot(int slot) {
        InventoryAction action = new InventoryAction(ActionType.SELECT_HOTBAR_SLOT);
        action.setSlot(slot);
        return action;
    }

    public static InventoryAction dropItem(int slot) {
        InventoryAction action = new InventoryAction(ActionType.DROP_ITEM);
        action.setSlot(slot);
        return action;
    }
}

package com.tevia.action.models;

/**
 * Actions related to crafting items.
 */
public class CraftingAction extends Action {

    private String itemToCraft;
    private int quantity;
    private String[] ingredients;
    private boolean useWorkbench;

    public CraftingAction(ActionType type) {
        super(type);
        this.quantity = 1;
    }

    public String getItemToCraft() {
        return itemToCraft;
    }

    public void setItemToCraft(String itemToCraft) {
        this.itemToCraft = itemToCraft;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isUseWorkbench() {
        return useWorkbench;
    }

    public void setUseWorkbench(boolean useWorkbench) {
        this.useWorkbench = useWorkbench;
    }

    @Override
    public String getDescription() {
        return String.format("Craft %dx %s", quantity, itemToCraft);
    }

    public static CraftingAction craft(String item, int quantity) {
        CraftingAction action = new CraftingAction(ActionType.CRAFT_ITEM);
        action.setItemToCraft(item);
        action.setQuantity(quantity);
        return action;
    }
}

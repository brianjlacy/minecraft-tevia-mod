package com.tevia.action.models;

/**
 * Actions for special interactions (enchanting, brewing, sleeping, etc.).
 */
public class SpecialAction extends Action {

    private String targetItem;
    private String text;
    private int x, y, z;
    private int value;

    public SpecialAction(ActionType type) {
        super(type);
    }

    public String getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(String targetItem) {
        this.targetItem = targetItem;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String getDescription() {
        switch (getType()) {
            case SLEEP:
                return "Sleep in bed";
            case WAKE_UP:
                return "Wake up";
            case ENCHANT_ITEM:
                return "Enchant " + targetItem;
            case BREW_POTION:
                return "Brew potion";
            case WRITE_BOOK:
                return "Write in book";
            case EDIT_SIGN:
                return "Edit sign";
            case FLIP_LEVER:
                return String.format("Flip lever at (%d, %d, %d)", x, y, z);
            case PRESS_BUTTON:
                return String.format("Press button at (%d, %d, %d)", x, y, z);
            case EAT_FOOD:
                return "Eat " + targetItem;
            case DRINK_POTION:
                return "Drink " + targetItem;
            case SHOOT_BOW:
                return "Shoot bow";
            case THROW_ITEM:
                return "Throw " + targetItem;
            default:
                return "Special action";
        }
    }

    public static SpecialAction sleep() {
        return new SpecialAction(ActionType.SLEEP);
    }

    public static SpecialAction eat(String food) {
        SpecialAction action = new SpecialAction(ActionType.EAT_FOOD);
        action.setTargetItem(food);
        return action;
    }

    public static SpecialAction shootBow() {
        return new SpecialAction(ActionType.SHOOT_BOW);
    }

    public static SpecialAction throwItem(String item) {
        SpecialAction action = new SpecialAction(ActionType.THROW_ITEM);
        action.setTargetItem(item);
        return action;
    }
}

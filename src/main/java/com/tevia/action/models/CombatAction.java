package com.tevia.action.models;

/**
 * Combat-related actions.
 */
public class CombatAction extends Action {

    private String targetEntity;
    private boolean continuous;

    public CombatAction(ActionType type) {
        super(type);
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }

    public boolean isContinuous() {
        return continuous;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    @Override
    public String getDescription() {
        switch (getType()) {
            case ATTACK:
                return targetEntity != null ? "Attack " + targetEntity : "Attack";
            case USE_ITEM:
                return "Use item in hand";
            case BLOCK:
                return "Block with shield";
            default:
                return getType().toString();
        }
    }

    public static CombatAction attack() {
        return new CombatAction(ActionType.ATTACK);
    }

    public static CombatAction attackTarget(String target) {
        CombatAction action = new CombatAction(ActionType.ATTACK);
        action.setTargetEntity(target);
        return action;
    }

    public static CombatAction useItem() {
        return new CombatAction(ActionType.USE_ITEM);
    }
}

package com.tevia.action.models;

/**
 * Actions for interacting with entities (riding, feeding, breeding, etc.).
 */
public class EntityInteractionAction extends Action {

    private String targetEntity;
    private double targetX, targetY, targetZ;
    private String itemToUse;

    public EntityInteractionAction(ActionType type) {
        super(type);
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }

    public double getTargetX() {
        return targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    public double getTargetZ() {
        return targetZ;
    }

    public void setTargetZ(double targetZ) {
        this.targetZ = targetZ;
    }

    public String getItemToUse() {
        return itemToUse;
    }

    public void setItemToUse(String itemToUse) {
        this.itemToUse = itemToUse;
    }

    @Override
    public String getDescription() {
        switch (getType()) {
            case RIDE_ENTITY:
                return "Ride " + targetEntity;
            case DISMOUNT_ENTITY:
                return "Dismount";
            case FEED_ENTITY:
                return "Feed " + targetEntity + " with " + itemToUse;
            case BREED_ENTITY:
                return "Breed " + targetEntity;
            case SHEAR_ENTITY:
                return "Shear " + targetEntity;
            case MILK_ENTITY:
                return "Milk " + targetEntity;
            case LEASH_ENTITY:
                return "Leash " + targetEntity;
            case UNLEASH_ENTITY:
                return "Unleash " + targetEntity;
            default:
                return "Interact with " + targetEntity;
        }
    }

    public static EntityInteractionAction ride(String entityType) {
        EntityInteractionAction action = new EntityInteractionAction(ActionType.RIDE_ENTITY);
        action.setTargetEntity(entityType);
        return action;
    }

    public static EntityInteractionAction dismount() {
        return new EntityInteractionAction(ActionType.DISMOUNT_ENTITY);
    }

    public static EntityInteractionAction feed(String entityType, String item) {
        EntityInteractionAction action = new EntityInteractionAction(ActionType.FEED_ENTITY);
        action.setTargetEntity(entityType);
        action.setItemToUse(item);
        return action;
    }
}

package com.tevia.action.models;

/**
 * Movement-related actions.
 */
public class MovementAction extends Action {

    private float yaw;
    private float pitch;
    private int durationTicks;

    public MovementAction(ActionType type) {
        super(type);
        this.durationTicks = 1;
    }

    public MovementAction(ActionType type, int durationTicks) {
        super(type);
        this.durationTicks = durationTicks;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public void setDurationTicks(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    @Override
    public String getDescription() {
        switch (getType()) {
            case MOVE_FORWARD:
                return "Move forward for " + durationTicks + " ticks";
            case MOVE_BACKWARD:
                return "Move backward for " + durationTicks + " ticks";
            case STRAFE_LEFT:
                return "Strafe left for " + durationTicks + " ticks";
            case STRAFE_RIGHT:
                return "Strafe right for " + durationTicks + " ticks";
            case JUMP:
                return "Jump";
            case SPRINT:
                return "Toggle sprint";
            case SNEAK:
                return "Toggle sneak";
            case LOOK:
                return String.format("Look at yaw=%.1f, pitch=%.1f", yaw, pitch);
            case STOP_MOVEMENT:
                return "Stop all movement";
            default:
                return getType().toString();
        }
    }

    public static MovementAction forward(int ticks) {
        return new MovementAction(ActionType.MOVE_FORWARD, ticks);
    }

    public static MovementAction backward(int ticks) {
        return new MovementAction(ActionType.MOVE_BACKWARD, ticks);
    }

    public static MovementAction strafeLeft(int ticks) {
        return new MovementAction(ActionType.STRAFE_LEFT, ticks);
    }

    public static MovementAction strafeRight(int ticks) {
        return new MovementAction(ActionType.STRAFE_RIGHT, ticks);
    }

    public static MovementAction jump() {
        return new MovementAction(ActionType.JUMP);
    }

    public static MovementAction look(float yaw, float pitch) {
        MovementAction action = new MovementAction(ActionType.LOOK);
        action.setYaw(yaw);
        action.setPitch(pitch);
        return action;
    }
}

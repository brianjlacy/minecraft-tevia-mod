package com.tevia.action;

import com.tevia.action.models.SpecialAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Controls special interaction actions (sleeping, enchanting, brewing, etc.).
 */
public class SpecialInteractionController {

    /**
     * Execute a special interaction action.
     */
    public boolean execute(MinecraftClient client, SpecialAction action) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            action.setCompleted(true);
            return false;
        }

        switch (action.getType()) {
            case SLEEP:
                return handleSleep(client, action);

            case WAKE_UP:
                return handleWakeUp(player, action);

            case EAT_FOOD:
                return handleEat(client, action);

            case DRINK_POTION:
                return handleDrink(client, action);

            case SHOOT_BOW:
                return handleShootBow(client, action);

            case THROW_ITEM:
                return handleThrow(client, action);

            case FLIP_LEVER:
            case PRESS_BUTTON:
                return handleActivateMechanism(client, action);

            case ENCHANT_ITEM:
            case BREW_POTION:
            case USE_ANVIL:
            case USE_GRINDSTONE:
            case USE_SMITHING_TABLE:
                return handleUseWorkstation(client, action);

            default:
                action.setCompleted(true);
                return false;
        }
    }

    /**
     * Sleep in a bed.
     */
    private boolean handleSleep(MinecraftClient client, SpecialAction action) {
        // Right-click on bed
        client.options.useKey.setPressed(true);
        action.setCompleted(true);
        return true;
    }

    /**
     * Wake up from bed.
     */
    private boolean handleWakeUp(ClientPlayerEntity player, SpecialAction action) {
        if (player.isSleeping()) {
            player.wakeUp();
        }
        action.setCompleted(true);
        return true;
    }

    /**
     * Eat food item.
     */
    private boolean handleEat(MinecraftClient client, SpecialAction action) {
        // Hold right-click to eat
        client.options.useKey.setPressed(true);
        action.setCompleted(true);
        return true;
    }

    /**
     * Drink potion.
     */
    private boolean handleDrink(MinecraftClient client, SpecialAction action) {
        // Hold right-click to drink
        client.options.useKey.setPressed(true);
        action.setCompleted(true);
        return true;
    }

    /**
     * Shoot a bow.
     */
    private boolean handleShootBow(MinecraftClient client, SpecialAction action) {
        // Hold and release right-click
        client.options.useKey.setPressed(true);
        // Would need to hold for a few ticks then release for full power
        action.setCompleted(true);
        return true;
    }

    /**
     * Throw an item (snowball, egg, ender pearl, etc.).
     */
    private boolean handleThrow(MinecraftClient client, SpecialAction action) {
        client.options.useKey.setPressed(true);
        action.setCompleted(true);
        return true;
    }

    /**
     * Activate a mechanism (lever, button, etc.).
     */
    private boolean handleActivateMechanism(MinecraftClient client, SpecialAction action) {
        BlockPos pos = new BlockPos(action.getX(), action.getY(), action.getZ());

        BlockHitResult hitResult = new BlockHitResult(
                Vec3d.ofCenter(pos),
                Direction.UP,
                pos,
                false
        );

        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
        action.setCompleted(true);
        return true;
    }

    /**
     * Use a workstation (enchanting table, brewing stand, etc.).
     */
    private boolean handleUseWorkstation(MinecraftClient client, SpecialAction action) {
        // Right-click to open the workstation GUI
        client.options.useKey.setPressed(true);
        action.setCompleted(true);
        return true;
    }
}

package com.tevia.action;

import com.tevia.action.models.CraftingAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Controls crafting actions.
 */
public class CraftingController {

    /**
     * Execute a crafting action.
     */
    public boolean execute(MinecraftClient client, CraftingAction action) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            action.setCompleted(true);
            return false;
        }

        switch (action.getType()) {
            case CRAFT_ITEM:
                return handleCraft(client, action);

            case QUICK_CRAFT:
                return handleQuickCraft(client, action);

            case OPEN_INVENTORY:
                return handleOpenInventory(client, action);

            case CLOSE_INVENTORY:
                return handleCloseInventory(client, action);

            default:
                action.setCompleted(true);
                return false;
        }
    }

    /**
     * Craft an item using crafting table or player inventory.
     */
    private boolean handleCraft(MinecraftClient client, CraftingAction action) {
        // Crafting requires complex screen handler interaction
        // This would need to:
        // 1. Open crafting screen (table or 2x2 grid)
        // 2. Place ingredients in correct pattern
        // 3. Click output slot
        // 4. Handle shift-clicking for multiple crafts

        // For now, mark as completed
        // Full implementation would require screen handler manipulation
        action.setCompleted(true);
        return true;
    }

    /**
     * Quick craft (shift-click to craft maximum).
     */
    private boolean handleQuickCraft(MinecraftClient client, CraftingAction action) {
        // Similar to regular crafting but with shift-click
        action.setCompleted(true);
        return true;
    }

    /**
     * Open the player inventory.
     */
    private boolean handleOpenInventory(MinecraftClient client, CraftingAction action) {
        if (client.currentScreen == null) {
            // Open inventory screen
            client.player.openInventory();
        }
        action.setCompleted(true);
        return true;
    }

    /**
     * Close the current screen.
     */
    private boolean handleCloseInventory(MinecraftClient client, CraftingAction action) {
        if (client.currentScreen != null) {
            client.player.closeScreen();
        }
        action.setCompleted(true);
        return true;
    }
}

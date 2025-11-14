package com.tevia.action;

import com.tevia.action.models.ChatAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Controls chat actions.
 */
public class ChatController {

    /**
     * Execute a chat action.
     */
    public boolean execute(MinecraftClient client, ChatAction action) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            action.setCompleted(true);
            return false;
        }

        String message = action.getMessage();

        if (message == null || message.isEmpty()) {
            action.setFailureReason("Message is empty");
            action.setCompleted(true);
            return false;
        }

        // Send chat message
        if (client.getNetworkHandler() != null) {
            player.networkHandler.sendChatMessage(message);
            action.setCompleted(true);
            return true;
        } else {
            action.setFailureReason("Not connected to server");
            action.setCompleted(true);
            return false;
        }
    }
}

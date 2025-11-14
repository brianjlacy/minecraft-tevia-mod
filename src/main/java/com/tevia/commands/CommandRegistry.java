package com.tevia.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.tevia.TeviaAutomationMod;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

/**
 * Registers Tevia mod commands.
 */
public class CommandRegistry {

    /**
     * Register all Tevia commands.
     */
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerTeviaCommand(dispatcher);
        });
    }

    /**
     * Register the main /tevia command.
     */
    private static void registerTeviaCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("tevia")
                .then(ClientCommandManager.literal("start")
                        .executes(CommandRegistry::startCommand))
                .then(ClientCommandManager.literal("stop")
                        .executes(CommandRegistry::stopCommand))
                .then(ClientCommandManager.literal("status")
                        .executes(CommandRegistry::statusCommand))
                .then(ClientCommandManager.literal("reload")
                        .executes(CommandRegistry::reloadCommand))
                .executes(CommandRegistry::helpCommand));
    }

    /**
     * Start the LLM automation.
     */
    private static int startCommand(CommandContext<FabricClientCommandSource> context) {
        try {
            TeviaAutomationMod mod = TeviaAutomationMod.getInstance();
            if (mod.isRunning()) {
                context.getSource().sendFeedback(Text.literal("§eTev ia automation is already running"));
            } else {
                mod.start();
                context.getSource().sendFeedback(Text.literal("§aTevia automation started"));
            }
        } catch (Exception e) {
            context.getSource().sendError(Text.literal("§cFailed to start Tevia: " + e.getMessage()));
            TeviaAutomationMod.getLogger().error("Failed to start automation", e);
        }
        return 1;
    }

    /**
     * Stop the LLM automation.
     */
    private static int stopCommand(CommandContext<FabricClientCommandSource> context) {
        try {
            TeviaAutomationMod mod = TeviaAutomationMod.getInstance();
            if (!mod.isRunning()) {
                context.getSource().sendFeedback(Text.literal("§eTevia automation is not running"));
            } else {
                mod.stop();
                context.getSource().sendFeedback(Text.literal("§cTevia automation stopped"));
            }
        } catch (Exception e) {
            context.getSource().sendError(Text.literal("§cFailed to stop Tevia: " + e.getMessage()));
            TeviaAutomationMod.getLogger().error("Failed to stop automation", e);
        }
        return 1;
    }

    /**
     * Show status of the automation.
     */
    private static int statusCommand(CommandContext<FabricClientCommandSource> context) {
        try {
            TeviaAutomationMod mod = TeviaAutomationMod.getInstance();
            String status = mod.isRunning() ? "§aRunning" : "§cStopped";
            context.getSource().sendFeedback(Text.literal("§bTevia Status: " + status));

            // Show configuration info
            var config = mod.getConfigManager().getConfig();
            context.getSource().sendFeedback(Text.literal("§7API Provider: " + config.getApiProvider()));
            context.getSource().sendFeedback(Text.literal("§7Model: " +
                    (config.getApiProvider().equals("anthropic") ? config.getAnthropicModel() : config.getReplicateModel())));
            context.getSource().sendFeedback(Text.literal("§7Profile: " + config.getCharacterProfilePath()));
        } catch (Exception e) {
            context.getSource().sendError(Text.literal("§cFailed to get status: " + e.getMessage()));
        }
        return 1;
    }

    /**
     * Reload configuration and character profile.
     */
    private static int reloadCommand(CommandContext<FabricClientCommandSource> context) {
        try {
            TeviaAutomationMod mod = TeviaAutomationMod.getInstance();
            mod.getConfigManager().reloadConfig();
            mod.getLLMCoordinator().reloadProfile();
            context.getSource().sendFeedback(Text.literal("§aConfiguration and profile reloaded"));
        } catch (Exception e) {
            context.getSource().sendError(Text.literal("§cFailed to reload: " + e.getMessage()));
            TeviaAutomationMod.getLogger().error("Failed to reload configuration", e);
        }
        return 1;
    }

    /**
     * Show help message.
     */
    private static int helpCommand(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("§b§l=== Tevia LLM Automation Mod ==="));
        context.getSource().sendFeedback(Text.literal("§e/tevia start §7- Start LLM automation"));
        context.getSource().sendFeedback(Text.literal("§e/tevia stop §7- Stop LLM automation (emergency stop)"));
        context.getSource().sendFeedback(Text.literal("§e/tevia status §7- Show current status"));
        context.getSource().sendFeedback(Text.literal("§e/tevia reload §7- Reload configuration and profile"));
        return 1;
    }
}

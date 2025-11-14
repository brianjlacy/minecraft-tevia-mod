package com.tevia.perception;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes the player's inventory state.
 */
public class InventoryAnalyzer {

    /**
     * Analyze inventory state.
     */
    public WorldState.InventoryState analyze(ClientPlayerEntity player) {
        WorldState.InventoryState state = new WorldState.InventoryState();

        // Main inventory (slots 9-35)
        List<WorldState.ItemStack> inventory = new ArrayList<>();
        for (int i = 9; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isEmpty()) {
                inventory.add(createItemStack(stack, i));
            }
        }
        state.setInventory(inventory);

        // Hotbar (slots 0-8)
        List<WorldState.ItemStack> hotbar = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isEmpty()) {
                hotbar.add(createItemStack(stack, i));
            }
        }
        state.setHotbar(hotbar);

        // Selected slot
        state.setSelectedSlot(player.getInventory().selectedSlot);

        // Main hand
        ItemStack mainHandStack = player.getMainHandStack();
        if (!mainHandStack.isEmpty()) {
            state.setMainHand(createItemStack(mainHandStack, -1));
        }

        // Off hand
        ItemStack offHandStack = player.getOffHandStack();
        if (!offHandStack.isEmpty()) {
            state.setOffHand(createItemStack(offHandStack, -1));
        }

        // Armor
        List<WorldState.ItemStack> armor = new ArrayList<>();
        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack armorStack = player.getEquippedStack(slot);
            if (!armorStack.isEmpty()) {
                armor.add(createItemStack(armorStack, slot.getEntitySlotId()));
            }
        }
        state.setArmor(armor);

        return state;
    }

    /**
     * Create an ItemStack representation from Minecraft ItemStack.
     */
    private WorldState.ItemStack createItemStack(ItemStack stack, int slot) {
        String itemName = stack.getItem().toString();
        int count = stack.getCount();
        return new WorldState.ItemStack(itemName, count, slot);
    }
}

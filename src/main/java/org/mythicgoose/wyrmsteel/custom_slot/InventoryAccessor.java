package org.mythicgoose.wyrmsteel.custom_slot;

import net.minecraft.world.item.ItemStack;

public interface InventoryAccessor {
    ItemStack weapons_of_death$getWeaponStashSlot();
    void weapons_of_death$setWeaponStashSlot(ItemStack stack);
}
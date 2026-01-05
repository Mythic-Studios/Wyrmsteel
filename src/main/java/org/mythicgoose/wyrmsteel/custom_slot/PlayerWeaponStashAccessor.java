package org.mythicgoose.wyrmsteel.custom_slot;

import net.minecraft.world.item.ItemStack;

public interface PlayerWeaponStashAccessor {
    ItemStack weapons_of_death$getWeaponStash();
    void weapons_of_death$setWeaponStash(ItemStack stack);
}

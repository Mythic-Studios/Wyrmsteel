package org.mythicgoose.wyrmsteel.custom_slot;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.world.item.ItemStack;

public class PlayerDeathHandler {

    public static void register() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            // Get the old stash
            ItemStack oldStash = ((PlayerWeaponStashAccessor) oldPlayer).weapons_of_death$getWeaponStash();

            // DEBUG: Print what we're trying to copy
            System.out.println("DEATH HANDLER:");
            System.out.println("  Old player has: " + oldStash.getCount() + " " + oldStash.getItem());
            System.out.println("  New player has (before copy): " + ((PlayerWeaponStashAccessor) newPlayer).weapons_of_death$getWeaponStash().getCount() + " " + ((PlayerWeaponStashAccessor) newPlayer).weapons_of_death$getWeaponStash().getItem());

            // Copy it to the new player's custom slot
            if (!oldStash.isEmpty()) {
                ((PlayerWeaponStashAccessor) newPlayer).weapons_of_death$setWeaponStash(oldStash.copy());
                System.out.println("  Copied successfully!");
            } else {
                System.out.println("  Old stash was empty, nothing to copy");
            }

            System.out.println("  New player has (after copy): " + ((PlayerWeaponStashAccessor) newPlayer).weapons_of_death$getWeaponStash().getCount() + " " + ((PlayerWeaponStashAccessor) newPlayer).weapons_of_death$getWeaponStash().getItem());

            // Clear it from regular inventory if it somehow got added there
            for (int i = 0; i < newPlayer.getInventory().items.size(); i++) {
                ItemStack stack = newPlayer.getInventory().items.get(i);
                if (!stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, oldStash)) {
                    newPlayer.getInventory().items.set(i, ItemStack.EMPTY);
                    System.out.println("  Removed duplicate from slot " + i);
                }
            }
        });
    }
}
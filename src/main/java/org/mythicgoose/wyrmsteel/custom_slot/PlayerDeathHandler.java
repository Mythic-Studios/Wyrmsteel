package org.mythicgoose.wyrmsteel.custom_slot;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.world.item.ItemStack;

public class PlayerDeathHandler {

    public static void register() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            // Get the old stash
            ItemStack oldStash = ((PlayerWeaponStashAccessor) oldPlayer).weapons_of_death$getWeaponStash();

            // Copy it to the new player's custom slot
            if (!oldStash.isEmpty()) {
                ((PlayerWeaponStashAccessor) newPlayer).weapons_of_death$setWeaponStash(oldStash.copy());
            }

            // Important: Remove it from regular inventory if it somehow got added there
            // This prevents duplication
            newPlayer.getInventory().items.removeIf(stack ->
                    !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, oldStash)
            );
        });
    }
}
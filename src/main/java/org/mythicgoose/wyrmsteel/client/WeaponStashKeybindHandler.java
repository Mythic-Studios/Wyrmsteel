// WeaponStashKeybindHandler.java - Modified Version
package org.mythicgoose.wyrmsteel.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.mythicgoose.wyrmsteel.custom_slot.InventoryAccessor;
import org.mythicgoose.wyrmsteel.keybinding.KeybindRegistry;

public class WeaponStashKeybindHandler {

    private static int lastHotbarSlot = -1;
    private static int ticksSinceEquip = 0;
    private static ItemStack previousMainHandItem = ItemStack.EMPTY;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            LocalPlayer player = client.player;
            ItemStack weaponStack = ((InventoryAccessor) player.getInventory())
                    .weapons_of_death$getWeaponStashSlot();

            if (KeybindRegistry.EQUIP_BACK_WEAPON.consumeClick()) {
                if (!weaponStack.isEmpty()) {
                    if (!WeaponStashState.isEquipped()) {
                        // Save current item and swap
                        previousMainHandItem = player.getMainHandItem().copy();

                        // Temporarily put weapon in hand (client-side visual only)
                        WeaponStashState.setEquipped(true);
                        WeaponStashState.setCachedStack(weaponStack.copy());
                        WeaponStashState.setPreviousHotbarSlot(player.getInventory().selected);
                        lastHotbarSlot = player.getInventory().selected;
                        ticksSinceEquip = 0;
                    } else {
                        // Restore
                        WeaponStashState.reset();
                        lastHotbarSlot = -1;
                        previousMainHandItem = ItemStack.EMPTY;
                    }
                }
            }

            if (WeaponStashState.isEquipped()) {
                ticksSinceEquip++;

                if (ticksSinceEquip > 2 && player.getInventory().selected != lastHotbarSlot) {
                    WeaponStashState.reset();
                    lastHotbarSlot = -1;
                    previousMainHandItem = ItemStack.EMPTY;
                }
            }
        });
    }
}
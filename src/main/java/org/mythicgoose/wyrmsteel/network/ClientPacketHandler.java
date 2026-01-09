package org.mythicgoose.wyrmsteel.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.mythicgoose.wyrmsteel.custom_slot.WeaponStashSlot;

public class ClientPacketHandler {

    /**
     * Register all client-side packet handlers
     * Call this from your client mod initializer
     */
    public static void registerClientHandlers() {
        // Handle weapon stash sync from server
        ClientPlayNetworking.registerGlobalReceiver(
                S2CBackWeaponSyncPacket.TYPE,
                (payload, context) -> {
                    context.client().execute(() -> {
                        Minecraft client = context.client();

                        // Get the player by ID
                        Player player = null;
                        if (client.level != null) {
                            player = (Player) client.level.getEntity(payload.playerId());
                        }

                        // If it's the local player, update their slot
                        if (player == client.player && player != null) {
                            if (player.inventoryMenu instanceof InventoryMenu menu) {
                                // Find the weapon stash slot
                                for (Slot slot : menu.slots) {
                                    if (slot instanceof WeaponStashSlot weaponSlot) {
                                        // Use setQuietly to avoid sending packet back
                                        weaponSlot.setQuietly(payload.weaponStack().copy());
                                        System.out.println("CLIENT: Synced weapon stash to: " + payload.weaponStack());
                                        break;
                                    }
                                }
                            }
                        }
                        // For other players, you might render it on their model/HUD
                        // but that's optional and depends on your use case
                    });
                }
        );
    }
}
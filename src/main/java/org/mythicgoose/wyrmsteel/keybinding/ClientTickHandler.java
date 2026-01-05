package org.mythicgoose.wyrmsteel.keybinding;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import org.mythicgoose.wyrmsteel.network.WeaponStashSwapPayload;

public class ClientTickHandler {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeybindRegistry.swapSlots.consumeClick()) {
                handleWeaponStashSwap(client);
            }
        });
    }

    private static void handleWeaponStashSwap(Minecraft client) {
        if (client.player == null) return;

        // Send packet to server to perform the swap
        ClientPlayNetworking.send(new WeaponStashSwapPayload());
    }
}
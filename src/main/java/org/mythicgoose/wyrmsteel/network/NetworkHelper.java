package org.mythicgoose.wyrmsteel.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class NetworkHelper {

    public static void syncBackWeaponToClients(ServerPlayer player, ItemStack weaponStack) {
        S2CBackWeaponSyncPacket packet = new S2CBackWeaponSyncPacket(
                player.getId(),
                weaponStack
        );

        // Send to the player
        ServerPlayNetworking.send(player, packet);

        // Send to all OTHER players tracking this player
        for (ServerPlayer trackingPlayer : PlayerLookup.tracking(player)) {
            if (trackingPlayer != player) {
                ServerPlayNetworking.send(trackingPlayer, packet);
            }
        }
    }
}
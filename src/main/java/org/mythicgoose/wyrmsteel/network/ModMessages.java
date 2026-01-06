package org.mythicgoose.wyrmsteel.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class ModMessages {

    public static void registerC2SPackets() {
        // Register client-to-server packets here if needed
    }

    public static void registerS2CPackets() {
        PayloadTypeRegistry.playS2C().register(
                TotemAnimationPayload.ID,
                TotemAnimationPayload.CODEC
        );
    }

    public static void sendToPlayer(ServerPlayer player, TotemAnimationPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }
}
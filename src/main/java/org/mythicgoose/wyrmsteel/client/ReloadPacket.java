package org.mythicgoose.wyrmsteel.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.item.HeadhunterItem;

public class ReloadPacket {

    // Payload record for the reload packet
    public record ReloadPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ReloadPayload> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "reload"));

        public static final StreamCodec<FriendlyByteBuf, ReloadPayload> CODEC =
                StreamCodec.unit(new ReloadPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    // Register the payload type
    public static void register() {
        // Register on both sides
        PayloadTypeRegistry.playC2S().register(ReloadPayload.TYPE, ReloadPayload.CODEC);
    }

    // Client side - send packet to server
    public static void send() {
        ClientPlayNetworking.send(new ReloadPayload());
    }

    // Server side - register packet handler
    public static void registerServerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(ReloadPayload.TYPE, (payload, context) -> {
            context.player().server.execute(() -> {
                var player = context.player();

                // Check main hand
                var mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (mainHand.getItem() instanceof HeadhunterItem headhunter) {
                    headhunter.reload(player.level(), player, mainHand);
                    return;
                }

                // Check off hand
                var offHand = player.getItemInHand(InteractionHand.OFF_HAND);
                if (offHand.getItem() instanceof HeadhunterItem headhunter) {
                    headhunter.reload(player.level(), player, offHand);
                }
            });
        });
    }
}
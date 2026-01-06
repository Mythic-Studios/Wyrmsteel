package org.mythicgoose.wyrmsteel.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public record TotemAnimationPayload(ItemStack itemStack) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TotemAnimationPayload> ID =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("wyrmsteel", "totem_animation"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TotemAnimationPayload> CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            TotemAnimationPayload::itemStack,
            TotemAnimationPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static void handle(TotemAnimationPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            Minecraft mc = context.client();
            if (mc.player != null) {
                mc.gameRenderer.displayItemActivation(payload.itemStack());
                mc.player.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
            }
        });
    }
}
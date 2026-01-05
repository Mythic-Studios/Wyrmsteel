package org.mythicgoose.wyrmsteel.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record S2CBackWeaponSyncPacket(int playerId, ItemStack weaponStack) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CBackWeaponSyncPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("weapons_of_death", "sync_back_weapon"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CBackWeaponSyncPacket> CODEC =
            StreamCodec.composite(
                    StreamCodec.of(
                            FriendlyByteBuf::writeVarInt,
                            RegistryFriendlyByteBuf::readVarInt
                    ),
                    S2CBackWeaponSyncPacket::playerId,
                    ItemStack.OPTIONAL_STREAM_CODEC,
                    S2CBackWeaponSyncPacket::weaponStack,
                    S2CBackWeaponSyncPacket::new
            );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
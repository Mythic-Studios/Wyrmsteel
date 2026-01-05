package org.mythicgoose.wyrmsteel.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.custom_slot.InventoryAccessor;

public record C2SWeaponStashSlotClickPacket(ItemStack newStack) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SWeaponStashSlotClickPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "weapon_stash_click"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SWeaponStashSlotClickPacket> CODEC =
            StreamCodec.composite(
                    ItemStack.OPTIONAL_STREAM_CODEC,
                    C2SWeaponStashSlotClickPacket::newStack,
                    C2SWeaponStashSlotClickPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(TYPE, CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
            context.player().server.execute(() -> {
                ServerPlayer player = context.player();
                Inventory inventory = player.getInventory();

                System.out.println("SERVER: Received slot click packet - setting to: " + payload.newStack());

                // Update the weapon stash slot on the server
                ((InventoryAccessor) inventory).weapons_of_death$setWeaponStashSlot(payload.newStack());
            });
        });
    }
}
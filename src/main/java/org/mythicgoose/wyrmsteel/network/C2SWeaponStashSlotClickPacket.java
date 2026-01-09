package org.mythicgoose.wyrmsteel.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.custom_slot.WeaponStashSlot;

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
        ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
            context.player().server.execute(() -> {
                ServerPlayer player = context.player();

                // Find the weapon stash slot in the inventory menu
                if (!(player.inventoryMenu instanceof InventoryMenu menu)) {
                    return;
                }

                WeaponStashSlot weaponSlot = null;
                for (Slot slot : menu.slots) {
                    if (slot instanceof WeaponStashSlot stashSlot) {
                        weaponSlot = stashSlot;
                        break;
                    }
                }

                if (weaponSlot != null) {
                    System.out.println("SERVER: Received click packet, setting to: " + payload.newStack());

                    // Use setQuietly to update without triggering another packet
                    weaponSlot.setQuietly(payload.newStack().copy());

                    // Mark as changed so client gets updated
                    weaponSlot.setChanged();

                    // Sync back to client and other watchers
                    NetworkHelper.syncBackWeaponToClients(player, payload.newStack().copy());
                }
            });
        });
    }
}
package org.mythicgoose.wyrmsteel.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.custom_slot.WeaponStashSlot;

public record WeaponStashSwapPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<WeaponStashSwapPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "weapon_stash_swap"));

    public static final StreamCodec<FriendlyByteBuf, WeaponStashSwapPayload> CODEC =
            StreamCodec.unit(new WeaponStashSwapPayload());

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

                if (weaponSlot == null) {
                    return;
                }

                // Get current items
                ItemStack stashItem = weaponSlot.getItem().copy();
                ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND).copy();

                System.out.println("SERVER BEFORE SWAP:");
                System.out.println("  Stash slot has: " + stashItem);
                System.out.println("  Main hand has: " + mainHandItem);

                // Perform the swap using the slot properly
                weaponSlot.setQuietly(mainHandItem); // Use setQuietly to avoid packet loop
                player.setItemInHand(InteractionHand.MAIN_HAND, stashItem);

                // Mark as changed
                weaponSlot.setChanged();

                System.out.println("SERVER AFTER SWAP:");
                System.out.println("  Stash slot now has: " + weaponSlot.getItem());
                System.out.println("  Main hand now has: " + player.getItemInHand(InteractionHand.MAIN_HAND));

                // Sync to clients
                NetworkHelper.syncBackWeaponToClients(player, mainHandItem);

                // Play sound
                player.level().playSound(
                        null,
                        player.blockPosition(),
                        SoundEvents.ARMOR_EQUIP_GENERIC.value(),
                        SoundSource.PLAYERS,
                        0.5f,
                        1.2f
                );
            });
        });
    }
}
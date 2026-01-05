package org.mythicgoose.wyrmsteel.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.custom_slot.InventoryAccessor;

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
        // THEN: Register the receiver
        ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
            context.player().server.execute(() -> {
                ServerPlayer player = context.player();
                Inventory inventory = player.getInventory();

                // Get the weapon stash slot item
                ItemStack stashItem = ((InventoryAccessor) inventory)
                        .weapons_of_death$getWeaponStashSlot().copy();

                // Get current main hand stack
                ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND).copy();

                // Swap the items on the SERVER
                ((InventoryAccessor) inventory)
                        .weapons_of_death$setWeaponStashSlot(mainHandItem);
                player.setItemInHand(InteractionHand.MAIN_HAND, stashItem);

                // **ADD THIS LINE: Sync the weapon stash slot to client**
                NetworkHelper.syncBackWeaponToClients(player, mainHandItem);

                System.out.println("SERVER BEFORE SWAP:");
                System.out.println("  Stash slot has: " + stashItem);
                System.out.println("  Main hand has: " + mainHandItem);

                ((InventoryAccessor) inventory)
                        .weapons_of_death$setWeaponStashSlot(mainHandItem);
                player.setItemInHand(InteractionHand.MAIN_HAND, stashItem);

                System.out.println("SERVER AFTER SWAP:");
                System.out.println("  Stash slot now has: " + ((InventoryAccessor) inventory).weapons_of_death$getWeaponStashSlot());
                System.out.println("  Main hand now has: " + player.getItemInHand(InteractionHand.MAIN_HAND));

                        // Play sound feedback
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
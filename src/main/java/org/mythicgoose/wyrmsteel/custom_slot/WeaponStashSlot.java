package org.mythicgoose.wyrmsteel.custom_slot;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.mythicgoose.wyrmsteel.network.C2SWeaponStashSlotClickPacket;
import org.mythicgoose.wyrmsteel.network.NetworkHelper;

public class WeaponStashSlot extends Slot {

    private final Inventory inventory;

    public WeaponStashSlot(Inventory inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
        this.inventory = inventory;
    }

    @Override
    public ItemStack getItem() {
        return ((InventoryAccessor) inventory)
                .weapons_of_death$getWeaponStashSlot();
    }

    @Override
    public void set(ItemStack stack) {
        ((InventoryAccessor) inventory)
                .weapons_of_death$setWeaponStashSlot(stack);
        notifySlotChange(stack);
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack current = getItem().copy();
        if (current.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack taken = current.split(amount);
        set(current);
        notifySlotChange(current);

        return taken;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        super.onTake(player, stack);
        notifySlotChange(getItem());
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        super.setByPlayer(newStack, oldStack);
        notifySlotChange(newStack);
    }

    // Notify both client and server about slot changes
    private void notifySlotChange(ItemStack newStack) {
        Player player = inventory.player;

        // Server side - sync to clients
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHelper.syncBackWeaponToClients(serverPlayer, newStack);
        }
        // Client side - send to server
        else if (player.level().isClientSide) {
            ClientPlayNetworking.send(new C2SWeaponStashSlotClickPacket(newStack.copy()));
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return true;
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }
}
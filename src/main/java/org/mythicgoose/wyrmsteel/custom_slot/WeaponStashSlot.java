package org.mythicgoose.wyrmsteel.custom_slot;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mythicgoose.wyrmsteel.network.C2SWeaponStashSlotClickPacket;
import org.mythicgoose.wyrmsteel.network.NetworkHelper;

public class WeaponStashSlot extends Slot {

    private final Inventory inventory;
    private boolean isSyncing = false;

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
        System.out.println("SET CALLED: " + stack);
        ItemStack oldStack = getItem().copy();
        ((InventoryAccessor) inventory)
                .weapons_of_death$setWeaponStashSlot(stack);

        // Only notify if the stack actually changed
        if (!ItemStack.matches(oldStack, stack)) {
            notifySlotChange(stack);
        }
    }

    @Override
    public @NotNull ItemStack remove(int amount) {
        System.out.println("REMOVE CALLED: amount=" + amount);
        ItemStack current = getItem();
        if (current.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result;
        if (current.getCount() <= amount) {
            result = current.copy();
            set(ItemStack.EMPTY);
        } else {
            result = current.split(amount);
            set(current);
        }

        // Force sync after removal
        Player player = inventory.player;
        if (player != null && !player.level().isClientSide) {
            NetworkHelper.syncBackWeaponToClients((ServerPlayer)player, getItem());
        }

        return result;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        System.out.println("ON TAKE CALLED: " + stack);
        // Just ensure slot is empty - don't send packet
        set(ItemStack.EMPTY);
        setChanged();
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        System.out.println("SET BY PLAYER CALLED: new=" + newStack + ", old=" + oldStack);
        // Don't call super - just set directly
        set(newStack);
        setChanged();
    }

    @Override
    public void setChanged() {
        // Mark inventory as changed
        inventory.setChanged();
    }

    // Remove notifySlotChange entirely and handle sync differently
    private void notifySlotChange(ItemStack newStack) {
        Player player = inventory.player;
        if (player == null) return;

        // Server side - sync to clients ONLY
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHelper.syncBackWeaponToClients(serverPlayer, newStack);
        }
        // Client side - DO NOTHING during normal inventory operations
        // The server will handle it through vanilla's packet system
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

    @Override
    public int getMaxStackSize() {
        return 1; // Only allow 1 item in this slot
    }
}
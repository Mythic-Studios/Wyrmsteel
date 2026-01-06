package org.mythicgoose.wyrmsteel.custom_slot;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
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
    public ItemStack remove(int amount) {
        System.out.println("REMOVE CALLED: amount=" + amount);
        ItemStack current = getItem();
        if (current.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result;
        if (current.getCount() <= amount) {
            result = current.copy();
            // This will trigger notifySlotChange with EMPTY
            set(ItemStack.EMPTY);
        } else {
            result = current.split(amount);
            // This will trigger notifySlotChange with the reduced stack
            set(current);
        }

        // Make sure the change is marked
        setChanged();

        return result;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        System.out.println("ON TAKE CALLED: " + stack);
        // Don't call super.onTake() because it tries to access inventory array at slot index
        // which doesn't exist for our custom slot

        // Explicitly ensure slot is empty and synced
        ItemStack current = getItem();
        if (!current.isEmpty()) {
            set(ItemStack.EMPTY);
        }

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

    private void notifySlotChange(ItemStack newStack) {
        Player player = inventory.player;
        if (player == null) return;

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

    @Override
    public int getMaxStackSize() {
        return 1; // Only allow 1 item in this slot
    }
}
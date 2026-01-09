package org.mythicgoose.wyrmsteel.custom_slot;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mythicgoose.wyrmsteel.network.C2SWeaponStashSlotClickPacket;
import org.mythicgoose.wyrmsteel.network.NetworkHelper;

public class WeaponStashSlot extends Slot {

    private final Inventory inventory;
    private boolean isSyncing = false; // Prevent infinite packet loops

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

        // Prevent recursive packet sends
        if (isSyncing) {
            ((InventoryAccessor) inventory)
                    .weapons_of_death$setWeaponStashSlot(stack);
            return;
        }

        ItemStack oldStack = getItem().copy();
        ((InventoryAccessor) inventory)
                .weapons_of_death$setWeaponStashSlot(stack);

        // Only send packet if on client side AND stack changed
        Player player = inventory.player;
        if (player != null && player.level().isClientSide && !ItemStack.matches(oldStack, stack)) {
            System.out.println("CLIENT: Sending stash update to server: " + stack);
            ClientPlayNetworking.send(new C2SWeaponStashSlotClickPacket(stack.copy()));
        }

        // Notify server-side change
        if (player instanceof ServerPlayer serverPlayer && !ItemStack.matches(oldStack, stack)) {
            notifySlotChange(serverPlayer, stack);
        }
    }

    /**
     * Set the item without triggering packet sends
     * Use this when receiving packets from server
     */
    public void setQuietly(ItemStack stack) {
        isSyncing = true;
        try {
            System.out.println("SET QUIETLY CALLED: " + stack);
            ((InventoryAccessor) inventory)
                    .weapons_of_death$setWeaponStashSlot(stack);
        } finally {
            isSyncing = false;
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
            // Taking all items
            result = current.copy();
            ((InventoryAccessor) inventory)
                    .weapons_of_death$setWeaponStashSlot(ItemStack.EMPTY);
        } else {
            // Taking partial stack
            result = current.split(amount);
            ((InventoryAccessor) inventory)
                    .weapons_of_death$setWeaponStashSlot(current);
        }

        setChanged();
        return result;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        System.out.println("ON TAKE CALLED: " + stack);
        // DON'T call set(ItemStack.EMPTY) here!
        // The remove() method already handled emptying the slot
        setChanged();
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        System.out.println("SET BY PLAYER CALLED: new=" + newStack + ", old=" + oldStack);

        // Only update if there's an actual change
        if (!ItemStack.matches(newStack, oldStack)) {
            set(newStack);
            setChanged();
        }
    }

    @Override
    public void setChanged() {
        inventory.setChanged();
    }

    private void notifySlotChange(ServerPlayer serverPlayer, ItemStack newStack) {
        // Sync to all clients watching this player
        NetworkHelper.syncBackWeaponToClients(serverPlayer, newStack);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return true; // Add your weapon validation logic here if needed
    }

    @Override
    public boolean mayPickup(Player player) {
        return true; // CRITICAL: must return true for manual pickup
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.mythicgoose.wyrmsteel.custom_slot.InventoryAccessor;
import org.mythicgoose.wyrmsteel.custom_slot.PlayerWeaponStashAccessor;
import org.mythicgoose.wyrmsteel.custom_slot.WeaponStashSlot;
import org.mythicgoose.wyrmsteel.network.NetworkHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {

    @Shadow
    public abstract Slot getSlot(int slotId);

    @Shadow
    public abstract ItemStack getCarried();

    @Shadow
    public abstract void setCarried(ItemStack stack);

    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void handleWeaponStashSlotClick(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
//        // ONLY HANDLE ON SERVER SIDE
//        if (!(player instanceof ServerPlayer)) {
//            return; // Let client prediction happen normally
//        }
//
//        // Check if the clicked slot is a WeaponStashSlot
//        try {
//            Slot slot = this.getSlot(slotId);
//
//            if (slot instanceof WeaponStashSlot) {
//                System.out.println("CUSTOM CLICK HANDLER (SERVER): slot=" + slotId + ", button=" + button + ", type=" + clickType);
//
//                // Handle the click ourselves
//                if (clickType == ClickType.PICKUP && button == 0) { // Left click only
//                    Inventory inventory = player.getInventory();
//                    ItemStack cursorStack = this.getCarried();
//                    ItemStack slotStack = ((InventoryAccessor) inventory).weapons_of_death$getWeaponStashSlot();
//
//                    System.out.println("  Cursor has: " + cursorStack);
//                    System.out.println("  Slot has: " + slotStack);
//
//                    if (cursorStack.isEmpty() && !slotStack.isEmpty()) {
//                        // Take item from slot to cursor
//                        this.setCarried(slotStack.copy());
//                        ((PlayerWeaponStashAccessor) player).weapons_of_death$setWeaponStash(ItemStack.EMPTY);
//                    } else if (!cursorStack.isEmpty() && slotStack.isEmpty()) {
//                        // Put cursor item into slot
//                        ((PlayerWeaponStashAccessor) player).weapons_of_death$setWeaponStash(cursorStack.copy());
//                        this.setCarried(ItemStack.EMPTY);
//                    } else if (!cursorStack.isEmpty() && !slotStack.isEmpty()) {
//                        // Swap cursor and slot
//                        ItemStack temp = cursorStack.copy();
//                        this.setCarried(slotStack.copy());
//                        ((PlayerWeaponStashAccessor) player).weapons_of_death$setWeaponStash(temp);
//                    }
//
//                    System.out.println("  After click - Cursor: " + this.getCarried() + ", Slot: " + ((InventoryAccessor) inventory).weapons_of_death$getWeaponStashSlot());
//
//                    ci.cancel(); // Cancel vanilla handling
//                }
//            }
//        } catch (IndexOutOfBoundsException e) {
//            // Slot ID is out of bounds, let vanilla handle it
//        }
    }
}
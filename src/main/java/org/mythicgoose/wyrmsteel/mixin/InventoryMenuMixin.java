package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.mythicgoose.wyrmsteel.custom_slot.WeaponStashSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu {

    protected InventoryMenuMixin(MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addWeaponStashSlot(Inventory inventory, boolean active, Player owner, CallbackInfo ci) {
        if (!active) {
            // Survival mode position
            this.addSlot(new WeaponStashSlot(inventory, 46, 77, 44));
        } else {
            // Creative mode position
            // Note: Creative inventory handling is usually done in CreativeModeInventoryScreen,
            // but adding the slot here ensures the container knows it exists.
            this.addSlot(new WeaponStashSlot(inventory, 46, 127, 20));
        }
    }

    // We use @Inject instead of overwriting the method.
    // This preserves vanilla behavior for crafting/armor slots.
    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void wyrmsteel$handleWeaponSlotShiftClick(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            ItemStack copy = originalStack.copy();

            // === CASE 1: Taking item OUT of Weapon Slot (Index 46) ===
            if (index == 46) {
                // Try to move to main inventory (Slots 9-45: Hotbar + Main Grid)
                if (!this.moveItemStackTo(originalStack, 9, 45, true)) {
                    cir.setReturnValue(ItemStack.EMPTY); // Failed to move
                    return;
                }

                slot.onQuickCraft(originalStack, copy);

                if (originalStack.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }

                if (originalStack.getCount() == copy.getCount()) {
                    cir.setReturnValue(ItemStack.EMPTY);
                    return;
                }

                slot.onTake(player, originalStack);

                // We successfully moved the item. Cancel vanilla logic so it doesn't get confused.
                cir.setReturnValue(copy);
            }

            // === CASE 2: Putting item INTO Weapon Slot (From Inventory) ===
            else {
                // Only try to handle this if the item is valid for our slot
                Slot weaponSlot = this.slots.get(46);

                // Check if slot 46 accepts this item and is currently empty
                if (weaponSlot.mayPlace(originalStack) && !weaponSlot.hasItem()) {

                    // Try to move ONE item into slot 46 (Since max stack size is 1)
                    if (this.moveItemStackTo(originalStack, 46, 47, false)) {

                        if (originalStack.isEmpty()) {
                            slot.set(ItemStack.EMPTY);
                        } else {
                            slot.setChanged();
                        }

                        if (originalStack.getCount() == copy.getCount()) {
                            return;
                        }

                        slot.onTake(player, originalStack);

                        // We successfully moved it to our slot. Cancel vanilla logic.
                        cir.setReturnValue(copy);
                    }
                    // If moveItemStackTo failed (e.g. slot was somehow full),
                    // we do NOT cancel. We let vanilla proceed.
                    // This allows the item to try other slots (like Armor slots).
                }
            }
        }
    }
}
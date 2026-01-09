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
            this.addSlot(new WeaponStashSlot(inventory, 46, 127, 20));
        }
    }

    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void wyrmsteel$handleWeaponSlotShiftClick(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        Slot slot = this.slots.get(index);

        if (slot == null || !slot.hasItem()) {
            return;
        }

        ItemStack originalStack = slot.getItem();
        ItemStack copy = originalStack.copy();

        // === ONLY handle moving OUT of Weapon Slot (Index 46) ===
        if (index == 46) {
            // Try to move to main inventory (9-35) then hotbar (0-8)
            if (!this.moveItemStackTo(originalStack, 9, 45, true)) {
                cir.setReturnValue(ItemStack.EMPTY);
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
            cir.setReturnValue(copy);
        }

        // For all other slots, let vanilla handle the shift-click logic
        // Don't intercept - this fixes Issue #1
    }
}
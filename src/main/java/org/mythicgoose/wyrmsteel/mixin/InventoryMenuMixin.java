package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import org.mythicgoose.wyrmsteel.custom_slot.WeaponStashSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
            // Creative mode position - at your background position
            this.addSlot(new WeaponStashSlot(inventory, 46, 127, 20));
        }
    }
}
// InventoryMixin.java
package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.mythicgoose.wyrmsteel.client.WeaponStashState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Inject(method = "getSelected", at = @At("RETURN"), cancellable = true)
    private void overrideSelectedItem(CallbackInfoReturnable<ItemStack> cir) {
        if (WeaponStashState.isEquipped() && !WeaponStashState.getCachedStack().isEmpty()) {
            cir.setReturnValue(WeaponStashState.getCachedStack());
        }
    }
}
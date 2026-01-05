package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.mythicgoose.wyrmsteel.custom_slot.InventoryAccessor;
import org.mythicgoose.wyrmsteel.network.NetworkHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {

    @Inject(method = "doClick", at = @At("RETURN"))
    private void syncWeaponStashAfterClick(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        // Always sync slot 46 after any click operation, just to be safe
        if (player instanceof ServerPlayer serverPlayer) {
            System.out.println("CONTAINER: Click on slot " + slotId + " - syncing weapon stash");
            NetworkHelper.syncBackWeaponToClients(
                    serverPlayer,
                    ((InventoryAccessor) player.getInventory()).weapons_of_death$getWeaponStashSlot()
            );
        }
    }
}
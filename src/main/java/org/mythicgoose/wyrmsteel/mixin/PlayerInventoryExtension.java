package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.mythicgoose.wyrmsteel.custom_slot.InventoryAccessor;
import org.mythicgoose.wyrmsteel.custom_slot.PlayerWeaponStashAccessor;
import org.mythicgoose.wyrmsteel.network.NetworkHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Inventory.class)
public abstract class PlayerInventoryExtension implements InventoryAccessor {

    @Final
    @Shadow
    public Player player;

    @Override
    public ItemStack weapons_of_death$getWeaponStashSlot() {
        return ((PlayerWeaponStashAccessor) this.player)
                .weapons_of_death$getWeaponStash();
    }

    @Override
    public void weapons_of_death$setWeaponStashSlot(ItemStack stack) {
        ((PlayerWeaponStashAccessor) this.player)
                .weapons_of_death$setWeaponStash(stack);
        // REMOVED SYNC - it's already in PlayerWeaponStashMixin
    }
}
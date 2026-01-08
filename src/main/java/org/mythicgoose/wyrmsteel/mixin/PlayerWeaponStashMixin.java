package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.mythicgoose.wyrmsteel.custom_slot.PlayerWeaponStashAccessor;
import org.mythicgoose.wyrmsteel.network.NetworkHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerWeaponStashMixin implements PlayerWeaponStashAccessor {

    @Unique
    private ItemStack weapons_of_death$weaponStash = ItemStack.EMPTY;

    @Override
    public ItemStack weapons_of_death$getWeaponStash() {
        return weapons_of_death$weaponStash;
    }

    @Override
    public void weapons_of_death$setWeaponStash(ItemStack stack) {
        this.weapons_of_death$weaponStash = stack;

        // Sync to clients if on server
        Player thisPlayer = (Player) (Object) this;
        if (thisPlayer instanceof ServerPlayer serverPlayer) {
            NetworkHelper.syncBackWeaponToClients(serverPlayer, stack);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveWeaponStash(CompoundTag tag, CallbackInfo ci) {
        if (!weapons_of_death$weaponStash.isEmpty()) {
            HolderLookup.Provider provider = ((Player)(Object)this).level().registryAccess();
            tag.put("WeaponsOfDeathWeaponStash", (CompoundTag) weapons_of_death$weaponStash.save(provider));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void loadWeaponStash(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("WeaponsOfDeathWeaponStash", 10)) {
            HolderLookup.Provider provider = ((Player)(Object)this).level().registryAccess();
            CompoundTag stashTag = tag.getCompound("WeaponsOfDeathWeaponStash");
            this.weapons_of_death$weaponStash = ItemStack.parseOptional(provider, stashTag);
        }
    }
}
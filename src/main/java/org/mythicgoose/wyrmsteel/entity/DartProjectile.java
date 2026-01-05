package org.mythicgoose.wyrmsteel.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.mythicgoose.wyrmsteel.init.ModEffects;

public class DartProjectile extends AbstractArrow {
    public enum DartType {
        NORMAL,   // Just damage
        POISON,   // Damage + Poison
        TORPOR    // Damage + Wither
    }

    private DartType dartType = DartType.NORMAL;

    public DartProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        // Ensure we always have a valid pickup item
        this.setPickupItemStack(this.getDefaultPickupItem());
    }

    public DartProjectile(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level level, ItemStack pickupItemStack) {
        super(type, x, y, z, level, pickupItemStack, null);
    }

    public DartProjectile(EntityType<? extends AbstractArrow> type, LivingEntity owner, Level level, ItemStack pickupItemStack) {
        super(type, owner, level, pickupItemStack, null);
    }

    public void setDartType(DartType type) {
        this.dartType = type;
    }

    public DartType getDartType() {
        return this.dartType;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        // Apply effects based on dart type
        if (result.getEntity() instanceof LivingEntity target) {
            switch (dartType) {
                case POISON -> {
                    // Apply Poison VI for  over an hour or day
                    target.addEffect(new MobEffectInstance(MobEffects.POISON, 100000, 2));
                }
                case TORPOR -> {
                    // Apply Torpor for over an hour or day
                    target.addEffect(new MobEffectInstance(ModEffects.TORPOR, 100000, 0));
                }
                case NORMAL -> {
                    // No additional effects, just damage
                }
            }
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.ARROW.getDefaultInstance(); // Return arrow item for pickup compatibility
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        // CRITICAL FIX: Ensure pickup item is set before saving
        if (this.getPickupItemStackOrigin().isEmpty()) {
            this.setPickupItemStack(this.getDefaultPickupItem());
        }

        super.addAdditionalSaveData(tag);
        tag.putString("DartType", this.dartType.name());
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        // Ensure we have a valid pickup item after loading
        if (this.getPickupItemStackOrigin().isEmpty()) {
            this.setPickupItemStack(this.getDefaultPickupItem());
        }

        if (tag.contains("DartType")) {
            try {
                this.dartType = DartType.valueOf(tag.getString("DartType"));
            } catch (IllegalArgumentException e) {
                this.dartType = DartType.NORMAL;
            }
        }
    }
}
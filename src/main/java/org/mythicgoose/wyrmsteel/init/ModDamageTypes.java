package org.mythicgoose.wyrmsteel.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.mythicgoose.wyrmsteel.Wyrmsteel;

public class ModDamageTypes {

    // Resource key for the blood loss damage type
    public static final ResourceKey<DamageType> BLOOD_LOSS =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "blood_loss"));

    // Helper method to create a DamageSource for blood loss
    public static DamageSource bloodLoss(Level level) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(BLOOD_LOSS)
        );
    }

    // Helper method to create a DamageSource with a causing entity
    public static DamageSource bloodLoss(Level level, Entity causingEntity) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(BLOOD_LOSS),
                causingEntity
        );
    }

    // Helper method to create a DamageSource with both direct and causing entity
    public static DamageSource bloodLoss(Level level, Entity directEntity, Entity causingEntity) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(BLOOD_LOSS),
                directEntity,
                causingEntity
        );
    }

    // Resource key for the cuts damage type
    public static final ResourceKey<DamageType> CUTS =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "cuts"));

    // Helper method to create a DamageSource for cuts
    public static DamageSource cuts(Level level) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(CUTS)
        );
    }

    // Helper method to create a DamageSource with a causing entity
    public static DamageSource cuts(Level level, Entity causingEntity) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(CUTS),
                causingEntity
        );
    }

    // Helper method to create a DamageSource with both direct and causing entity
    public static DamageSource cuts(Level level, Entity directEntity, Entity causingEntity) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(CUTS),
                directEntity,
                causingEntity
        );
    }
}
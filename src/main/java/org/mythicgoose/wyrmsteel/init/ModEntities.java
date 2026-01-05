package org.mythicgoose.wyrmsteel.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.entity.DartProjectile;

public class ModEntities {
    public static final EntityType<DartProjectile> DART = register(
            "dart",
            EntityType.Builder.<DartProjectile>of(DartProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("dart")
    );

    private static <T extends EntityType<?>> T register(String id, T entityType) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, id), entityType);
    }

    public static void register() {
        // Registration happens via static initialization
    }
}
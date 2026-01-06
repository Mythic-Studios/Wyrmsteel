package org.mythicgoose.wyrmsteel.init;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.mythicgoose.wyrmsteel.Wyrmsteel;

public class ModParticles {
    public static final SimpleParticleType BLOOD_BUBBLE =
            registerParticle("blood_bubble_particle", FabricParticleTypes.simple());

//    SimpleParticleType BLOOD_BUBBLE_SPLATTER = create("blood_bubble_splatter", FabricParticleTypes.simple(true));


    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, name), particleType);
    }

    public static void registerParticles() {
        Wyrmsteel.LOGGER.info("Registering Particles for " + Wyrmsteel.MOD_ID);
    }
}
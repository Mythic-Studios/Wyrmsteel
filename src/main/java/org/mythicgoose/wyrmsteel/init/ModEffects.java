package org.mythicgoose.wyrmsteel.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.effect.NoRegenEffect;

public class ModEffects {
    public static final Holder.Reference<MobEffect> TORPOR = registerStatusEffect("torpor",
            new NoRegenEffect());


    private static Holder.Reference<MobEffect> registerStatusEffect(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, name), statusEffect);
    }

    public static void registerEffects() {
        Wyrmsteel.LOGGER.info("Registering Mod Effects for " + Wyrmsteel.MOD_ID);
    }
}

package org.mythicgoose.wyrmsteel.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.mythicgoose.mythic_core.indexing.MobEffectRegistrar;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.effect.EmptyStatusEffect;

public class ModEffects {
    static {
        MobEffectRegistrar.setRegistrarID(Wyrmsteel.MOD_ID);
    }

    public static final Holder.Reference<MobEffect> TORPOR = MobEffectRegistrar.registerMobEffect("torpor",
            new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0xD8C0B8));

    public static final Holder.Reference<MobEffect> VULNERABILITY = MobEffectRegistrar.registerMobEffect("vulnerable",
            new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x67574A));


    public static void registerEffects() {
        Wyrmsteel.LOGGER.info("Registering Mod Effects for " + Wyrmsteel.MOD_ID);
    }
}

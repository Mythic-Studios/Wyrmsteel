package org.mythicgoose.wyrmsteel.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.effect.EmptyStatusEffect;

public class ModEffects {
    public static final Holder.Reference<MobEffect> TORPOR = registerStatusEffect("torpor",
            new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0xD8C0B8));

    public static final Holder.Reference<MobEffect> VULNERABILITY = registerStatusEffect("vulnerable",
            new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x67574A));

    public static final Holder.Reference<MobEffect> DIMENSIONAL_DESYNC = registerStatusEffect("dimensional_desync",
            new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x67574A).addAttributeModifier(
                    Attributes.MAX_HEALTH,
                    ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "dimensional_desync"),
                    -0.5, // -50% (multiply operation)
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ));



    private static Holder.Reference<MobEffect> registerStatusEffect(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, name), statusEffect);
    }

    public static void registerEffects() {
        Wyrmsteel.LOGGER.info("Registering Mod Effects for " + Wyrmsteel.MOD_ID);
    }
}

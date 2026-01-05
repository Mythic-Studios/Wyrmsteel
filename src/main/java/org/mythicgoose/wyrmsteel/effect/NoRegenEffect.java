package org.mythicgoose.wyrmsteel.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class NoRegenEffect extends MobEffect {

    public NoRegenEffect() {
        super(MobEffectCategory.HARMFUL, 0xD8C0B8); // Purple color
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.mythicgoose.wyrmsteel.init.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void onHeal(float amount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasEffect(ModEffects.TORPOR)) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private float multiplyDamageForVulnerability(float amount) {
        LivingEntity entity = (LivingEntity) (Object) this;

        MobEffectInstance effect = entity.getEffect(ModEffects.VULNERABILITY);
        if (effect != null) {
            int amplifier = effect.getAmplifier();
            float multiplier = 0.5f * (amplifier + 1);
            return amount + (amount * multiplier);
        }

        return amount;
    }
}
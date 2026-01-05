package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.mythicgoose.wyrmsteel.init.ModEffects; // Replace with your effects class
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void onHeal(float amount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasEffect(ModEffects.TORPOR)) { // Replace with your effect reference
            ci.cancel();
        }
    }
}
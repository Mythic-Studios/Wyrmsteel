package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.mythicgoose.wyrmsteel.custom_slot.BackWeaponRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void weapons_of_death$backBlade(EntityRendererProvider.Context ctx, boolean slim, CallbackInfo ci) {
        PlayerRenderer renderer = (PlayerRenderer) (Object) this;
        renderer.addLayer(new BackWeaponRenderLayer(renderer));
    }
}
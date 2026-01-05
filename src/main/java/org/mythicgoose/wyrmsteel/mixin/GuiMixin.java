package org.mythicgoose.wyrmsteel.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.mythicgoose.wyrmsteel.init.ModEffects;
import org.mythicgoose.wyrmsteel.util.CustomHeartType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    private Player getCameraPlayer() {
        throw new AssertionError();
    }

    @Unique
    private Player weapons_of_death$currentPlayer;

    // Capture the player at the start of health rendering
    @Inject(
            method = "renderPlayerHealth",
            at = @At("HEAD")
    )
    private void capturePlayer(GuiGraphics guiGraphics, CallbackInfo ci) {
        weapons_of_death$currentPlayer = this.getCameraPlayer();
    }

    // Modify the heart rendering to use custom sprites
    @Inject(
            method = "renderHeart",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderCustomHeart(
            GuiGraphics guiGraphics,
            Gui.HeartType heartType,
            int x,
            int y,
            boolean hardcore,
            boolean blinking,
            boolean halfHeart,
            CallbackInfo ci
    ) {
        Player player = weapons_of_death$currentPlayer;
        if (player == null) {
            return; // Let vanilla handle it
        }

        // Check if we need custom rendering for torpor
        if (player.hasEffect(ModEffects.TORPOR)) {
            ci.cancel();

            CustomHeartType customType;
            // Map vanilla types to custom types, using TORPOR for health hearts
            switch (heartType) {
                case CONTAINER:
                    customType = CustomHeartType.CONTAINER;
                    break;
                case ABSORBING:
                    customType = CustomHeartType.ABSORBING;
                    break;
                case POISIONED:
                case WITHERED:
                case FROZEN:
                case NORMAL:
                default:
                    // Use torpor for actual health hearts
                    customType = CustomHeartType.TORPOR;
                    break;
            }

            ResourceLocation sprite = customType.getSprite(hardcore, halfHeart, blinking);
            guiGraphics.blitSprite(sprite, x, y, 9, 9);
        }
        // If no torpor effect, let vanilla rendering proceed normally
    }
}
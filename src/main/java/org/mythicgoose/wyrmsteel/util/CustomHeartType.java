package org.mythicgoose.wyrmsteel.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.init.ModEffects;


@Environment(EnvType.CLIENT)
public enum CustomHeartType {
    CONTAINER(ResourceLocation.withDefaultNamespace("hud/heart/container"), ResourceLocation.withDefaultNamespace("hud/heart/container_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/container"), ResourceLocation.withDefaultNamespace("hud/heart/container_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/container_hardcore"), ResourceLocation.withDefaultNamespace("hud/heart/container_hardcore_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/container_hardcore"), ResourceLocation.withDefaultNamespace("hud/heart/container_hardcore_blinking")),
    NORMAL(ResourceLocation.withDefaultNamespace("hud/heart/full"), ResourceLocation.withDefaultNamespace("hud/heart/full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/half"), ResourceLocation.withDefaultNamespace("hud/heart/half_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/hardcore_full"), ResourceLocation.withDefaultNamespace("hud/heart/hardcore_full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/hardcore_half"), ResourceLocation.withDefaultNamespace("hud/heart/hardcore_half_blinking")),
    POISIONED(ResourceLocation.withDefaultNamespace("hud/heart/poisoned_full"), ResourceLocation.withDefaultNamespace("hud/heart/poisoned_full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/poisoned_half"), ResourceLocation.withDefaultNamespace("hud/heart/poisoned_half_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/poisoned_hardcore_full"), ResourceLocation.withDefaultNamespace("hud/heart/poisoned_hardcore_full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/poisoned_hardcore_half"), ResourceLocation.withDefaultNamespace("hud/heart/poisoned_hardcore_half_blinking")),
    WITHERED(ResourceLocation.withDefaultNamespace("hud/heart/withered_full"), ResourceLocation.withDefaultNamespace("hud/heart/withered_full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/withered_half"), ResourceLocation.withDefaultNamespace("hud/heart/withered_half_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/withered_hardcore_full"), ResourceLocation.withDefaultNamespace("hud/heart/withered_hardcore_full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/withered_hardcore_half"), ResourceLocation.withDefaultNamespace("hud/heart/withered_hardcore_half_blinking")),
    ABSORBING(ResourceLocation.withDefaultNamespace("hud/heart/absorbing_full"), ResourceLocation.withDefaultNamespace("hud/heart/absorbing_full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/absorbing_half"), ResourceLocation.withDefaultNamespace("hud/heart/absorbing_half_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/absorbing_hardcore_full"), ResourceLocation.withDefaultNamespace("hud/heart/absorbing_hardcore_full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/absorbing_hardcore_half"), ResourceLocation.withDefaultNamespace("hud/heart/absorbing_hardcore_half_blinking")),
    FROZEN(ResourceLocation.withDefaultNamespace("hud/heart/frozen_full"), ResourceLocation.withDefaultNamespace("hud/heart/frozen_full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/frozen_half"), ResourceLocation.withDefaultNamespace("hud/heart/frozen_half_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/frozen_hardcore_full"), ResourceLocation.withDefaultNamespace("hud/heart/frozen_hardcore_full_blinking"), ResourceLocation.withDefaultNamespace("hud/heart/frozen_hardcore_half"), ResourceLocation.withDefaultNamespace("hud/heart/frozen_hardcore_half_blinking")),
    TORPOR(ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "hud/heart/torpor_full"), ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "hud/heart/torpor_full_blinking"), ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "hud/heart/torpor_half"), ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "hud/heart/torpor_half_blinking"), ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "hud/heart/hardcore_full"), ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "hud/heart/hardcore_full_blinking"), ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "hud/heart/hardcore_half"), ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "hud/heart/hardcore_half_blinking"));

    private final ResourceLocation full;
    private final ResourceLocation fullBlinking;
    private final ResourceLocation half;
    private final ResourceLocation halfBlinking;
    private final ResourceLocation hardcoreFull;
    private final ResourceLocation hardcoreFullBlinking;
    private final ResourceLocation hardcoreHalf;
    private final ResourceLocation hardcoreHalfBlinking;

    CustomHeartType(final ResourceLocation resourceLocation, final ResourceLocation resourceLocation2, final ResourceLocation resourceLocation3, final ResourceLocation resourceLocation4, final ResourceLocation resourceLocation5, final ResourceLocation resourceLocation6, final ResourceLocation resourceLocation7, final ResourceLocation resourceLocation8) {
        this.full = resourceLocation;
        this.fullBlinking = resourceLocation2;
        this.half = resourceLocation3;
        this.halfBlinking = resourceLocation4;
        this.hardcoreFull = resourceLocation5;
        this.hardcoreFullBlinking = resourceLocation6;
        this.hardcoreHalf = resourceLocation7;
        this.hardcoreHalfBlinking = resourceLocation8;
    }

    public ResourceLocation getSprite(boolean bl, boolean bl2, boolean bl3) {
        if (!bl) {
            if (bl2) {
                return bl3 ? this.halfBlinking : this.half;
            } else {
                return bl3 ? this.fullBlinking : this.full;
            }
        } else if (bl2) {
            return bl3 ? this.hardcoreHalfBlinking : this.hardcoreHalf;
        } else {
            return bl3 ? this.hardcoreFullBlinking : this.hardcoreFull;
        }
    }

    public static CustomHeartType forPlayer(Player player) {
        CustomHeartType heartType;
        if (player.hasEffect(MobEffects.POISON)) {
            heartType = POISIONED;
        } else if (player.hasEffect(MobEffects.WITHER)) {
            heartType = WITHERED;
        } else if (player.isFullyFrozen()) {
            heartType = FROZEN;
        } else if (player.hasEffect(ModEffects.TORPOR)) {
            heartType = TORPOR;
        } else {
            heartType = NORMAL;
        }

        return heartType;
    }
}

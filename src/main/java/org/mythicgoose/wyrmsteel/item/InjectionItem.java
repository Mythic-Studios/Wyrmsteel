package org.mythicgoose.wyrmsteel.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.awt.*;
import java.util.List;

public class InjectionItem extends Item {
    public final MobEffect InjectionEffect;
    public final int NoUse;
    public final int ItemEffectColor;
    private final int effectDuration;
    private final int effectAmplifier;
    private final boolean clearEffects; // True if this should clear all effects (like milk)

    public InjectionItem(Properties properties, MobEffect injectionEffect, int itemEffectColor, int noUse) {
        this(properties, injectionEffect, itemEffectColor, noUse, 600, 0, false); // Default 30 seconds, level 1, no clear
    }

    public InjectionItem(Properties properties, MobEffect injectionEffect, int itemEffectColor, int noUse, int duration, int amplifier) {
        this(properties, injectionEffect, itemEffectColor, noUse, duration, amplifier, false);
    }

    public InjectionItem(Properties properties, MobEffect injectionEffect, int itemEffectColor, int noUse, boolean clearEffects) {
        this(properties, injectionEffect, itemEffectColor, noUse, 600, 0, clearEffects);
    }

    public InjectionItem(Properties properties, MobEffect injectionEffect, int itemEffectColor, int noUse, int duration, int amplifier, boolean clearEffects) {
        super(properties);
        InjectionEffect = injectionEffect;
        ItemEffectColor = itemEffectColor;
        NoUse = noUse;
        this.effectDuration = duration;
        this.effectAmplifier = amplifier;
        this.clearEffects = clearEffects;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // If this is a milk-based injection, clear all effects first
            if (clearEffects) {
                player.removeAllEffects();
            }

            // Then apply the new effect (if one is specified)
            if (InjectionEffect != null) {
                player.addEffect(new MobEffectInstance(
                        level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.MOB_EFFECT).wrapAsHolder(InjectionEffect),
                        effectDuration,
                        effectAmplifier
                ));
            }

            // Set cooldown (600 ticks = 30 seconds = 0.5 minutes)
            if (!player.isCreative()) {
                player.getCooldowns().addCooldown(this, 600);
            }

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (clearEffects) {
            tooltip.add(Component.literal("§6Milk"));
            tooltip.add(Component.literal("§7Clears all effects"));
        }
        if (InjectionEffect != null) {
            String effectName = Component.translatable(InjectionEffect.getDescriptionId()).getString();
            tooltip.add(Component.literal("§6Effect: " + effectName));
            tooltip.add(Component.literal("§9Duration: " + (effectDuration / 20) + "s"));
            tooltip.add(Component.literal("§5Level: " + (effectAmplifier + 1)));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }

    public int getColor(int tintIndex) {
        // layer0 = base (no tint)
        if (tintIndex == 0) {
            return 0xFFFFFFFF;
        }
        // layer1 = liquid
        return this.ItemEffectColor;
    }

}
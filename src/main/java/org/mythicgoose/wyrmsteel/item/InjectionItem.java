package org.mythicgoose.wyrmsteel.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.mythicgoose.wyrmsteel.init.ModItems;

import java.util.List;

public class InjectionItem extends Item {
    public final MobEffect InjectionEffect;
    public final int NoUse;
    public final int ItemEffectColor;
    private final int effectDuration;
    private final int effectAmplifier;
    private final boolean clearEffects;

    public InjectionItem(Properties properties, MobEffect injectionEffect, int itemEffectColor, int noUse) {
        this(properties, injectionEffect, itemEffectColor, noUse, 600, 0, false);
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
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Apply clear effects if configured (like milk injection)
        if (clearEffects) {
            target.removeAllEffects();
        }

        // Apply the effect to the attacked entity
        if (InjectionEffect != null) {
            target.addEffect(new MobEffectInstance(
                    attacker.level().registryAccess()
                            .registryOrThrow(net.minecraft.core.registries.Registries.MOB_EFFECT)
                            .wrapAsHolder(InjectionEffect),
                    effectDuration,
                    effectAmplifier
            ));
        }

        // Replace the item with a dirt block in the attacker's hand
        if (attacker instanceof Player player) {
            InteractionHand hand = player.getUsedItemHand();
            // getUsedItemHand() is only valid during use(); check both hands
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.getItem() == this) {
                mainHand.shrink(1);
                if (mainHand.isEmpty()) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.EMPTY_INJECTION));
                } else {
                    // If stacked, add dirt to inventory separately
                    player.getInventory().add(new ItemStack(ModItems.EMPTY_INJECTION));
                }
            } else {
                ItemStack offHand = player.getOffhandItem();
                offHand.shrink(1);
                if (offHand.isEmpty()) {
                    player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ModItems.EMPTY_INJECTION));
                } else {
                    player.getInventory().add(new ItemStack(ModItems.EMPTY_INJECTION));
                }
            }
        }

        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (clearEffects) {
                player.removeAllEffects();
            }

            if (InjectionEffect != null) {
                player.addEffect(new MobEffectInstance(
                        level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.MOB_EFFECT).wrapAsHolder(InjectionEffect),
                        effectDuration,
                        effectAmplifier
                ));
            }

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
        if (tintIndex == 0) {
            return 0xFFFFFFFF;
        }
        return this.ItemEffectColor;
    }
}
package org.mythicgoose.wyrmsteel.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.mythicgoose.wyrmsteel.init.ModEffects;
import org.mythicgoose.wyrmsteel.init.ModEnchantments;

public class CustomEnderpearlItem extends Item {
    private static int COOLDOWN = 600; // In ticks (lowest time)

    public CustomEnderpearlItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            ThrownEnderpearl thrownEnderpearl = new ThrownEnderpearl(level, player);
            thrownEnderpearl.setItem(itemStack);
            thrownEnderpearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(thrownEnderpearl);
        }

        player.awardStat(Stats.ITEM_USED.get(this));

        if (!player.isCreative()) {
            handleCooldowns(player, itemStack, COOLDOWN);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }



    private void handleCooldowns(Player player, ItemStack itemStack, int cooldown) {
        int level = getEnchantmentLevel(itemStack, player, ModEnchantments.COOLNESS_FACTOR);

        if (level > 0) {
            player.getCooldowns().addCooldown(this, cooldown);
        } else {
            player.getCooldowns().addCooldown(this, cooldown * 2);
        }
    }

    private int getEnchantmentLevel(ItemStack stack, LivingEntity entity, ResourceKey<Enchantment> enchantmentKey) {
        Holder<Enchantment> enchantmentHolder = entity.level()
                .registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(enchantmentKey);

        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack);
    }
}

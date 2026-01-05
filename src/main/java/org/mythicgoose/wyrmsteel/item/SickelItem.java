package org.mythicgoose.wyrmsteel.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.mythicgoose.wyrmsteel.init.ModEnchantments;

import java.util.UUID;

public class SickelItem extends SwordItem {
    private static final UUID SPECIAL_PLAYER_UUID = UUID.fromString("c6901742-061a-4b3d-9963-3a99536aafd7");

    public SickelItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        // Handle bleed before damage is applied
        handleBleed(itemStack, target, attacker);

        return super.hurtEnemy(itemStack, target, attacker);
    }

    @Override
    public void postHurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        // Check for each enchantment and apply its effect
        handleSliceAndDice(itemStack, target, attacker);

        super.postHurtEnemy(itemStack, target, attacker);
    }

    /**
     * Handles the Slice and Dice enchantment effect
     */
    private void handleSliceAndDice(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        int level = getEnchantmentLevel(itemStack, attacker, ModEnchantments.SLICE_AND_DICE);

        if (level > 0) {
            // Instant kill chance (5% per level)
            float instantKillChance = 0.05f * level;

            // Check if attacker is a player with the special UUID
            if (attacker instanceof Player player && player.getUUID().equals(SPECIAL_PLAYER_UUID)) {
                instantKillChance = 1.0f;
            }

            if (attacker.level().random.nextFloat() < instantKillChance) {
                target.setHealth(0.0f);
            }
        }
    }

    /**
     * Handles the Bleed enchantment effect
     * Reduces base damage but has a 20% chance to inflict double (original) base damage
     */
    private void handleBleed(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        int level = getEnchantmentLevel(itemStack, attacker, ModEnchantments.BLEED);

        if (level > 0) {
            // Get the base attack damage of the weapon
            float baseDamage = this.getTier().getAttackDamageBonus();

            // 20% chance to deal double damage
            if (attacker.level().random.nextFloat() < 0.20f) {
                // Deal the original base damage as bonus damage
                target.hurt(attacker.damageSources().mobAttack(attacker), baseDamage);
            } else {
                // Reduce damage by 50% (heal back half the damage that was dealt)
                // This effectively reduces the total damage
                target.heal(baseDamage * 0.5f);
            }
        }
    }

    /**
     * Generic method to get enchantment level for any enchantment
     */
    private int getEnchantmentLevel(ItemStack stack, LivingEntity entity, ResourceKey<Enchantment> enchantmentKey) {
        Holder<Enchantment> enchantmentHolder = entity.level()
                .registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(enchantmentKey);

        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack);
    }
}
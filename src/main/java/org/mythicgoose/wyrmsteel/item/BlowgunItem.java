package org.mythicgoose.wyrmsteel.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.mythicgoose.wyrmsteel.init.ModEnchantments;
import org.mythicgoose.wyrmsteel.entity.DartProjectile;
import org.mythicgoose.wyrmsteel.init.ModEntities;

public class BlowgunItem extends Item {
    private static final int COOLDOWN_TICKS = 40; // 2 seconds (20 ticks = 1 second)
    private static final float PROJECTILE_VELOCITY = 3.0F;
    private static final float PROJECTILE_INACCURACY = 1.0F;

    public BlowgunItem(Properties properties) {
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
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Check if player is on cooldown
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        // Determine dart type based on enchantments
        DartProjectile.DartType dartType = getDartTypeFromEnchantments(stack);

        // Shoot projectile
        if (!level.isClientSide) {
            // Create dart projectile
            DartProjectile dart = new DartProjectile(
                    ModEntities.DART,
                    player.getX(),
                    player.getEyeY() - 0.1,
                    player.getZ(),
                    level,
                    ItemStack.EMPTY
            );

            // Set the dart's owner and type
            dart.setOwner(player);
            dart.setDartType(dartType);

            // Shoot the dart from the player's rotation
            dart.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, PROJECTILE_VELOCITY, PROJECTILE_INACCURACY);

            // Set dart properties
            dart.setBaseDamage(3.0); // Base damage
            dart.pickup = DartProjectile.Pickup.CREATIVE_ONLY; // Prevent pickup

            // Spawn the dart
            level.addFreshEntity(dart);

            // Play sound
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.4F);

            // Update stats
            player.awardStat(Stats.ITEM_USED.get(this));
        } else {
            // Show dart type to player
            String dartName = switch (dartType) {
                case POISON -> "Poison Dart";
                case TORPOR -> "Torpor Dart";
                case VULNERABLE -> "Breaching Dart";
                case NORMAL -> "Normal Dart";
            };
        }

        // Apply cooldown
        if (!player.isCreative()) {
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private DartProjectile.DartType getDartTypeFromEnchantments(ItemStack stack) {
        // Get the enchantment registry
        var enchantments = stack.getEnchantments();

        // Check for Puncture enchantment (Wither)
        for (var entry : enchantments.entrySet()) {
            if (entry.getKey().is(ModEnchantments.PUNCTURE)) {
                return DartProjectile.DartType.TORPOR;
            }
        }

        // Check for Junglebound enchantment (Poison)
        for (var entry : enchantments.entrySet()) {
            if (entry.getKey().is(ModEnchantments.JUNGLEBOUND)) {
                return DartProjectile.DartType.POISON;
            }
        }

        // Check for Breaching
        for (var entry : enchantments.entrySet()) {
            if (entry.getKey().is(ModEnchantments.BREACHING)) {
                return DartProjectile.DartType.VULNERABLE;
            }
        }

        // No enchantments, return normal
        return DartProjectile.DartType.NORMAL;
    }
}
package org.mythicgoose.wyrmsteel.item;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.mythicgoose.wyrmsteel.init.ModDamageTypes;
import org.mythicgoose.wyrmsteel.init.ModEnchantments;
import org.mythicgoose.wyrmsteel.init.ModParticles;

import java.util.ArrayList;
import java.util.List;

public class ScytheItem extends SwordItem {
    public ScytheItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        int shareLoveLevel = getEnchantmentLevel(itemStack, player, ModEnchantments.SHARE_LOVE);

        if (shareLoveLevel > 0) {
            if (!level.isClientSide) {
                handleShareTheLove(player, level);
            }

            return InteractionResultHolder.success(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void postHurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        handleDecaying(itemStack, target, attacker);

        super.postHurtEnemy(itemStack, target, attacker);
    }

    /**
     * Handles the Share the Love enchantment effect
     * Sends a ray that inflicts the target with the same status effects as the attacker
     * Deals 1.5 hearts damage to target and player loses 1.5 hearts
     * Sets player's effect durations to 20 ticks when hitting a target
     */
    private void handleShareTheLove(Player player, Level world) {
        // Player loses 1.5 hearts (3.0 damage)
        player.hurt(ModDamageTypes.bloodLoss(world, player), 3.0f);

        // Raycast parameters
        double range = 32.0; // Maximum range for the ray
        Vec3 start = player.getEyePosition();
        Vec3 direction = player.getLookAngle();
        Vec3 end = start.add(direction.scale(range));

        // First check for block collision
        BlockHitResult blockHit = player.level().clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        // If we hit a block, limit our range
        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }

        // Find all entities along the ray
        AABB searchBox = new AABB(start, end).inflate(2.0);
        List<LivingEntity> entities = player.level().getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                entity -> entity != player && entity.isAlive()
        );

        // Find the closest entity that intersects with our ray
        LivingEntity closestTarget = null;
        double closestDistance = range;
        Vec3 closestPoint = end;

        for (LivingEntity entity : entities) {
            AABB entityBox = entity.getBoundingBox().inflate(0.3);
            Vec3 intersection = entityBox.clip(start, end).orElse(null);

            if (intersection != null) {
                double distance = start.distanceTo(intersection);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestTarget = entity;
                    closestPoint = intersection;
                }
            }
        }

        // Spawn particle trail to the hit point
        if (player.level() instanceof ServerLevel serverLevel) {
            spawnParticleTrail(serverLevel, start, closestPoint);
        }

        // If we hit a living entity, deal damage and copy effects
        if (closestTarget != null) {
            // Deal 1.5 hearts (3.0 damage) to the target
            closestTarget.hurt(ModDamageTypes.cuts(world, closestTarget, player), 3.0f);

            // Store effects to copy before modifying player's effects
            List<MobEffectInstance> effectsToCopy = new ArrayList<>();
            for (MobEffectInstance effect : player.getActiveEffects()) {
                effectsToCopy.add(new MobEffectInstance(
                        effect.getEffect(),
                        effect.getDuration(),
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.isVisible(),
                        effect.showIcon()
                ));
            }

            // Copy effects to target
            for (MobEffectInstance effect : effectsToCopy) {
                closestTarget.addEffect(effect);
            }

            // Reduce all player's effect durations to 20 ticks after hitting target
            // We need to remove and re-add because addEffect won't replace with shorter duration
            for (MobEffectInstance effect : effectsToCopy) {
                player.removeEffect(effect.getEffect());

                MobEffectInstance reducedEffect = new MobEffectInstance(
                        effect.getEffect(),
                        20, // Set duration to 20 ticks (1 second)
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.isVisible(),
                        effect.showIcon()
                );

                player.addEffect(reducedEffect);
            }

            // Spawn extra particles at the target
            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ModParticles.BLOOD_BUBBLE,
                        closestTarget.getX(),
                        closestTarget.getY() + closestTarget.getBbHeight() / 2,
                        closestTarget.getZ(),
                        3, // more particles at target
                        0.5, 0.5, 0.5, // spread
                        0.1 // speed
                );
            }
        }
    }

    /**
     * Spawns a particle trail from start to end position
     */
    private void spawnParticleTrail(ServerLevel level, Vec3 start, Vec3 end) {
        Vec3 direction = end.subtract(start);
        double distance = direction.length();

        if (distance < 0.1) return; // Skip if too short

        Vec3 normalized = direction.normalize();

        // Number of particles based on distance (one particle every 0.2 blocks for denser trail)
        int particleCount = Math.max(2, (int) (distance / 0.2));

        for (int i = 0; i < particleCount; i++) {
            double progress = (double) i / particleCount;
            Vec3 particlePos = start.add(normalized.scale(distance * progress));

            level.sendParticles(
                    ModParticles.BLOOD_BUBBLE,
                    particlePos.x,
                    particlePos.y,
                    particlePos.z,
                    1, // particle count
                    0.25f, 0.25f, 0.25f, // small offset for variety
                    0.0 // speed
            );
        }
    }

    /**
     * Handles the Decaying enchantment effect
     */
    private void handleDecaying(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        int level = getEnchantmentLevel(itemStack, attacker, ModEnchantments.DECAYING);

        if (level > 0) {
            // Apply wither effect (duration and amplifier scale with enchantment level)
            int duration = 300; // 300 ticks (15 seconds)
            int amplifier = 20; // Wither effect strength

            target.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, amplifier, false, true));
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
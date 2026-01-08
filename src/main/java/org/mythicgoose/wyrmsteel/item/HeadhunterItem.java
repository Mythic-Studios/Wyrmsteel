package org.mythicgoose.wyrmsteel.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.mythicgoose.wyrmsteel.init.ModEnchantments;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HeadhunterItem extends Item {
    private final int MAX_AMMO;
    private final int COOLDOWN_TICKS; // 5 seconds
    private static final float DAMAGE = 8.0F;
    private static final float MARKED_DAMAGE_MULTIPLIER = 2.0F;
    private static final double RANGE = 50.0;

    public HeadhunterItem(Properties pProperties, int maxAmmo, int cooldownTicks) {
        super(pProperties.stacksTo(1));
        MAX_AMMO = maxAmmo;
        COOLDOWN_TICKS = cooldownTicks;
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    // Helper method to find entity player is looking at
    @Nullable
    private LivingEntity getTargetEntity(Level pLevel, Player pPlayer, double range) {
        var start = pPlayer.getEyePosition();
        var lookVec = pPlayer.getLookAngle();
        var end = start.add(lookVec.scale(range));

        var aabb = pPlayer.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1.0);

        LivingEntity closestEntity = null;
        double closestDistance = range;

        for (var entity : pLevel.getEntities(pPlayer, aabb)) {
            if (entity instanceof LivingEntity living && entity != pPlayer) {
                var entityAABB = living.getBoundingBox().inflate(0.3);
                var clip = entityAABB.clip(start, end);

                if (clip.isPresent()) {
                    double distance = start.distanceTo(clip.get());
                    if (distance < closestDistance) {
                        closestEntity = living;
                        closestDistance = distance;
                    }
                }
            }
        }

        return closestEntity;
    }

    // Right-click to shoot OR mark (depending on sneak)
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        int enchantmentLevel = getEnchantmentLevel(itemStack, pPlayer, ModEnchantments.MARKSMAN);


        if (!pLevel.isClientSide) {
            // Shift + Right-Click = Mark target
            if (pPlayer.isShiftKeyDown() && enchantmentLevel > 0) {
                LivingEntity target = getTargetEntity(pLevel, pPlayer, RANGE);

                if (target != null) {
                    // Mark the target
                    var tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                    tag.putUUID("MarkedTarget", target.getUUID());
                    itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

                    pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                            SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.5F);

                    pPlayer.displayClientMessage(
                            Component.literal("Target Marked!").withStyle(ChatFormatting.RED),
                            true
                    );

                    if (pLevel instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.CRIT,
                                target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                                20, 0.5, 0.5, 0.5, 0.1);
                    }

                    return InteractionResultHolder.success(itemStack);
                }

                pPlayer.displayClientMessage(
                        Component.literal("No target to mark!").withStyle(ChatFormatting.GRAY),
                        true
                );
                return InteractionResultHolder.pass(itemStack);
            }

            // Normal Right-Click = Shoot
            shoot(pLevel, pPlayer, itemStack);
            return InteractionResultHolder.success(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }

    // Public reload method (called from keybind)
    public boolean reload(Level pLevel, Player pPlayer, ItemStack pStack) {
        if (pLevel.isClientSide) return false;

        int currentAmmo = getAmmo(pStack);

        // Can't reload if already at max
        if (currentAmmo >= MAX_AMMO) {
            pPlayer.displayClientMessage(
                    Component.literal("Already at max ammo!").withStyle(ChatFormatting.GRAY),
                    true
            );
            return false;
        }

        // Can't reload if already Reloading
        if (pPlayer.getCooldowns().isOnCooldown(this)) {
            pPlayer.displayClientMessage(
                    Component.literal("Already Reloading!").withStyle(ChatFormatting.RED),
                    true
            );
            return false;
        }

        // Start reload
        setAmmo(pStack, MAX_AMMO);
        pPlayer.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                SoundEvents.IRON_GOLEM_REPAIR, SoundSource.PLAYERS, 0.8F, 1.2F);

        pPlayer.displayClientMessage(
                Component.literal("Reloading...").withStyle(ChatFormatting.YELLOW),
                true
        );

        return true;
    }

    // Left-click shooting method (call from event handler)
    public boolean shoot(Level pLevel, Player pPlayer, ItemStack pStack) {
        if (pLevel.isClientSide) return false;

        int ammo = getAmmo(pStack);

        // Check ammo
        if (ammo <= 0) {
            pPlayer.displayClientMessage(
                    Component.literal("Out of ammo!").withStyle(ChatFormatting.YELLOW),
                    true
            );
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                    SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 0.5F, 0.8F);
            return false;
        }

        // Always consume ammo and play shoot sound
        setAmmo(pStack, ammo - 1);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);

        // Find target entity
        LivingEntity target = getTargetEntity(pLevel, pPlayer, RANGE);

        if (target != null) {
            UUID markedUUID = getMarkedTarget(pStack);
            boolean isMarked = markedUUID != null && markedUUID.equals(target.getUUID());

            float damage = isMarked ? DAMAGE * MARKED_DAMAGE_MULTIPLIER : DAMAGE;
            target.hurt(pLevel.damageSources().playerAttack(pPlayer), damage);

            // Hit feedback particles
            if (pLevel instanceof ServerLevel serverLevel) {
                SimpleParticleType particleType = isMarked ? ParticleTypes.FLAME : ParticleTypes.CRIT;
                serverLevel.sendParticles(particleType,
                        target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                        15, 0.3, 0.3, 0.3, 0.05);
            }
        } else {
            // Miss feedback
            if (pLevel instanceof ServerLevel serverLevel) {
                var lookVec = pPlayer.getLookAngle();
                serverLevel.sendParticles(ParticleTypes.SMOKE,
                        pPlayer.getX() + lookVec.x * 2,
                        pPlayer.getEyeY() + lookVec.y * 2,
                        pPlayer.getZ() + lookVec.z * 2,
                        5, 0.1, 0.1, 0.1, 0.01);
            }
        }

        // Start cooldown if out of ammo (auto-reload)
        if (getAmmo(pStack) <= 0) {
            pPlayer.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            // Clear mark on empty
            CustomData data = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            var tag = data.copyTag();
            tag.remove("MarkedTarget");
            pStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

            pPlayer.displayClientMessage(
                    Component.literal("Reloading...").withStyle(ChatFormatting.YELLOW),
                    true
            );
        }

        return true;
    }

    // Ammo management
    private int getAmmo(ItemStack pStack) {
        CustomData data = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (!data.contains("Ammo")) {
            // Initialize with max ammo
            setAmmo(pStack, MAX_AMMO);
            return MAX_AMMO;
        }
        return data.copyTag().getInt("Ammo");
    }

    private void setAmmo(ItemStack pStack, int ammo) {
        CustomData data = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = data.copyTag();
        tag.putInt("Ammo", Math.max(0, ammo));
        pStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Nullable
    private UUID getMarkedTarget(ItemStack pStack) {
        CustomData data = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (!data.contains("MarkedTarget")) {
            return null;
        }
        return data.copyTag().getUUID("MarkedTarget");
    }

    // Durability bar for ammo
    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        int ammo = getAmmo(pStack);
        return Math.round(12.0F * ammo / MAX_AMMO);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        int ammo = getAmmo(pStack);
        float ratio = (float) ammo / MAX_AMMO;

        // Red when low, yellow when medium, green when high
        if (ratio > 0.66F) {
            return 0x00FF00; // Green
        } else if (ratio > 0.33F) {
            return 0xFFFF00; // Yellow
        } else {
            return 0xFF0000; // Red
        }
    }

    // Auto-reload when cooldown ends
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player player) {
            if (getAmmo(pStack) <= 0 && !player.getCooldowns().isOnCooldown(this)) {
                setAmmo(pStack, MAX_AMMO);
                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.IRON_GOLEM_REPAIR, SoundSource.PLAYERS, 0.8F, 1.2F);

                player.displayClientMessage(
                        Component.literal("Reloaded!").withStyle(ChatFormatting.GREEN),
                        true
                );
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        int ammo = getAmmo(pStack);

        pTooltipComponents.add(Component.literal("Ammo: " + ammo + "/" + MAX_AMMO)
                .withStyle(ammo > 0 ? ChatFormatting.GREEN : ChatFormatting.RED));

        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.literal("Right Click: ").withStyle(ChatFormatting.BLUE)
                .append(Component.literal("Shoot").withStyle(ChatFormatting.WHITE)));

        // Check for MARKSMAN enchantment
        boolean hasMarksman = pContext != null && getEnchantmentLevel(pStack, pContext, ModEnchantments.MARKSMAN) > 0;

        if (hasMarksman) {
            pTooltipComponents.add(Component.literal("Shift + Right Click: ").withStyle(ChatFormatting.LIGHT_PURPLE)
                    .append(Component.literal("Mark Target").withStyle(ChatFormatting.WHITE)));
        }

        pTooltipComponents.add(Component.literal("Z: ").withStyle(ChatFormatting.YELLOW)
                .append(Component.literal("Manual Reload").withStyle(ChatFormatting.WHITE)));

        pTooltipComponents.add(Component.literal("Auto-reloads when empty").withStyle(ChatFormatting.GRAY));

        if (hasMarksman) {
            pTooltipComponents.add(Component.literal("Marked targets take 2x damage!").withStyle(ChatFormatting.GOLD));
        }
    }

    private int getEnchantmentLevel(ItemStack stack, TooltipContext context, ResourceKey<Enchantment> enchantmentKey) {
        if (context.registries() == null) {
            return 0;
        }

        Optional<HolderLookup.RegistryLookup<Enchantment>> registryLookup = context.registries()
                .lookup(Registries.ENCHANTMENT);

        if (registryLookup.isEmpty()) {
            return 0;
        }

        Optional<Holder.Reference<Enchantment>> enchantmentHolder = registryLookup.get().get(enchantmentKey);

        return enchantmentHolder.map(enchantmentReference -> EnchantmentHelper.getItemEnchantmentLevel(enchantmentReference, stack)).orElse(0);

    }

    // Prevent normal attack behavior
    @Override
    public boolean canAttackBlock(net.minecraft.world.level.block.state.BlockState pState, Level pLevel, net.minecraft.core.BlockPos pPos, Player pPlayer) {
        return false;
    }

    private int getEnchantmentLevel(ItemStack stack, LivingEntity entity, ResourceKey<Enchantment> enchantmentKey) {
        Holder<Enchantment> enchantmentHolder = entity.level()
                .registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(enchantmentKey);

        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack);
    }
}
package org.mythicgoose.wyrmsteel.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.mythicgoose.wyrmsteel.init.ModEffects;
import org.mythicgoose.wyrmsteel.network.ModMessages;
import org.mythicgoose.wyrmsteel.network.TotemAnimationPayload;

public class TotemOfHealthiness extends Item {
    public TotemOfHealthiness(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if ((player.getHealth() <= 15)) {
                // Set player health to max
                player.setHealth(player.getMaxHealth());

                // Set hunger to full
                FoodData foodData = player.getFoodData();
                foodData.setFoodLevel(20);
                foodData.setSaturation(20.0F);

                // Remove all effects
                player.removeAllEffects();

                player.addEffect(new MobEffectInstance(ModEffects.TORPOR, 100000, 0));
                player.addEffect(new MobEffectInstance(ModEffects.VULNERABILITY, 100000, 1));

                // Spawn totem particles around the player
                if (level instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 100; i++) {
                        double offsetX = (level.random.nextDouble() - 0.5) * 2.0;
                        double offsetY = level.random.nextDouble() * 2.0;
                        double offsetZ = (level.random.nextDouble() - 0.5) * 2.0;

                        serverLevel.sendParticles(
                                ParticleTypes.TOTEM_OF_UNDYING,
                                player.getX() + offsetX,
                                player.getY() + offsetY,
                                player.getZ() + offsetZ,
                                1,
                                0.0, 0.0, 0.0,
                                0.5
                        );
                    }
                }

                // Send custom packet to show YOUR item in the totem animation
                ModMessages.sendToPlayer((ServerPlayer) player, new TotemAnimationPayload(stack.copy()));

                // Play the totem sound effect (server-side for other players)
                player.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);

                // Trigger the game event
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);

                // Update stats
                player.awardStat(Stats.ITEM_USED.get(this));

                // Consume the item
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
//                    stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));

                }
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
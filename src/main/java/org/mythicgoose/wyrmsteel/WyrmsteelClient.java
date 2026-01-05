package org.mythicgoose.wyrmsteel;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.mythicgoose.wyrmsteel.custom_slot.InventoryAccessor;
import org.mythicgoose.wyrmsteel.entity.DartRenderer;
import org.mythicgoose.wyrmsteel.init.ModEntities;
import org.mythicgoose.wyrmsteel.init.ModInjections;
import org.mythicgoose.wyrmsteel.item.InjectionItem;
import org.mythicgoose.wyrmsteel.keybinding.ClientTickHandler;
import org.mythicgoose.wyrmsteel.keybinding.KeybindRegistry;
import org.mythicgoose.wyrmsteel.network.S2CBackWeaponSyncPacket;

public class WyrmsteelClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeybindRegistry.register();
        ClientTickHandler.register();
        ClientPlayNetworking.registerGlobalReceiver(
                S2CBackWeaponSyncPacket.TYPE,
                (payload, context) -> {
                    System.out.println("CLIENT: Received sync packet - weaponStack: " + payload.weaponStack());
                    context.client().execute(() -> {
                        ClientLevel level = context.client().level;
                        if (level != null) {
                            Entity entity = level.getEntity(payload.playerId());
                            if (entity instanceof Player player) {
                                System.out.println("CLIENT: Before update, stash has: " + ((InventoryAccessor) player.getInventory()).weapons_of_death$getWeaponStashSlot());
                                ((InventoryAccessor) player.getInventory())
                                        .weapons_of_death$setWeaponStashSlot(payload.weaponStack());
                                System.out.println("CLIENT: After update, stash has: " + ((InventoryAccessor) player.getInventory()).weapons_of_death$getWeaponStashSlot());
                            }
                        }
                    });
                }
        );
        EntityRendererRegistry.register(ModEntities.DART, DartRenderer::new);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    if (stack.getItem() instanceof InjectionItem injectionItem) {
                        int color = injectionItem.getColor(tintIndex);
                        // Add alpha channel to make it visible!
                        return 0xFF000000 | color;
                    }
                    return 0xFFFFFFFF; // White with full alpha
                },
                ModInjections.INJECTION_MILK,
                ModInjections.INJECTION_POISON,
                ModInjections.INJECTION_WITHER,
                ModInjections.INJECTION_TORPOR,
                ModInjections.INJECTION_STRENGTH,
                ModInjections.INJECTION_LEAPING,
                ModInjections.INJECTION_VISION,
                ModInjections.INJECTION_REGEN,
                ModInjections.INJECTION_INVIS,
                ModInjections.INJECTION_SPEED,
                ModInjections.INJECTION_FIRE_RESIST,
                ModInjections.INJECTION_SLOWNESS,
                ModInjections.INJECTION_SLOW_FALL,
                ModInjections.INJECTION_WATER_BREATHING,

                ModInjections.INJECTION_WEAVING,
                ModInjections.INJECTION_OOZING,
                ModInjections.INJECTION_WIND_CHARGED,
                ModInjections.INJECTION_INFESTING
        );
    }
}

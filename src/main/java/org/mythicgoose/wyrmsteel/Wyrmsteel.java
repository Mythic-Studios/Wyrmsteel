package org.mythicgoose.wyrmsteel;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.mythicgoose.wyrmsteel.client.ReloadPacket;
import org.mythicgoose.wyrmsteel.custom_slot.InventoryAccessor;
import org.mythicgoose.wyrmsteel.custom_slot.PlayerDeathHandler;
import org.mythicgoose.wyrmsteel.init.*;
import org.mythicgoose.wyrmsteel.network.ModMessages;
import org.mythicgoose.wyrmsteel.network.ModPackets;
import org.mythicgoose.wyrmsteel.network.NetworkHelper;
import org.mythicgoose.wyrmsteel.network.WeaponStashSwapPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wyrmsteel implements ModInitializer {
    public static final String MOD_ID = "wyrmsteel";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("For Special Server");

        ModPackets.registerPackets();
        PlayerDeathHandler.register();
        WeaponStashSwapPayload.register();
        ModParticles.registerParticles();

        ModItems.init();
        ModInjections.init();
        ModItemGroups.init();
        ModEntities.register();
        ModEffects.registerEffects();

        ModMessages.registerC2SPackets();
        ModMessages.registerS2CPackets();

        ReloadPacket.register();
        ReloadPacket.registerServerReceiver();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            ItemStack weapon = ((InventoryAccessor) player.getInventory())
                    .weapons_of_death$getWeaponStashSlot();
            NetworkHelper.syncBackWeaponToClients(player, weapon);
        });

        // Sync when entity loads (for when players come into view)
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayer player) {
                ItemStack weapon = ((InventoryAccessor) player.getInventory())
                        .weapons_of_death$getWeaponStashSlot();
                NetworkHelper.syncBackWeaponToClients(player, weapon);
            }
        });
	}
}
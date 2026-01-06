package org.mythicgoose.wyrmsteel.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModPackets {

    public static void registerPackets() {
        // Register S2C packets
        PayloadTypeRegistry.playS2C().register(
                S2CBackWeaponSyncPacket.TYPE,
                S2CBackWeaponSyncPacket.CODEC
        );

        // Register C2S packet types
        PayloadTypeRegistry.playC2S().register(
                WeaponStashSwapPayload.TYPE,
                WeaponStashSwapPayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                C2SWeaponStashSlotClickPacket.TYPE,
                C2SWeaponStashSlotClickPacket.CODEC
        );

        // Register packet RECEIVERS (this is what you were missing!)
        WeaponStashSwapPayload.register();
        C2SWeaponStashSlotClickPacket.register();
    }
}
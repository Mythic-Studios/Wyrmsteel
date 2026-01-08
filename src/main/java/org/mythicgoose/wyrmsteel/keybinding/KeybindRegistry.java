package org.mythicgoose.wyrmsteel.keybinding;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import org.mythicgoose.wyrmsteel.client.ReloadPacket;

public class KeybindRegistry {
    public static KeyMapping reloadKey;
    public static KeyMapping swapSlots;
    public static KeyMapping EQUIP_BACK_WEAPON;

    public static void register() {
        // Register the reload keybind (default: R key)
        reloadKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.wyrmsteel.reload",
                GLFW.GLFW_KEY_Z,
                "key.categories.wyrmsteel"
        ));

        // Register tick handler to check for key presses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (reloadKey.consumeClick()) {
                // Send reload request to server
                ReloadPacket.send();
            }
        });
        swapSlots = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.wyrmsteel.swap_slots", // Translation key
                GLFW.GLFW_KEY_G, // Default key (G)
                "key.categories.wyrmsteel" // Category
        ));
        EQUIP_BACK_WEAPON = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.wyrmsteel.equip_back_weapon",
                GLFW.GLFW_KEY_R, // Default to R key, change as needed
                "key.categories.wyrmsteel"
        ));
    }
}
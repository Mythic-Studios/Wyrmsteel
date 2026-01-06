package org.mythicgoose.wyrmsteel.keybinding;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeybindRegistry {
    public static KeyMapping swapSlots;
    public static KeyMapping EQUIP_BACK_WEAPON;

    public static void register() {
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
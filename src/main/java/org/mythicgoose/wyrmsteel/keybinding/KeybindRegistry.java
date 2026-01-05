package org.mythicgoose.wyrmsteel.keybinding;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeybindRegistry {
    public static KeyMapping swapSlots;

    public static void register() {
        swapSlots = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.wyrmsteel.swap_slots", // Translation key
                GLFW.GLFW_KEY_R, // Default key (R)
                "key.categories.wyrmsteel" // Category
        ));
    }
}
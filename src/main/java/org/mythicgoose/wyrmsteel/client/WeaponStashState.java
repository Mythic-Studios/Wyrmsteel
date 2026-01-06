// WeaponStashState.java
package org.mythicgoose.wyrmsteel.client;

import net.minecraft.world.item.ItemStack;

public class WeaponStashState {
    private static boolean equipped = false;
    private static ItemStack cachedStack = ItemStack.EMPTY;
    private static int previousHotbarSlot = -1;

    public static boolean isEquipped() {
        return equipped;
    }

    public static void setEquipped(boolean value) {
        equipped = value;
        if (!value) {
            cachedStack = ItemStack.EMPTY;
            previousHotbarSlot = -1;
        }
    }

    public static ItemStack getCachedStack() {
        return cachedStack;
    }

    public static void setCachedStack(ItemStack stack) {
        cachedStack = stack;
    }

    public static int getPreviousHotbarSlot() {
        return previousHotbarSlot;
    }

    public static void setPreviousHotbarSlot(int slot) {
        previousHotbarSlot = slot;
    }

    public static void reset() {
        equipped = false;
        cachedStack = ItemStack.EMPTY;
        previousHotbarSlot = -1;
    }
}
// HotbarWeaponStashRenderer.java
package org.mythicgoose.wyrmsteel.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.mythicgoose.wyrmsteel.client.WeaponStashState;
import org.mythicgoose.wyrmsteel.custom_slot.InventoryAccessor;

@Environment(EnvType.CLIENT)
public class HotbarWeaponStashRenderer implements HudRenderCallback {

    private static final ResourceLocation HOTBAR_OFFHAND_RIGHT_SPRITE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/sprites/hud/hotbar_offhand_right.png");

    private static final ResourceLocation HOTBAR_SELECTION_SPRITE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/sprites/hud/hotbar_selection.png");

    @Override
    public void onHudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) return;

        // Get the weapon stash item
        ItemStack weaponStack = ((InventoryAccessor) player.getInventory())
                .weapons_of_death$getWeaponStashSlot();

        // Only render if there's an item
        if (weaponStack.isEmpty()) {
            return;
        }

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        // Position: Right of the off-hand slot
        int x = screenWidth / 2 + 91;
        int y = screenHeight - 23;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Always render the slot background first
        guiGraphics.blit(HOTBAR_OFFHAND_RIGHT_SPRITE, x, y, 0, 0, 29, 24, 29, 24);

        // If equipped, render the selection sprite OVER the slot
        if (WeaponStashState.isEquipped()) {
            // Selection sprite is 24x23, center it over the slot (29x24)
            // Offset by +2 on X to center horizontally
            guiGraphics.blit(HOTBAR_SELECTION_SPRITE, x + 6, y, 0, 0, 24, 23, 24, 24);
        }

        // Render the item on top
        guiGraphics.renderItem(weaponStack, x + 10, y + 4);
        guiGraphics.renderItemDecorations(minecraft.font, weaponStack, x + 10, y + 4);

        RenderSystem.disableBlend();
    }
}
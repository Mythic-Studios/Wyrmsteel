package org.mythicgoose.wyrmsteel.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import org.mythicgoose.wyrmsteel.custom_slot.WeaponStashSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractContainerScreen<AbstractContainerMenu> {

    @Shadow
    private static CreativeModeTab selectedTab;

    @Unique
    private static final ResourceLocation INVENTORY_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/gui/container/inventory.png");

    public CreativeInventoryScreenMixin(AbstractContainerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @WrapOperation(
            method = "selectTab",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/NonNullList;add(Ljava/lang/Object;)Z",
                    ordinal = 2
            )
    )
    private boolean moveWeaponSlot(NonNullList<Slot> slots, Object object, Operation<Boolean> operation) {
        if (object instanceof CreativeModeInventoryScreen.SlotWrapper newSlot) {
            Slot slot = ((CreativeSlotAccessor) newSlot).getSlot();
            if (slot instanceof WeaponStashSlot) {
                return operation.call(slots, new CreativeModeInventoryScreen.SlotWrapper(slot, slot.index, 128, 21));
            }
        }
        return operation.call(slots, object);
    }

    @Inject(method = "renderBg", at = @At("RETURN"))
    private void renderWeaponStashBackground(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (selectedTab == null || selectedTab.getType() != CreativeModeTab.Type.INVENTORY) {
            return;
        }

        // Render the slot background
        int x = this.leftPos + 127;
        int y = this.topPos + 20;
        guiGraphics.blit(INVENTORY_LOCATION, x, y, 7, 83, 18, 18);
    }

    // Force render the custom slot (slot 46)
    @Inject(method = "render", at = @At("RETURN"))
    private void renderWeaponStashSlot(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (selectedTab == null || selectedTab.getType() != CreativeModeTab.Type.INVENTORY) {
            return;
        }

        // Manually render slot 46 (our custom slot)
        if (this.menu.slots.size() > 46) {
            Slot customSlot = this.menu.slots.get(46);

            // Render the slot's item
            int slotX = this.leftPos + customSlot.x;
            int slotY = this.topPos + customSlot.y;

            guiGraphics.renderItem(customSlot.getItem(), slotX, slotY);
            guiGraphics.renderItemDecorations(this.font, customSlot.getItem(), slotX, slotY);
        }
    }
}
package org.mythicgoose.wyrmsteel.custom_slot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.mythicgoose.wyrmsteel.init.ModItems;
import org.mythicgoose.wyrmsteel.util.Tags;

public class BackWeaponRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public BackWeaponRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> context) {
        super(context);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        // Get the back weapon stack from custom inventory slot
        ItemStack stack = ((InventoryAccessor) player.getInventory()).weapons_of_death$getWeaponStashSlot();
        if (stack.isEmpty()) return;

        matrices.pushPose();

        // Check for cape and chestplate
        boolean hasCape = player.getSkin().capeTexture() != null
                && !player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
                && player.isModelPartShown(PlayerModelPart.CAPE);
        boolean hasChestPlate = !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty();

        matrices.translate(0.0F, 0.0F, 0.05F + (hasCape ? 0.05f : 0f) + (hasChestPlate ? 0.05f : 0f));

        // Cape physics calculations
        double d = Mth.lerp(partialTick, player.xCloakO, player.xCloak)
                - Mth.lerp(partialTick, player.xo, player.getX());
        double e = Mth.lerp(partialTick, player.yCloakO, player.yCloak)
                - Mth.lerp(partialTick, player.yo, player.getY());
        double m = Mth.lerp(partialTick, player.zCloakO, player.zCloak)
                - Mth.lerp(partialTick, player.zo, player.getZ());
        float n = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
        double o = Mth.sin(n * (float) (Math.PI / 180.0));
        double p = (-Mth.cos(n * (float) (Math.PI / 180.0)));
        float q = (float) e * 10.0F;
        q = Mth.clamp(q, -6.0F, 0f);
        float r = (float) (d * o + m * p) * 100.0F;
        r = Mth.clamp(r, 0.0F, 40.0F);
        float s = (float) (d * p - m * o) * 100.0F;
        s = Mth.clamp(s, -20.0F, 20.0F);
        if (r < 0.0F) {
            r = 0.0F;
        }

        float t = Mth.lerp(partialTick, player.oBob, player.bob);
        q += Mth.sin(Mth.lerp(partialTick, player.oAttackAnim, player.attackAnim) * 6.0F) * 32.0F * t;
        if (player.isCrouching()) {
            q += 25.0F;
        }

        // Apply cape physics with reduced effect for pocket weapons
        boolean isPocketWeapon = stack.is(Tags.POCKET_WEAPONS);
        float physicsMultiplier = isPocketWeapon ? 0.4f : 1.0f;

        matrices.mulPose(Axis.XP.rotationDegrees(6f + (r / 2.0F + q) * physicsMultiplier));
        matrices.mulPose(Axis.ZP.rotationDegrees(s / 2.0F * physicsMultiplier));
        matrices.mulPose(Axis.YP.rotationDegrees(180.0F - s / 2.0F * physicsMultiplier));

        float scale = 0.85f;
        if (stack.is(Tags.BIG_WEAPONS)) {
            scale = 1.6f;
            matrices.translate(0.0, 0.2, -0.15);
        }
        else if (isPocketWeapon) {
            if (stack.is(ModItems.SICKLE)) {
                scale = 0.6f;

                matrices.translate(0.275, 0.8, 0.225);

                matrices.mulPose(Axis.XP.rotationDegrees(-102.5F));
                matrices.mulPose(Axis.YP.rotationDegrees(-90.0F));
                matrices.mulPose(Axis.ZP.rotationDegrees(180.0F));
            } else if (stack.is(ModItems.BLOWGUN)) {
                scale = 0.6f;

                matrices.translate(0.275, 0.8, 0.125);

                matrices.mulPose(Axis.XP.rotationDegrees(152.0F));
                matrices.mulPose(Axis.YP.rotationDegrees(-90.0F));
                matrices.mulPose(Axis.ZP.rotationDegrees(180.0F));
            } else {
                scale = 0.6f;

                matrices.translate(0.275, 0.8, 0.225);

                matrices.mulPose(Axis.XP.rotationDegrees(-102.5F));
                matrices.mulPose(Axis.YP.rotationDegrees(-90.0F));
                matrices.mulPose(Axis.ZP.rotationDegrees(180.0F));
            }
        }
        else {
            matrices.translate(0, 0.3, -0.1);
        }
        if (stack.is(Tags.SHIELDS)) {
            scale = 1.8f;
            matrices.translate(0.0, 0.2, 0.0);
        }

        matrices.scale(scale, scale, scale);

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, player.level(), 0);
        matrices.popPose();
    }
}
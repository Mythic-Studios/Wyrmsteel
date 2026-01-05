package org.mythicgoose.wyrmsteel.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.mythicgoose.wyrmsteel.Wyrmsteel;

public class DartRenderer extends ArrowRenderer<DartProjectile> {
    private static final ResourceLocation DART_TEXTURE = ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "textures/entity/projectiles/poison_dart.png");

    public DartRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(DartProjectile entity) {
        return DART_TEXTURE;
    }
}
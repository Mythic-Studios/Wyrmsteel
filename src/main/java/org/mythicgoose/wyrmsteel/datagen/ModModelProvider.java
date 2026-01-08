package org.mythicgoose.wyrmsteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.init.ModInjections;
import org.mythicgoose.wyrmsteel.init.ModItems;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        ModelTemplate injectionTemplate = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "item/injection_template")),
                Optional.empty()
        );

        itemModelGenerators.generateFlatItem(ModItems.WEAPON_CORE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.UTILITY_CORE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WYRMSTEEL_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WYRMSTEEL_NUGGET, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WARDEN_HIDE, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateFlatItem(ModItems.EMPTY_INJECTION, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.TOTEM_OF_HEALTHINESS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.INFINITE_PEARL, ModelTemplates.FLAT_ITEM);

        // Loop through the list directly
        for (Item item : ModInjections.ALL_INJECTIONS) {
            itemModelGenerators.generateFlatItem(item, injectionTemplate);
        }
    }
}
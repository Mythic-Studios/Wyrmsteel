package org.mythicgoose.wyrmsteel.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.mythicgoose.wyrmsteel.Wyrmsteel;

public class ModItemGroups {


    public static CreativeModeTab CORE;
    public static CreativeModeTab INJECTIONS;


    public static void init() {

        CORE = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "wyrmsteel_c"),
                FabricItemGroup.builder().title(Component.translatable("itemgroup.wyrmsteel.core"))
                        .icon(() -> new ItemStack(ModItems.WYRMSTEEL_INGOT)).displayItems((itemDisplayParameters, output) -> {

                            output.accept(ModItems.WYRMSTEEL_INGOT);
                            output.accept(ModItems.WYRMSTEEL_NUGGET);

                            output.accept(ModItems.WEAPON_CORE);
                            output.accept(ModItems.SCYTHE);
                            output.accept(ModItems.SICKLE);
                            output.accept(ModItems.BLOWGUN);

                            output.accept(ModItems.UTILITY_CORE);
                            output.accept(ModItems.EMPTY_INJECTION);
                            output.accept(ModItems.TOTEM_OF_HEALTHINESS);
                            output.accept(ModItems.INFINITE_PEARL);


                        }).build());

        INJECTIONS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "wyrmsteel_i"),
                FabricItemGroup.builder().title(Component.translatable("itemgroup.wyrmsteel.injections"))
                        .icon(() -> new ItemStack(ModItems.EMPTY_INJECTION)).displayItems((itemDisplayParameters, output) -> {

                            for (var injection : ModInjections.ALL_INJECTIONS) {
                                output.accept(injection);
                            }

                        }).build());
    }
}

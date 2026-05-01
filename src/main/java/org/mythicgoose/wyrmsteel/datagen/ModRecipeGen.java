package org.mythicgoose.wyrmsteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.mythicgoose.wyrmsteel.init.ModInjections;
import org.mythicgoose.wyrmsteel.init.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeGen extends FabricRecipeProvider {
    public ModRecipeGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {

        // Other

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WEAPON_CORE)
                .pattern("DAD")
                .pattern("ASA")
                .pattern("DAD")
                .define('A', Items.AMETHYST_SHARD)
                .define('D', Items.DIAMOND)
                .define('S', Items.NETHER_STAR)
                .unlockedBy("needs_nether_star", has(Items.NETHER_STAR))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UTILITY_CORE)
                .pattern("CAC")
                .pattern("ASA")
                .pattern("CAC")
                .define('A', Items.AMETHYST_SHARD)
                .define('C', Blocks.GLASS.asItem())
                .define('S', Items.NETHER_STAR)
                .unlockedBy("needs_nether_star", has(Items.NETHER_STAR))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WYRMSTEEL_INGOT)
                .pattern("CAC")
                .pattern("ASA")
                .pattern("CAC")
                .define('A', Items.AMETHYST_SHARD)
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.NETHERITE_INGOT)
                .unlockedBy("needs_netherite", has(Items.NETHERITE_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WYRMSTEEL_INGOT)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.WYRMSTEEL_NUGGET)
                .unlockedBy("needs_wyrmsteel_nugget", has(ModItems.WYRMSTEEL_NUGGET))
                .save(output, "wyrmsteel_from_nuggets");


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.WYRMSTEEL_NUGGET, 9)
                .requires(ModItems.WYRMSTEEL_INGOT)
                .unlockedBy("wyrmsteel_ingot", has(ModItems.WYRMSTEEL_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.TOTEM_OF_HEALTHINESS)
                .pattern("III")
                .pattern("ITI")
                .pattern("ICI")
                .define('I', ModItems.WYRMSTEEL_INGOT)
                .define('T', Items.TOTEM_OF_UNDYING)
                .define('C', ModItems.UTILITY_CORE)
                .unlockedBy("needs_utility_core", has(ModItems.UTILITY_CORE))
                .unlockedBy("needs_wrymsteel", has(ModItems.WYRMSTEEL_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.INFINITE_PEARL)
                .pattern(" I ")
                .pattern("ITI")
                .pattern(" C ")
                .define('I', ModItems.WYRMSTEEL_INGOT)
                .define('T', Items.ENDER_PEARL)
                .define('C', ModItems.UTILITY_CORE)
                .unlockedBy("needs_utility_core", has(ModItems.UTILITY_CORE))
                .unlockedBy("needs_wrymsteel", has(ModItems.WYRMSTEEL_INGOT))
                .save(output);

        // Weapons

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SCYTHE)
                .pattern("IIW")
                .pattern(" S ")
                .pattern("S  ")
                .define('I', ModItems.WYRMSTEEL_INGOT)
                .define('S', Items.STICK)
                .define('W', ModItems.WEAPON_CORE)
                .unlockedBy("needs_weapon_core", has(ModItems.WEAPON_CORE))
                .unlockedBy("needs_wrymsteel", has(ModItems.WYRMSTEEL_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SICKLE)
                .pattern("III")
                .pattern(" WI")
                .pattern(" S ")
                .define('I', ModItems.WYRMSTEEL_INGOT)
                .define('S', Items.STICK)
                .define('W', ModItems.WEAPON_CORE)
                .unlockedBy("needs_weapon_core", has(ModItems.WEAPON_CORE))
                .unlockedBy("needs_wrymsteel", has(ModItems.WYRMSTEEL_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BLOWGUN)
                .pattern("  W")
                .pattern("IB ")
                .pattern("II ")
                .define('I', ModItems.WYRMSTEEL_INGOT)
                .define('B', Items.BAMBOO)
                .define('W', ModItems.WEAPON_CORE)
                .unlockedBy("needs_weapon_core", has(ModItems.WEAPON_CORE))
                .unlockedBy("needs_wrymsteel", has(ModItems.WYRMSTEEL_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HEADHUNTER)
                .pattern("IIW")
                .pattern("IN ")
                .pattern("L  ")
                .define('I', Items.IRON_INGOT)
                .define('N', ModItems.WYRMSTEEL_NUGGET)
                .define('L', Items.LEATHER)
                .define('W', ModItems.WEAPON_CORE)
                .unlockedBy("needs_weapon_core", has(ModItems.WEAPON_CORE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HEADHUNTER_AMMO)
                .pattern("IIW")
                .pattern("IN ")
                .pattern("L  ")
                .define('I', ModItems.WYRMSTEEL_INGOT)
                .define('N', ModItems.WYRMSTEEL_NUGGET)
                .define('L', Items.LEATHER)
                .define('W', ModItems.WEAPON_CORE)
                .unlockedBy("needs_weapon_core", has(ModItems.WEAPON_CORE))
                .unlockedBy("needs_wrymsteel", has(ModItems.WYRMSTEEL_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HEADHUNTER_SPEED)
                .pattern("IIW")
                .pattern("IN ")
                .pattern("L  ")
                .define('I', Items.IRON_INGOT)
                .define('N', ModItems.WYRMSTEEL_NUGGET)
                .define('L', ModItems.WARDEN_HIDE)
                .define('W', ModItems.WEAPON_CORE)
                .unlockedBy("needs_weapon_core", has(ModItems.WEAPON_CORE))
                .unlockedBy("needs_warden_hide", has(ModItems.WARDEN_HIDE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HEADHUNTER_AMMO_SPEED)
                .pattern("IIW")
                .pattern("IN ")
                .pattern("L  ")
                .define('I', ModItems.WYRMSTEEL_INGOT)
                .define('N', ModItems.WYRMSTEEL_NUGGET)
                .define('L', ModItems.WARDEN_HIDE)
                .define('W', ModItems.WEAPON_CORE)
                .unlockedBy("needs_weapon_core", has(ModItems.WEAPON_CORE))
                .unlockedBy("needs_wrymsteel", has(ModItems.WYRMSTEEL_INGOT))
                .unlockedBy("needs_warden_hide", has(ModItems.WARDEN_HIDE))
                .save(output);

        // Injections

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.EMPTY_INJECTION)
                .pattern("  N")
                .pattern("NI ")
                .pattern("CN ")
                .define('N', ModItems.WYRMSTEEL_NUGGET)
                .define('I', ModItems.WYRMSTEEL_INGOT)
                .define('C', ModItems.UTILITY_CORE)
                .unlockedBy("wyrmsteel_nugget", has(ModItems.WYRMSTEEL_NUGGET))
                .unlockedBy("core", has(ModItems.UTILITY_CORE))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_MILK)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.MILK_BUCKET)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_POISON)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.SPIDER_EYE)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_WITHER)
                .requires(ModInjections.INJECTION_POISON)
                .requires(Items.FERMENTED_SPIDER_EYE)
                .unlockedBy("poison_injection", has(ModInjections.INJECTION_POISON))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_WITHER)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.FERMENTED_SPIDER_EYE)
                .requires(Items.WITHER_ROSE)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output, "alternative_wither_injection_path");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_TORPOR)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.GLISTERING_MELON_SLICE)
                .requires(Items.WITHER_ROSE)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_VULNERABLE)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.SHIELD)
                .requires(Items.WITHER_ROSE)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_STRENGTH)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_SPEED)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.SUGAR)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_LEAPING)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.RABBIT_FOOT)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_REGEN)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.GHAST_TEAR)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_VISION)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.GOLDEN_CARROT)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_INVIS)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.GOLDEN_CARROT)
                .requires(Items.FERMENTED_SPIDER_EYE)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output, "main_invis");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_INVIS)
                .requires(ModInjections.INJECTION_VISION)
                .requires(Items.FERMENTED_SPIDER_EYE)
                .unlockedBy("vision_injection", has(ModInjections.INJECTION_VISION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_FIRE_RESIST)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.MAGMA_CREAM)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_SLOWNESS)
                .requires(ModInjections.INJECTION_SPEED)
                .requires(Items.FERMENTED_SPIDER_EYE)
                .unlockedBy("speed_inject", has(ModInjections.INJECTION_SPEED))
                .save(output, "speed_path_slowness");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_SLOWNESS)
                .requires(ModInjections.INJECTION_LEAPING)
                .requires(Items.FERMENTED_SPIDER_EYE)
                .unlockedBy("jump_inject", has(ModInjections.INJECTION_LEAPING))
                .save(output, "jump_path_slowness");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_SLOW_FALL)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.PHANTOM_MEMBRANE)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_WATER_BREATHING)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.PUFFERFISH)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_WEAVING)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Blocks.COBWEB.asItem())
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_WIND_CHARGED)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Items.BREEZE_ROD)
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_OOZING)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Blocks.SLIME_BLOCK.asItem())
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModInjections.INJECTION_INFESTING)
                .requires(ModItems.EMPTY_INJECTION)
                .requires(Blocks.STONE.asItem())
                .unlockedBy("empty_injection", has(ModItems.EMPTY_INJECTION))
                .save(output);

    }
}

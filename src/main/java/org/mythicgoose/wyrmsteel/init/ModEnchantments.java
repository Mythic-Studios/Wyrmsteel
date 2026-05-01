package org.mythicgoose.wyrmsteel.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.mythicgoose.wyrmsteel.Wyrmsteel;

public class ModEnchantments {
    /*
    Scythe - Decaying (Has a chance to give attacked entity Wither)
    Sickles - Slice and Dice (Some hits can deal up to 3x base damage and an extremely low chance for hits to be fatal)
    Blowgun - Puncture (Darts are now replaced with darts that prevent health regen), Junglebound (Darts are now replaced with darts give entities perma poison till death or they drink milk)
    Headhunter (Revolver) - Marksman (Right-Clicking in a direction of an entity marks them, Deals double damage to the marked targets) - Check item
     */

    public static final ResourceKey<Enchantment> DECAYING =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "decaying"));
    public static final ResourceKey<Enchantment> SHARE_LOVE =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "share_love"));

    public static final ResourceKey<Enchantment> SLICE_AND_DICE =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "slice_and_dice"));
    public static final ResourceKey<Enchantment> BLEED =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "bleed"));

    public static final ResourceKey<Enchantment> PUNCTURE =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "puncture"));
    public static final ResourceKey<Enchantment> JUNGLEBOUND =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "junglebound"));
    public static final ResourceKey<Enchantment> BREACHING =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "breaching"));

    public static final ResourceKey<Enchantment> MARKSMAN =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "marksman"));

    public static final ResourceKey<Enchantment> COOLNESS_FACTOR =
            ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "coolness_factor"));

    // Create the tag reference
    public static final TagKey<Item> SCYTHE_ENCHANTABLE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "scythe_enchantable"));
    public static final TagKey<Item> SICKLE_ENCHANTABLE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "sickle_enchantable"));
    public static final TagKey<Item> DART_ENCHANTABLE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "dart_enchantable"));
    public static final TagKey<Item> HEADHUNTER_ENCHANTABLE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "headhunter_enchantable"));
    public static final TagKey<Item> PEARL_ENCHANTABLE =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, "pearl_enchantable"));


    public static void bootstrap(BootstrapContext<Enchantment> registerable) {
        HolderGetter<Item> items = registerable.lookup(Registries.ITEM);

        register(registerable, DECAYING, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(SCYTHE_ENCHANTABLE), // Your custom tag
                        items.getOrThrow(SCYTHE_ENCHANTABLE), // Same tag for primary
                        5,  // Weight
                        1,  // Max level
                        Enchantment.dynamicCost(1, 10),
                        Enchantment.dynamicCost(1, 10),
                        2,  // Anvil cost
                        EquipmentSlotGroup.MAINHAND
                ))
        );
        register(registerable, SHARE_LOVE, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(SCYTHE_ENCHANTABLE), // Your custom tag
                        items.getOrThrow(SCYTHE_ENCHANTABLE), // Same tag for primary
                        5,  // Weight
                        1,  // Max level
                        Enchantment.dynamicCost(1, 10),
                        Enchantment.dynamicCost(1, 10),
                        2,  // Anvil cost
                        EquipmentSlotGroup.MAINHAND
                ))
        );

        register(registerable, SLICE_AND_DICE, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(SICKLE_ENCHANTABLE), // Your custom tag
                        items.getOrThrow(SICKLE_ENCHANTABLE), // Same tag for primary
                        5,  // Weight
                        1,  // Max level
                        Enchantment.dynamicCost(1, 10),
                        Enchantment.dynamicCost(1, 10),
                        2,  // Anvil cost
                        EquipmentSlotGroup.MAINHAND
                ))
        );
        register(registerable, BLEED, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(SICKLE_ENCHANTABLE), // Your custom tag
                        items.getOrThrow(SICKLE_ENCHANTABLE), // Same tag for primary
                        5,  // Weight
                        1,  // Max level
                        Enchantment.dynamicCost(1, 10),
                        Enchantment.dynamicCost(1, 10),
                        2,  // Anvil cost
                        EquipmentSlotGroup.MAINHAND
                ))
        );


        register(registerable, PUNCTURE, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(DART_ENCHANTABLE), // Your custom tag
                        items.getOrThrow(DART_ENCHANTABLE), // Same tag for primary
                        5,  // Weight
                        1,  // Max level
                        Enchantment.dynamicCost(1, 10),
                        Enchantment.dynamicCost(1, 10),
                        2,  // Anvil cost
                        EquipmentSlotGroup.MAINHAND
                ))
        );
        register(registerable, JUNGLEBOUND, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(DART_ENCHANTABLE), // Your custom tag
                        items.getOrThrow(DART_ENCHANTABLE), // Same tag for primary
                        5,  // Weight
                        1,  // Max level
                        Enchantment.dynamicCost(1, 10),
                        Enchantment.dynamicCost(1, 10),
                        2,  // Anvil cost
                        EquipmentSlotGroup.MAINHAND
                ))
        );
        register(registerable, BREACHING, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(DART_ENCHANTABLE), // Your custom tag
                        items.getOrThrow(DART_ENCHANTABLE), // Same tag for primary
                        5,  // Weight
                        1,  // Max level
                        Enchantment.dynamicCost(1, 10),
                        Enchantment.dynamicCost(1, 10),
                        2,  // Anvil cost
                        EquipmentSlotGroup.MAINHAND
                ))
        );
        register(registerable, MARKSMAN, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(HEADHUNTER_ENCHANTABLE), // Your custom tag
                        items.getOrThrow(HEADHUNTER_ENCHANTABLE), // Same tag for primary
                        5,  // Weight
                        1,  // Max level
                        Enchantment.dynamicCost(1, 10),
                        Enchantment.dynamicCost(1, 10),
                        2,  // Anvil cost
                        EquipmentSlotGroup.MAINHAND
                ))
        );
        register(registerable, COOLNESS_FACTOR, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(PEARL_ENCHANTABLE), // Your custom tag
                        items.getOrThrow(PEARL_ENCHANTABLE), // Same tag for primary
                        5,  // Weight
                        1,  // Max level
                        Enchantment.dynamicCost(1, 10),
                        Enchantment.dynamicCost(1, 10),
                        2,  // Anvil cost
                        EquipmentSlotGroup.MAINHAND
                ))
        );
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }
}
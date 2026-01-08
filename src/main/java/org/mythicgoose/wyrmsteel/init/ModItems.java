package org.mythicgoose.wyrmsteel.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.item.*;

import java.util.List;

public class ModItems {
    public static Item WYRMSTEEL_INGOT;
    public static Item WYRMSTEEL_NUGGET;
    public static Item WARDEN_HIDE;

    public static Item WEAPON_CORE;
    public static Item SCYTHE;
    public static Item SICKLE;
    public static Item BLOWGUN;
    public static Item HEADHUNTER;
    public static Item HEADHUNTER_AMMO;
    public static Item HEADHUNTER_SPEED;
    public static Item HEADHUNTER_AMMO_SPEED;

    public static Item UTILITY_CORE;
    public static Item EMPTY_INJECTION;
    public static Item TOTEM_OF_HEALTHINESS;
    public static Item INFINITE_PEARL;


    public static void init() {
        WYRMSTEEL_INGOT = createItem("wyrmsteel_ingot", new Item(new Item.Properties().stacksTo(64)));
        WYRMSTEEL_NUGGET = createItem("wyrmsteel_nugget", new Item(new Item.Properties().stacksTo(64)));
        WARDEN_HIDE = createItem("warden_hide", new Item(new Item.Properties().stacksTo(64)));
        WEAPON_CORE = createItem("weapon_core", new Item(new Item.Properties().stacksTo(16)));
        SCYTHE = createItem("scythe", new ScytheItem(ModTiers.BLADES, new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(ModTiers.BLADES, 13, -2.4f))));
        SICKLE = createItem("sickle", new SickelItem(ModTiers.FAST_WEAPON, new Item.Properties().stacksTo(2).attributes(SwordItem.createAttributes(ModTiers.FAST_WEAPON, 6, 60.0f))));
        BLOWGUN = createItem("blowgun", new BlowgunItem(new Item.Properties().stacksTo(1)));
        HEADHUNTER = createItem("headhunter", new HeadhunterItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 6, 120));
        HEADHUNTER_AMMO = createItem("headhunter_ammo", new HeadhunterItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 12, 120));
        HEADHUNTER_SPEED = createItem("headhunter_speed", new HeadhunterItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 6, 60));
        HEADHUNTER_AMMO_SPEED = createItem("headhunter_ammo_speed", new HeadhunterItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 12, 60));

        UTILITY_CORE = createItem("utility_core", new Item(new Item.Properties().stacksTo(16)));
        EMPTY_INJECTION = createItem("injection", new Item(new Item.Properties().stacksTo(1)) {
            @Override
            public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
                list.add(Component.translatable("tooltip.no_effect"));

                super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
            }
        });
        TOTEM_OF_HEALTHINESS = createItem("totem_of_healthiness", new TotemOfHealthiness(new Item.Properties().stacksTo(1)));
        INFINITE_PEARL = createItem("infinite_pearl", new CustomEnderpearlItem(new Item.Properties().stacksTo(1)));
    }


    private static Item createItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, name), item);
    }
}

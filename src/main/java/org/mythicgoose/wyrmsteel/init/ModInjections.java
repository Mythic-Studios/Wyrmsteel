package org.mythicgoose.wyrmsteel.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import org.mythicgoose.mythic_core.indexing.ItemRegistrar;
import org.mythicgoose.wyrmsteel.Wyrmsteel;
import org.mythicgoose.wyrmsteel.item.InjectionItem;

import java.util.ArrayList;
import java.util.List;

public class ModInjections {
    static {
        ItemRegistrar.setRegistrarID(Wyrmsteel.MOD_ID);
    }
    
    // Make it mutable so we can add items as we create them
    public static final List<Item> ALL_INJECTIONS = new ArrayList<>();

    public static Item INJECTION_MILK = createItem("injection_milk",
            new InjectionItem(new Item.Properties().stacksTo(1),
                    null, 16777215, 16777215, true));

    public static Item INJECTION_POISON = createItem("injection_poison",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.POISON.value(),
                    0x4E9331,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 2
            )
    );
    public static Item INJECTION_WITHER = createItem("injection_wither",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.WITHER.value(),
                    0x544646,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 2
            )
    );
    public static Item INJECTION_STRENGTH = createItem("injection_strength",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.DAMAGE_BOOST.value(),
                    0xbda700,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 2
            )
    );
    public static Item INJECTION_REGEN = createItem("injection_regen",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.REGENERATION.value(),
                    0xdd52a6,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 2
            )
    );
    public static Item INJECTION_LEAPING = createItem("injection_leaping",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.JUMP.value(),
                    0x8cb889,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 2
            )
    );
    public static Item INJECTION_VISION = createItem("injection_vison",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.NIGHT_VISION.value(),
                    0x0eff00,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 0
            )
    );
    public static Item INJECTION_INVIS = createItem("injection_invis",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.INVISIBILITY.value(),
                    0x9f9f9f,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 0
            )
    );
    public static Item INJECTION_SPEED = createItem("injection_speed",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.MOVEMENT_SPEED.value(),
                    0x00ffff,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 2
            )
    );
    public static Item INJECTION_FIRE_RESIST = createItem("injection_fire_resist",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.FIRE_RESISTANCE.value(),
                    0xf97a00,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 2
            )
    );
    public static Item INJECTION_SLOW_FALL = createItem("injection_slow_fall",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.SLOW_FALLING.value(),
                    0xb285a0,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 0
            )
    );
    public static Item INJECTION_WATER_BREATHING = createItem("injection_water_breathing",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.WATER_BREATHING.value(),
                    0x85a6b2,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 0
            )
    );
    public static Item INJECTION_SLOWNESS = createItem("injection_slowness",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.MOVEMENT_SLOWDOWN.value(),
                    0x2a5667,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 2
            )
    );
    public static Item INJECTION_WEAVING = createItem("injection_weaving",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.WEAVING.value(),
                    0x453b26,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 0
            )
    );
    public static Item INJECTION_OOZING = createItem("injection_oozing",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.OOZING.value(),
                    0x47ac7c,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 0
            )
    );
    public static Item INJECTION_WIND_CHARGED = createItem("injection_wind_charged",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.WIND_CHARGED.value(),
                    0x4751ac,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 0
            )
    );
    public static Item INJECTION_INFESTING = createItem("injection_infesting",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    MobEffects.INFESTED.value(),
                    0x4a564d,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 0
            )
    );
    public static Item INJECTION_TORPOR = createItem("injection_torpor",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    ModEffects.TORPOR.value(),
                    0x5806b4,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 0
            )
    );
    public static Item INJECTION_VULNERABLE = createItem("injection_vulnerable",
            new InjectionItem(
                    new Item.Properties().stacksTo(1),
                    ModEffects.VULNERABILITY.value(),
                    0x67574A,   // ItemEffectColor (overlay)
                    0xFFFFFF,   // NoUse (base)
                    100000, 2
            )
    );


    private static Item createItem(String name, Item item) {
        Item registered = Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, name), item);
        ALL_INJECTIONS.add(registered); // Add to list after registering
        return registered;
    }

    public static void init() {
        Wyrmsteel.LOGGER.info("Creating Injections for " + Wyrmsteel.MOD_ID);
    }
}
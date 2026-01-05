package org.mythicgoose.wyrmsteel.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.mythicgoose.wyrmsteel.Wyrmsteel;

public class Tags {
    public static TagKey<Item> BIG_WEAPONS = create("big_weapons");
    public static TagKey<Item> SHIELDS = create("shields");
    public static TagKey<Item> POCKET_WEAPONS = create("pocket_weapons");

    private static TagKey<Item> create(String id) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Wyrmsteel.MOD_ID, id));
    }
}

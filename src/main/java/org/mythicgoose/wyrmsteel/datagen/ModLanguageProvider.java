package org.mythicgoose.wyrmsteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.mythicgoose.wyrmsteel.init.ModEntities;
import org.mythicgoose.wyrmsteel.init.ModInjections;
import org.mythicgoose.wyrmsteel.init.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {

        translationBuilder.add(ModItems.WEAPON_CORE, "Weapon Core");
        translationBuilder.add(ModItems.UTILITY_CORE, "Utility Core");
        translationBuilder.add(ModItems.WYRMSTEEL_INGOT, "Wyrmsteel Ingot");
        translationBuilder.add(ModItems.WYRMSTEEL_NUGGET, "Wyrmsteel Nugget");

        translationBuilder.add(ModItems.SCYTHE, "Scythe");
        translationBuilder.add(ModItems.SICKLE, "Sickle");
        translationBuilder.add(ModItems.BLOWGUN, "Blowgun");

        translationBuilder.add(ModItems.EMPTY_INJECTION, "Injection");
        translationBuilder.add(ModItems.TOTEM_OF_HEALTHINESS, "Totem of Healthiness");
        translationBuilder.add(ModItems.INFINITE_PEARL, "Infinite Enderpearl");

        // Add all injections automatically
        for (var injection : ModInjections.ALL_INJECTIONS) {
            translationBuilder.add(injection, "Injection");
        }

        translationBuilder.add(ModEntities.DART, "Throwing Dart");

        translationBuilder.add("death.attack.blood_loss", "%1$s bled out");
        translationBuilder.add("death.attack.blood_loss.player", "%1$s bled out whilst fighting %2$s");

        translationBuilder.add("death.attack.cuts", "%1$s was shredded to ribbons");
        translationBuilder.add("death.attack.cuts.player", "%1$s was shredded to ribbons whilst fighting %2$s");

        translationBuilder.add("tooltip.no_effect", "§7No Effects");

        translationBuilder.add("effect.wyrmsteel.torpor", "Torpor");
        translationBuilder.add("effect.wyrmsteel.vulnerable", "Vulnerability");
        translationBuilder.add("effect.wyrmsteel.dimensional_desync", "Dimensional Desynchronization");

        translationBuilder.add("itemgroup.wyrmsteel.core", "Wyrmsteel");
        translationBuilder.add("itemgroup.wyrmsteel.injections", "Wyrmsteel: Injections");

        translationBuilder.add("enchantment.wyrmsteel.decaying", "Decaying");
        translationBuilder.add("enchantment.wyrmsteel.decaying.desc", "Has a chance to give target the Wither effect");
        translationBuilder.add("enchantment.wyrmsteel.share_love", "Share the Love");
        translationBuilder.add("enchantment.wyrmsteel.share_love.desc", "When Right-Clicked sends a ray that inflicts the target with the exact same status effects the attacker has");

        translationBuilder.add("enchantment.wyrmsteel.slice_and_dice", "Slice & Dice");
        translationBuilder.add("enchantment.wyrmsteel.slice_and_dice.desc", "Some hits have an extremely low chance to be fatal");
        translationBuilder.add("enchantment.wyrmsteel.bleed", "Wounding");
        translationBuilder.add("enchantment.wyrmsteel.bleed.desc", "Reduces base damage but has a 20% chance to inflict double (original) base damage");

        translationBuilder.add("enchantment.wyrmsteel.puncture", "Puncture");
        translationBuilder.add("enchantment.wyrmsteel.puncture.desc", "Darts are now replaced with darts that prevent health regen");
        translationBuilder.add("enchantment.wyrmsteel.junglebound", "Junglebound");
        translationBuilder.add("enchantment.wyrmsteel.junglebound.desc", "Darts are now replaced with darts give entities perma poison till death or they drink milk");
        translationBuilder.add("enchantment.wyrmsteel.breaching", "Breaching");
        translationBuilder.add("enchantment.wyrmsteel.breaching.desc", "Darts are now replaced with darts give entities Vulnerability till death or they drink milk");

        translationBuilder.add("enchantment.wyrmsteel.coolness_factor", "Coolness Factor");
        translationBuilder.add("enchantment.wyrmsteel.coolness_factor.desc", "When thrown it reduces the time needed to wait to throw the pearl again");
        translationBuilder.add("enchantment.wyrmsteel.synchronised", "Synchronization");
        translationBuilder.add("enchantment.wyrmsteel.synchronised.desc", "When thrown it prevents you from experiencing a Dimension Desynchronization");

        translationBuilder.add("key.categories.wyrmsteel", "Wyrmsteel");
        translationBuilder.add("key.wyrmsteel.swap_slots", "Swap Backslot Item");
        translationBuilder.add("key.wyrmsteel.equip_back_weapon", "Equip Backslot Item");

    }
}
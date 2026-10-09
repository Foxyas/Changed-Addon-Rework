package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.init.ChangedAddonBestiaryEntries;
import net.foxyas.changedaddon.process.bestiary.BestiaryCondition;
import net.foxyas.changedaddon.process.bestiary.BestiaryEntry;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantHolder;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.ChatFormatting;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class BestiaryEntriesProvider {

    protected final String modid;

    public BestiaryEntriesProvider(String modid) {
        this.modid = modid;
    }

    public void bootstrap(BootstapContext<BestiaryEntry> context) {
        context.register(key("test"), new BestiaryEntry(List.of(new TransfurVariantHolder(ChangedTransfurVariants.GAS_WOLF_MALE.get())),
                Component.literal("TEST FILES TITLE").withStyle(ChatFormatting.GOLD),
                Component.literal("TEST FILES DESCRIPTION").withStyle(style -> style.withItalic(true)),
                new BestiaryCondition(Optional.empty()),
                0)
        );
        context.register(key("test2"), new BestiaryEntry(List.of(new TransfurVariantHolder(ChangedTransfurVariants.GAS_WOLF_MALE.get())),
                Component.literal("TEST FILES TITLE2").withStyle(ChatFormatting.GOLD),
                Component.literal("TEST FILES DESCRIPTION2").withStyle(style -> style.withItalic(true)),
                new BestiaryCondition(Optional.empty()),
                0)
        );
    }

    private ResourceKey<BestiaryEntry> key(String name) {
        return ResourceKey.create(ChangedAddonBestiaryEntries.BESTIARY_ENTRIES_KEY, ResourceLocation.fromNamespaceAndPath(this.modid, name));
    }
}
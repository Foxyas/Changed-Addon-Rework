package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class TrimPatterns {
    public static final ResourceKey<TrimPattern> TEST = registryKey("test");

    public static void bootstrap(BootstapContext<TrimPattern> pContext) {
        register(pContext, Items.STICK, TEST);
    }

    public static Optional<Holder.Reference<TrimPattern>> getFromTemplate(RegistryAccess pRegistry, ItemStack pTemplate) {
        return pRegistry.registryOrThrow(Registries.TRIM_PATTERN).holders().filter((p_266833_) -> {
            return pTemplate.is(p_266833_.value().templateItem());
        }).findFirst();
    }

    private static void register(BootstapContext<TrimPattern> pContext, Item pTemplateItem, ResourceKey<TrimPattern> pTrimPatternKey) {
        TrimPattern trimpattern = new TrimPattern(pTrimPatternKey.location(), ForgeRegistries.ITEMS.getHolder(pTemplateItem).orElseThrow(), Component.translatable(Util.makeDescriptionId("trim_pattern", pTrimPatternKey.location())));
        pContext.register(pTrimPatternKey, trimpattern);
    }

    private static ResourceKey<TrimPattern> registryKey(String pKey) {
        return ResourceKey.create(Registries.TRIM_PATTERN, ChangedAddonMod.resourceLoc(pKey));
    }
}
package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class TrimMaterials {

    public static final ResourceKey<TrimMaterial> IRIDIUM = registryKey("iridium");

    public static void bootstrap(BootstapContext<TrimMaterial> pContext) {
        register(pContext, IRIDIUM, ChangedAddonItems.IRIDIUM.get(), Style.EMPTY.withColor(Color.WHITE.getRGB()), 1F);
    }

    public static Optional<Holder.Reference<TrimMaterial>> getFromIngredient(RegistryAccess pRegistryAccess, ItemStack pIngredient) {
        return pRegistryAccess.registryOrThrow(Registries.TRIM_MATERIAL).holders().filter((p_266876_) -> {
            return pIngredient.is(p_266876_.value().ingredient());
        }).findFirst();
    }

    private static void register(BootstapContext<TrimMaterial> pContext, ResourceKey<TrimMaterial> pMaterialKey, Item pIngredient, Style pStyle, float pItemModelIndex) {
        register(pContext, pMaterialKey, pIngredient, pStyle, pItemModelIndex, Map.of());
    }

    private static void register(BootstapContext<TrimMaterial> pContext, ResourceKey<TrimMaterial> pMaterialKey, Item pIngredient, Style pStyle, float pItemModelIndex, Map<ArmorMaterials, String> pOverrideArmorMaterials) {
        TrimMaterial trimmaterial = TrimMaterial.create(pMaterialKey.location().getPath(), pIngredient, pItemModelIndex, Component.translatable(Util.makeDescriptionId("trim_material", pMaterialKey.location())).withStyle(pStyle), pOverrideArmorMaterials);
        pContext.register(pMaterialKey, trimmaterial);
    }

    private static ResourceKey<TrimMaterial> registryKey(String pKey) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, ChangedAddonMod.resourceLoc(pKey));
    }
}
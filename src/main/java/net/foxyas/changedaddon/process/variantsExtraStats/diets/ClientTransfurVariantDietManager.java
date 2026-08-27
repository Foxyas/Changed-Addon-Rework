package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ClientTransfurVariantDietManager {
    private static Map<ResourceLocation, TransfurVariantDiet> CLIENT_DIETS = new HashMap<>();

    public static void setClientDiets(Map<ResourceLocation, TransfurVariantDiet> diets) {
        CLIENT_DIETS.clear();
        CLIENT_DIETS.putAll(diets);
    }

    /**
     * Returns ALL diets associated with a specific transfur variant.
     */
    public static List<TransfurVariantDiet> getDietsForVariant(TransfurVariant<?> variant) {
        if (variant == null) return List.of();

        return CLIENT_DIETS.values().stream()
                .filter(diet -> diet.matchesVariant(variant))
                .toList();
    }
}
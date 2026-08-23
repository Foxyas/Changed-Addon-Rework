package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;

import java.util.ArrayList;
import java.util.List;

public class ClientTransfurVariantDietManager {
    private static List<TransfurVariantDiet> CLIENT_DIETS = new ArrayList<>();

    public static void setClientDiets(List<TransfurVariantDiet> diets) {
        CLIENT_DIETS = diets;
    }

    /**
     * Returns ALL diets associated with a specific transfur variant.
     */
    public static List<TransfurVariantDiet> getDietsForVariant(TransfurVariant<?> variant) {
        if (variant == null) return List.of();

        return CLIENT_DIETS.stream()
                .filter(diet -> diet.matchesVariant(variant))
                .toList();
    }
}
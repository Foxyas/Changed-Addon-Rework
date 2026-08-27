package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.util.ExtraCodecs;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;

import java.util.List;

public record TransfurVariantDiet(List<VariantHolder> variantHolders, List<FoodDietEntry> foods) {

    public static final Codec<TransfurVariantDiet> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.listOrSingle(VariantHolder.CODEC).fieldOf("variants").forGetter(TransfurVariantDiet::variantHolders),
                    ExtraCodecs.listOrSingle(FoodDietEntry.CODEC).fieldOf("foods").forGetter(TransfurVariantDiet::foods)
            ).apply(instance, TransfurVariantDiet::new)
    );

    /**
     * Verifica se uma variante bate com qualquer uma das entradas (ID individual ou Tag) contidas nesta dieta.
     */
    public boolean matchesVariant(TransfurVariant<?> variant) {
        if (variant == null) return false;
        for (VariantHolder holder : variantHolders) {
            if (holder.matches(variant)) {
                return true;
            }
        }
        return false;
    }
}
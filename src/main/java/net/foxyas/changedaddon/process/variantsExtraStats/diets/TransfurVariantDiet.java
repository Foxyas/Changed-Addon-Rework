package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.util.ExtraCodecs;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;

import java.util.ArrayList;
import java.util.List;

public record TransfurVariantDiet(
        List<TransfurVariantHolder> transfurVariantHolders,
        List<FoodDietEntry> foods,
        List<MobEffectHolder> offDietEffects
) {

    public TransfurVariantDiet(List<TransfurVariantHolder> transfurVariantHolders,
                               List<FoodDietEntry> foods) {
        this(transfurVariantHolders, foods, List.of());
    }

    public static final Codec<TransfurVariantDiet> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.listOrSingle(TransfurVariantHolder.CODEC).fieldOf("variants").forGetter(TransfurVariantDiet::transfurVariantHolders),
                    ExtraCodecs.listOrSingle(FoodDietEntry.CODEC).fieldOf("foods").forGetter(TransfurVariantDiet::foods),
                    ExtraCodecs.listOrSingle(MobEffectHolder.CODEC).optionalFieldOf("offDietEffects", List.of()).forGetter(TransfurVariantDiet::offDietEffects)
            ).apply(instance, TransfurVariantDiet::new)
    );

    /**
     * Checks whether a transfur variant matches any entry (individual ID or tag) in this diet setup.
     */
    public boolean matchesVariant(TransfurVariant<?> variant) {
        if (variant == null) return false;
        for (TransfurVariantHolder holder : transfurVariantHolders) {
            if (holder.matches(variant)) {
                return true;
            }
        }
        return false;
    }
}
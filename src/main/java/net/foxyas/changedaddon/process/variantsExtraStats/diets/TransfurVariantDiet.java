package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;

public record TransfurVariantDiet(
        List<VariantHolder> variantHolders,
        List<FoodDietEntry> foods
) {

    public record VariantHolder(Either<TransfurVariant<?>, TagKey<TransfurVariant<?>>> value) {

        public VariantHolder(TransfurVariant<?> variant) {
            this(Either.left(variant));
        }

        public VariantHolder(TagKey<TransfurVariant<?>> variantTag) {
            this(Either.right(variantTag));
        }

        public static final Codec<VariantHolder> CODEC = Codec.STRING.comapFlatMap(
                str -> {
                    if (str.startsWith("#")) {
                        ResourceLocation tagId = ResourceLocation.tryParse(str.substring(1));
                        if (tagId == null) return DataResult.error(() -> "Invalid Tag Location: " + str);
                        TagKey<TransfurVariant<?>> tagKey = TagKey.create(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), tagId);
                        return DataResult.success(new VariantHolder(Either.right(tagKey)));
                    } else {
                        ResourceLocation id = ResourceLocation.tryParse(str);
                        if (id == null) return DataResult.error(() -> "Invalid Variant Location: " + str);
                        TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(id);
                        if (variant == null) return DataResult.error(() -> "Unknown TransfurVariant: " + id);
                        return DataResult.success(new VariantHolder(Either.left(variant)));
                    }
                },
                holder -> holder.value().map(
                        variant -> ChangedRegistry.TRANSFUR_VARIANT.get().getKey(variant).toString(),
                        tagKey -> "#" + tagKey.location()
                )
        );

        public boolean matches(TransfurVariant<?> variant) {
//            return value.map(
//                    singleVariant -> singleVariant == variant,
//                    tagKey -> {
//                        var registry = ChangedRegistry.TRANSFUR_VARIANT.get();
//                        Holder<TransfurVariant<?>> holder = registry.getHolder(registry.getKey(variant)).orElse(null);
//                        return holder != null && holder.is(tagKey);
//                    }
//            );

            if (variant == null) return false;
            return value.map(
                    variant::is,
                    variant::is
            );
        }
    }

    // Aceita tanto uma lista ["variant1", "#tag1"] quanto um valor único "variant1" / "#tag1"
    private static final Codec<List<VariantHolder>> VARIANTS_LIST_CODEC = Codec.either(
            VariantHolder.CODEC.listOf(),
            VariantHolder.CODEC
    ).xmap(
            either -> either.map(list -> list, List::of),
            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
    );

    public static final Codec<TransfurVariantDiet> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    VARIANTS_LIST_CODEC.fieldOf("variants").forGetter(TransfurVariantDiet::variantHolders),
                    FoodDietEntry.CODEC.listOf().fieldOf("foods").forGetter(TransfurVariantDiet::foods)
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
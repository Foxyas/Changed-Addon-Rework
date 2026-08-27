package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

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

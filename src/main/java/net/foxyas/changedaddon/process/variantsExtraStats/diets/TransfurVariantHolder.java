package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.foxyas.changedaddon.init.ChangedAddonCodecs;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public record TransfurVariantHolder(Either<TransfurVariant<?>, TagKey<TransfurVariant<?>>> value) {

    public TransfurVariantHolder(TransfurVariant<?> variant) {
        this(Either.left(variant));
    }

    public TransfurVariantHolder(TagKey<TransfurVariant<?>> variantTag) {
        this(Either.right(variantTag));
    }

    public static TransfurVariantHolder of(TransfurVariant<?> variant) {
        return new TransfurVariantHolder(variant);
    }

    public static TransfurVariantHolder of(TagKey<TransfurVariant<?>> tag) {
        return new TransfurVariantHolder(tag);
    }

    public static final Codec<TransfurVariantHolder> CODEC = ChangedAddonCodecs.TRANSFUR_VARIANT_HOLDER_CODEC;

    public boolean matches(TransfurVariant<?> variant) {
        if (variant == null) return false;
        return value.map(
                variant::is,
                variant::is
        );
    }
}

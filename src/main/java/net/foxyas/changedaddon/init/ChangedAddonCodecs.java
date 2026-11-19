package net.foxyas.changedaddon.init;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantHolder;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangedAddonCodecs {

    public static final Codec<TransfurVariantHolder> TRANSFUR_VARIANT_HOLDER_CODEC = Codec.STRING.comapFlatMap(
            str -> {
                if (str.startsWith("#")) {
                    ResourceLocation tagId = ResourceLocation.tryParse(str.substring(1));
                    if (tagId == null) return DataResult.error(() -> "Invalid Tag Location: " + str);
                    TagKey<TransfurVariant<?>> tagKey = TagKey.create(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), tagId);
                    return DataResult.success(new TransfurVariantHolder(Either.right(tagKey)));
                } else {
                    ResourceLocation id = ResourceLocation.tryParse(str);
                    if (id == null) return DataResult.error(() -> "Invalid Variant Location: " + str);
                    TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(id);
                    if (variant == null) return DataResult.error(() -> "Unknown TransfurVariant: " + id);
                    return DataResult.success(new TransfurVariantHolder(Either.left(variant)));
                }
            },
            holder -> holder.value().map(
                    variant -> ChangedRegistry.TRANSFUR_VARIANT.get().getKey(variant).toString(),
                    tagKey -> "#" + tagKey.location()
            )
    );

}

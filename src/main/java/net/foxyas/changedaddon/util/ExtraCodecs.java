package net.foxyas.changedaddon.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

public class ExtraCodecs {

    // Custom Codec to wrap Ingredient.fromJson / toJson
    public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.PASSTHROUGH.flatXmap(
            dynamic -> {
                try {
                    // Convert the dynamic DFU element back to a raw GSON JsonElement
                    JsonElement json = dynamic.convert(JsonOps.INSTANCE).getValue();
                    return DataResult.success(Ingredient.fromJson(json));
                } catch (Exception e) {
                    return DataResult.error(() -> "Failed to parse Ingredient: " + e.getMessage());
                }
            },
            ingredient -> {
                try {
                    // Serialize back to JsonElement, then wrap in a Dynamic
                    JsonElement json = ingredient.toJson();
                    return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, json));
                } catch (Exception e) {
                    return DataResult.error(() -> "Failed to serialize Ingredient: " + e.getMessage());
                }
            }
    );

    // Custom MobEffectInstance Codec
    public static final Codec<MobEffectInstance> MOB_EFFECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.comapFlatMap(
                            id -> {
                                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(id);
                                return effect != null
                                        ? DataResult.success(effect)
                                        : DataResult.error(() -> "Unknown MobEffect: " + id);
                            },
                            ForgeRegistries.MOB_EFFECTS::getKey
                    ).fieldOf("id").forGetter(MobEffectInstance::getEffect),
                    Codec.INT.optionalFieldOf("duration", 160).forGetter(MobEffectInstance::getDuration),
                    Codec.INT.optionalFieldOf("amplifier", 0).forGetter(MobEffectInstance::getAmplifier),
                    Codec.BOOL.optionalFieldOf("ambient", false).forGetter(MobEffectInstance::isAmbient),
                    Codec.BOOL.optionalFieldOf("visible", true).forGetter(MobEffectInstance::isVisible)
            ).apply(instance, MobEffectInstance::new)
    );
}

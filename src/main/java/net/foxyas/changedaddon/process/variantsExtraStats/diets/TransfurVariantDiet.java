package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.init.ChangedAddonTransfurDiets;
import net.foxyas.changedaddon.util.ExtraCodecs;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

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

    public ResourceLocation getId(Level level) {
        return ChangedAddonTransfurDiets.registry(level).getKey(this);
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<TransfurVariantHolder> holders = new ArrayList<>();
        private final List<FoodDietEntry> foods = new ArrayList<>();
        private final List<MobEffectHolder> offDietEffects = new ArrayList<>();

        public Builder variant(TransfurVariant<?> variant) {
            this.holders.add(new TransfurVariantHolder(variant));
            return this;
        }

        public Builder variant(TagKey<TransfurVariant<?>> tag) {
            this.holders.add(new TransfurVariantHolder(tag));
            return this;
        }

        public Builder tag(TagKey<TransfurVariant<?>> tag) {
            return variant(tag);
        }

        public Builder holder(TransfurVariantHolder holder) {
            this.holders.add(holder);
            return this;
        }

        public Builder holders(TransfurVariantHolder... holders) {
            this.holders.addAll(List.of(holders));
            return this;
        }

        public Builder food(FoodDietEntry food) {
            this.foods.add(food);
            return this;
        }

        public Builder foods(FoodDietEntry... foods) {
            this.foods.addAll(List.of(foods));
            return this;
        }

        public Builder foods(List<FoodDietEntry> foods) {
            this.foods.addAll(foods);
            return this;
        }

        public Builder offDietEffect(MobEffectHolder effect) {
            this.offDietEffects.add(effect);
            return this;
        }

        public Builder offDietEffects(MobEffectHolder... effects) {
            this.offDietEffects.addAll(List.of(effects));
            return this;
        }

        public Builder defaultOffDietEffects() {
            this.offDietEffects.addAll(List.of(
                    MobEffectHolder.builder(new MobEffectInstance(MobEffects.HUNGER, 120, 2, true, false)).chance(0.99f).build(),
                    MobEffectHolder.builder(new MobEffectInstance(MobEffects.CONFUSION, 120, 0, true, false)).chance(0.85f).build(),
                    MobEffectHolder.builder(new MobEffectInstance(MobEffects.POISON, 120, 0, true, false)).chance(0.45f).build()
            ));
            return this;
        }

        public TransfurVariantDiet build() {
            return new TransfurVariantDiet(holders, foods, offDietEffects);
        }
    }
}
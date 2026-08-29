package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

public record MobEffectHolder(
        MobEffectInstance mobEffectInstance,
        FloatProvider chanceProvider,
        EffectOperation operation
) {
    public static final Codec<MobEffectHolder> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.MOB_EFFECT_CODEC.fieldOf("effect").forGetter(MobEffectHolder::mobEffectInstance),
                    FloatProvider.CODEC.optionalFieldOf("chance", ConstantFloat.of(1.0f)).forGetter(MobEffectHolder::chanceProvider),
                    EffectOperation.CODEC.optionalFieldOf("operation", EffectOperation.ANY).forGetter(MobEffectHolder::operation)
            ).apply(instance, MobEffectHolder::new)
    );

    public void addEffect(LivingEntity livingEntity, FoodDietEntry foodDietEntry) {
        livingEntity.addEffect(mobEffectInstance);
    }

    public boolean shouldApplyEffect(LivingEntity livingEntity) {
        if (livingEntity == null) return false;

        // Standard chance check (0.0 to 1.0)
        RandomSource randomSource = livingEntity.getRandom();
        float chance = chanceProvider.sample(randomSource);
        return randomSource.nextFloat() <= chance;
    }

    public boolean shouldApplyEffect(LivingEntity livingEntity, @Nullable FoodDietEntry foodDietEntry) {
        if (livingEntity == null) return false;
        if (foodDietEntry == null) return shouldApplyEffect(livingEntity);

        // Check if the operation condition matches the current diet entry state
        if (!operation.test(livingEntity, foodDietEntry)) {
            return false;
        }

        // Standard chance check (0.0 to 1.0)
        RandomSource randomSource = livingEntity.getRandom();
        float chance = chanceProvider.sample(randomSource);
        return randomSource.nextFloat() <= chance;
    }

    public enum EffectOperation implements StringRepresentable, BiPredicate<LivingEntity, FoodDietEntry> {
        WHEN_SICK_DIET((entity, diet) -> diet.isSickFor(entity)),
        WHEN_GOOD_DIET((entity, diet) -> !diet.isSickFor(entity)),
        ANY((entity, diet) -> true),
        NONE((entity, diet) -> false);

        public static final Codec<EffectOperation> CODEC = StringRepresentable.fromEnum(EffectOperation::values);

        private final BiPredicate<LivingEntity, FoodDietEntry> predicate;

        EffectOperation(BiPredicate<LivingEntity, FoodDietEntry> predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean test(LivingEntity livingEntity, FoodDietEntry foodDietEntry) {
            return predicate.test(livingEntity, foodDietEntry);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
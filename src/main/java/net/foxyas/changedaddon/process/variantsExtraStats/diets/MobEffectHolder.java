package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.util.ExtraCodecs;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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

    /**
     * Aplica o efeito no alvo disparando o evento TransfurVariantDietEvent.ApplyMobEffect.
     */
    public boolean addMobEffects(LivingEntity livingEntity, TransfurVariantInstance<?> variantInstance, TransfurVariantDiet diet, boolean foundAnyGoodFoodMatch, ItemStack itemStack, @Nullable FoodDietEntry foodDietEntry, boolean isOffDiet) {
        if (livingEntity == null) return false;

        // Cria a instância base do efeito
        MobEffectInstance baseInstance = new MobEffectInstance(this.mobEffectInstance);

        // Dispara o sub-evento de MobEffect
        TransfurVariantDietEvent.ApplyMobEffect effectEvent = new TransfurVariantDietEvent.ApplyMobEffect(
                variantInstance, diet, livingEntity, foundAnyGoodFoodMatch, itemStack, this, foodDietEntry, baseInstance, isOffDiet
        );

        // Se o evento for cancelado, o efeito não é aplicado
        if (ChangedAddonMod.postEvent(effectEvent)) {
            return false;
        }

        // Aplica o efeito modificado pelo evento (caso tenha sido alterado via setMobEffectInstance)
        return livingEntity.addEffect(effectEvent.getMobEffectInstance());
    }

    // Sobrecarga de compatibilidade caso queira chamar sem o contexto completo
    public void addMobEffects(LivingEntity livingEntity, FoodDietEntry foodDietEntry, boolean isOffDiet) {
        livingEntity.addEffect(mobEffectInstance);
    }

    public boolean shouldApplyEffect(LivingEntity livingEntity, boolean isOffDiet) {
        if (livingEntity == null) return false;

        // Standard chance check (0.0 to 1.0)
        RandomSource randomSource = livingEntity.getRandom();
        float chance = chanceProvider.sample(randomSource);
        return randomSource.nextFloat() <= chance;
    }

    public boolean shouldApplyEffect(LivingEntity livingEntity, @Nullable FoodDietEntry foodDietEntry, boolean isOffDiet) {
        if (livingEntity == null) return false;
        if (foodDietEntry == null) return shouldApplyEffect(livingEntity, isOffDiet); // Default behavior

        // Check if the operation condition matches the current diet entry state + normal apply effect logic
        return shouldApplyEffect(livingEntity, isOffDiet) && operation.test(livingEntity, foodDietEntry);
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

    public static Builder builder(MobEffectInstance effectInstance) {
        return new Builder(effectInstance);
    }

    public static class Builder {
        private final MobEffectInstance effectInstance;
        private FloatProvider chance = ConstantFloat.of(1.0f);
        private EffectOperation operation = EffectOperation.ANY;

        public Builder(MobEffectInstance effectInstance) {
            this.effectInstance = effectInstance;
        }

        public Builder chance(float chance) {
            this.chance = ConstantFloat.of(chance);
            return this;
        }

        public Builder chance(FloatProvider chance) {
            this.chance = chance;
            return this;
        }

        public Builder operation(EffectOperation operation) {
            this.operation = operation;
            return this;
        }

        public Builder whenGood() {
            this.operation = EffectOperation.WHEN_GOOD_DIET;
            return this;
        }

        public Builder whenSick() {
            this.operation = EffectOperation.WHEN_SICK_DIET;
            return this;
        }

        public MobEffectHolder build() {
            return new MobEffectHolder(effectInstance, chance, operation);
        }
    }
}
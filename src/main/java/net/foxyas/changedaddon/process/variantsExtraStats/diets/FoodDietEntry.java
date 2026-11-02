package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.util.ExtraCodecs;
import net.foxyas.changedaddon.util.MathOperation;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record FoodDietEntry(
        List<Ingredient> ingredients,
        FloatProvider hungerBonus,
        FloatProvider saturationBonus,
        List<MobEffectHolder> mobEffect,
        boolean isSickType,
        MathOperation mathOperation
) {
    public static final Codec<FoodDietEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.INGREDIENT_CODEC.listOf().fieldOf("ingredient").forGetter(FoodDietEntry::ingredients),
                    FloatProvider.CODEC.fieldOf("hungerBonus").forGetter(FoodDietEntry::hungerBonus),
                    FloatProvider.CODEC.fieldOf("saturationBonus").forGetter(FoodDietEntry::saturationBonus),
                    MobEffectHolder.CODEC.listOf().fieldOf("mobEffect").forGetter(FoodDietEntry::mobEffect),
                    Codec.BOOL.optionalFieldOf("isSickType", false).forGetter(FoodDietEntry::isSickType),
                    MathOperation.CODEC.optionalFieldOf("mathOperation", MathOperation.ADD).forGetter(FoodDietEntry::mathOperation)
            ).apply(instance, FoodDietEntry::new)
    );

    // Overloaded constructor for ease of use in code without specifying mathOperation explicitly
    public FoodDietEntry(Ingredient ingredient,
                         FloatProvider hungerBonus,
                         FloatProvider saturationBonus,
                         List<MobEffectHolder> mobEffect,
                         boolean isSickType) {
        this(List.of(ingredient), hungerBonus, saturationBonus, mobEffect, isSickType, MathOperation.ADD);
    }

    public FoodDietEntry(List<Ingredient> ingredients,
                         FloatProvider hungerBonus,
                         FloatProvider saturationBonus,
                         List<MobEffectHolder> mobEffect,
                         boolean isSickType) {
        this(ingredients, hungerBonus, saturationBonus, mobEffect, isSickType, MathOperation.ADD);
    }

    /**
     * Applies hunger/saturation bonuses and any associated mob effects when an entity consumes this food item.
     */
    public boolean applyEffectsAfterEat(LivingEntity livingEntity, TransfurVariantInstance<?> variantInstance, TransfurVariantDiet diet, ItemStack itemStack, boolean foundAnyGoodFoodMatch) {
        RandomSource random = livingEntity.getRandom();

        // Calculate bonuses (inverts positive values into negative ones if isSickType is true)
        int calculatedHunger = Math.round(getCalculatedHunger(random));
        float calculatedSaturation = getCalculatedSaturation(random);

        // Apply food data to players
        if (livingEntity instanceof Player player) {

            FoodProperties itemFoodProperties = itemStack.getFoodProperties(livingEntity);
            int itemFood = itemFoodProperties != null ? itemFoodProperties.getNutrition() : 0;
            float itemSaturation = itemFoodProperties != null ? itemFoodProperties.getSaturationModifier() : 0;

            int finalCalculatedHunger = (int) mathOperation.apply(itemFood, calculatedHunger);
            float finalCalculatedSaturation = mathOperation.apply(itemSaturation, calculatedSaturation);

            // Dispara o evento de comida
            TransfurVariantDietEvent.ApplyFood foodEvent = new TransfurVariantDietEvent.ApplyFood(
                    variantInstance, diet, player, foundAnyGoodFoodMatch, itemStack, this, finalCalculatedHunger, finalCalculatedSaturation
            );

            if (ChangedAddonMod.postEvent(foodEvent)) {
                return false; // Cancelado
            }

            player.getFoodData().eat(foodEvent.getHunger(), foodEvent.getSaturation());
        }

        boolean sickFor = this.isSickFor(livingEntity);

        // Apply optional mob effect if present
        for (MobEffectHolder mobEffectHolder : mobEffect()) {
            boolean isOffDietEffect = sickFor; // Como veio de uma entry de comida válida, não é off-diet genérico
            if (mobEffectHolder.shouldApplyEffect(livingEntity, this, isOffDietEffect)) {

                MobEffectInstance baseInstance = mobEffectHolder.mobEffectInstance();

                // Dispara o evento de mob effect para esta comida
                TransfurVariantDietEvent.ApplyMobEffect effectEvent = new TransfurVariantDietEvent.ApplyMobEffect(
                        variantInstance, diet, livingEntity, foundAnyGoodFoodMatch, itemStack, mobEffectHolder, this, baseInstance, isOffDietEffect
                );

                if (!ChangedAddonMod.postEvent(effectEvent)) {
                    livingEntity.addEffect(effectEvent.getMobEffectInstance());
                }
            }
        }

        // Play sound depending on whether the food is sick or beneficial
        float pitch = sickFor ? 0.5f : 1.5f;
        livingEntity.playSound(SoundEvents.GENERIC_EAT, 1.0f, pitch);
        return true;
    }

    /**
     * Determines whether diet effects (hunger/saturation adjustments, sickness debuffs, or mob effects)
     * should be applied to a player upon consuming a specific food item.
     *
     * @param player The player consuming the food item.
     * @param item   The item stack being eaten.
     * @return {@code true} if the diet effects should be applied; {@code false} otherwise.
     */
    public boolean shouldApplyEffects(Player player, ItemStack item) {
        if (player == null || item.isEmpty()) {
            return false;
        }

        TransfurVariantInstance<?> latexInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (latexInstance == null) {
            return false;
        }

        // Do not process non-food items tagged as exempt
        if (item.is(ChangedAddonTags.Items.UNIVERSAL_FOODS)) {
            return false;
        }

        // Check if the entry causes sickness
        if (this.isSickFor(player)) {
            // If debuffs are disabled in configuration, do not apply sickness effects
            if (!ChangedAddonServerConfiguration.DEBUFFS.get()) {
                return false;
            }

            // If the player has surpassed the adaptation age threshold, they are immune to diet sickness
            if (latexInstance.ageAsVariant >= ChangedAddonServerConfiguration.AGE_NEED.get()) {
                return false;
            }
        }

        return true;
    }

    public boolean isSickFor(LivingEntity player) {
        return this.isSickType();
    }

    // Dynamic calculation helpers applying the sickness rule
    public float getCalculatedHunger(RandomSource random) {
        float val = hungerBonus.sample(random);
        return isSickType ? -Math.abs(val) : val;
    }

    public float getCalculatedSaturation(RandomSource random) {
        float val = saturationBonus.sample(random);
        return isSickType ? -Math.abs(val) : val;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Ingredient> ingredients = new ArrayList<>();
        private FloatProvider hungerBonus = ConstantFloat.of(0);
        private FloatProvider saturationBonus = ConstantFloat.of(0);
        private final List<MobEffectHolder> mobEffects = new ArrayList<>();
        private boolean isSickType = false;
        private MathOperation mathOperation = MathOperation.ADD;

        public Builder ingredient(Ingredient ingredient) {
            this.ingredients.add(ingredient);
            return this;
        }

        public Builder ingredients(Ingredient... ingredients) {
            this.ingredients.addAll(List.of(ingredients));
            return this;
        }

        public Builder ingredients(List<Ingredient> ingredients) {
            this.ingredients.addAll(ingredients);
            return this;
        }

        public Builder item(net.minecraft.world.item.Item item) {
            this.ingredients.add(Ingredient.of(item));
            return this;
        }

        public Builder items(net.minecraft.world.item.Item... items) {
            for (net.minecraft.world.item.Item item : items) {
                this.ingredients.add(Ingredient.of(item));
            }
            return this;
        }

        public Builder tag(net.minecraft.tags.TagKey<net.minecraft.world.item.Item> tag) {
            this.ingredients.add(Ingredient.of(tag));
            return this;
        }

        public Builder hunger(float hunger) {
            this.hungerBonus = ConstantFloat.of(hunger);
            return this;
        }

        public Builder hunger(float min, float max) {
            this.hungerBonus = min == max ? ConstantFloat.of(min) : UniformFloat.of(min, max);
            return this;
        }

        public Builder hunger(FloatProvider hungerBonus) {
            this.hungerBonus = hungerBonus;
            return this;
        }

        public Builder saturation(float saturation) {
            this.saturationBonus = ConstantFloat.of(saturation);
            return this;
        }

        public Builder saturation(FloatProvider saturationBonus) {
            this.saturationBonus = saturationBonus;
            return this;
        }

        public Builder effect(MobEffectHolder effect) {
            this.mobEffects.add(effect);
            return this;
        }

        public Builder effects(MobEffectHolder... effects) {
            this.mobEffects.addAll(List.of(effects));
            return this;
        }

        public Builder effects(List<MobEffectHolder> effects) {
            this.mobEffects.addAll(effects);
            return this;
        }

        public Builder sick() {
            this.isSickType = true;
            return this;
        }

        public Builder sick(boolean isSick) {
            this.isSickType = isSick;
            return this;
        }

        public Builder mathOperation(MathOperation operation) {
            this.mathOperation = operation;
            return this;
        }

        public FoodDietEntry build() {
            return new FoodDietEntry(ingredients, hungerBonus, saturationBonus, mobEffects, isSickType, mathOperation);
        }
    }
}
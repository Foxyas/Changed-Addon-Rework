package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.util.ExtraCodecs;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record FoodDietEntry(
        Ingredient ingredient,
        FloatProvider hungerBonus,
        FloatProvider saturationBonus,
        List<MobEffectInstance> mobEffect,
        boolean isSickType
) {
    public static final Codec<FoodDietEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.INGREDIENT_CODEC.fieldOf("ingredient").forGetter(FoodDietEntry::ingredient),
                    FloatProvider.CODEC.fieldOf("hungerBonus").forGetter(FoodDietEntry::hungerBonus),
                    FloatProvider.CODEC.fieldOf("saturationBonus").forGetter(FoodDietEntry::saturationBonus),
                    ExtraCodecs.MOB_EFFECT_CODEC.listOf().fieldOf("mobEffect").forGetter(FoodDietEntry::mobEffect),
                    Codec.BOOL.optionalFieldOf("isSickType", false).forGetter(FoodDietEntry::isSickType)
            ).apply(instance, FoodDietEntry::new)
    );

    /**
     * Applies hunger/saturation bonuses and any associated mob effects when an entity consumes this food item.
     *
     * @param livingEntity The target entity eating the food.
     * @param itemStack    The item stack consumed.
     */
    public boolean applyEffectsAfterEat(LivingEntity livingEntity, ItemStack itemStack) {
        RandomSource random = livingEntity.getRandom();

        // Calculate bonuses (inverts positive values into negative ones if isSickType is true)
        int calculatedHunger = Math.round(getCalculatedHunger(random));
        float calculatedSaturation = getCalculatedSaturation(random);

        // Apply food data to players
        if (livingEntity instanceof Player player) {
            player.getFoodData().eat(calculatedHunger, calculatedSaturation);
        }

        // Apply optional mob effect if present
        mobEffect.forEach(effect -> livingEntity.addEffect(new MobEffectInstance(effect)));

        // Play sound depending on whether the food is sick or beneficial
        float pitch = this.isSickFor(livingEntity) ? 0.5f : 1.5f;
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
        if (item.is(ChangedAddonTags.Items.NOT_FOOD)) {
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

}
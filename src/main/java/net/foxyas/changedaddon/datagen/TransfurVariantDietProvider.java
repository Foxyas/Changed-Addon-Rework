package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedAddonTransfurDiets;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.FoodDietEntry;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.MobEffectHolder;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantHolder;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransfurVariantDietProvider {

    protected final String modid;

    public TransfurVariantDietProvider(String modid) {
        this.modid = modid;
    }

    public void bootstrap(BootstapContext<TransfurVariantDiet> context) {
        addDietWithHoldersWithGenericOffDietEffect(context, "canine_diet",
                List.of(
                        new TransfurVariantHolder(ChangedAddonTags.TransfurVariants.HAS_CANINE_DIET)
                ),
                List.of(
                        food(Ingredient.of(ChangedAddonTags.Items.WOLF_DIET),
                                2.0f,
                                4.0f,
                                0.5f,
                                effect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)))
                )
        );

        addDietWithHoldersWithGenericOffDietEffect(context, "aquatic_diet",
                List.of(
                        new TransfurVariantHolder(ChangedAddonTags.TransfurVariants.HAS_AQUATIC_DIET)
                ),
                List.of(
                        food(Ingredient.of(ChangedAddonTags.Items.AQUATIC_DIET),
                                2.0f,
                                4.0f,
                                0.5f,
                                effect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)))
                )
        );

        addDietWithHoldersWithGenericOffDietEffect(context, "shark_diet",
                List.of(
                        new TransfurVariantHolder(ChangedAddonTags.TransfurVariants.HAS_SHARK_DIET)
                ),
                List.of(
                        food(Ingredient.of(ChangedAddonTags.Items.SHARK_DIET),
                                2.0f,
                                4.0f,
                                0.5f,
                                effect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)))
                )
        );

        addDietWithHoldersWithGenericOffDietEffect(context, "feline_diet",
                List.of(
                        new TransfurVariantHolder(ChangedAddonTags.TransfurVariants.HAS_FELINE_DIET)
                ),
                List.of(
                        food(Ingredient.of(ChangedAddonTags.Items.CAT_DIET),
                                2.0f,
                                4.0f,
                                0.5f,
                                effect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)))
                )
        );

        addDietWithHoldersWithGenericOffDietEffect(context, "dragon_diet",
                List.of(
                        new TransfurVariantHolder(ChangedAddonTags.TransfurVariants.HAS_DRACONIC_DIET)
                ),
                List.of(
                        food(Ingredient.of(ChangedAddonTags.Items.DRAGON_DIET),
                                2.0f,
                                4.0f,
                                0.5f,
                                effect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)))
                )
        );

        addDietWithHoldersWithGenericOffDietEffect(context, "fox_diet",
                List.of(
                        new TransfurVariantHolder(ChangedAddonTags.TransfurVariants.HAS_FOX_DIET)
                ),
                List.of(
                        food(Ingredient.of(ChangedAddonTags.Items.FOX_DIET),
                                2.0f,
                                4.0f,
                                0.5f,
                                effect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)))
                )
        );

        addDietWithHoldersWithGenericOffDietEffect(context, "special_diet",
                List.of(
                        new TransfurVariantHolder(ChangedAddonTags.TransfurVariants.HAS_SPECIAL_DIET)
                ),
                List.of(
                        food(Ingredient.of(ChangedAddonTags.Items.SPECIAL_DIET),
                                2.0f,
                                4.0f,
                                0.5f,
                                effect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)))
                )
        );

        addDietWithHoldersWithGenericOffDietEffect(context, "sweet_tooth",
                List.of(
                        new TransfurVariantHolder(ChangedAddonTags.TransfurVariants.HAS_SWEET_DIET)
                ),
                List.of(
                        food(Ingredient.of(ChangedAddonTags.Items.SWEET_DIET),
                                2.0f,
                                4.0f,
                                0.5f,
                                List.of(
                                        effect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)),
                                        effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0))
                                )
                        )
                )
        );
    }

    // --- Helper Methods ---

    protected void addDietWithHolders(BootstapContext<TransfurVariantDiet> context, String name, List<TransfurVariantHolder> holders, List<FoodDietEntry> foods) {
        context.register(key(name), new TransfurVariantDiet(holders, foods));
    }

    protected void addDietWithHoldersWithGenericOffDietEffect(BootstapContext<TransfurVariantDiet> context, String name, List<TransfurVariantHolder> holders, List<FoodDietEntry> foods, MobEffectHolder... extraOffDietEffects) {
        List<MobEffectHolder> offDietEffects = new ArrayList<>(List.of(
                new MobEffectHolder(new MobEffectInstance(MobEffects.CONFUSION, 40, 0, true, false), ConstantFloat.of(0.85f), MobEffectHolder.EffectOperation.ANY),
                new MobEffectHolder(new MobEffectInstance(MobEffects.HUNGER, 40, 2, true, false), ConstantFloat.of(0.75f), MobEffectHolder.EffectOperation.ANY),
                new MobEffectHolder(new MobEffectInstance(MobEffects.POISON, 40, 0, true, false), ConstantFloat.of(0.25f), MobEffectHolder.EffectOperation.ANY)
        ));

        offDietEffects.addAll(Arrays.stream(extraOffDietEffects).toList());

        context.register(key(name), new TransfurVariantDiet(holders,
                        foods,
                        offDietEffects
                )
        );
    }

    protected void addDietWithHolders(BootstapContext<TransfurVariantDiet> context, String name, List<TransfurVariantHolder> holders, List<FoodDietEntry> foods, List<MobEffectHolder> offDietEffects) {
        context.register(key(name), new TransfurVariantDiet(holders, foods, offDietEffects));
    }

    private @NotNull ResourceKey<TransfurVariantDiet> key(String name) {
        return ResourceKey.create(ChangedAddonTransfurDiets.TRANSFUR_VARIANT_DIET_KEY, ResourceLocation.fromNamespaceAndPath(this.modid, name));
    }

    protected void addDiet(BootstapContext<TransfurVariantDiet> context, String name, List<TransfurVariant<?>> variants, List<FoodDietEntry> foods) {
        List<TransfurVariantHolder> holders = variants.stream()
                .map(TransfurVariantHolder::new)
                .toList();
        addDietWithHolders(context, name, holders, foods);
    }

    protected void addDiet(BootstapContext<TransfurVariantDiet> context, String name, List<TransfurVariant<?>> variants, List<FoodDietEntry> foods, List<MobEffectHolder> offDietEffects) {
        List<TransfurVariantHolder> holders = variants.stream()
                .map(TransfurVariantHolder::new)
                .toList();
        addDietWithHolders(context, name, holders, foods, offDietEffects);
    }

    // Support List<Ingredient> directly
    protected FoodDietEntry food(List<Ingredient> ingredients, FloatProvider hunger, FloatProvider saturation, List<MobEffectHolder> effects, boolean isSick) {
        return new FoodDietEntry(ingredients, hunger, saturation, effects, isSick);
    }

    protected FoodDietEntry food(List<Ingredient> ingredients, FloatProvider hunger, FloatProvider saturation, MobEffectHolder effect, boolean isSick) {
        return food(ingredients, hunger, saturation, List.of(effect), isSick);
    }

    // Helper for Item varargs -> Converts items into a List of Ingredients
    protected FoodDietEntry food(FloatProvider hunger, FloatProvider saturation, MobEffectHolder effect, boolean isSick, Item... items) {
        List<Ingredient> ingredients = java.util.Arrays.stream(items)
                .map(Ingredient::of)
                .toList();
        return food(ingredients, hunger, saturation, effect, isSick);
    }

    // Single/Multiple Item Food Helpers
    protected FoodDietEntry food(Ingredient ingredients, float minHunger, float maxHunger, float saturation, MobEffectHolder effect) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        return food(List.of(ingredients), hunger, ConstantFloat.of(saturation), List.of(effect), false);
    }

    protected FoodDietEntry food(Ingredient ingredients, float minHunger, float maxHunger, float saturation, List<MobEffectHolder> effects) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        return food(List.of(ingredients), hunger, ConstantFloat.of(saturation), effects, false);
    }

    protected FoodDietEntry food(Item item, float minHunger, float maxHunger, float saturation, MobEffectHolder effect) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        return food(List.of(Ingredient.of(item)), hunger, ConstantFloat.of(saturation), List.of(effect), false);
    }

    protected FoodDietEntry food(Item item, float minHunger, float maxHunger, float saturation, List<MobEffectHolder> effects) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        return food(List.of(Ingredient.of(item)), hunger, ConstantFloat.of(saturation), effects, false);
    }

    protected FoodDietEntry food(Item item, float hunger, float saturation, MobEffectHolder mobEffectInstance) {
        return food(item, hunger, hunger, saturation, List.of(mobEffectInstance));
    }

    protected FoodDietEntry food(Item item, float hunger, float saturation) {
        return food(item, hunger, hunger, saturation, List.of());
    }

    protected FoodDietEntry food(List<Item> items, float minHunger, float maxHunger, float saturation, MobEffectHolder effect) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        List<Ingredient> ingredients = items.stream().map(Ingredient::of).toList();
        return food(ingredients, hunger, ConstantFloat.of(saturation), effect, false);
    }

    // Sick Food Helpers
    protected FoodDietEntry sickFood(Item item, float minHunger, float maxHunger, float saturation) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        return food(List.of(Ingredient.of(item)), hunger, ConstantFloat.of(saturation), List.of(), true);
    }

    protected FoodDietEntry sickFood(Item item, float hunger, float saturation) {
        return sickFood(item, hunger, hunger, saturation);
    }

    protected FoodDietEntry sickFood(List<Item> items, float minHunger, float maxHunger, float saturation) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        List<Ingredient> ingredients = items.stream().map(Ingredient::of).toList();
        return food(ingredients, hunger, ConstantFloat.of(saturation), List.of(), true);
    }

    // ==========================================
    // --- MOB EFFECT HOLDER HELPERS ---
    // ==========================================

    /**
     * Creates a MobEffectHolder with 100% chance and ANY operation.
     */
    protected MobEffectHolder effect(MobEffectInstance effect) {
        return new MobEffectHolder(effect, ConstantFloat.of(1.0f), MobEffectHolder.EffectOperation.ANY);
    }

    /**
     * Creates a MobEffectHolder with a specified chance (0.0 to 1.0) and ANY operation.
     */
    protected MobEffectHolder effect(MobEffectInstance effect, float chance) {
        return new MobEffectHolder(effect, ConstantFloat.of(chance), MobEffectHolder.EffectOperation.ANY);
    }

    /**
     * Creates a MobEffectHolder with a chance provider and ANY operation.
     */
    protected MobEffectHolder effect(MobEffectInstance effect, FloatProvider chanceProvider) {
        return new MobEffectHolder(effect, chanceProvider, MobEffectHolder.EffectOperation.ANY);
    }

    /**
     * Creates a MobEffectHolder targeting GOOD diets (100% chance).
     */
    protected MobEffectHolder goodEffect(MobEffectInstance effect) {
        return new MobEffectHolder(effect, ConstantFloat.of(1.0f), MobEffectHolder.EffectOperation.WHEN_GOOD_DIET);
    }

    /**
     * Creates a MobEffectHolder targeting GOOD diets with a specified chance.
     */
    protected MobEffectHolder goodEffect(MobEffectInstance effect, float chance) {
        return new MobEffectHolder(effect, ConstantFloat.of(chance), MobEffectHolder.EffectOperation.WHEN_GOOD_DIET);
    }

    /**
     * Creates a MobEffectHolder targeting SICK/BAD diets (100% chance).
     */
    protected MobEffectHolder sickEffect(MobEffectInstance effect) {
        return new MobEffectHolder(effect, ConstantFloat.of(1.0f), MobEffectHolder.EffectOperation.WHEN_SICK_DIET);
    }

    /**
     * Creates a MobEffectHolder targeting SICK/BAD diets with a specified chance.
     */
    protected MobEffectHolder sickEffect(MobEffectInstance effect, float chance) {
        return new MobEffectHolder(effect, ConstantFloat.of(chance), MobEffectHolder.EffectOperation.WHEN_SICK_DIET);
    }

    /**
     * Creates a fully custom MobEffectHolder.
     */
    protected MobEffectHolder customEffect(MobEffectInstance effect, FloatProvider chanceProvider, MobEffectHolder.EffectOperation operation) {
        return new MobEffectHolder(effect, chanceProvider, operation);
    }
}
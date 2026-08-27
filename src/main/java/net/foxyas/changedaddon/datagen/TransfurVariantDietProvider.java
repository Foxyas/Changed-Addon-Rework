package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTransfurDiets;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.FoodDietEntry;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.VariantHolder;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
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

import java.util.List;

public class TransfurVariantDietProvider {

    protected final String modid;

    public TransfurVariantDietProvider(String modid) {
        this.modid = modid;
    }

    public void bootstrap(BootstapContext<TransfurVariantDiet> context) {
        TagKey<TransfurVariant<?>> canineTag = TagKey.create(
                ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(),
                ResourceLocation.fromNamespaceAndPath(ChangedAddonMod.MODID, "canines")
        );

        addDietWithHolders(context, "canine_diet",
                List.of(
                        new VariantHolder(canineTag), // Usando a Tag #changedaddon:canines
                        new VariantHolder(ChangedTransfurVariants.DARK_LATEX_WOLF_PUP.get()) // Usando a variant diretamente
                ),
                List.of(
                        food(Items.COOKED_BEEF, 2.0f, 4.0f, 0.5f, new MobEffectInstance(MobEffects.REGENERATION, 100, 0)),
                        sickFood(Items.APPLE, 3.0f, 1.0f)
                )
        );
    }

    // --- Helper Methods ---

    protected void addDietWithHolders(BootstapContext<TransfurVariantDiet> context, String name, List<VariantHolder> holders, List<FoodDietEntry> foods) {
        context.register(ResourceKey.create(ChangedAddonTransfurDiets.TRANSFUR_VARIANT_DIET_KEY, ResourceLocation.fromNamespaceAndPath(this.modid, name)),
                new TransfurVariantDiet(holders, foods));
    }

    protected void addDiet(BootstapContext<TransfurVariantDiet> context, String name, List<TransfurVariant<?>> variants, List<FoodDietEntry> foods) {
        List<VariantHolder> holders = variants.stream()
                .map(VariantHolder::new)
                .toList();
        addDietWithHolders(context, name, holders, foods);
    }

    // Support List<Ingredient> directly
    protected FoodDietEntry food(List<Ingredient> ingredients, FloatProvider hunger, FloatProvider saturation, List<MobEffectInstance> effects, boolean isSick) {
        return new FoodDietEntry(ingredients, hunger, saturation, effects, isSick);
    }

    protected FoodDietEntry food(List<Ingredient> ingredients, FloatProvider hunger, FloatProvider saturation, MobEffectInstance effect, boolean isSick) {
        return food(ingredients, hunger, saturation, List.of(effect), isSick);
    }

    // Helper for Item varargs -> Converts items into a List of Ingredients
    protected FoodDietEntry food(FloatProvider hunger, FloatProvider saturation, MobEffectInstance effect, boolean isSick, Item... items) {
        List<Ingredient> ingredients = java.util.Arrays.stream(items)
                .map(Ingredient::of)
                .toList();
        return food(ingredients, hunger, saturation, effect, isSick);
    }

    // Single/Multiple Item Food Helpers
    protected FoodDietEntry food(Item item, float minHunger, float maxHunger, float saturation, MobEffectInstance effect) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        return food(List.of(Ingredient.of(item)), hunger, ConstantFloat.of(saturation), List.of(effect), false);
    }

    protected FoodDietEntry food(Item item, float minHunger, float maxHunger, float saturation, List<MobEffectInstance> effects) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        return food(List.of(Ingredient.of(item)), hunger, ConstantFloat.of(saturation), effects, false);
    }

    protected FoodDietEntry food(Item item, float hunger, float saturation, MobEffectInstance mobEffectInstance) {
        return food(item, hunger, hunger, saturation, List.of(mobEffectInstance));
    }

    protected FoodDietEntry food(Item item, float hunger, float saturation) {
        return food(item, hunger, hunger, saturation, List.of());
    }

    protected FoodDietEntry food(List<Item> items, float minHunger, float maxHunger, float saturation, MobEffectInstance effect) {
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
}
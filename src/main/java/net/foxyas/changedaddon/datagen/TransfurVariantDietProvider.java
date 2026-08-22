package net.foxyas.changedaddon.datagen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.FoodDietEntry;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet.VariantHolder;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TransfurVariantDietProvider extends JsonCodecProvider<TransfurVariantDiet> {

    public TransfurVariantDietProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(
                output,
                existingFileHelper,
                ChangedAddonMod.MODID,
                JsonOps.INSTANCE,
                PackType.SERVER_DATA,
                "transfurVariant/diets",
                TransfurVariantDiet.CODEC,
                new HashMap<>()
        );
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        generateTransfurDiets(this.entries);
        return super.run(cache);
    }

    protected void generateTransfurDiets(Map<ResourceLocation, TransfurVariantDiet> map) {
        // Exemplo usando mistura de Variante Direta e Tag
        TagKey<TransfurVariant<?>> canineTag = TagKey.create(
                ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(),
                ResourceLocation.fromNamespaceAndPath(ChangedAddonMod.MODID, "canines")
        );

        addDietWithHolders(map, "canine_diet",
                List.of(
                        new VariantHolder(canineTag), // Usando a Tag #changedaddon:canines
                        new VariantHolder(ChangedTransfurVariants.DARK_LATEX_PUP.get()) // Usando a variant diretamente
                ),
                List.of(
                        food(Items.COOKED_BEEF, 2.0f, 4.0f, 0.5f, new MobEffectInstance(MobEffects.REGENERATION, 100, 0)),
                        sickFood(Items.APPLE, 3.0f, 1.0f)
                )
        );
    }

    // --- Métodos Auxiliares ---

    protected void addDietWithHolders(Map<ResourceLocation, TransfurVariantDiet> map, String name, List<VariantHolder> holders, List<FoodDietEntry> foods) {
        map.put(ResourceLocation.fromNamespaceAndPath(this.modid, name), new TransfurVariantDiet(holders, foods));
    }

    protected void addDiet(Map<ResourceLocation, TransfurVariantDiet> map, String name, List<TransfurVariant<?>> variants, List<FoodDietEntry> foods) {
        List<VariantHolder> holders = variants.stream()
                .map(v -> new VariantHolder(Either.left(v)))
                .toList();
        addDietWithHolders(map, name, holders, foods);
    }

    protected FoodDietEntry food(Ingredient ingredient, FloatProvider hunger, FloatProvider saturation, MobEffectInstance effect, boolean isSick) {
        return new FoodDietEntry(ingredient, hunger, saturation, Optional.ofNullable(effect), isSick);
    }

    protected FoodDietEntry food(Item item, float minHunger, float maxHunger, float saturation, MobEffectInstance effect) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        return food(Ingredient.of(item), hunger, ConstantFloat.of(saturation), effect, false);
    }

    protected FoodDietEntry food(Item item, float hunger, float saturation) {
        return food(item, hunger, hunger, saturation, null);
    }

    protected FoodDietEntry sickFood(Item item, float minHunger, float maxHunger, float saturation) {
        FloatProvider hunger = minHunger == maxHunger ? ConstantFloat.of(minHunger) : UniformFloat.of(minHunger, maxHunger);
        return food(Ingredient.of(item), hunger, ConstantFloat.of(saturation), null, true);
    }

    protected FoodDietEntry sickFood(Item item, float hunger, float saturation) {
        return sickFood(item, hunger, hunger, saturation);
    }
}
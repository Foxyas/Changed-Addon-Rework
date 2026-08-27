package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.foxyas.changedaddon.init.ChangedAddonTransfurDiets;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TransfurVariantDietManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    // Global list containing all registered diets loaded via JSON
    private static final Map<ResourceLocation, TransfurVariantDiet> ALL_DIETS = new HashMap<>();

    public TransfurVariantDietManager() {
        super(GSON, "transfur_variant/diets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        ALL_DIETS.clear();

        object.forEach((location, jsonElement) -> {
            TransfurVariantDiet.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                    .resultOrPartial(err -> System.err.println("Failed to load diet " + location + ": " + err))
                    .ifPresent((transfurVariantDiet -> ALL_DIETS.put(location, transfurVariantDiet)));
        });
    }

    /**
     * Returns ALL diets associated with a specific transfur variant.
     */
    public static List<TransfurVariantDiet> getDietsForVariant(TransfurVariantInstance<?> variant) {
        if (variant == null || variant.getParent() == null) return List.of();
        Registry<TransfurVariantDiet> registry = ChangedAddonTransfurDiets.registry(variant.getHost().level());

        Map<ResourceLocation, TransfurVariantDiet> allDiets = ALL_DIETS;
        Map<ResourceLocation, TransfurVariantDiet> levelRegistry = new HashMap<>();

        for (TransfurVariantDiet transfurVariantDiet : registry.stream().toList()) {
            ResourceLocation key = registry.getKey(transfurVariantDiet);
            if (key != null) {
                levelRegistry.put(key, transfurVariantDiet);
            }
        }

        levelRegistry.forEach(allDiets::replace);

        return allDiets.values().stream()
                .filter(diet -> diet.matchesVariant(variant.getParent()))
                .toList();
    }

    /**
     * Finds the first FoodDietEntry matching the consumed food item for a specific variant.
     */
    public static Optional<FoodDietEntry> getFirstDietItemFor(TransfurVariantInstance<?> variant, ItemStack foodStack) {
        List<FoodDietEntry> diets = getDietItemsFor(variant, foodStack);
        return diets.stream().findFirst();
    }

    /**
     * Finds all FoodDietEntry objects matching the consumed food item for a specific variant across all registered diets.
     */
    public static List<FoodDietEntry> getDietItemsFor(TransfurVariantInstance<?> variant, ItemStack foodStack) {
        ArrayList<FoodDietEntry> list = new ArrayList<>();
        List<TransfurVariantDiet> diets = getDietsForVariant(variant);

        for (TransfurVariantDiet diet : diets) {
            for (FoodDietEntry foodDietEntry : diet.foods()) {
                for (Ingredient ingredient : foodDietEntry.ingredients()) {
                    if (ingredient.test(foodStack)) {
                        list.add(foodDietEntry);
                    }
                }
            }
        }

        if (variant.getChangedEntity() instanceof IVariantExtraStats iVariantExtraStats) {
            list.addAll(iVariantExtraStats.getExtraDietTypes());
        }

        return list;
    }

    public static Map<ResourceLocation, TransfurVariantDiet> getAllDiets() {
        return ALL_DIETS;
    }
}
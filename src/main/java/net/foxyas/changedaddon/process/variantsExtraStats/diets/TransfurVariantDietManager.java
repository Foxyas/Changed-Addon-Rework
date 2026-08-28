package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.foxyas.changedaddon.init.ChangedAddonTransfurDiets;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class TransfurVariantDietManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    // Global list containing all registered diets loaded via JSON
    private static final Map<ResourceLocation, TransfurVariantDiet> ALL_DIETS = new HashMap<>();

    public TransfurVariantDietManager() {
        super(GSON, "transfur_variant/diets");
        //I gonna keep the class extending the SimpleJsonResourceReloadListener because I feel that someone will need it for some reason
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
     * Merges static/legacy diets with level registry diets (level registry takes priority).
     */
    public static List<TransfurVariantDiet> getDietsForVariant(TransfurVariantInstance<?> variant) {
        if (variant == null || variant.getParent() == null || variant.getHost() == null) return List.of();

        Level level = variant.getHost().level();
        Registry<TransfurVariantDiet> registry = ChangedAddonTransfurDiets.registry(level);

        // 1. Create a local copy initialized with the old/fallback diets
        Map<ResourceLocation, TransfurVariantDiet> combinedDiets = new HashMap<>(ALL_DIETS);

        // 2. Add all registry entries. putAll replaces existing keys and adds new ones in one step
        registry.entrySet().forEach(entry ->
                combinedDiets.put(entry.getKey().location(), entry.getValue())
        );

        // 3. Filter for matching variants
        return combinedDiets.values().stream()
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

    public static Map<ResourceLocation, TransfurVariantDiet> getAllDietsForLevel(Level level) {
        return ChangedAddonTransfurDiets.registry(level)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().location(),
                        Map.Entry::getValue
                ));
    }

    public static Map<ResourceLocation, TransfurVariantDiet> getAllDietsMap() {
        return ALL_DIETS;
    }
}
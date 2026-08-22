package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransfurVariantDietManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    // Global list containing all registered diets loaded via JSON
    private static final List<TransfurVariantDiet> ALL_DIETS = new ArrayList<>();

    public TransfurVariantDietManager() {
        super(GSON, "transfurVariant/diets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        ALL_DIETS.clear();

        object.forEach((location, jsonElement) -> {
            TransfurVariantDiet.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                    .resultOrPartial(err -> System.err.println("Failed to load diet " + location + ": " + err))
                    .ifPresent(ALL_DIETS::add);
        });
    }

    /**
     * Returns ALL diets associated with a specific transfur variant.
     */
    public static List<TransfurVariantDiet> getDietsForVariant(TransfurVariant<?> variant) {
        if (variant == null) return List.of();

        return ALL_DIETS.stream()
                .filter(diet -> diet.matchesVariant(variant))
                .toList();
    }

    /**
     * Finds the first FoodDietEntry matching the consumed food item for a specific variant.
     */
    public static Optional<FoodDietEntry> getDietItemFor(TransfurVariant<?> variant, ItemStack foodStack) {
        List<TransfurVariantDiet> diets = getDietsForVariant(variant);

        for (TransfurVariantDiet diet : diets) {
            for (FoodDietEntry foodDietEntry : diet.foods()) {
                if (foodDietEntry.ingredient().test(foodStack)) {
                    return Optional.of(foodDietEntry);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Finds all FoodDietEntry objects matching the consumed food item for a specific variant across all registered diets.
     */
    public static List<FoodDietEntry> getDietItemsFor(TransfurVariant<?> variant, ItemStack foodStack) {
        ArrayList<FoodDietEntry> list = new ArrayList<>();
        List<TransfurVariantDiet> diets = getDietsForVariant(variant);

        for (TransfurVariantDiet diet : diets) {
            for (FoodDietEntry foodDietEntry : diet.foods()) {
                if (foodDietEntry.ingredient().test(foodStack)) {
                    list.add(foodDietEntry);
                }
            }
        }

        return list;
    }

    public static List<TransfurVariantDiet> getAllDiets() {
        return ALL_DIETS;
    }
}
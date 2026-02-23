package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.advancements.critereon.UsedItemAmountTrigger;
import net.foxyas.changedaddon.datagen.customData.AdvancementWriter;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdvancementProvider extends net.minecraft.data.advancements.AdvancementProvider {

    public static final String[] ADDON_FORM_RECIPES = new String[]{
            "form_avali",
            "form_biosynth_snow_leopard",
            "form_blue_lizard",
            "form_buny",
            "form_dazed_latex",
            "form_exp6",
            "form_exp_2",
            "form_experiment009",
            "form_experiment_10",
            "form_fengqi_wolf",
            "form_himalayan_crystal_gas_cat",
            "form_latex_calico_cat",
            "form_latex_cheetah",
            "form_latex_dragon_snow_leopard_shark",
            "form_latex_kitsune",
            "form_latex_snow_fox",
            "form_latex_snow_leopard_partial",
            "form_latex_white_snow_leopard",
            "form_luminara_flower_beast",
            "form_luminarctic_leopard",
            "form_lynx",
            "form_mirror_white_tiger",
            "form_puro_kind",
            "form_wolfy",
            "form_dark_latex_yufeng_queen"
    };

    public static final String[] ADDON_FORM_RECIPES_JSON = Arrays.stream(ADDON_FORM_RECIPES).map(id -> id.endsWith(".json") ? id : id + ".json").toArray(String[]::new);

    public static final AdvancementWriter advancementWrite = new AdvancementWriter();

    protected final PackOutput output;

    public AdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper fileHelperIn) {
        super(output, lookup, List.of(AdvancementProvider::generate));
        this.output = output;
    }

    private static void generate(HolderLookup.@NotNull Provider lookup, @NotNull Consumer<Advancement> out) {

    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        CompletableFuture<?> normalAdvancements = super.run(pOutput);
        CompletableFuture<?> customAdvancements = writeCustomAdvancements(pOutput);
        return CompletableFuture.allOf(normalAdvancements, customAdvancements);
    }

    protected CompletableFuture<?> writeCustomAdvancements(CachedOutput cache) {
        Advancement.Builder foxtaBuilder = Advancement.Builder.advancement()
                .parent(ResourceLocation.fromNamespaceAndPath("changed_addon", "drink_foxta"))
                .display(
                        ChangedAddonItems.FOXTA.get(),
                        Component.translatable("advancements.foxta_addictive.title"),
                        Component.translatable("advancements.foxta_addictive.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion(
                        "foxta_addictive",
                        new UsedItemAmountTrigger.Instance(ContextAwarePredicate.ANY, ChangedAddonItems.FOXTA.get(), 100, null)
                )
                .rewards(AdvancementRewards.Builder.experience(3500).build());

        Advancement.Builder snepsiBuilder = Advancement.Builder.advancement()
                .parent(ResourceLocation.fromNamespaceAndPath("changed_addon", "drink_snepsi"))
                .display(
                        ChangedAddonItems.SNEPSI.get(),
                        Component.translatable("advancements.snepsi_addictive.title"),
                        Component.translatable("advancements.snepsi_addictive.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion(
                        "snepsi_addictive",
                        new UsedItemAmountTrigger.Instance(ContextAwarePredicate.ANY, ChangedAddonItems.SNEPSI.get(), 100, null)
                )
                .rewards(AdvancementRewards.Builder.experience(3500).build());


        advancementWrite.write(cache, output, ResourceLocation.parse("changed_addon:snepsi_addictive"), snepsiBuilder);
        advancementWrite.write(cache, output, ResourceLocation.parse("changed_addon:foxta_addictive"), foxtaBuilder);

        AdvancementRewards.Builder formsRecipes = new AdvancementRewards.Builder();
        for (String id : ADDON_FORM_RECIPES) {
            formsRecipes.addRecipe(ChangedAddonMod.resourceLoc(id));
        }

        Advancement.Builder formsRecipesGiver = Advancement.Builder.advancement()
                .rewards(formsRecipes)
                .addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedItems.LATEX_BASE.get()))
                .addCriterion("has_recipe", RecipeUnlockedTrigger.unlocked(Changed.modResource("form_white_latex_wolf")));

        advancementWrite.write(cache,
                output,
                Path.of("recipes", "changed_addon_forms"),
                ResourceLocation.parse("changed_addon:latex_forms"),
                formsRecipesGiver
        );


        return CompletableFuture.allOf(advancementWrite.completableFutureList.toArray(CompletableFuture[]::new));
    }

    // ---------------------------------------------------------
    //  BASIC PUBLIC METHODS
    // ---------------------------------------------------------

    /**
     * Create an advancement using a builder modifier.
     */
    public void add(Consumer<Advancement> consumer, ResourceLocation id, Function<Advancement.Builder, Advancement.Builder> modifier) {
        Advancement.Builder builder = Advancement.Builder.advancement();
        builder = modifier.apply(builder);
        builder.save(consumer, id.toString());
    }

    public void addSimpleDisplayCriterion(Consumer<Advancement> consumer, ResourceLocation id, DisplayInfo displayInfo, String criterionId, Criterion criterion) {
        Advancement.Builder builder = Advancement.Builder.advancement();
        builder.display(displayInfo);
        builder.addCriterion(criterionId, criterion);
        builder.save(consumer, id.toString());
    }

    public void addSimpleDisplayCriterion(Consumer<Advancement> consumer, ResourceLocation id, DisplayInfo displayInfo, String criterionId, CriterionTriggerInstance criterion) {
        Advancement.Builder builder = Advancement.Builder.advancement();
        builder.display(displayInfo);
        builder.addCriterion(criterionId, criterion);
        builder.save(consumer, id.toString());
    }

    /**
     * Create a simple advancement with title, description and icon.
     */
    public void simple(Consumer<Advancement> consumer, ResourceLocation id, String title, String description, ItemLike icon, ItemPredicate itemPredicate) {
        add(consumer, id, b -> b
                .display(
                        icon,
                        Component.literal(title),
                        Component.literal(description),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(itemPredicate))
        );
    }

    /**
     * Simple advancement with parent + criteria.
     */
    public void simpleWithParent(Consumer<Advancement> consumer, ResourceLocation id, ResourceLocation parent, String title, String description, ItemLike icon, ItemPredicate itemPredicate) {
        add(consumer, id, b -> b
                .parent(Advancement.Builder.advancement().build(parent)) // or fileHelper support if needed
                .display(
                        icon,
                        Component.literal(title),
                        Component.literal(description),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(itemPredicate))
        );
    }

    /**
     * Create a reward-only advancement (used sometimes to trigger functions).
     */
    public void reward(Consumer<Advancement> consumer, ItemLike icon, ResourceLocation id, AdvancementRewards rewards, ItemPredicate itemPredicate) {
        add(consumer, id, b -> b
                .display(
                        icon,
                        Component.literal(id.getPath()),
                        Component.literal(""),
                        null,
                        FrameType.CHALLENGE,
                        false,
                        false,
                        false
                )
                .rewards(rewards)
                .addCriterion("tick", InventoryChangeTrigger.TriggerInstance.hasItems(itemPredicate)) // minimal criterion
        );
    }
}

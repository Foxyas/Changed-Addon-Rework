package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.advancements.critereon.HoldingItemsTrigger;
import net.foxyas.changedaddon.advancements.critereon.UsedItemAmountTrigger;
import net.foxyas.changedaddon.datagen.customData.AdvancementWriter;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public class ModAdvancementProvider extends AdvancementProvider {

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
    public static final ResourceLocation ADVANCEMENT_ROOT = ResourceLocation.fromNamespaceAndPath("changed_addon", "advancements_root");

    protected final PackOutput output;
    protected final ExistingFileHelper fileHelperIn;
    protected final CompletableFuture<HolderLookup.Provider> lookup;

    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper fileHelperIn) {
        super(output, lookup, List.of(ModAdvancementProvider::generate));
        this.lookup = lookup;
        this.fileHelperIn = fileHelperIn;
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

        Advancement.Builder latexInsulatorAdvancement = Advancement.Builder.advancement()
                .parent(ADVANCEMENT_ROOT)
                .display(
                        ChangedAddonItems.SNEPSI.get(),
                        Component.translatable("advancements.latex_insulator_advancement.title"),
                        Component.translatable("advancements.latex_insulator_advancement.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        true
                )
                .addCriterion(
                        "place",
                        ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ChangedAddonBlocks.LATEX_INSULATOR.get())
                )
                .rewards(AdvancementRewards.Builder.experience(0).build());

        advancementWrite.write(cache, output, ResourceLocation.parse("changed_addon:latex_insulator"), latexInsulatorAdvancement);

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

        Advancement.Builder obtainImpureAmmonia = Advancement.Builder.advancement();
        Advancement.Builder obtainAmmonia = Advancement.Builder.advancement();
        Advancement.Builder obtainCompressedAmmonia = Advancement.Builder.advancement();
        Advancement.Builder obtainAmmoniaParticles = Advancement.Builder.advancement();
        Advancement.Builder hazyPurple = Advancement.Builder.advancement();

        obtainImpureAmmonia.parent(ADVANCEMENT_ROOT)
                .addCriterion("obtain_item", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.IMPURE_AMMONIA.get()));
        obtainAmmonia.parent(ADVANCEMENT_ROOT)
                .addCriterion("obtain_item", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.AMMONIA.get()));
        obtainCompressedAmmonia.parent(ADVANCEMENT_ROOT)
                .addCriterion("obtain_item", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.AMMONIA_COMPRESSED.get()));
        obtainAmmoniaParticles.parent(ADVANCEMENT_ROOT)
                .addCriterion("obtain_item", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.AMMONIA_PARTICLE.get()));

        ItemPredicate Exp10Dna = simpleItemPredicate(ChangedAddonItems.EXPERIMENT_10_DNA.get());
        ItemPredicate Exp9Dna = simpleItemPredicate(ChangedAddonItems.EXPERIMENT_009_DNA.get());

        hazyPurple = Advancement.Builder.advancement()
                .parent(ChangedAddonMod.resourceLoc("kill_experiment_009"))
                .display(
                        Items.PURPLE_DYE, // Icon
                        Component.translatable("advancements.hazy_purple.title"), // Title
                        Component.translatable("advancements.hazy_purple.descr"), // Description
                        null, // Background (null because it has a parent)
                        FrameType.CHALLENGE, // Frame
                        true, // show_toast
                        true, // announce_to_chat
                        true  // hidden
                )
                .rewards(AdvancementRewards.Builder.experience(10000))
                .addCriterion("holding_items", HoldingItemsTrigger.Instance.holdingBoth(Exp9Dna, Exp10Dna, true));

        advancementWrite.write(cache, output, ChangedAddonMod.resourceLoc("obtain_impure_ammonia"), obtainImpureAmmonia);
        advancementWrite.write(cache, output, ChangedAddonMod.resourceLoc("obtain_ammonia"), obtainAmmonia);
        advancementWrite.write(cache, output, ChangedAddonMod.resourceLoc("obtain_compressed_ammonia"), obtainCompressedAmmonia);
        advancementWrite.write(cache, output, ChangedAddonMod.resourceLoc("obtain_ammonia_particles"), obtainAmmoniaParticles);
        advancementWrite.write(cache, output, ChangedAddonMod.resourceLoc("hazy_purple"), hazyPurple);


        return CompletableFuture.allOf(advancementWrite.completableFutureList.toArray(CompletableFuture[]::new));
    }

    protected static ItemPredicate simpleItemPredicate(ItemLike item) {
        return ItemPredicate.Builder.item()
                .of(item)
                .build();
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

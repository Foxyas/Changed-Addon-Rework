package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.advancements.critereon.*;
import net.foxyas.changedaddon.advancements.critereon.api.DynamicTransfurPredicate;
import net.foxyas.changedaddon.datagen.customData.AdvancementWriter;
import net.foxyas.changedaddon.init.*;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.advancements.critereon.TransfurPredicate;
import net.ltxprogrammer.changed.advancements.critereon.TransfurTrigger;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdvancementProvider extends ForgeAdvancementProvider {

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

    public static final AdvancementWriter advancementWrite = new AdvancementWriter();
    public static final ResourceLocation ADVANCEMENT_ROOT = ResourceLocation.fromNamespaceAndPath("changed_addon", "advancements_root");

    protected final PackOutput output;
    protected final ExistingFileHelper fileHelperIn;
    protected final CompletableFuture<HolderLookup.Provider> lookup;

    public AdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper fileHelperIn) {
        super(output, lookup, fileHelperIn, List.of(AdvancementProvider::generate));
        this.lookup = lookup;
        this.fileHelperIn = fileHelperIn;
        this.output = output;
    }

    private static void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper) {
        //Depth 0
        Advancement root = Advancement.Builder.recipeAdvancement()//recipeAdvancement doesnt send telemetry
                .display(
                        ChangedAddonItems.CHANGED_BOOK.get(),
                        Component.translatable("advancements.advancements_root.title"),
                        Component.translatable("advancements.advancements_root.descr"),
                        ChangedAddonMod.texLoc("screens/wall_white_with_silver"),
                        FrameType.TASK,
                        true,
                        false,
                        false
                )
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(saver, ChangedAddonMod.resourceLoc("advancements_root"), existingFileHelper);

        //Depth 1
        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        Items.SALMON_BUCKET,
                        Component.translatable("advancements.big_one.title"),
                        Component.translatable("advancements.big_one.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                .rewards(AdvancementRewards.Builder.experience(400))
                .save(saver, ChangedAddonMod.resourceLoc("big_one"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.CATALYZER.get(),
                        Component.translatable("advancements.catalyzer_advancement.title"),
                        Component.translatable("advancements.catalyzer_advancement.descr"),
                        null,
                        FrameType.TASK,
                        true,
                        false,
                        false
                )
                .addCriterion("catalyzer", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.CATALYZER.get()))
                .save(saver, ChangedAddonMod.resourceLoc("catalyzer_advancement"), existingFileHelper);

        Advancement unifuser = Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.UNIFUSER.get(),
                        Component.translatable("advancements.obtain_unifuser.title"),
                        Component.translatable("advancements.obtain_unifuser.descr"),
                        null,
                        FrameType.TASK,
                        true,
                        false,
                        false
                )
                .addCriterion("unifuser", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.UNIFUSER.get()))
                .save(saver, ChangedAddonMod.resourceLoc("obtain_unifuser"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        Items.BLACK_DYE,
                        Component.translatable("advancements.crystal_dyer.title"),
                        Component.translatable("advancements.crystal_dyer.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                .rewards(AdvancementRewards.Builder.experience(80))
                .save(saver, ChangedAddonMod.resourceLoc("crystal_dyer"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.DARK_LATEX_WOLF_PLUSH.get(),
                        Component.translatable("advancements.cuddles_of_eternal_night.title"),
                        Component.translatable("advancements.cuddles_of_eternal_night.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("sleep_next_to_plushy", new SleepNextAPlushyTrigger.Instance(ContextAwarePredicate.ANY, null, true, "dark_latex_plushy"))
                .rewards(AdvancementRewards.Builder.experience(2500))
                .save(saver, ChangedAddonMod.resourceLoc("cuddles_of_eternal_night"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.SPAWNEGGOFFOXYAS.get(),
                        Component.translatable("advancements.foxyas_advancement.title"),
                        Component.translatable("advancements.foxyas_advancement.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        true
                )
                .addCriterion("kill", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ChangedAddonEntities.LATEX_SNOW_FOX_FOXYAS.get()))))
                .save(saver, ChangedAddonMod.resourceLoc("foxyas_advancement"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.FRIENDLY_GOEY_ICON.get(),
                        Component.translatable("advancements.gooey_friend.title"),
                        Component.translatable("advancements.gooey_friend.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        false,
                        false
                )
                .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                .rewards(AdvancementRewards.Builder.experience(5))
                .save(saver, ChangedAddonMod.resourceLoc("gooey_friend"), existingFileHelper);

        Advancement hugger = Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        Items.PINK_WOOL,
                        Component.translatable("advancements.grab.hug.title").withStyle(ChatFormatting.RED),
                        Component.translatable("advancements.grab.hug.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("hug", new GrabEntityTrigger.Instance(ContextAwarePredicate.ANY, null, "hug"))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, ChangedAddonMod.resourceLoc("hugger"), existingFileHelper);

        Advancement kill009 = Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.EXP_9_CONTAINMENT_VIAL.get(),
                        Component.translatable("advancements.kill_experiment_009.title"),
                        Component.translatable("advancements.kill_experiment_009.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("kill", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ChangedAddonEntities.EXPERIMENT_009_BOSS.get()))))
                .save(saver, ChangedAddonMod.resourceLoc("kill_experiment_009"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.SNEP_ICON.get(),
                        Component.translatable("advancements.leaper.title"),
                        Component.translatable("advancements.leaper.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, ChangedAddonMod.resourceLoc("leaper"), existingFileHelper);

        Advancement katana = Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.ELECTRIC_KATANA.get(),
                        Component.translatable("advancements.obtain_a_electric_katana.title"),
                        Component.translatable("advancements.obtain_a_electric_katana.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        false,
                        true
                )
                .addCriterion("katana", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.ELECTRIC_KATANA.get()))
                .save(saver, ChangedAddonMod.resourceLoc("obtain_a_electric_katana"), existingFileHelper);

        Advancement redDagger = Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.CRYSTAL_DAGGER_RED.get(),
                        Component.translatable("advancements.obtain_red_crystal_dagger.title"),
                        Component.translatable("advancements.obtain_red_crystal_dagger.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        false,
                        true
                )
                .addCriterion("dagger", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.CRYSTAL_DAGGER_RED.get()))
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, ChangedAddonMod.resourceLoc("obtain_red_crystal_dagger"), existingFileHelper);

        Advancement obtainFoxta = Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.FOXTA.get(),
                        Component.translatable("advancements.obtain_foxta.title"),
                        Component.translatable("advancements.obtain_foxta.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        false,
                        false
                )
                .addCriterion("foxta", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.FOXTA.get()))
                .rewards(AdvancementRewards.Builder.experience(25))
                .save(saver, ChangedAddonMod.resourceLoc("obtain_foxta"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.PAINITE.get(),
                        Component.translatable("advancements.obtain_painite.title"),
                        Component.translatable("advancements.obtain_painite.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("painite", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.PAINITE.get()))
                .save(saver, ChangedAddonMod.resourceLoc("obtain_painite"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.ORANGE_JUICE.get(),
                        Component.translatable("advancements.orange_juice_is_yummy.title"),
                        Component.translatable("advancements.orange_juice_is_yummy.descr"),
                        null,
                        FrameType.TASK,
                        true,
                        false,
                        false
                )
                .addCriterion("juice", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.ORANGE_JUICE.get()))
                .save(saver, ChangedAddonMod.resourceLoc("orange_juice_is_yummy"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.DYEABLE_SHORTS.get(),
                        Component.translatable("advancements.pants_dyer.title"),
                        Component.translatable("advancements.pants_dyer.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("shorts", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.DYEABLE_SHORTS.get()))
                .save(saver, ChangedAddonMod.resourceLoc("pants_dyer"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.FRIENDLY_GOEY_ICON.get(),
                        Component.translatable("advancements.pat_advancement.title"),
                        Component.translatable("advancements.pat_advancement.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("pat", new PatEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, "chance", ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, EntityTypePredicate.ANY))
                .rewards(AdvancementRewards.Builder.experience(1000))
                .save(saver, ChangedAddonMod.resourceLoc("pat_advancement"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.FRIENDLY_GOEY_ICON.get(),
                        Component.translatable("advancements.paticifier.title"),
                        Component.translatable("advancements.paticifier.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("pat", new PatEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, "paticifier", ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, EntityTypePredicate.ANY))
                .rewards(AdvancementRewards.Builder.experience(10000))
                .save(saver, ChangedAddonMod.resourceLoc("paticifier"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.FRIENDLY_GOEY_ICON.get(),
                        Component.translatable("advancements.pats_on_the_beast.title"),
                        Component.translatable("advancements.pats_on_the_beast.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("pat", new PatEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, "pats_on_the_beast", ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, EntityTypePredicate.ANY))
                .rewards(AdvancementRewards.Builder.experience(10000))
                .save(saver, ChangedAddonMod.resourceLoc("pats_on_the_beast"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedAddonItems.PAT_ICON.get(),
                        Component.translatable("advancements.stealth_pats.title"),
                        Component.translatable("advancements.stealth_pats.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("pat", new PatEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, "stealth", ItemPredicate.Builder.item().of(ChangedAddonItems.DARK_LATEX_HEAD_CAP.get()).build(), ItemPredicate.Builder.item().of(ChangedAddonItems.DARK_LATEX_COAT.get()).build(), ItemPredicate.ANY, ItemPredicate.ANY, EntityTypePredicate.ANY))
                .rewards(AdvancementRewards.Builder.experience(160))
                .save(saver, ChangedAddonMod.resourceLoc("stealth_pats"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        Items.SALMON,
                        Component.translatable("advancements.swim_regret.title"),
                        Component.translatable("advancements.swim_regret.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, ChangedAddonMod.resourceLoc("swim_regret"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(root)
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
                .addCriterion("place", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ChangedAddonBlocks.LATEX_INSULATOR.get()))
                .save(saver, ChangedAddonMod.resourceLoc("latex_insulator"), existingFileHelper);

        Advancement crystalAdventurer = Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        ChangedItems.DARK_DRAGON_CRYSTAL_FRAGMENT.get(),
                        Component.translatable("advancements.crystal_adventurer.title"),
                        Component.translatable("advancements.crystal_adventurer.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("dl_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedItems.DARK_LATEX_CRYSTAL_FRAGMENT.get()))
                .addCriterion("dd_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedItems.DARK_DRAGON_CRYSTAL_FRAGMENT.get()))
                .addCriterion("bf_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedItems.BEIFENG_CRYSTAL_FRAGMENT.get()))
                .addCriterion("w_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedItems.WOLF_CRYSTAL_FRAGMENT.get()))
                .addCriterion("ow_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.ORANGE_WOLF_CRYSTAL_FRAGMENT.get()))
                .addCriterion("ww_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.WHITE_WOLF_CRYSTAL_FRAGMENT.get()))
                .addCriterion("yw_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.YELLOW_WOLF_CRYSTAL_FRAGMENT.get()))
                .addCriterion("bw_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.BLUE_WOLF_CRYSTAL_FRAGMENT.get()))
                .rewards(AdvancementRewards.Builder.experience(160))
                .save(saver, ChangedAddonMod.resourceLoc("crystal_adventurer"), existingFileHelper);

        //Depth 2
        Advancement crystalCollector = Advancement.Builder.recipeAdvancement()
                .parent(crystalAdventurer)
                .display(
                        ChangedItems.WOLF_CRYSTAL_FRAGMENT.get(),
                        Component.translatable("advancements.crystal_collector.title"),
                        Component.translatable("advancements.crystal_collector.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("crystals", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ChangedItems.DARK_LATEX_CRYSTAL_FRAGMENT.get(), ChangedItems.DARK_DRAGON_CRYSTAL_FRAGMENT.get(),
                        ChangedItems.BEIFENG_CRYSTAL_FRAGMENT.get(), ChangedItems.WOLF_CRYSTAL_FRAGMENT.get(),
                        ChangedAddonItems.ORANGE_WOLF_CRYSTAL_FRAGMENT.get(), ChangedAddonItems.WHITE_WOLF_CRYSTAL_FRAGMENT.get(),
                        ChangedAddonItems.YELLOW_WOLF_CRYSTAL_FRAGMENT.get(), ChangedAddonItems.BLUE_WOLF_CRYSTAL_FRAGMENT.get()
                ))
                .rewards(AdvancementRewards.Builder.experience(160))
                .save(saver, ChangedAddonMod.resourceLoc("crystal_collector"), existingFileHelper);

        Advancement drinkFoxta = Advancement.Builder.recipeAdvancement()
                .parent(obtainFoxta)
                .display(
                        ChangedAddonItems.FOXTA.get(),
                        Component.translatable("advancements.drink_foxta.title"),
                        Component.translatable("advancements.drink_foxta.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        true
                )
                .addCriterion("foxta", ConsumeItemTrigger.TriggerInstance.usedItem(ChangedAddonItems.FOXTA.get()))
                .rewards(AdvancementRewards.Builder.experience(1000))
                .save(saver, ChangedAddonMod.resourceLoc("drink_foxta"), existingFileHelper);

        Advancement greenDagger = Advancement.Builder.recipeAdvancement()
                .parent(redDagger)
                .display(
                        ChangedAddonItems.CRYSTAL_DAGGER_GREEN.get(),
                        Component.translatable("advancements.obtain_green_crystal_dagger.title"),
                        Component.translatable("advancements.obtain_green_crystal_dagger.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        false,
                        true
                )
                .addCriterion("dagger", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.CRYSTAL_DAGGER_GREEN.get()))
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, ChangedAddonMod.resourceLoc("obtain_green_crystal_dagger"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(katana)
                .display(
                        ChangedAddonItems.ELECTRIC_KATANA_RED.get(),
                        Component.translatable("advancements.obtain_red_electric_katana.title"),
                        Component.translatable("advancements.obtain_red_electric_katana.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("katana", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.ELECTRIC_KATANA_RED.get()))
                .rewards(AdvancementRewards.Builder.experience(450))
                .save(saver, ChangedAddonMod.resourceLoc("obtain_red_electric_katana"), existingFileHelper);

        Advancement obtainSnepsi = Advancement.Builder.recipeAdvancement()
                .parent(obtainFoxta)
                .display(
                        ChangedAddonItems.SNEPSI.get(),
                        Component.translatable("advancements.obtain_snepsi.title"),
                        Component.translatable("advancements.obtain_snepsi.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        false,
                        false
                )
                .addCriterion("snepsi", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.SNEPSI.get()))
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, ChangedAddonMod.resourceLoc("obtain_snepsi"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(hugger)
                .display(
                        Items.PINK_WOOL,
                        Component.translatable("advancements.grab.hug_tight.title").withStyle(ChatFormatting.RED),
                        Component.translatable("advancements.grab.hug_tight.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("hug", new GrabEntityTrigger.Instance(ContextAwarePredicate.ANY, null, "hug_tight"))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, ChangedAddonMod.resourceLoc("tight_hugger"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(kill009)
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
                .addCriterion("holding_items", HoldingItemsTrigger.Instance.holdingBoth(simpleItemPredicate(ChangedAddonItems.EXPERIMENT_009_DNA.get()), simpleItemPredicate(ChangedAddonItems.EXPERIMENT_10_DNA.get()), true))
                .save(saver, ChangedAddonMod.resourceLoc("hazy_purple"), existingFileHelper);

        Advancement tfTotemUse = Advancement.Builder.recipeAdvancement()
                .parent(kill009)
                .display(
                        ChangedAddonItems.TRANSFUR_TOTEM.get(),
                        Component.translatable("advancements.transfur_totem_untransfur.title"),
                        Component.translatable("advancements.transfur_totem_untransfur.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        false,
                        true
                )
                .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, ChangedAddonMod.resourceLoc("transfur_totem_untransfur"), existingFileHelper);

        Advancement untf = Advancement.Builder.advancement()
                .parent(unifuser)
                .display(
                        ChangedAddonItems.POT_WITH_CAMMONIA.get(),
                        Component.translatable("advancements.untransfur_item.title"),
                        Component.translatable("advancements.untransfur_item.descr"),
                        null,
                        FrameType.GOAL,
                        true,  // show_toast
                        true,  // announce_to_chat
                        true   // hidden
                )
                .addCriterion(
                        "untransfur_item",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ChangedAddonTags.Items.UNTRANSFUR_ITEMS).build())
                )
                .save(saver, ChangedAddonMod.resourceLoc("untransfur_item"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(unifuser)
                .display(
                        ChangedAddonItems.LITIX_CAMMONIA.get(),
                        Component.translatable("advancements.craft_litix_cammonia.title"),
                        Component.translatable("advancements.craft_litix_cammonia.descr"),
                        null,
                        FrameType.GOAL,
                        true,  // show_toast
                        false, // announce_to_chat
                        true   // hidden
                )
                .addCriterion(
                        "craft_litix_cammonia",
                        RecipeCraftedTrigger.TriggerInstance.craftedItem(
                                ChangedAddonMod.resourceLoc("litix_cammonia") // Recipe ID
                        )
                )
                .save(saver, ChangedAddonMod.resourceLoc("craft_litix_cammonia"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(unifuser)
                .display(
                        ChangedAddonItems.POT_WITH_CAMMONIA.get(),
                        Component.translatable("advancements.pot_with_litix_cammonia_use.title"),
                        Component.translatable("advancements.pot_with_litix_cammonia_use.descr"),
                        null,
                        FrameType.TASK,
                        true,  // show_toast
                        false, // announce_to_chat
                        false   // hidden
                )
                .addCriterion("drink", ConsumeItemTrigger.TriggerInstance.usedItem(ChangedAddonItems.POT_WITH_CAMMONIA.get()))
                .save(saver, ChangedAddonMod.resourceLoc("pot_with_litix_cammonia_use"), existingFileHelper);

        //Depth 3
        Advancement.Builder.advancement()
                .parent(drinkFoxta)
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
                .rewards(AdvancementRewards.Builder.experience(3500))
                .save(saver, ChangedAddonMod.resourceLoc("foxta_addictive"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(tfTotemUse)
                .display(
                        ChangedAddonItems.TRANSFUR_TOTEM.get(),
                        Component.translatable("advancements.transfur_totem_benign_salvation.title"),
                        Component.translatable("advancements.transfur_totem.benign_salvation.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        false,
                        true
                )
                .addCriterion("untransfur", new UntransfurTrigger.TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(ChangedAddonItems.TRANSFUR_TOTEM.get()).build(), MobEffectsPredicate.ANY, Optional.of("totem_salvation")))
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, ChangedAddonMod.resourceLoc("transfur_totem_benign_salvation"), existingFileHelper);

        Advancement.Builder.recipeAdvancement()
                .parent(crystalCollector)
                .display(
                        ChangedAddonItems.LUMINAR_CRYSTAL_SHARD_HEARTED.get(),
                        Component.translatable("advancements.crystals_addicted.title"),
                        Component.translatable("advancements.crystals_addicted.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("lh_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD_HEARTED.get()))
                .addCriterion("dl_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedItems.DARK_LATEX_CRYSTAL_FRAGMENT.get()))
                .addCriterion("dd_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedItems.DARK_DRAGON_CRYSTAL_FRAGMENT.get()))
                .addCriterion("bf_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedItems.BEIFENG_CRYSTAL_FRAGMENT.get()))
                .addCriterion("w_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedItems.WOLF_CRYSTAL_FRAGMENT.get()))
                .addCriterion("ow_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.ORANGE_WOLF_CRYSTAL_FRAGMENT.get()))
                .addCriterion("ww_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.WHITE_WOLF_CRYSTAL_FRAGMENT.get()))
                .addCriterion("yw_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.YELLOW_WOLF_CRYSTAL_FRAGMENT.get()))
                .addCriterion("bw_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.BLUE_WOLF_CRYSTAL_FRAGMENT.get()))
                .addCriterion("lu_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD.get()))
                .rewards(AdvancementRewards.Builder.experience(2500))
                .save(saver, ChangedAddonMod.resourceLoc("crystals_addicted"), existingFileHelper);

        Advancement drinkSnepsi = Advancement.Builder.recipeAdvancement()
                .parent(obtainSnepsi)
                .display(
                        ChangedAddonItems.SNEPSI.get(),
                        Component.translatable("advancements.drink_snepsi.title"),
                        Component.translatable("advancements.drink_snepsi.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        true
                )
                .addCriterion("snepsi", ConsumeItemTrigger.TriggerInstance.usedItem(ChangedAddonItems.SNEPSI.get()))
                .rewards(AdvancementRewards.Builder.experience(1000))
                .save(saver, ChangedAddonMod.resourceLoc("drink_snepsi"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(untf)
                .display(
                        ChangedAddonItems.POT_WITH_CAMMONIA.get(),
                        Component.translatable("advancements.untransfur_mob_effect_slow.title"),
                        Component.translatable("advancements.untransfur_mob_effect_slow.descr"),
                        null,
                        FrameType.GOAL,
                        true,  // show_toast
                        true,  // announce_to_chat
                        true   // hidden
                )
                .addCriterion(
                        "untransfur_mob_effect_slow",
                        new UntransfurTrigger.TriggerInstance(
                                ContextAwarePredicate.ANY,
                                ItemPredicate.ANY,
                                MobEffectsPredicate.effects().and(ChangedAddonMobEffects.UNTRANSFUR.get()),
                                Optional.empty())
                ).save(saver, ChangedAddonMod.resourceLoc("untransfur_mob_effect_slow"), existingFileHelper);//=="untransfur_advancement"

        Advancement untfSyringe = Advancement.Builder.advancement()
                .parent(untf)
                .display(
                        ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(),
                        Component.translatable("advancements.untransfur_syringe_fast.title"),
                        Component.translatable("advancements.untransfur_syringe_fast.descr"),
                        null,
                        FrameType.GOAL,
                        true,  // show_toast
                        true,  // announce_to_chat
                        true   // hidden
                )
                .addCriterion(
                        "untransfur_syringe_fast",
                        new UntransfurTrigger.TriggerInstance(
                                ContextAwarePredicate.ANY,
                                ItemPredicate.Builder.item().of(ChangedAddonTags.Items.CAUSE_FAST_UNTRANSFUR).build(),
                                MobEffectsPredicate.ANY,
                                Optional.empty())
                )
                .save(saver, ChangedAddonMod.resourceLoc("untransfur_syringe_fast"), existingFileHelper);//=="untransfur_advancement_2"

        Advancement.Builder.recipeAdvancement()
                .parent(greenDagger)
                .display(
                        ChangedAddonItems.CRYSTAL_DAGGER_BLACK.get(),
                        Component.translatable("advancements.obtain_dark_crystal_dagger.title"),
                        Component.translatable("advancements.obtain_dark_crystal_dagger.descr"),
                        null,
                        FrameType.GOAL,
                        true,
                        false,
                        true
                )
                .addCriterion("dagger", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.CRYSTAL_DAGGER_BLACK.get()))
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, ChangedAddonMod.resourceLoc("obtain_dark_crystal_dagger"), existingFileHelper);

        //Depth 4
        Advancement.Builder.advancement()
                .parent(drinkSnepsi)
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
                .addCriterion("snepsi_addictive", new UsedItemAmountTrigger.Instance(ContextAwarePredicate.ANY, ChangedAddonItems.SNEPSI.get(), 100, null))
                .rewards(AdvancementRewards.Builder.experience(3500))
                .save(saver, ChangedAddonMod.resourceLoc("snepsi_addictive"), existingFileHelper);

        Advancement syringeUsed32 = Advancement.Builder.advancement()
                .parent(untfSyringe)
                .display(
                        ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(),
                        Component.translatable("advancements.used_untransfur_syringe_warning_32.title"),
                        Component.translatable("advancements.used_untransfur_syringe_warning_32.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        false,
                        true
                )
                .addCriterion("syringe_use", new UsedItemAmountTrigger.Instance(ContextAwarePredicate.ANY, ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(), 32, null))
                .save(saver, ChangedAddonMod.resourceLoc("used_untransfur_syringe_warning_32"), existingFileHelper);

        Advancement syringeUsed64 = Advancement.Builder.advancement()
                .parent(syringeUsed32)
                .display(
                        ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(),
                        Component.translatable("advancements.used_untransfur_syringe_warning_64.title"),
                        Component.translatable("advancements.used_untransfur_syringe_warning_64.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        false,
                        true
                )
                .addCriterion("syringe_use", new UsedItemAmountTrigger.Instance(ContextAwarePredicate.ANY, ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(), 64, null))
                .save(saver, ChangedAddonMod.resourceLoc("used_untransfur_syringe_warning_64"), existingFileHelper);

        Advancement syringeUsed128 = Advancement.Builder.advancement()
                .parent(syringeUsed64)
                .display(
                        ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(),
                        Component.translatable("advancements.used_untransfur_syringe_warning_128.title"),
                        Component.translatable("advancements.used_untransfur_syringe_warning_128.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        false,
                        true
                )
                .addCriterion("syringe_use", new UsedItemAmountTrigger.Instance(ContextAwarePredicate.ANY, ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(), 128, null))
                .save(saver, ChangedAddonMod.resourceLoc("used_untransfur_syringe_warning_128"), existingFileHelper);

        Advancement.Builder.advancement()
                .parent(syringeUsed128)
                .display(
                        ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(),
                        Component.translatable("advancements.over_dose.title"),
                        Component.translatable("advancements.over_dose.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("syringe_use", new UsedItemAmountTrigger.Instance(ContextAwarePredicate.ANY, ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(), 132, null))
                .save(saver, ChangedAddonMod.resourceLoc("over_dose"), existingFileHelper);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        CompletableFuture<?> normalAdvancements = super.run(pOutput);
        CompletableFuture<?> customAdvancements = writeCustomAdvancements(pOutput);
        return CompletableFuture.allOf(normalAdvancements, customAdvancements);
    }

    protected CompletableFuture<?> writeCustomAdvancements(CachedOutput cache) {
        advancementWrite.write(cache, output, ChangedAddonMod.resourceLoc("rock_fish"), Advancement.Builder.recipeAdvancement()
                .parent(ResourceLocation.parse("minecraft:changed/aquatic_swimming"))
                .display(
                        Items.LAVA_BUCKET,
                        Component.translatable("advancements.main.rock_fish.title"),
                        Component.translatable("advancements.main.rock_fish.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("lava_swimming", new LavaSwimmingTrigger.Instance(ContextAwarePredicate.ANY))
                .rewards(AdvancementRewards.Builder.experience(550)));

        advancementWrite.write(cache, output, ChangedAddonMod.resourceLoc("wolfy_transfur"), Advancement.Builder.recipeAdvancement()
                .parent(ResourceLocation.parse("minecraft:changed/transfur_dark"))
                .display(
                        ChangedItems.DARK_LATEX_MASK.get(),
                        Component.translatable("advancements.wolfy_transfur.title"),
                        Component.translatable("advancements.wolfy_transfur.descr"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("tf", DynamicTransfurTrigger.TriggerInstance.transfurredInto(DynamicTransfurPredicate.builder().form(ChangedAddonTransfurVariants.WOLFY.get()).build()))
                .rewards(AdvancementRewards.Builder.experience(250)));

        advancementWrite.write(cache, output, ChangedAddonMod.resourceLoc("organic_transfur"), Advancement.Builder.advancement()
                .parent(ResourceLocation.parse("minecraft:changed/transfur"))
                .display(
                        Items.BONE,
                        Component.translatable("advancements.organic_transfur.title"),
                        Component.translatable("advancements.organic_transfur.descr"),
                        null,
                        FrameType.GOAL,
                        true,  // show_toast
                        true, // announce_to_chat
                        false   // hidden
                )
                .addCriterion(
                        "organic_transfur",
                        DynamicTransfurTrigger.TriggerInstance.transfurredInto(DynamicTransfurPredicate.builder().entityTypeTag(ChangedTags.EntityTypes.LATEX).inverted().build())
                ));

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


        ResourceLocation obtainImpureAmmoniaId = ChangedAddonMod.resourceLoc("obtain_impure_ammonia");
        obtainImpureAmmonia.parent(ADVANCEMENT_ROOT)
                .display(
                        ChangedAddonItems.IMPURE_AMMONIA.get(),
                        Component.translatable("advancements.obtain_impure_ammonia.title"),
                        Component.translatable("advancements.obtain_impure_ammonia.descr"),
                        null, // background (null for non-root advancements)
                        FrameType.GOAL,
                        true, // show_toast
                        true, // announce_to_chat
                        true  // hidden
                )
                .addCriterion("obtain_item", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.IMPURE_AMMONIA.get()));

        ResourceLocation obtainAmmoniaParticlesId = ChangedAddonMod.resourceLoc("obtain_ammonia_particles");
        obtainAmmoniaParticles.parent(obtainImpureAmmoniaId) // Parent updated to obtain_impure_ammonia
                .display(
                        ChangedAddonItems.AMMONIA_PARTICLE.get(),
                        Component.translatable("advancements.obtain_ammonia_particles.title"),
                        Component.translatable("advancements.obtain_ammonia_particles.descr"),
                        null,
                        FrameType.GOAL,
                        true, // show_toast
                        true, // announce_to_chat
                        true  // hidden
                )
                .addCriterion("obtain_item", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.AMMONIA_PARTICLE.get()));


        ResourceLocation obtainCompressedAmmoniaId = ChangedAddonMod.resourceLoc("obtain_compressed_ammonia");
        obtainCompressedAmmonia.parent(obtainAmmoniaParticlesId)
                .display(
                        ChangedAddonItems.AMMONIA_COMPRESSED.get(),
                        Component.translatable("advancements.obtain_compressed_ammonia.title"),
                        Component.translatable("advancements.obtain_compressed_ammonia.descr"),
                        null,
                        FrameType.GOAL,
                        true, // show_toast
                        true, // announce_to_chat
                        false // hidden
                )
                .addCriterion("obtain_item", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.AMMONIA_COMPRESSED.get()));

        ResourceLocation obtainAmmoniaId = ChangedAddonMod.resourceLoc("obtain_ammonia");
        obtainAmmonia.parent(obtainCompressedAmmoniaId)
                .display(
                        ChangedAddonItems.AMMONIA.get(),
                        Component.translatable("advancements.obtain_ammonia.title"),
                        Component.translatable("advancements.obtain_ammonia.descr"),
                        null,
                        FrameType.GOAL,
                        true, // show_toast
                        true, // announce_to_chat
                        false // hidden
                )
                .addCriterion("obtain_item", InventoryChangeTrigger.TriggerInstance.hasItems(ChangedAddonItems.AMMONIA.get()));

        advancementWrite.write(cache, output, obtainImpureAmmoniaId, obtainImpureAmmonia);
        advancementWrite.write(cache, output, obtainAmmoniaId, obtainAmmonia);
        advancementWrite.write(cache, output, obtainCompressedAmmoniaId, obtainCompressedAmmonia);
        advancementWrite.write(cache, output, obtainAmmoniaParticlesId, obtainAmmoniaParticles);

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
}

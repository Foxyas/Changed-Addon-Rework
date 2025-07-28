package net.foxyas.changedaddon.datagen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

import static net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants.*;
import static net.ltxprogrammer.changed.init.ChangedTransfurVariants.*;

public class TFTagsProvider extends TagsProvider<TransfurVariant<?>> {

    public TFTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, new Registry<>(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), Lifecycle.stable()) {
            @Override
            public @Nullable ResourceLocation getKey(@NotNull TransfurVariant<?> p_123006_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().getKey(p_123006_);
            }

            @Override
            public @NotNull Optional<ResourceKey<TransfurVariant<?>>> getResourceKey(@NotNull TransfurVariant<?> p_123008_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().getResourceKey(p_123008_);
            }

            @Override
            public int getId(@Nullable TransfurVariant<?> p_122977_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().getID(p_122977_);
            }

            @Override
            public @Nullable TransfurVariant<?> get(@Nullable ResourceKey<TransfurVariant<?>> p_122980_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().getRaw(p_122980_.location());
            }

            @Override
            public @Nullable TransfurVariant<?> get(@Nullable ResourceLocation p_123002_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().getRaw(p_123002_);
            }

            @Override
            public @NotNull Lifecycle lifecycle(@NotNull TransfurVariant<?> p_123012_) {
                return Lifecycle.stable();
            }

            @Override
            public @NotNull Lifecycle elementsLifecycle() {
                return Lifecycle.stable();
            }

            @Override
            public @NotNull Set<ResourceLocation> keySet() {
                return ChangedRegistry.TRANSFUR_VARIANT.get().getKeys();
            }

            @Override
            public @NotNull Set<Map.Entry<ResourceKey<TransfurVariant<?>>, TransfurVariant<?>>> entrySet() {
                return Set.of();
            }

            @Override
            public @NotNull Optional<Holder<TransfurVariant<?>>> getRandom(@NotNull Random p_205998_) {
                return Optional.empty();
            }

            @Override
            public boolean containsKey(@NotNull ResourceLocation p_123011_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().containsKey(p_123011_);
            }

            @Override
            public boolean containsKey(@NotNull ResourceKey<TransfurVariant<?>> p_175475_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().containsKey(p_175475_.location());
            }

            @Override
            public @NotNull Registry<TransfurVariant<?>> freeze() {
                return this;
            }

            @Override
            public @NotNull Holder<TransfurVariant<?>> getOrCreateHolder(@NotNull ResourceKey<TransfurVariant<?>> p_206057_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().getHolder(p_206057_).get();
            }

            @Override
            public Holder.@NotNull Reference<TransfurVariant<?>> createIntrusiveHolder(@NotNull TransfurVariant<?> p_206068_) {
                return Holder.Reference.createStandAlone(this, ResourceKey.create(key(), ChangedRegistry.TRANSFUR_VARIANT.get().getKey(p_206068_)));
            }

            @Override
            public @NotNull Optional<Holder<TransfurVariant<?>>> getHolder(int p_206051_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().getHolder(ChangedRegistry.TRANSFUR_VARIANT.get().getKey(p_206051_));
            }

            @Override
            public @NotNull Optional<Holder<TransfurVariant<?>>> getHolder(@NotNull ResourceKey<TransfurVariant<?>> p_206050_) {
                return ChangedRegistry.TRANSFUR_VARIANT.get().getHolder(p_206050_);
            }

            @Override
            public @NotNull Stream<Holder.Reference<TransfurVariant<?>>> holders() {
                return Stream.empty();
            }

            @Override
            public @NotNull Optional<HolderSet.Named<TransfurVariant<?>>> getTag(@NotNull TagKey<TransfurVariant<?>> p_206052_) {
                return Optional.empty();
            }

            @Override
            public HolderSet.@NotNull Named<TransfurVariant<?>> getOrCreateTag(@NotNull TagKey<TransfurVariant<?>> p_206045_) {
                return new HolderSet.Named<>(this, TagKey.create(key(), new ResourceLocation("null")));
            }

            @Override
            public Stream<Pair<TagKey<TransfurVariant<?>>, HolderSet.Named<TransfurVariant<?>>>> getTags() {
                return Stream.empty();
            }

            @Override
            public @NotNull Stream<TagKey<TransfurVariant<?>>> getTagNames() {
                return Stream.empty();
            }

            @Override
            public boolean isKnownTagName(@NotNull TagKey<TransfurVariant<?>> p_205983_) {
                return false;
            }

            @Override
            public void resetTags() {

            }

            @Override
            public void bindTags(Map<TagKey<TransfurVariant<?>>, List<Holder<TransfurVariant<?>>>> p_205997_) {

            }

            @Override
            public @Nullable TransfurVariant<?> byId(int p_122651_) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public @NotNull Iterator<TransfurVariant<?>> iterator() {
                return ObjectIterators.EMPTY_ITERATOR;
            }
        }, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ChangedAddonTags.TransfurTypes.ABLE_TO_CARRY).add(
                EXP6.get());

        tag(ChangedAddonTags.TransfurTypes.CAUSE_FREEZING).add(
                LUMINARCTIC_LEOPARD.get(),
                FEMALE_LUMINARCTIC_LEOPARD.get());

        tag(ChangedAddonTags.TransfurTypes.GLOWING).add(
                KET_EXPERIMENT_009.get(),
                KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(),
                EXPERIMENT_10.get(),
                EXPERIMENT_10_BOSS.get());

        tag(ChangedAddonTags.TransfurTypes.HAS_CLAWS).add(
                LATEX_KITSUNE_FEMALE.get(),
                LATEX_KITSUNE_MALE.get(),
                FENGQI_WOLF.get());

        //Type of Transfurs
        tag(ChangedAddonTags.TransfurTypes.AQUATIC_LIKE).add(LATEX_DRAGON_SNEP_SHARK.get());

        tag(ChangedAddonTags.TransfurTypes.CAT_LIKE).add(
                LATEX_HYPNO_CAT.get(),
                LATEX_WATERMELON_CAT.get(),
                LATEX_MING_CAT.get(),
                EXPERIMENT_10.get(),
                MIRROR_WHITE_TIGER.get(),
                LUMINARCTIC_LEOPARD.get(),
                FEMALE_LUMINARCTIC_LEOPARD.get(),
                LYNX.get(),
                SNEPSI_LEOPARD.get(),
                HIMALAYAN_CRYSTAL_GAS_CAT_FEMALE.get(),
                HIMALAYAN_CRYSTAL_GAS_CAT_MALE.get(),
                LATEX_CALICO_CAT.get());

        tag(ChangedAddonTags.TransfurTypes.DRAGON_LIKE).add(LATEX_DRAGON_SNEP_SHARK.get());

        tag(ChangedAddonTags.TransfurTypes.FOX_LIKE).add(
                EXP1_MALE.get(),
                EXP1_FEMALE.get(),
                ADDON_LATEX_SNOW_FOX_MALE.get(),
                ADDON_LATEX_SNOW_FOX_FEMALE.get(),
                KET_EXPERIMENT_009.get(),
                KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(),
                FOXTA_FOXY.get(),
                VOID_FOX.get(),
                HAYDEN_FENNEC_FOX.get(),
                LATEX_KITSUNE_MALE.get(),
                LATEX_KITSUNE_FEMALE.get(),
                LATEX_FENNEC_FOX.get());

        tag(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE).add(
                LATEX_SNOW_LEOPARD_FEMALE.get(),
                LATEX_SNOW_LEOPARD_MALE.get(),
                LATEX_RED_PANDA.get(),
                LATEX_RACCOON.get(),
                EXP2_FEMALE.get(),
                EXP2_MALE.get(),
                SNOW_LEOPARD_PARTIAL.get(),
                EXP6.get(),
                LATEX_SNEP.get(),
                LATEX_SNEP_FERAL_FORM.get(),
                LUMINARCTIC_LEOPARD.get(),
                FEMALE_LUMINARCTIC_LEOPARD.get(),
                LYNX.get(),
                SNEPSI_LEOPARD.get(),
                ORGANIC_SNOW_LEOPARD_FEMALE.get(),
                ORGANIC_SNOW_LEOPARD_MALE.get(),
                LATEX_DRAGON_SNEP_SHARK.get());

        tag(ChangedAddonTags.TransfurTypes.SHARK_LIKE).add(
                LATEX_SHARK.get(),
                LATEX_SHARK_FUSION_FEMALE.get(),
                LATEX_SHARK_FUSION_MALE.get(),
                LATEX_TIGER_SHARK.get(),
                LATEX_ORCA.get(),
                LATEX_MANTA_RAY_FEMALE.get(),
                LATEX_MANTA_RAY_MALE.get(),
                LATEX_SQUID_TIGER_SHARK.get());
        tag(ChangedAddonTags.TransfurTypes.WOLF_LIKE).add(
                LATEX_KEON_WOLF.get(),
                DARK_LATEX_WOLF_FEMALE.get(),
                DARK_LATEX_WOLF_MALE.get(),
                WHITE_LATEX_WOLF_MALE.get(),
                WHITE_LATEX_WOLF_FEMALE.get(),
                LATEX_BLUE_WOLF.get(),
                GAS_WOLF.get(),
                LATEX_PURPLE_FOX.get(),
                CRYSTAL_WOLF.get(),
                CRYSTAL_WOLF_HORNED.get(),
                SNIPER_DOG.get(),
                LATEX_SQUID_DOG_MALE.get(),
                LATEX_SQUID_DOG_FEMALE.get(),
                LATEX_BENIGN_WOLF.get(),
                WHITE_WOLF_MALE.get(),
                WHITE_WOLF_FEMALE.get(),
                FENGQI_WOLF.get(),
                LATEX_SQUID_TIGER_SHARK.get());

        //Diets
        tag(ChangedAddonTags.TransfurTypes.AQUATIC_DIET);
        tag(ChangedAddonTags.TransfurTypes.SHARK_DIET);
        tag(ChangedAddonTags.TransfurTypes.CAT_DIET).add(
                EXP6.get(),
                LYNX.get(),
                SNEPSI_LEOPARD.get());
        tag(ChangedAddonTags.TransfurTypes.DRAGON_DIET);
        tag(ChangedAddonTags.TransfurTypes.FOX_DIET).add(
                EXP1_MALE.get(),
                EXP1_FEMALE.get(),
                ADDON_LATEX_SNOW_FOX_FEMALE.get(),
                ADDON_LATEX_SNOW_FOX_MALE.get(),
                KET_EXPERIMENT_009.get(),
                KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(),
                FOXTA_FOXY.get(),
                VOID_FOX.get());
        tag(ChangedAddonTags.TransfurTypes.SPECIAL_DIET).add(EXP6.get());
        tag(ChangedAddonTags.TransfurTypes.SWEET_DIET).add(
                HAYDEN_FENNEC_FOX.get(),
                KET_EXPERIMENT_009.get(),
                KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get());
        tag(ChangedAddonTags.TransfurTypes.WOLF_DIET);
        tag(ChangedAddonTags.TransfurTypes.NO_DIET).add(REYN.get());
    }

    @Override
    public @NotNull String getName() {
        return "Transfur Type Tags";
    }
}

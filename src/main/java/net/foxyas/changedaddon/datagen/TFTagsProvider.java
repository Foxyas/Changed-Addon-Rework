package net.foxyas.changedaddon.datagen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
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
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants.*;

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
        tag(ChangedAddonTags.TransfurTypes.ABLE_TO_CARRY).add(EXP6.get());
        tag(ChangedAddonTags.TransfurTypes.CAUSE_FREEZING).add(LUMINARCTIC_LEOPARD.get(), FEMALE_LUMINARCTIC_LEOPARD.get());
        tag(ChangedAddonTags.TransfurTypes.GLOWING).add(KET_EXPERIMENT_009.get(), KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(), EXPERIMENT_10.get(), EXPERIMENT_10_BOSS.get());
        tag(ChangedAddonTags.TransfurTypes.HAS_CLAWS).add(LATEX_KITSUNE_FEMALE.get(), LATEX_KITSUNE_MALE.get(), FENGQI_WOLF.get());


        tag(ChangedAddonTags.TransfurTypes.AQUATIC_LIKE).add(LATEX_DRAGON_SNEP_SHARK.get());

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.SHARK_LIKE), var -> var.getRegistryName().getPath().contains("shark")).add(ChangedTransfurVariants.LATEX_ORCA.get(), ChangedTransfurVariants.LATEX_MANTA_RAY_FEMALE.get(), ChangedTransfurVariants.LATEX_MANTA_RAY_MALE.get());

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.CAT_LIKE), var -> var.getRegistryName().getPath().contains("cat")).add(EXPERIMENT_10.get(), MIRROR_WHITE_TIGER.get(), LUMINARCTIC_LEOPARD.get(), FEMALE_LUMINARCTIC_LEOPARD.get(), LYNX.get(), SNEPSI_LEOPARD.get());

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.DRAGON_LIKE), var -> var.getRegistryName().getPath().contains("dragon"));

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.FOX_LIKE), var -> var.getRegistryName().getPath().contains("fox")).add(EXP1_MALE.get(), EXP1_FEMALE.get(), KET_EXPERIMENT_009.get(), KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(), LATEX_KITSUNE_MALE.get(), LATEX_KITSUNE_FEMALE.get());

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE), var -> var.getRegistryName().getPath().contains("leopard")).add(ChangedTransfurVariants.LATEX_RED_PANDA.get(), ChangedTransfurVariants.LATEX_RACCOON.get(), EXP2_FEMALE.get(), EXP2_MALE.get(), EXP6.get(), LATEX_SNEP.get(), LATEX_SNEP_FERAL_FORM.get(), LYNX.get(), LATEX_DRAGON_SNEP_SHARK.get());

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.WOLF_LIKE), var -> {
            String path = var.getRegistryName().getPath();
            return path.contains("dog") || path.contains("wolf");
        }).add(ChangedTransfurVariants.LATEX_PURPLE_FOX.get(), LATEX_SQUID_TIGER_SHARK.get());



        tag(ChangedAddonTags.TransfurTypes.AQUATIC_DIET)
                .addTag(ChangedAddonTags.TransfurTypes.AQUATIC_LIKE);

        tag(ChangedAddonTags.TransfurTypes.SHARK_DIET)
                .addTag(ChangedAddonTags.TransfurTypes.SHARK_LIKE);

        tag(ChangedAddonTags.TransfurTypes.CAT_DIET)
                .addTags(ChangedAddonTags.TransfurTypes.CAT_LIKE, ChangedAddonTags.TransfurTypes.LEOPARD_LIKE);

        tag(ChangedAddonTags.TransfurTypes.DRAGON_DIET)
                .addTag(ChangedAddonTags.TransfurTypes.DRAGON_LIKE);

        tag(ChangedAddonTags.TransfurTypes.FOX_DIET)
                .addTag(ChangedAddonTags.TransfurTypes.FOX_LIKE);

        tag(ChangedAddonTags.TransfurTypes.SPECIAL_DIET)
                .add(EXP6.get(), WOLFY.get(), ADDON_PURO_KIND_FEMALE.get(), ADDON_PURO_KIND_MALE.get());

        tag(ChangedAddonTags.TransfurTypes.SWEET_DIET)
                .add(HAYDEN_FENNEC_FOX.get(), KET_EXPERIMENT_009.get(), KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get());

        tag(ChangedAddonTags.TransfurTypes.WOLF_DIET)
                .addTag(ChangedAddonTags.TransfurTypes.WOLF_LIKE).add(BLUE_LIZARD.get());

        tag(ChangedAddonTags.TransfurTypes.NO_DIET).add(REYN.get());
    }

    protected TagAppender<TransfurVariant<?>> addAllMatching(TagAppender<TransfurVariant<?>> tag, Predicate<TransfurVariant<?>> predicate){
        for(TransfurVariant<?> var : ChangedRegistry.TRANSFUR_VARIANT.get().getValues()){
            if(predicate.test(var)) tag.add(var);
        }

        return tag;
    }

    @Override
    public @NotNull String getName() {
        return "Transfur Type Tags";
    }
}

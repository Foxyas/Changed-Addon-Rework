package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants.*;

public class TFTagsProvider extends TagsProvider<TransfurVariant<?>> {

    public TFTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), pLookupProvider, ChangedAddonMod.MODID, existingFileHelper);
    }

    private static <T extends ChangedEntity> ResourceKey<TransfurVariant<?>> cast(RegistryObject<TransfurVariant<T>> key) {
        return (ResourceKey<TransfurVariant<?>>) (Object) key.getKey();
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(ChangedAddonTags.TransfurTypes.ABLE_TO_CARRY).add(cast(EXP6), cast(LUMINARA_FLOWER_BEAST));
        tag(ChangedAddonTags.TransfurTypes.CAUSE_FREEZING).add(cast(LUMINARCTIC_LEOPARD_MALE), cast(LUMINARCTIC_LEOPARD_FEMALE));
        tag(ChangedAddonTags.TransfurTypes.GLOWING_VARIANTS).add(cast(EXPERIMENT_009), cast(EXPERIMENT_009_BOSS), cast(EXPERIMENT_10), cast(EXPERIMENT_10_BOSS));
        tag(ChangedAddonTags.TransfurTypes.HAS_CLAWS).add(
                cast(LATEX_KITSUNE_FEMALE),
                cast(LATEX_KITSUNE_MALE),
                cast(FENGQI_WOLF),
                cast(LUMINARA_FLOWER_BEAST),
                cast(PROTOGEN_0SENIA0),
                cast(LATEX_KAYLA_SHARK)
        );


        tag(ChangedAddonTags.TransfurTypes.AQUATIC_LIKE).add(cast(LATEX_DRAGON_SNEP_SHARK));

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.SHARK_LIKE), var -> var.getFormId().getPath().contains("shark")).add(cast(ChangedTransfurVariants.LATEX_ORCA),
                cast(ChangedTransfurVariants.LATEX_MANTA_RAY_FEMALE),
                cast(ChangedTransfurVariants.LATEX_MANTA_RAY_MALE));

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.CAT_LIKE), var -> var.getFormId().getPath().contains("cat")).add(cast(EXPERIMENT_10),
                cast(MIRROR_WHITE_TIGER),
                cast(LUMINARCTIC_LEOPARD_MALE),
                cast(LUMINARCTIC_LEOPARD_FEMALE),
                cast(LYNX),
                cast(SNEPSI_LEOPARD),
                cast(LATEX_CHEETAH_MALE),
                cast(LATEX_CHEETAH_FEMALE),
                cast(ChangedTransfurVariants.LATEX_STIGER),
                cast(ChangedTransfurVariants.LATEX_WHITE_TIGER));

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.DRAGON_LIKE), var -> var.getFormId().getPath().contains("dragon")).add(cast(LUMINARA_FLOWER_BEAST));

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.FOX_LIKE), var -> var.getFormId().getPath().contains("fox")).add(cast(EXP1_MALE),
                cast(EXP1_FEMALE),
                cast(EXPERIMENT_009),
                cast(EXPERIMENT_009_BOSS),
                cast(LATEX_KITSUNE_MALE),
                cast(LATEX_KITSUNE_FEMALE));

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE), var -> var.getFormId().getPath().contains("leopard"))
                .add(cast(ChangedTransfurVariants.LATEX_RED_PANDA),
                        cast(ChangedTransfurVariants.LATEX_RACCOON),
                        cast(EXP2_FEMALE),
                        cast(EXP2_MALE),
                        cast(EXP6),
                        cast(LATEX_SNEP),
                        cast(LATEX_SNEP_FERAL_FORM),
                        cast(LYNX),
                        cast(LATEX_DRAGON_SNEP_SHARK),
                        cast(BOREALIS_MALE),
                        cast(BOREALIS_FEMALE));

        addAllMatching(tag(ChangedAddonTags.TransfurTypes.WOLF_LIKE), var -> {
            String path = var.getFormId().getPath();
            return path.contains("dog") || path.contains("wolf");
        }).add(cast(ChangedTransfurVariants.LATEX_PURPLE_FOX), cast(LATEX_SQUID_TIGER_SHARK));

        tag(ChangedAddonTags.TransfurTypes.SPIDER_LIKE)
                .add(cast(ChangedTransfurVariants.LATEX_STIGER));


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
                .add(cast(EXP6), cast(WOLFY), cast(PURO_KIND_FEMALE), cast(PURO_KIND_MALE));

        tag(ChangedAddonTags.TransfurTypes.SWEET_DIET)
                .add(cast(HAYDEN_FENNEC_FOX), cast(EXPERIMENT_009), cast(EXPERIMENT_009_BOSS));

        tag(ChangedAddonTags.TransfurTypes.WOLF_DIET)
                .addTag(ChangedAddonTags.TransfurTypes.WOLF_LIKE).add(cast(BLUE_LIZARD));

        tag(ChangedAddonTags.TransfurTypes.NO_DIET).add(cast(REYN));
    }

    protected TagAppender<TransfurVariant<?>> addAllMatching(TagAppender<TransfurVariant<?>> tag, Predicate<TransfurVariant<?>> predicate) {
        for (Map.Entry<ResourceKey<TransfurVariant<?>>, TransfurVariant<?>> entry : ChangedRegistry.TRANSFUR_VARIANT.get().getEntries()) {
            if (predicate.test(entry.getValue())) tag.add(entry.getKey());
        }

        return tag;
    }

    @Override
    public @NotNull String getName() {
        return "Transfur Type Tags";
    }
}

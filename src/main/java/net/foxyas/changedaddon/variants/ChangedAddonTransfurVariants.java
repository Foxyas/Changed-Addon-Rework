package net.foxyas.changedaddon.variants;

import net.foxyas.changedaddon.registers.ChangedAddonEntitys;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.ChangedAddonAbilities;
import net.foxyas.changedaddon.entity.*;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.UseItemMode;
import net.ltxprogrammer.changed.entity.variant.GenderedPair;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonTransfurVariants {
    //public static UseItemMode ABO = UseItemMode.create("ABO",false,true,true,true,true);
    //this is For Not Show The Hot Bar
    //.itemUseMode(ABO)

    public static class TransfurVariantTags {
        public static final TagKey<TransfurVariant<?>> WOLF_LIKE = create("wolf_like");
        public static final TagKey<TransfurVariant<?>> SHARK_LIKE = create("shark_like");
        public static final TagKey<TransfurVariant<?>> CAT_LIKE = create("cat_like");
        public static final TagKey<TransfurVariant<?>> LEOPARD_LIKE = create("leopard_like");

        public TransfurVariantTags() {
        }

        private static TagKey<TransfurVariant<?>> create(String name) {
            return TagKey.create(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(),
                    new ResourceLocation("changed_addon",name));
        }
    }

    public static final DeferredRegister<TransfurVariant<?>> REGISTRY = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(ChangedAddonMod.MODID);

    public static List<TransfurVariant<?>> getRemovedVariantsList() {
        return List.of(REYN.get(), KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(), EXPERIMENT_10_BOSS.get(), LATEX_SNEP_FERAL_FORM.get(), LUMINARCTIC_LEOPARD.get());
    }

    public static final RegistryObject<TransfurVariant<PuroKindEntity>> ADDON_PURO_KIND_MALE = register("form_puro_kind/male",TransfurVariant.Builder.of(ChangedAddonModEntities.PURO_KIND)
            .transfurMode(TransfurMode.REPLICATION).faction(LatexType.DARK_LATEX).addAbility(ChangedAddonAbilities.CARRY).scares(List.of()));

    public static final RegistryObject<TransfurVariant<PuroKindFemaleEntity>> ADDON_PURO_KIND_FEMALE = register("form_puro_kind/female",TransfurVariant.Builder.of(ChangedAddonModEntities.PURO_KIND_FEMALE)
            .transfurMode(TransfurMode.ABSORPTION).faction(LatexType.DARK_LATEX).addAbility(ChangedAddonAbilities.CARRY).scares(List.of()));

    public static final RegistryObject<TransfurVariant<SnowLeopardMaleOrganicEntity>> ORGANIC_SNOW_LEOPARD_MALE = register("form_biosynth_snow_leopard/male",
            () -> TransfurVariant.Builder.of(/*LATEX_SNOW_LEOPARD_MALE.get(),*/ChangedAddonModEntities.SNOW_LEOPARD_MALE_ORGANIC)
                    .transfurMode(TransfurMode.REPLICATION).nightVision().breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAddonAbilities.CARRY).addAbility(ChangedAddonAbilities.TURN_FERAL_SNEP).scares(List.of(Creeper.class)));

    public static final RegistryObject<TransfurVariant<SnowLeopardFemaleOrganicEntity>> ORGANIC_SNOW_LEOPARD_FEMALE = register("form_biosynth_snow_leopard/female",
            () -> TransfurVariant.Builder.of(/*LATEX_SNOW_LEOPARD_FEMALE.get(),*/ChangedAddonModEntities.SNOW_LEOPARD_FEMALE_ORGANIC)
                    .transfurMode(TransfurMode.ABSORPTION).nightVision().breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAddonAbilities.CARRY).addAbility(ChangedAddonAbilities.TURN_FERAL_SNEP).scares(List.of(Creeper.class)));

    public static final RegistryObject<TransfurVariant<LatexSnowFoxEntity>> ADDON_LATEX_SNOW_FOX_MALE = register("form_latex_snow_fox/male",
            () -> TransfurVariant.Builder.of(/*WHITE_LATEX_WOLF_MALE.get(),*/ChangedAddonModEntities.LATEX_SNOW_FOX)
                    .nightVision());

    public static final RegistryObject<TransfurVariant<LatexSnowFoxFemaleEntity>> ADDON_LATEX_SNOW_FOX_FEMALE = register("form_latex_snow_fox/female",
            () -> TransfurVariant.Builder.of(/*WHITE_LATEX_WOLF_FEMALE.get(),*/ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE)
                    .nightVision());

    public static final RegistryObject<TransfurVariant<DazedEntity>> DAZED_LATEX = register("form_dazed_latex",TransfurVariant.Builder.of(ChangedAddonModEntities.DAZED)
            .transfurMode(TransfurMode.ABSORPTION).addAbility(ChangedAddonAbilities.DAZED_PUDDLE_ABILITY).scares(List.of()).nightVision());

    public static final RegistryObject<TransfurVariant<BunyEntity>> BUNY = register("form_buny",TransfurVariant.Builder.of(ChangedAddonModEntities.BUNY)
            .jumpStrength(1.5F).reducedFall().transfurMode(TransfurMode.ABSORPTION).scares(List.of()));

    public static final RegistryObject<TransfurVariant<MirrorWhiteTigerEntity>> MIRROR_WHITE_TIGER = register("form_mirror_white_tiger_female",
            () -> TransfurVariant.Builder.of(/*LATEX_WHITE_TIGER.get(),*/ChangedAddonModEntities.MIRROR_WHITE_TIGER)
                    .stepSize(0.7F).reducedFall().breatheMode(TransfurVariant.BreatheMode.NORMAL).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<WolfyEntity>> WOLFY = register("form_wolfy",TransfurVariant.Builder.of(ChangedAddonModEntities.WOLFY)
            .stepSize(0.7F).breatheMode(TransfurVariant.BreatheMode.NORMAL).transfurMode(TransfurMode.NONE).scares(List.of()).nightVision());

    public static final RegistryObject<TransfurVariant<SnowLeopardPartialEntity>> SNOW_LEOPARD_PARTIAL = register("form_latex_snow_leopard_partial",TransfurVariant.Builder.of(ChangedAddonEntitys.SNOW_LEOPARD_PARTIAL)
            .stepSize(0.7F).jumpStrength(1.3F).reducedFall().breatheMode(TransfurVariant.BreatheMode.NORMAL).scares(List.of()).nightVision());

    public static final RegistryObject<TransfurVariant<ReynEntity>> REYN = register("form_reyn",TransfurVariant.Builder.of(ChangedAddonEntitys.REYN)
            .stepSize(0.7F).jumpStrength(1.0f).reducedFall().breatheMode(TransfurVariant.BreatheMode.NORMAL).scares(List.of()));

    //Experiments
    public static final RegistryObject<TransfurVariant<Exp1MaleEntity>> EXP1_MALE = register("form_exp1/male",
            () -> TransfurVariant.Builder.of(/*WHITE_LATEX_WOLF_MALE.get(),*/ChangedAddonModEntities.EXP_1_MALE)
                    .nightVision().extraJumps(2)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_PULSE)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_HOLD)
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .transfurMode(TransfurMode.NONE));

    public static final RegistryObject<TransfurVariant<Exp1FemaleEntity>> EXP1_FEMALE = register("form_exp1/female",
            () -> TransfurVariant.Builder.of(/*WHITE_LATEX_WOLF_FEMALE.get(),*/ChangedAddonModEntities.EXP_1_FEMALE)
                    .nightVision().extraJumps(2)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_PULSE)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_HOLD)
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .transfurMode(TransfurMode.NONE));

    public static final RegistryObject<TransfurVariant<Exp2MaleEntity>> EXP2_MALE = register("form_exp2/male",
            () -> TransfurVariant.Builder.of(/*LATEX_SNOW_LEOPARD_MALE.get(),*/ChangedAddonModEntities.EXP_2_MALE)
                    .transfurMode(TransfurMode.REPLICATION).breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F)
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<Exp2FemaleEntity>> EXP2_FEMALE = register("form_exp2/female",
            () -> TransfurVariant.Builder.of(/*LATEX_SNOW_LEOPARD_FEMALE.get(),*/ChangedAddonModEntities.EXP_2_FEMALE)
                    .transfurMode(TransfurMode.ABSORPTION).breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F)
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<Exp6Entity>> EXP6 = register("form_exp6",TransfurVariant.Builder.of(ChangedAddonModEntities.EXP_6)
            .reducedFall().jumpStrength(1.05F).abilities(List.of(
                    entityType -> ChangedAddonAbilities.CARRY.get(),
                    entityType -> ChangedAddonAbilities.DISSOLVE.get()
            )).scares(List.of(Creeper.class)).transfurMode(TransfurMode.ABSORPTION).nightVision());

    public static final RegistryObject<TransfurVariant<LatexSnepEntity>> LATEX_SNEP = register("form_latex_snep",
            () -> TransfurVariant.Builder.of(ChangedAddonModEntities.LATEX_SNEP)
                    .transfurMode(TransfurMode.NONE).breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .itemUseMode(UseItemMode.MOUTH).reducedFall().jumpStrength(1.5F).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<LatexSnepEntity>> LATEX_SNEP_FERAL_FORM = register("form_latex_snep_feral",
            () -> TransfurVariant.Builder.of(ChangedAddonModEntities.LATEX_SNEP)
                    .transfurMode(TransfurMode.NONE).breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .addAbility(ChangedAddonAbilities.TURN_FERAL_SNEP)
                    .itemUseMode(UseItemMode.MOUTH).reducedFall().jumpStrength(1.3F).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<LuminarcticLeopardEntity>> LUMINARCTIC_LEOPARD  = register("form_luminarctic_leopard",
            () -> TransfurVariant.Builder.of(/*LATEX_SNOW_LEOPARD_FEMALE.get(),*/ChangedAddonModEntities.LUMINARCTIC_LEOPARD)
                    .transfurMode(TransfurMode.ABSORPTION).breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.35F)
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAbilities.HYPNOSIS)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .scares(List.of(Creeper.class)).nightVision().doubleJump());


    public static final RegistryObject<TransfurVariant<KetExperiment009Entity>> KET_EXPERIMENT_009 = register("form_ket_experiment009",TransfurVariant.Builder.of(ChangedAddonModEntities.KET_EXPERIMENT_009)
            .reducedFall().jumpStrength(1.4F).abilities(List.of(
                    entityType -> ChangedAddonAbilities.THUNDERBOLT.get(),
                    entityType -> ChangedAddonAbilities.SHOCKWAVE.get()
            )).transfurMode(TransfurMode.ABSORPTION).nightVision());

    public static final RegistryObject<TransfurVariant<Experiment10Entity>> EXPERIMENT_10 = register("form_experiment_10",TransfurVariant.Builder.of(ChangedAddonModEntities.EXPERIMENT_10)
            .reducedFall().jumpStrength(1.5F).abilities(List.of(
                    entityType -> ChangedAddonAbilities.WITHER_WAVE.get(),
                    entityType -> ChangedAbilities.HYPNOSIS.get()
            )).transfurMode(TransfurMode.ABSORPTION)
            .scares(List.of(EnderMan.class, WitherSkeleton.class,Creeper.class, AbstractGolem.class, Piglin.class, PiglinBrute.class))
            .nightVision());


    //Boss Transfurs
    public static UseItemMode Ket_Boss = UseItemMode.create("Ket_Boss",false,true,true,true,true);
    public static final RegistryObject<TransfurVariant<KetExperiment009BossEntity>> KET_EXPERIMENT_009_BOSS_LATEX_VARIANT = register("form_ket_experiment009_boss",TransfurVariant.Builder.of(ChangedAddonModEntities.KET_EXPERIMENT_009_BOSS)
            .reducedFall().jumpStrength(1.75F)
            .abilities(List.of(
                    entityType -> ChangedAddonAbilities.THUNDERBOLT.get(),
                    entityType -> ChangedAddonAbilities.SHOCKWAVE.get()
            )).transfurMode(TransfurMode.ABSORPTION)
            .scares(List.of(Zombie.class,WitherSkeleton.class, AbstractVillager.class, Skeleton.class, AbstractGolem.class))
            .nightVision());

    public static final RegistryObject<TransfurVariant<Experiment10BossEntity>> EXPERIMENT_10_BOSS = register("form_experiment_10_boss",TransfurVariant.Builder.of(ChangedAddonModEntities.EXPERIMENT_10_BOSS)
            .reducedFall().jumpStrength(1.5F).abilities(List.of(
                    entityType -> ChangedAddonAbilities.WITHER_WAVE.get(),
                    entityType -> ChangedAbilities.HYPNOSIS.get()
            )).transfurMode(TransfurMode.ABSORPTION)
            .scares(List.of(EnderMan.class, WitherSkeleton.class,Creeper.class, AbstractGolem.class, Piglin.class, PiglinBrute.class))
            .nightVision());

    public static class Gendered {
        public static final GenderedPair<PuroKindEntity,PuroKindFemaleEntity> ADDON_PURO_KIND =  new GenderedPair<>(ADDON_PURO_KIND_MALE,ADDON_PURO_KIND_FEMALE);
        public static final GenderedPair<SnowLeopardMaleOrganicEntity,SnowLeopardFemaleOrganicEntity> ORGANIC_SNOW_LEOPARD =  new GenderedPair<>(ORGANIC_SNOW_LEOPARD_MALE,ORGANIC_SNOW_LEOPARD_FEMALE);
        public static final GenderedPair<LatexSnowFoxEntity, LatexSnowFoxFemaleEntity> ADDON_LATEX_SNOW_FOX =  new GenderedPair<>(ADDON_LATEX_SNOW_FOX_MALE,ADDON_LATEX_SNOW_FOX_FEMALE);
        public static final GenderedPair<Exp1MaleEntity, Exp1FemaleEntity> EXP1 =  new GenderedPair<>(EXP1_MALE,EXP1_FEMALE);
        public static final GenderedPair<Exp2MaleEntity,Exp2FemaleEntity> EXP2 =  new GenderedPair<>(EXP2_MALE,EXP2_FEMALE);

    }


    //Annotation; Dazed Maybe is of .faction(LatexType.WHITE_LATEX)

    @SubscribeEvent
    public static void registerTransfurVariants(FMLConstructModEvent event) {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static <T extends ChangedEntity> RegistryObject<TransfurVariant<T>> register(String name, TransfurVariant.Builder<T> builder) {
        Objects.requireNonNull(builder);
        builder.addAbility(entityType -> {
            if (entityType.is(ChangedTags.EntityTypes.LATEX)
                    && !entityType.is(ChangedTags.EntityTypes.PARTIAL_LATEX)){
                return ChangedAddonAbilities.SOFTEN_ABILITY.get();
            } return null;
        });
        return REGISTRY.register(name, builder::build);
    }

    private static <T extends ChangedEntity> RegistryObject<TransfurVariant<T>> register(String name, Supplier<TransfurVariant.Builder<T>> builder) {
        return REGISTRY.register(name, () -> builder.get().addAbility(entityType -> {
            if (entityType.is(ChangedTags.EntityTypes.LATEX)
                    && !entityType.is(ChangedTags.EntityTypes.PARTIAL_LATEX)){
                return ChangedAddonAbilities.SOFTEN_ABILITY.get();
            } return null;
        }).build());
    }
}
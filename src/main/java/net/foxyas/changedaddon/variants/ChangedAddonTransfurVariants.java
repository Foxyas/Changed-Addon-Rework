package net.foxyas.changedaddon.variants;

import net.foxyas.changedaddon.ChangedAddonEntitys;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.ChangedAddonAbilitys;
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

    public static final DeferredRegister<TransfurVariant<?>> REGISTRY = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(ChangedAddonMod.MODID);

    public static final RegistryObject<TransfurVariant<PuroKindEntity>> ADDON_PURO_KIND_MALE = register("form_puro_kind/male",TransfurVariant.Builder.of(ChangedAddonModEntities.PURO_KIND)
            .transfurMode(TransfurMode.REPLICATION).faction(LatexType.DARK_LATEX).addAbility(ChangedAddonAbilitys.CARRY).scares(List.of()));
    public static final RegistryObject<TransfurVariant<PuroKindFemaleEntity>> ADDON_PURO_KIND_FEMALE = register("form_puro_kind/female",TransfurVariant.Builder.of(ChangedAddonModEntities.PURO_KIND_FEMALE)
            .transfurMode(TransfurMode.ABSORPTION).faction(LatexType.DARK_LATEX).addAbility(ChangedAddonAbilitys.CARRY).scares(List.of()));
    public static final RegistryObject<TransfurVariant<SnowLeopardMaleOrganicEntity>> ORGANIC_SNOW_LEOPARD_MALE = register("form_biosynth_snow_leopard/male",
            () -> TransfurVariant.Builder.of(/*LATEX_SNOW_LEOPARD_MALE.get(),*/ChangedAddonModEntities.SNOW_LEOPARD_MALE_ORGANIC)
                    .transfurMode(TransfurMode.REPLICATION).nightVision().breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAddonAbilitys.CARRY).scares(List.of(Creeper.class)));
    public static final RegistryObject<TransfurVariant<SnowLeopardFemaleOrganicEntity>> ORGANIC_SNOW_LEOPARD_FEMALE = register("form_biosynth_snow_leopard/female",
            () -> TransfurVariant.Builder.of(/*LATEX_SNOW_LEOPARD_FEMALE.get(),*/ChangedAddonModEntities.SNOW_LEOPARD_FEMALE_ORGANIC)
                    .transfurMode(TransfurMode.ABSORPTION).nightVision().breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAddonAbilitys.CARRY).scares(List.of(Creeper.class)));
    public static final RegistryObject<TransfurVariant<LatexSnowFoxEntity>> ADDON_LATEX_SNOW_FOX_MALE = register("form_latex_snow_fox/male",
            () -> TransfurVariant.Builder.of(/*WHITE_LATEX_WOLF_MALE.get(),*/ChangedAddonModEntities.LATEX_SNOW_FOX)
                    .nightVision());
    public static final RegistryObject<TransfurVariant<LatexSnowFoxFemaleEntity>> ADDON_LATEX_SNOW_FOX_FEMALE = register("form_latex_snow_fox/female",
            () -> TransfurVariant.Builder.of(/*WHITE_LATEX_WOLF_FEMALE.get(),*/ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE)
                    .nightVision());

    public static final RegistryObject<TransfurVariant<DazedEntity>> DAZED_LATEX = register("form_dazed_latex",TransfurVariant.Builder.of(ChangedAddonModEntities.DAZED)
            .transfurMode(TransfurMode.ABSORPTION).scares(List.of()).nightVision());

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
    public static final RegistryObject<TransfurVariant<LatexSnowFoxEntity>> EXP1_MALE = register("form_exp1/male",
            () -> TransfurVariant.Builder.of(/*WHITE_LATEX_WOLF_MALE.get(),*/ChangedAddonModEntities.LATEX_SNOW_FOX)
                    .nightVision().extraJumps(2).addAbility(ChangedAddonAbilitys.PSYCHIC_PULSE).addAbility(ChangedAddonAbilitys.PSYCHIC_HOLD).addAbility(ChangedAbilities.SWITCH_GENDER).transfurMode(TransfurMode.NONE));
    public static final RegistryObject<TransfurVariant<LatexSnowFoxFemaleEntity>> EXP1_FEMALE = register("form_exp1/female",
            () -> TransfurVariant.Builder.of(/*WHITE_LATEX_WOLF_FEMALE.get(),*/ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE)
                    .nightVision().extraJumps(2).addAbility(ChangedAddonAbilitys.PSYCHIC_PULSE).addAbility(ChangedAddonAbilitys.PSYCHIC_HOLD).addAbility(ChangedAbilities.SWITCH_GENDER).transfurMode(TransfurMode.NONE));

    public static final RegistryObject<TransfurVariant<Exp2MaleEntity>> EXP2_MALE = register("form_exp2/male",
            () -> TransfurVariant.Builder.of(/*LATEX_SNOW_LEOPARD_MALE.get(),*/ChangedAddonModEntities.EXP_2_MALE)
                    .transfurMode(TransfurMode.REPLICATION).breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAbilities.SWITCH_GENDER).addAbility(ChangedAddonAbilitys.DODGE).addAbility(ChangedAddonAbilitys.CARRY).scares(List.of(Creeper.class)).nightVision());
    public static final RegistryObject<TransfurVariant<Exp2FemaleEntity>> EXP2_FEMALE = register("form_exp2/female",
            () -> TransfurVariant.Builder.of(/*LATEX_SNOW_LEOPARD_FEMALE.get(),*/ChangedAddonModEntities.EXP_2_FEMALE)
                    .transfurMode(TransfurMode.ABSORPTION).breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAbilities.SWITCH_GENDER).addAbility(ChangedAddonAbilitys.DODGE).addAbility(ChangedAddonAbilitys.CARRY).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<Exp6Entity>> EXP6 = register("form_exp6",TransfurVariant.Builder.of(ChangedAddonModEntities.EXP_6)
            .reducedFall().jumpStrength(1.05F).abilities(List.of(
                    entityType -> ChangedAddonAbilitys.CARRY.get(),
                    entityType -> ChangedAddonAbilitys.DISSOLVE.get()
            )).scares(List.of(Creeper.class)).transfurMode(TransfurMode.ABSORPTION).nightVision());

    public static final RegistryObject<TransfurVariant<KetExperiment009Entity>> KET_EXPERIMENT_009 = register("form_ket_experiment009",TransfurVariant.Builder.of(ChangedAddonModEntities.KET_EXPERIMENT_009)
            .reducedFall().jumpStrength(1.4F).addAbility(ChangedAddonAbilitys.THUNDERBOLT).addAbility(ChangedAddonAbilitys.SHOCKWAVE).transfurMode(TransfurMode.NONE).nightVision());

    public static final RegistryObject<TransfurVariant<Experiment10Entity>> EXPERIMENT_10 = register("form_experiment_10",TransfurVariant.Builder.of(ChangedAddonModEntities.EXPERIMENT_10)
            .reducedFall().jumpStrength(1.5F).abilities(List.of(
                    entityType -> ChangedAddonAbilitys.WITHER_WAVE.get(),
                    entityType -> ChangedAbilities.HYPNOSIS.get()
            )).transfurMode(TransfurMode.ABSORPTION)
            .scares(List.of(EnderMan.class, WitherSkeleton.class,Creeper.class, AbstractGolem.class, Piglin.class, PiglinBrute.class))
            .nightVision());

    //Boss Transfurs
    public static UseItemMode Ket_Boss = UseItemMode.create("Ket_Boss",false,true,true,true,true);
    public static final RegistryObject<TransfurVariant<KetExperiment009Entity>> KET_EXPERIMENT_009_BOSS_LATEX_VARIANT = register("form_ket_experiment009_boss",TransfurVariant.Builder.of(ChangedAddonModEntities.KET_EXPERIMENT_009)
            .reducedFall().jumpStrength(1.75F)
            .addAbility(ChangedAddonAbilitys.THUNDERBOLT)
            .addAbility(ChangedAddonAbilitys.SHOCKWAVE).transfurMode(TransfurMode.NONE)
            .scares(List.of(Zombie.class,WitherSkeleton.class, AbstractVillager.class, Skeleton.class, AbstractGolem.class))
            .nightVision());

    public static class Gendered {
        public static final GenderedPair<PuroKindEntity,PuroKindFemaleEntity> ADDON_PURO_KIND =  new GenderedPair<>(ADDON_PURO_KIND_MALE,ADDON_PURO_KIND_FEMALE);
        public static final GenderedPair<SnowLeopardMaleOrganicEntity,SnowLeopardFemaleOrganicEntity> ORGANIC_SNOW_LEOPARD =  new GenderedPair<>(ORGANIC_SNOW_LEOPARD_MALE,ORGANIC_SNOW_LEOPARD_FEMALE);
        public static final GenderedPair<LatexSnowFoxEntity, LatexSnowFoxFemaleEntity> ADDON_LATEX_SNOW_FOX =  new GenderedPair<>(ADDON_LATEX_SNOW_FOX_MALE,ADDON_LATEX_SNOW_FOX_FEMALE);
        public static final GenderedPair<LatexSnowFoxEntity, LatexSnowFoxFemaleEntity> EXP1 =  new GenderedPair<>(EXP1_MALE,EXP1_FEMALE);
        public static final GenderedPair<Exp2MaleEntity,Exp2FemaleEntity> EXP2 =  new GenderedPair<>(EXP2_MALE,EXP2_FEMALE);

    }


    //Annotation Dazed Maybe is of .faction(LatexType.WHITE_LATEX)

    @SubscribeEvent
    public static void registerTransfurVariants(FMLConstructModEvent event) {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static <T extends ChangedEntity> RegistryObject<TransfurVariant<T>> register(String name, TransfurVariant.Builder<T> builder) {
        Objects.requireNonNull(builder);
        return REGISTRY.register(name, builder::build);
    }

    private static <T extends ChangedEntity> RegistryObject<TransfurVariant<T>> register(String name, Supplier<TransfurVariant.Builder<T>> builder) {
        return REGISTRY.register(name, () -> builder.get().build());
    }
}
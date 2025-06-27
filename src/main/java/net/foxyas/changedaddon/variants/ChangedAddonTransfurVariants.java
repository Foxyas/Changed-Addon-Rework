package net.foxyas.changedaddon.variants;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.abilities.ChangedAddonAbilities;
import net.foxyas.changedaddon.entity.*;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.registers.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.variant.GenderedPair;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
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
        public static final TagKey<TransfurVariant<?>> FOX_LIKE = create("fox_like");
        public static final TagKey<TransfurVariant<?>> SHARK_LIKE = create("shark_like");
        public static final TagKey<TransfurVariant<?>> CAT_LIKE = create("cat_like");
        public static final TagKey<TransfurVariant<?>> LEOPARD_LIKE = create("leopard_like");
        public static final TagKey<TransfurVariant<?>> CAUSE_FREEZE_DMG = create("cause_freeze_dmg");

        public TransfurVariantTags() {
        }

        private static TagKey<TransfurVariant<?>> create(String name) {
            return TagKey.create(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), new ResourceLocation("changed_addon", name));
        }
    }

    public static final DeferredRegister<TransfurVariant<?>> REGISTRY = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(ChangedAddonMod.MODID);

    public static List<TransfurVariant<?>> getRemovedVariantsList() {
        return List.of(VOID_FOX.get(), REYN.get(), FENGQI_WOLF.get(), KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(), EXPERIMENT_10_BOSS.get(), LATEX_SNEP_FERAL_FORM.get(), LUMINARCTIC_LEOPARD.get(), FEMALE_LUMINARCTIC_LEOPARD.get());
    }

    public static Collection<TransfurVariant<?>> getVariantsRemovedFromSyringes() {
        ArrayList<TransfurVariant<?>> SyringeItemRemovedList, DroppedSyringeRemovedList;
        SyringeItemRemovedList = new ArrayList<>(getRemovedVariantsList());
        DroppedSyringeRemovedList = new ArrayList<>(List.of(LUMINARCTIC_LEOPARD.get(), FEMALE_LUMINARCTIC_LEOPARD.get()));
        SyringeItemRemovedList.addAll(DroppedSyringeRemovedList);

        return SyringeItemRemovedList;
    }

    public static List<TransfurVariant<?>> getBossesVariantsList() {
        return List.of(KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(), EXPERIMENT_10_BOSS.get(), KET_EXPERIMENT_009.get(), EXPERIMENT_10.get(), VOID_FOX.get());
    }

    protected static List<TransfurVariant<?>> getOcVariantList() {
        return List.of(BLUE_LIZARD.get(), HAYDEN_FENNEC_FOX.get(), HIMALAYAN_CRYSTAL_GAS_CAT_MALE.get(), HIMALAYAN_CRYSTAL_GAS_CAT_FEMALE.get(), REYN.get(), LYNX.get(), FENGQI_WOLF.get(), FOXTA_FOXY.get(), SNEPSI_LEOPARD.get(), KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(), EXPERIMENT_10_BOSS.get(), KET_EXPERIMENT_009.get(), EXPERIMENT_10.get());
    }

    public static boolean isVariantOC(TransfurVariant<?> transfurVariant, @Nullable Level level) {
        if (level != null && transfurVariant.getEntityType().create(level) instanceof PatronOC) {
            return true;
        } else return getOcVariantList().contains(transfurVariant);
    }

    public static boolean isVariantOC(ResourceLocation transfurVariantID, @Nullable Level level) {
        TransfurVariant<?> variantFromID = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(transfurVariantID);
        if (variantFromID != null) {
            return isVariantOC(variantFromID, level);
        }
        return false;
    }

    public static boolean isVariantOC(String transfurVariantString, @Nullable Level level) {
        ResourceLocation transfurVariantID = new ResourceLocation(transfurVariantString);
        TransfurVariant<?> variantFromID = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(transfurVariantID);
        if (variantFromID != null) {
            return isVariantOC(variantFromID, level);
        }
        return false;
    }

    public static final RegistryObject<TransfurVariant<PuroKindEntity>> ADDON_PURO_KIND_MALE = register("form_puro_kind/male", TransfurVariant.Builder.of(ChangedAddonModEntities.PURO_KIND).transfurMode(TransfurMode.REPLICATION).faction(LatexType.DARK_LATEX).addAbility(ChangedAddonAbilities.CARRY).scares(List.of()));

    public static final RegistryObject<TransfurVariant<PuroKindFemaleEntity>> ADDON_PURO_KIND_FEMALE = register("form_puro_kind/female", TransfurVariant.Builder.of(ChangedAddonModEntities.PURO_KIND_FEMALE).transfurMode(TransfurMode.ABSORPTION).faction(LatexType.DARK_LATEX).addAbility(ChangedAddonAbilities.CARRY).scares(List.of()));

    public static final RegistryObject<TransfurVariant<SnowLeopardMaleOrganicEntity>> ORGANIC_SNOW_LEOPARD_MALE = register("form_biosynth_snow_leopard/male", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.SNOW_LEOPARD_MALE_ORGANIC).transfurMode(TransfurMode.REPLICATION).nightVision().breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAddonAbilities.CARRY).addAbility(ChangedAddonAbilities.TURN_FERAL_SNEP).addAbility(ChangedAddonAbilities.CLAWS).scares(List.of(Creeper.class)));

    public static final RegistryObject<TransfurVariant<SnowLeopardFemaleOrganicEntity>> ORGANIC_SNOW_LEOPARD_FEMALE = register("form_biosynth_snow_leopard/female", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.SNOW_LEOPARD_FEMALE_ORGANIC).transfurMode(TransfurMode.ABSORPTION).nightVision().breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAddonAbilities.CARRY).addAbility(ChangedAddonAbilities.TURN_FERAL_SNEP).addAbility(ChangedAddonAbilities.CLAWS).scares(List.of(Creeper.class)));

    public static final RegistryObject<TransfurVariant<LatexSnowFoxEntity>> ADDON_LATEX_SNOW_FOX_MALE = register("form_latex_snow_fox/male", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.LATEX_SNOW_FOX).nightVision().scares(Rabbit.class));

    public static final RegistryObject<TransfurVariant<LatexSnowFoxFemaleEntity>> ADDON_LATEX_SNOW_FOX_FEMALE = register("form_latex_snow_fox/female", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE).nightVision().scares(Rabbit.class));

    public static final RegistryObject<TransfurVariant<DazedEntity>> DAZED_LATEX = register("form_dazed_latex", TransfurVariant.Builder.of(ChangedAddonModEntities.DAZED).transfurMode(TransfurMode.ABSORPTION).addAbility(ChangedAddonAbilities.DAZED_PUDDLE).scares(List.of()).nightVision());


    public static final RegistryObject<TransfurVariant<MirrorWhiteTigerEntity>> MIRROR_WHITE_TIGER = register("form_mirror_white_tiger_female", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.MIRROR_WHITE_TIGER).stepSize(0.7F).reducedFall().breatheMode(TransfurVariant.BreatheMode.NORMAL).addAbility(ChangedAddonAbilities.CLAWS).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<SnepsiLeopardEntity>> SNEPSI_LEOPARD = register("form_snepsi_leopard", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.SNEPSI_LEOPARD).stepSize(0.7F).jumpStrength(1.3F).reducedFall().breatheMode(TransfurVariant.BreatheMode.NORMAL).addAbility(ChangedAddonAbilities.CLAWS).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<FoxtaFoxyEntity>> FOXTA_FOXY = register("form_foxta_foxy", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.FOXTA_FOXY).stepSize(0.7F).breatheMode(TransfurVariant.BreatheMode.NORMAL).addAbility(ChangedAddonAbilities.CLAWS).scares(List.of(Rabbit.class)).nightVision());


    public static final RegistryObject<TransfurVariant<LatexDragonSnowLeopardSharkEntity>> LATEX_DRAGON_SNEP_SHARK = register("form_latex_dragon_snow_leopard_shark", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.LATEX_DRAGON_SNOW_LEOPARD_SHARK).glide().gills().nightVision().addAbility(entityType -> ChangedAddonAbilities.WING_FLAP_ABILITY.get()));
    //Partials
    public static final RegistryObject<TransfurVariant<SnowLeopardPartialEntity>> SNOW_LEOPARD_PARTIAL = register("form_latex_snow_leopard_partial", TransfurVariant.Builder.of(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL).stepSize(0.7F).jumpStrength(1.3F).reducedFall().breatheMode(TransfurVariant.BreatheMode.NORMAL).addAbility(ChangedAddonAbilities.CLAWS).scares(List.of(Creeper.class)).nightVision());

    //Bosses
    public static final RegistryObject<TransfurVariant<VoidFoxEntity>> VOID_FOX = register("form_void_fox", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.VOID_FOX).addAbility(ChangedAddonAbilities.CLAWS).jumpStrength(1.1f).nightVision().scares(List.of(Creeper.class, Rabbit.class)));

    //Advanced
    public static final RegistryObject<TransfurVariant<AvaliEntity>> AVALI = register("form_avali", TransfurVariant.Builder.of(ChangedAddonEntities.AVALI).stepSize(0.7F).breatheMode(TransfurVariant.BreatheMode.NORMAL).glide().transfurMode(TransfurMode.NONE).scares(List.of()).nightVision());

    //OCs
    public static final RegistryObject<TransfurVariant<WolfyEntity>> WOLFY = register("form_wolfy", TransfurVariant.Builder.of(ChangedAddonModEntities.WOLFY).stepSize(0.7F).breatheMode(TransfurVariant.BreatheMode.NORMAL).transfurMode(TransfurMode.NONE).scares(List.of()).nightVision());
    public static final RegistryObject<TransfurVariant<ReynEntity>> REYN = register("form_reyn", TransfurVariant.Builder.of(ChangedAddonEntities.REYN).stepSize(0.7F).jumpStrength(1.0f).reducedFall().breatheMode(TransfurVariant.BreatheMode.NORMAL).scares(List.of()));
    public static final RegistryObject<TransfurVariant<LynxEntity>> LYNX = register("form_lynx", TransfurVariant.Builder.of(ChangedAddonEntities.LYNX).stepSize(0.7F).jumpStrength(1.35f).reducedFall().addAbility(ChangedAddonAbilities.LEAP).addAbility(ChangedAddonAbilities.CLAWS).breatheMode(TransfurVariant.BreatheMode.NORMAL).scares(List.of(Creeper.class)));
    public static final RegistryObject<TransfurVariant<FengQIWolfEntity>> FENGQI_WOLF = register("form_fengqi_wolf", TransfurVariant.Builder.of(ChangedAddonModEntities.FENGQI_WOLF).stepSize(0.7F).breatheMode(TransfurVariant.BreatheMode.NORMAL).addAbility(ChangedAddonAbilities.CLAWS).scares(List.of(Rabbit.class)).nightVision());
    public static final RegistryObject<TransfurVariant<BunyEntity>> BUNY = register("form_buny", TransfurVariant.Builder.of(ChangedAddonModEntities.BUNY).jumpStrength(1.5F).reducedFall().transfurMode(TransfurMode.ABSORPTION).scares(List.of()));
    public static final RegistryObject<TransfurVariant<BagelEntity>> BAGEL = register("form_bagel", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.BAGEL).nightVision().scares(Rabbit.class));
    public static final RegistryObject<TransfurVariant<HaydenFennecFoxEntity>> HAYDEN_FENNEC_FOX = register("form_hayden_fennec_fox", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.HAYDEN_FENNEC_FOX).addAbility(ChangedAddonAbilities.ADVANCED_HEARING).reducedFall().nightVision().scares(Rabbit.class));
    public static final RegistryObject<TransfurVariant<CrystalGasCatMaleEntity>> HIMALAYAN_CRYSTAL_GAS_CAT_MALE = register("form_himalayan_crystal_gas_cat/male", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.CRYSTAL_GAS_CAT_MALE).addAbility(ChangedAddonAbilities.CLAWS).jumpStrength(1.1f).nightVision().scares(List.of(Creeper.class, Rabbit.class)));
    public static final RegistryObject<TransfurVariant<CrystalGasCatFemaleEntity>> HIMALAYAN_CRYSTAL_GAS_CAT_FEMALE = register("form_himalayan_crystal_gas_cat/female", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.CRYSTAL_GAS_CAT_FEMALE).addAbility(ChangedAddonAbilities.CLAWS).jumpStrength(1.1f).nightVision().scares(List.of(Creeper.class, Rabbit.class)));
    public static final RegistryObject<TransfurVariant<BlueLizard>> BLUE_LIZARD = register("form_blue_lizard", TransfurVariant.Builder.of(ChangedAddonEntities.BLUE_LIZARD).addAbility(ChangedAddonAbilities.DODGE).stepSize(0.7F).sound(ChangedSounds.SOUND3.getLocation()).nightVision());

    //Experiments
    public static final RegistryObject<TransfurVariant<Exp1MaleEntity>> EXP1_MALE = register("form_exp1/male", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.EXP_1_MALE).nightVision().extraJumps(2).addAbility(ChangedAddonAbilities.PSYCHIC_PULSE).addAbility(ChangedAddonAbilities.PSYCHIC_HOLD).addAbility(ChangedAddonAbilities.PSYCHIC_GRAB).addAbility(ChangedAbilities.SWITCH_GENDER).transfurMode(TransfurMode.NONE));

    public static final RegistryObject<TransfurVariant<Exp1FemaleEntity>> EXP1_FEMALE = register("form_exp1/female", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.EXP_1_FEMALE).nightVision().extraJumps(2).addAbility(ChangedAddonAbilities.PSYCHIC_PULSE).addAbility(ChangedAddonAbilities.PSYCHIC_HOLD).addAbility(ChangedAddonAbilities.PSYCHIC_GRAB).addAbility(ChangedAbilities.SWITCH_GENDER).transfurMode(TransfurMode.NONE));

    public static final RegistryObject<TransfurVariant<Exp2MaleEntity>> EXP2_MALE = register("form_exp2/male", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.EXP_2_MALE).transfurMode(TransfurMode.REPLICATION).breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAbilities.SWITCH_GENDER).addAbility(ChangedAddonAbilities.DODGE).addAbility(ChangedAddonAbilities.CARRY).addAbility(ChangedAddonAbilities.CLAWS).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<Exp2FemaleEntity>> EXP2_FEMALE = register("form_exp2/female", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.EXP_2_FEMALE).transfurMode(TransfurMode.ABSORPTION).breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.3F).addAbility(ChangedAbilities.SWITCH_GENDER).addAbility(ChangedAddonAbilities.DODGE).addAbility(ChangedAddonAbilities.CARRY).addAbility(ChangedAddonAbilities.CLAWS).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<Exp6Entity>> EXP6 = register("form_exp6", TransfurVariant.Builder.of(ChangedAddonModEntities.EXP_6).reducedFall().jumpStrength(1.05F).abilities(List.of(entityType -> ChangedAddonAbilities.CARRY.get(), entityType -> ChangedAddonAbilities.DISSOLVE.get(), entityType -> ChangedAddonAbilities.CLAWS.get(), entityType -> ChangedAbilities.GRAB_ENTITY_ABILITY.get())).scares(List.of(Creeper.class)).transfurMode(TransfurMode.ABSORPTION).nightVision());

    public static final RegistryObject<TransfurVariant<LatexSnepEntity>> LATEX_SNEP = register("form_latex_snep", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.LATEX_SNEP).transfurMode(TransfurMode.NONE).breatheMode(TransfurVariant.BreatheMode.NORMAL).addAbility(ChangedAddonAbilities.CLAWS).addAbility(ChangedAddonAbilities.LEAP).addAbility(ChangedAddonAbilities.DODGE).addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION).itemUseMode(UseItemMode.MOUTH).reducedFall().jumpStrength(1.5F).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<LatexSnepEntity>> LATEX_SNEP_FERAL_FORM = register("form_latex_snep_feral", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.LATEX_SNEP).transfurMode(TransfurMode.NONE).breatheMode(TransfurVariant.BreatheMode.NORMAL).addAbility(ChangedAddonAbilities.CLAWS).addAbility(ChangedAddonAbilities.LEAP).addAbility(ChangedAddonAbilities.DODGE).addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION).addAbility(ChangedAddonAbilities.TURN_FERAL_SNEP).itemUseMode(UseItemMode.MOUTH).reducedFall().jumpStrength(1.3F).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<LuminarcticLeopardEntity>> LUMINARCTIC_LEOPARD = register("form_luminarctic_leopard/male", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.LUMINARCTIC_LEOPARD).transfurMode(TransfurMode.ABSORPTION).breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.35F).abilities(List.of(entityType -> ChangedAddonAbilities.CLAWS.get(), entityType -> ChangedAbilities.GRAB_ENTITY_ABILITY.get(), entityType -> ChangedAddonAbilities.DODGE.get(), entityType -> ChangedAddonAbilities.LEAP.get(), entityType -> ChangedAbilities.HYPNOSIS.get(), entityType -> ChangedAddonAbilities.CUSTOM_INTERACTION.get())).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<FemaleLuminarcticLeopardEntity>> FEMALE_LUMINARCTIC_LEOPARD = register("form_luminarctic_leopard/female", () -> TransfurVariant.Builder.of(ChangedAddonModEntities.FEMALE_LUMINARCTIC_LEOPARD).transfurMode(TransfurMode.ABSORPTION).breatheMode(TransfurVariant.BreatheMode.NORMAL).reducedFall().jumpStrength(1.35F).abilities(List.of(entityType -> ChangedAddonAbilities.CLAWS.get(), entityType -> ChangedAbilities.GRAB_ENTITY_ABILITY.get(), entityType -> ChangedAddonAbilities.DODGE.get(), entityType -> ChangedAddonAbilities.LEAP.get(), entityType -> ChangedAbilities.HYPNOSIS.get(), entityType -> ChangedAddonAbilities.CUSTOM_INTERACTION.get())).scares(List.of(Creeper.class)).nightVision());

    public static final RegistryObject<TransfurVariant<LatexSquidTigerSharkEntity>> LATEX_SQUID_TIGER_SHARK = register("form_latex_squid_tiger_shark", TransfurVariant.Builder.of(ChangedAddonModEntities.LATEX_SQUID_TIGER_SHARK).extraHands().addAbility(ChangedAbilities.CREATE_INKBALL).gills().addAbility(ChangedAbilities.SUMMON_SHARKS));

    public static final RegistryObject<TransfurVariant<KetExperiment009Entity>> KET_EXPERIMENT_009 = register("form_ket_experiment009", TransfurVariant.Builder.of(ChangedAddonModEntities.KET_EXPERIMENT_009).reducedFall().jumpStrength(1.35F).abilities(List.of(entityType -> ChangedAddonAbilities.THUNDERBOLT.get(), entityType -> ChangedAddonAbilities.SHOCKWAVE.get(), entityType -> ChangedAddonAbilities.THUNDER_PATH.get())).transfurMode(TransfurMode.ABSORPTION).nightVision());

    public static final RegistryObject<TransfurVariant<Experiment10Entity>> EXPERIMENT_10 = register("form_experiment_10", TransfurVariant.Builder.of(ChangedAddonModEntities.EXPERIMENT_10).reducedFall().jumpStrength(1.4F).abilities(List.of(entityType -> ChangedAddonAbilities.WITHER_WAVE.get(), entityType -> ChangedAbilities.HYPNOSIS.get(), entityType -> ChangedAddonAbilities.CLAWS.get(), entityType -> ChangedAddonAbilities.LEAP.get())).transfurMode(TransfurMode.ABSORPTION).scares(List.of(EnderMan.class, WitherSkeleton.class, Creeper.class, AbstractGolem.class, Piglin.class, PiglinBrute.class)).nightVision());


    //Boss Transfurs
    public static UseItemMode Ket_Boss = UseItemMode.create("Ket_Boss", false, true, true, true, true);
    public static final RegistryObject<TransfurVariant<KetExperiment009BossEntity>> KET_EXPERIMENT_009_BOSS_LATEX_VARIANT = register("form_ket_experiment009_boss", TransfurVariant.Builder.of(ChangedAddonModEntities.KET_EXPERIMENT_009_BOSS).reducedFall().jumpStrength(1.5F).abilities(List.of(entityType -> ChangedAddonAbilities.THUNDERBOLT.get(), entityType -> ChangedAddonAbilities.SHOCKWAVE.get(), entityType -> ChangedAddonAbilities.THUNDER_PATH.get())).transfurMode(TransfurMode.ABSORPTION).scares(List.of(Zombie.class, WitherSkeleton.class, AbstractVillager.class, Skeleton.class, AbstractGolem.class)).nightVision());

    public static final RegistryObject<TransfurVariant<Experiment10BossEntity>> EXPERIMENT_10_BOSS = register("form_experiment_10_boss", TransfurVariant.Builder.of(ChangedAddonModEntities.EXPERIMENT_10_BOSS).reducedFall().jumpStrength(1.5F).abilities(List.of(entityType -> ChangedAddonAbilities.WITHER_WAVE.get(), entityType -> ChangedAbilities.HYPNOSIS.get(), entityType -> ChangedAddonAbilities.CLAWS.get(), entityType -> ChangedAddonAbilities.LEAP.get())).transfurMode(TransfurMode.ABSORPTION).scares(List.of(EnderMan.class, WitherSkeleton.class, Creeper.class, AbstractGolem.class, Piglin.class, PiglinBrute.class)).nightVision());

    public static class Gendered {
        public static final GenderedPair<PuroKindEntity, PuroKindFemaleEntity> ADDON_PURO_KIND = new GenderedPair<>(ADDON_PURO_KIND_MALE, ADDON_PURO_KIND_FEMALE);
        public static final GenderedPair<SnowLeopardMaleOrganicEntity, SnowLeopardFemaleOrganicEntity> ORGANIC_SNOW_LEOPARD = new GenderedPair<>(ORGANIC_SNOW_LEOPARD_MALE, ORGANIC_SNOW_LEOPARD_FEMALE);
        public static final GenderedPair<LatexSnowFoxEntity, LatexSnowFoxFemaleEntity> ADDON_LATEX_SNOW_FOX = new GenderedPair<>(ADDON_LATEX_SNOW_FOX_MALE, ADDON_LATEX_SNOW_FOX_FEMALE);
        public static final GenderedPair<Exp1MaleEntity, Exp1FemaleEntity> EXP1 = new GenderedPair<>(EXP1_MALE, EXP1_FEMALE);
        public static final GenderedPair<Exp2MaleEntity, Exp2FemaleEntity> EXP2 = new GenderedPair<>(EXP2_MALE, EXP2_FEMALE);
        public static final GenderedPair<LuminarcticLeopardEntity, FemaleLuminarcticLeopardEntity> LUMINARCTIC_LEOPARDS = new GenderedPair<>(LUMINARCTIC_LEOPARD, FEMALE_LUMINARCTIC_LEOPARD);
        public static final GenderedPair<CrystalGasCatMaleEntity, CrystalGasCatFemaleEntity> HIMALAYAN_CRYSTAL_GAS_CAT = new GenderedPair<>(HIMALAYAN_CRYSTAL_GAS_CAT_MALE, HIMALAYAN_CRYSTAL_GAS_CAT_FEMALE);
    }


    //@Annotation: Dazed Maybe is of .faction(LatexType.WHITE_LATEX)

    @SubscribeEvent
    public static void registerTransfurVariants(FMLConstructModEvent event) {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static <T extends ChangedEntity> RegistryObject<TransfurVariant<T>> register(String name, TransfurVariant.Builder<T> builder) {
        Objects.requireNonNull(builder);
        builder.addAbility(entityType -> {
            if (entityType.is(ChangedTags.EntityTypes.LATEX) && !entityType.is(ChangedTags.EntityTypes.PARTIAL_LATEX)) {
                return ChangedAddonAbilities.SOFTEN_ABILITY.get();
            }
            return null;
        });
        return REGISTRY.register(name, builder::build);
    }

    private static <T extends ChangedEntity> RegistryObject<TransfurVariant<T>> register(String name, Supplier<TransfurVariant.Builder<T>> builder) {
        return REGISTRY.register(name, () -> builder.get().addAbility(entityType -> {
            if (entityType.is(ChangedTags.EntityTypes.LATEX) && !entityType.is(ChangedTags.EntityTypes.PARTIAL_LATEX)) {
                return ChangedAddonAbilities.SOFTEN_ABILITY.get();
            }
            return null;
        }).build());
    }
}

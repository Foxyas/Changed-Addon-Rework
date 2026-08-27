package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.advanced.*;
import net.foxyas.changedaddon.entity.api.IOriginalCharacterEntity;
import net.foxyas.changedaddon.entity.bosses.*;
import net.foxyas.changedaddon.entity.partials.SnowLeopardPartialEntity;
import net.foxyas.changedaddon.entity.simple.*;
import net.foxyas.changedaddon.util.TagKeyUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.PatronOC;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.UseItemMode;
import net.ltxprogrammer.changed.entity.beast.AquaticEntity;
import net.ltxprogrammer.changed.entity.variant.GenderedPair;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

import static net.foxyas.changedaddon.variant.TransfurVariantsInfo.OCS;
import static net.ltxprogrammer.changed.init.ChangedTransfurVariants.Gendered.registerPair;

public class ChangedAddonTransfurVariants {

    public static final DeferredRegister<TransfurVariant<?>> REGISTRY = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(ChangedAddonMod.MODID);

    // =========================================================== Basics =========================================================== //
    public static final RegistryObject<TransfurVariant<PuroKindMaleEntity>> PURO_KIND_MALE = register("form_puro_kind/male",
            TransfurVariant.Builder.of(ChangedAddonEntities.PURO_KIND_MALE)
                    .transfurMode(TransfurMode.REPLICATION)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .scares(List.of()));

    public static final RegistryObject<TransfurVariant<PuroKindFemaleEntity>> PURO_KIND_FEMALE = register("form_puro_kind/female",
            TransfurVariant.Builder.of(ChangedAddonEntities.PURO_KIND_FEMALE)
                    .transfurMode(TransfurMode.ABSORPTION)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .scares(List.of()));

    public static final RegistryObject<TransfurVariant<SnowLeopardMaleOrganicEntity>> ORGANIC_SNOW_LEOPARD_MALE = register("form_biosynth_snow_leopard/male",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.SNOW_LEOPARD_MALE_ORGANIC)
                    .transfurMode(TransfurMode.REPLICATION)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .addAbility(ChangedAddonAbilities.TURN_FERAL_SNEP)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Creeper.class)));

    public static final RegistryObject<TransfurVariant<SnowLeopardFemaleOrganicEntity>> ORGANIC_SNOW_LEOPARD_FEMALE = register("form_biosynth_snow_leopard/female",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.SNOW_LEOPARD_FEMALE_ORGANIC)
                    .transfurMode(TransfurMode.ABSORPTION)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .addAbility(ChangedAddonAbilities.TURN_FERAL_SNEP)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Creeper.class)));

    public static final RegistryObject<TransfurVariant<WhiteFoxEntity>> WHITE_FOX = register("form_white_fox",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.WHITE_FOX)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .scares(Rabbit.class));

    public static final RegistryObject<TransfurVariant<LatexSnowFoxMaleEntity>> LATEX_SNOW_FOX_MALE = register("form_latex_snow_fox/male",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_SNOW_FOX_MALE)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .scares(Rabbit.class));

    public static final RegistryObject<TransfurVariant<LatexSnowFoxFemaleEntity>> LATEX_SNOW_FOX_FEMALE = register("form_latex_snow_fox/female",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_SNOW_FOX_FEMALE)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .scares(Rabbit.class));

    public static final RegistryObject<TransfurVariant<DazedLatexEntity>> DAZED_LATEX = register("form_dazed_latex",
            TransfurVariant.Builder.of(ChangedAddonEntities.DAZED_LATEX)
                    .transfurMode(TransfurMode.ABSORPTION)
                    .addAbility(ChangedAddonAbilities.DAZED_PUDDLE)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<BuffDazedLatexEntity>> BUFF_DAZED_LATEX = register("form_buff_dazed_latex",
            TransfurVariant.Builder.of(ChangedAddonEntities.BUFF_DAZED_LATEX)
                    .transfurMode(TransfurMode.ABSORPTION)
                    //.addAbility(ChangedAddonAbilities.DAZED_PUDDLE) the big one can't use puddle mode canonically
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<MirrorWhiteTigerEntity>> MIRROR_WHITE_TIGER = register("form_mirror_white_tiger_female",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.MIRROR_WHITE_TIGER)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<SnepsiLeopardEntity>> SNEPSI_LEOPARD = register("form_snepsi_leopard",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.SNEPSI_LEOPARD)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<FoxtaFoxyEntity>> FOXTA_FOXY = register("form_foxta_foxy",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.FOXTA_FOXY)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Rabbit.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LatexKitsuneMaleEntity>> LATEX_KITSUNE_MALE = register("form_latex_kitsune/male",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_KITSUNE_MALE)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.TELEPORT)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Rabbit.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LatexKitsuneFemaleEntity>> LATEX_KITSUNE_FEMALE = register("form_latex_kitsune/female",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_KITSUNE_FEMALE)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.TELEPORT)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Rabbit.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LatexCalicoCatEntity>> LATEX_CALICO_CAT = register("form_latex_calico_cat",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_CALICO_CAT)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LatexDragonSnowLeopardSharkEntity>> LATEX_DRAGON_SNEP_SHARK = register("form_latex_dragon_snow_leopard_shark",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_DRAGON_SNOW_LEOPARD_SHARK)
                    .glide()
                    .gills()
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LatexSnepEntity>> LATEX_SNEP = register("form_latex_snep",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_SNEP)
                    .transfurMode(TransfurMode.NONE)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .itemUseMode(UseItemMode.MOUTH)
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LatexSnepEntity>> LATEX_SNEP_FERAL_FORM = register("form_latex_snep_feral",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_SNEP)
                    .transfurMode(TransfurMode.NONE)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .addAbility(ChangedAddonAbilities.TURN_FERAL_SNEP)
                    .itemUseMode(UseItemMode.MOUTH)
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LuminarcticLeopardMaleEntity>> LUMINARCTIC_LEOPARD_MALE = register("form_luminarctic_leopard/male",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LUMINARCTIC_LEOPARD_MALE)
                    .transfurMode(TransfurMode.ABSORPTION)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .abilities(List.of(entityType -> ChangedAddonAbilities.CLAWS.get(), entityType -> ChangedAbilities.GRAB_ENTITY_ABILITY.get(), entityType -> ChangedAddonAbilities.DODGE.get(), entityType -> ChangedAddonAbilities.LEAP.get(), entityType -> ChangedAbilities.HYPNOSIS.get(), entityType -> ChangedAddonAbilities.CUSTOM_INTERACTION.get()))
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LuminarcticLeopardFemaleEntity>> LUMINARCTIC_LEOPARD_FEMALE = register("form_luminarctic_leopard/female",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LUMINARCTIC_LEOPARD_FEMALE)
                    .transfurMode(TransfurMode.ABSORPTION)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .abilities(List.of(entityType -> ChangedAddonAbilities.CLAWS.get(), entityType -> ChangedAbilities.GRAB_ENTITY_ABILITY.get(), entityType -> ChangedAddonAbilities.DODGE.get(), entityType -> ChangedAddonAbilities.LEAP.get(), entityType -> ChangedAbilities.HYPNOSIS.get(), entityType -> ChangedAddonAbilities.CUSTOM_INTERACTION.get()))
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LatexSquidTigerSharkEntity>> LATEX_SQUID_TIGER_SHARK = register("form_latex_squid_tiger_shark",
            TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_SQUID_TIGER_SHARK)
                    .extraHands()
                    .addAbility(ChangedAbilities.CREATE_INKBALL)
                    .gills()
                    .addAbility(ChangedAbilities.SUMMON_SHARKS));

    public static final RegistryObject<TransfurVariant<LuminaraFlowerBeastEntity>> LUMINARA_FLOWER_BEAST = register("form_luminara_flower_beast",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LUMINARA_FLOWER_BEAST)
                    .addAbility(ChangedAddonAbilities.LUMINARA_FIREBALL)
                    .addAbility(ChangedAddonAbilities.TELEPORT)
                    .addAbility(ChangedAddonAbilities.TELEPORT_DODGE)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .glide()
                    .sound(ChangedSounds.TRANSFUR_BY_LATEX.get().getLocation()));

    public static final RegistryObject<TransfurVariant<LatexWhiteSnowLeopardMale>> LATEX_WHITE_SNOW_LEOPARD_MALE = register("form_latex_white_snow_leopard/male",
            TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_WHITE_SNOW_LEOPARD_MALE)
                    .scares(Creeper.class)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
    );

    public static final RegistryObject<TransfurVariant<LatexWhiteSnowLeopardFemale>> LATEX_WHITE_SNOW_LEOPARD_FEMALE = register("form_latex_white_snow_leopard/female",
            TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_WHITE_SNOW_LEOPARD_FEMALE)
                    .scares(Creeper.class)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .absorbing());

    public static final RegistryObject<TransfurVariant<LatexCheetahMale>> LATEX_CHEETAH_MALE = register("form_latex_cheetah/male",
            TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_CHEETAH_MALE)
                    .scares(Creeper.class)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
    );

    public static final RegistryObject<TransfurVariant<LatexCheetahFemale>> LATEX_CHEETAH_FEMALE = register("form_latex_cheetah/female",
            TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_CHEETAH_FEMALE)
                    .scares(Creeper.class)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .absorbing());

    public static final RegistryObject<TransfurVariant<DarkLatexYufengQueenEntity>> DARK_LATEX_YUFENG_QUEEN = register("form_dark_latex_yufeng_queen",
            TransfurVariant.Builder.of(ChangedAddonEntities.DARK_LATEX_YUFENG_QUEEN)
                    .glide()
                    .absorbing()
                    .addAbility(ChangedAbilities.TOGGLE_WAVE_VISION)
                    .addAbility(ChangedAddonAbilities.SUMMON_DL_PUP));

    //Partials
    public static final RegistryObject<TransfurVariant<SnowLeopardPartialEntity>> SNOW_LEOPARD_PARTIAL = register("form_latex_snow_leopard_partial",
            TransfurVariant.Builder.of(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    //Bosses
    public static final RegistryObject<TransfurVariant<VoidFoxEntity>> VOID_FOX = register("form_void_fox",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.VOID_FOX)
                    .addAbility(ChangedAddonAbilities.SONAR)
                    .addAbility(ChangedAddonAbilities.DASH)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAbilities.TOGGLE_WAVE_VISION)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .scares(List.of(Creeper.class, Rabbit.class)));

    //Advanced
    public static final RegistryObject<TransfurVariant<AvaliEntity>> AVALI = register("form_avali",
            TransfurVariant.Builder.of(ChangedAddonEntities.AVALI)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .glide()
                    .transfurMode(TransfurMode.NONE)
                    .scares(List.of())
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<AvaliZerGodMasterEntity>> AVALI_ZERGODMASTER = register("form_avali_zergodmaster",
            TransfurVariant.Builder.of(ChangedAddonEntities.AVALI_ZERGODMASTER)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .glide()
                    .transfurMode(TransfurMode.REPLICATION)
                    .scares(List.of())
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<ProtogenEntity>> PROTOGEN = register("form_protogen",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.PROTOGEN)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<PrototypeEntity>> PROTOTYPE = register("form_prototype",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.PROTOTYPE)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));


    // ============================================================ OCs ============================================================ //
    public static final RegistryObject<TransfurVariant<LatexSnowFoxFoxyasEntity>> FOXYAS = register("form_foxyas",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_SNOW_FOX_FOXYAS)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    //.addAbility(ChangedAbilities.GRAB_ENTITY_ABILITY)// Already applied in TFVariant.Builder.<init>
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .nightVision()
                    .transfurMode(TransfurMode.NONE)
                    .scares(List.of(Creeper.class))
                    .sound(ChangedSounds.TRANSFUR_BY_LATEX.get().getLocation()));

    public static final RegistryObject<TransfurVariant<Protogen0senia0Entity>> PROTOGEN_0SENIA0 = register("form_protogen_0senia0",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.PROTOGEN_0SENIA0)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .addAbility(ChangedAbilities.GRAB_ENTITY_ABILITY)
                    .addAbility(ChangedAddonAbilities.DASH)
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .addAbility(ChangedAddonAbilities.SONAR)
                    .nightVision()
                    .transfurMode(TransfurMode.NONE)
                    .scares(List.of(Creeper.class))
                    .sound(ChangedSounds.TRANSFUR_BY_LATEX.get().getLocation()));

    public static final RegistryObject<TransfurVariant<LatexKaylaSharkEntity>> LATEX_KAYLA_SHARK = register("form_latex_kayla_shark",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_KAYLA_SHARK)
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .addAbility(ChangedAddonAbilities.CUSTOM_INTERACTION)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAbilities.HYPNOSIS)
                    .nightVision()
                    .gills()
    );

    public static final RegistryObject<TransfurVariant<WolfyEntity>> WOLFY = register("form_wolfy",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.WOLFY)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .transfurMode(TransfurMode.NONE)
                    .scares(List.of())
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .addAbility(ChangedAddonAbilities.COUNTER_DODGE)
    );

    public static final RegistryObject<TransfurVariant<ReynEntity>> REYN = register("form_reyn",
            TransfurVariant.Builder.of(ChangedAddonEntities.REYN)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .scares(List.of()));

    public static final RegistryObject<TransfurVariant<LynxEntity>> LYNX = register("form_lynx",
            TransfurVariant.Builder.of(ChangedAddonEntities.LYNX)
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .scares(List.of(Creeper.class)));

    public static final RegistryObject<TransfurVariant<FengQIWolfEntity>> FENGQI_WOLF = register("form_fengqi_wolf",
            TransfurVariant.Builder.of(ChangedAddonEntities.FENGQI_WOLF)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .scares(List.of(Rabbit.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<LatexBorderCollieEntity>> LATEX_BORDER_COLLIE = register("form_latex_border_collie",
            TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_BORDER_COLLIE)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL));

    public static final RegistryObject<TransfurVariant<BunyEntity>> BUNY = register("form_buny",
            TransfurVariant.Builder.of(ChangedAddonEntities.BUNY)
                    .scares(List.of()));

    public static final RegistryObject<TransfurVariant<BagelEntity>> BAGEL = register("form_bagel",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.BAGEL)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .scares(Rabbit.class));

    public static final RegistryObject<TransfurVariant<HaydenFennecFoxEntity>> HAYDEN_FENNEC_FOX = register("form_hayden_fennec_fox",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.HAYDEN_FENNEC_FOX)
                    .addAbility(ChangedAddonAbilities.ADVANCED_HEARING)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .scares(Rabbit.class));

    public static final RegistryObject<TransfurVariant<CrystalGasCatMaleEntity>> HIMALAYAN_CRYSTAL_GAS_CAT_MALE = register("form_himalayan_crystal_gas_cat/male",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.CRYSTAL_GAS_CAT_MALE)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .scares(List.of(Creeper.class, Rabbit.class)));

    public static final RegistryObject<TransfurVariant<CrystalGasCatFemaleEntity>> HIMALAYAN_CRYSTAL_GAS_CAT_FEMALE = register("form_himalayan_crystal_gas_cat/female",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.CRYSTAL_GAS_CAT_FEMALE)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .scares(List.of(Creeper.class, Rabbit.class)));

    public static final RegistryObject<TransfurVariant<BlueLizard>> BLUE_LIZARD = register("form_blue_lizard",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.BLUE_LIZARD)
                    .canClimb()
                    .addAbility(ChangedAddonAbilities.TOGGLE_CLIMB)
                    .addAbility(ChangedAddonAbilities.APPLY_REGENERATION_PASSIVE)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .sound(ChangedSounds.TRANSFUR_BY_LATEX.get().getLocation())
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<MongooseEntity>> MONGOOSE = register("form_latex_mongoose",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.MONGOOSE)
                    .sound(ChangedSounds.TRANSFUR_BY_LATEX.get().getLocation()));

    public static final RegistryObject<TransfurVariant<PinkCyanSkunkEntity>> PINK_CYAN_SKUNK = register("form_pink_cyan_skunk",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.PINK_CYAN_SKUNK)
                    .sound(ChangedSounds.TRANSFUR_BY_LATEX.get().getLocation()));

    public static final RegistryObject<TransfurVariant<LatexWindCatMaleEntity>> LATEX_WIND_CAT_MALE = register("form_latex_wind_cat/male",
            TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_WIND_CAT_MALE)
                    .scares(Creeper.class)
                    .extraJumps(2)
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .nightVision()
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAddonAbilities.WIND_CONTROL)
                    .addAbility(ChangedAddonAbilities.WIND_PASSIVE));

    public static final RegistryObject<TransfurVariant<LatexWindCatFemaleEntity>> LATEX_WIND_CAT_FEMALE = register("form_latex_wind_cat/female",
            TransfurVariant.Builder.of(ChangedAddonEntities.LATEX_WIND_CAT_FEMALE)
                    .scares(Creeper.class)
                    .extraJumps(2)
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .nightVision()
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAddonAbilities.WIND_CONTROL)
                    .addAbility(ChangedAddonAbilities.WIND_PASSIVE));

    public static final RegistryObject<TransfurVariant<BorealisMaleEntity>> BOREALIS_MALE = register("form_borealis/male",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.BOREALIS_MALE)
                    .addAbility(ChangedAbilities.GRAB_ENTITY_ABILITY)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .nightVision()
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .scares(List.of(Creeper.class))
                    .sound(ChangedSounds.TRANSFUR_BY_LATEX.get().getLocation()));

    public static final RegistryObject<TransfurVariant<BorealisFemaleEntity>> BOREALIS_FEMALE = register("form_borealis/female",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.BOREALIS_FEMALE)
                    .addAbility(ChangedAbilities.GRAB_ENTITY_ABILITY)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.LEAP)
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .nightVision()
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .scares(List.of(Creeper.class))
                    .sound(ChangedSounds.TRANSFUR_BY_LATEX.get().getLocation()));

    // ======================================================== Experiments ======================================================== //
    public static final RegistryObject<TransfurVariant<Exp1MaleEntity>> EXP1_MALE = register("form_exp1/male",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.EXP_1_MALE)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .extraJumps(2)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_PULSE)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_HOLD)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_GRAB)
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .transfurMode(TransfurMode.NONE));

    public static final RegistryObject<TransfurVariant<Exp1FemaleEntity>> EXP1_FEMALE = register("form_exp1/female",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.EXP_1_FEMALE)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION)
                    .extraJumps(2)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_PULSE)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_HOLD)
                    .addAbility(ChangedAddonAbilities.PSYCHIC_GRAB)
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .transfurMode(TransfurMode.NONE));

    public static final RegistryObject<TransfurVariant<Exp2MaleEntity>> EXP2_MALE = register("form_exp2/male",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.EXP_2_MALE)
                    .transfurMode(TransfurMode.REPLICATION)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .addAbility(ChangedAddonAbilities.UNFUSE)
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<Exp2FemaleEntity>> EXP2_FEMALE = register("form_exp2/female",
            () -> TransfurVariant.Builder.of(ChangedAddonEntities.EXP_2_FEMALE)
                    .transfurMode(TransfurMode.ABSORPTION)
                    .breatheMode(TransfurVariant.BreatheMode.NORMAL)
                    .addAbility(ChangedAbilities.SWITCH_GENDER)
                    .addAbility(ChangedAddonAbilities.DODGE)
                    .addAbility(ChangedAddonAbilities.CARRY)
                    .addAbility(ChangedAddonAbilities.CLAWS)
                    .addAbility(ChangedAddonAbilities.UNFUSE)
                    .scares(List.of(Creeper.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<Exp6Entity>> EXP6 = register("form_exp6",
            TransfurVariant.Builder.of(ChangedAddonEntities.EXP_6)
                    .abilities(List.of(entityType -> ChangedAddonAbilities.CARRY.get(), entityType -> ChangedAddonAbilities.DISSOLVE.get(), entityType -> ChangedAddonAbilities.CLAWS.get(), entityType -> ChangedAbilities.GRAB_ENTITY_ABILITY.get()))
                    .scares(List.of(Creeper.class))
                    .transfurMode(TransfurMode.ABSORPTION)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<Experiment009Entity>> EXPERIMENT_009 = register("form_experiment009",
            TransfurVariant.Builder.of(ChangedAddonEntities.EXPERIMENT_009)
                    .abilities(List.of(
                                    entityType -> ChangedAddonAbilities.THUNDERBOLT.get(),
                                    entityType -> ChangedAddonAbilities.THUNDER_PATH.get(),
                                    entityType -> ChangedAddonAbilities.SHOCKWAVE.get(),
                                    entityType -> ChangedAddonAbilities.CUSTOM_INTERACTION.get()
                            )
                    )
                    .transfurMode(TransfurMode.ABSORPTION)
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<Experiment10Entity>> EXPERIMENT_10 = register("form_experiment_10",
            TransfurVariant.Builder.of(ChangedAddonEntities.EXPERIMENT_10)
                    .abilities(List.of(
                                    entityType -> ChangedAddonAbilities.WITHER_WAVE.get(),
                                    entityType -> ChangedAddonAbilities.WITHER_GRENADE.get(),
                                    entityType -> ChangedAbilities.HYPNOSIS.get(),
                                    entityType -> ChangedAddonAbilities.CLAWS.get(),
                                    entityType -> ChangedAddonAbilities.LEAP.get(),
                                    entityType -> ChangedAddonAbilities.CUSTOM_INTERACTION.get()
                            )
                    )
                    .transfurMode(TransfurMode.ABSORPTION)
                    .scares(List.of(EnderMan.class,
                            WitherSkeleton.class,
                            Creeper.class,
                            AbstractGolem.class,
                            Piglin.class,
                            PiglinBrute.class))
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    // ======================================================= Boss Transfurs ======================================================= //
    public static final RegistryObject<TransfurVariant<Experiment009BossEntity>> EXPERIMENT_009_BOSS = register("form_experiment009_boss",
            TransfurVariant.Builder.of(ChangedAddonEntities.EXPERIMENT_009_BOSS)
                    .abilities(List.of(
                                    entityType -> ChangedAddonAbilities.THUNDERBOLT.get(),
                                    entityType -> ChangedAddonAbilities.THUNDER_PATH.get(),
                                    entityType -> ChangedAddonAbilities.SHOCKWAVE.get(),
                                    entityType -> ChangedAddonAbilities.DODGE.get(),
                                    entityType -> ChangedAddonAbilities.CUSTOM_INTERACTION.get()
                            )
                    )
                    .transfurMode(TransfurMode.ABSORPTION)
                    .scares(List.of(
                                    Zombie.class,
                                    WitherSkeleton.class,
                                    AbstractVillager.class,
                                    Skeleton.class,
                                    AbstractGolem.class
                            )
                    )
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));

    public static final RegistryObject<TransfurVariant<Experiment10BossEntity>> EXPERIMENT_10_BOSS = register("form_experiment_10_boss",
            TransfurVariant.Builder.of(ChangedAddonEntities.EXPERIMENT_10_BOSS)
                    .abilities(List.of(
                                    entityType -> ChangedAddonAbilities.WITHER_WAVE.get(),
                                    entityType -> ChangedAddonAbilities.WITHER_GRENADE.get(),
                                    entityType -> ChangedAbilities.HYPNOSIS.get(),
                                    entityType -> ChangedAddonAbilities.CLAWS.get(),
                                    entityType -> ChangedAddonAbilities.LEAP.get(),
                                    entityType -> ChangedAddonAbilities.CUSTOM_INTERACTION.get()
                            )
                    )
                    .transfurMode(TransfurMode.ABSORPTION)
                    .scares(List.of(
                                    EnderMan.class,
                                    WitherSkeleton.class,
                                    Creeper.class,
                                    AbstractGolem.class,
                                    Piglin.class,
                                    PiglinBrute.class
                            )
                    )
                    .nightVision()
                    .addAbility(ChangedAbilities.TOGGLE_NIGHT_VISION));
    public static final List<Supplier<TransfurVariant<?>>> humanForms = List.of(ChangedTransfurVariants.LATEX_HUMAN::get);

    //@Annotation: Dazed Maybe is of .faction(LatexType.WHITE_LATEX)
    private static List<TransfurVariant<?>> REMOVED_VARS;
    private static List<TransfurVariant<?>> REMOVED_FROM_SYRINGES;

    // Utils
    private static List<TransfurVariant<?>> BOSS_VARS;
    private static Map<TransfurVariant<?>, TransfurVariant<?>> BOSS_VARS_MAP;

    private static <T extends ChangedEntity> RegistryObject<TransfurVariant<T>> register(String name, TransfurVariant.Builder<T> builder) {
        Objects.requireNonNull(builder);
        return REGISTRY.register(name, builder::build);
    }

    private static <T extends ChangedEntity> RegistryObject<TransfurVariant<T>> register(String name, Supplier<TransfurVariant.Builder<T>> builder) {
        return REGISTRY.register(name, () -> builder.get()
                .build());
    }

    public static boolean isAquatic(TransfurVariantInstance<?> variantInstance) {
        ChangedEntity entity = variantInstance.getChangedEntity();
        TransfurVariant<?> variant = variantInstance.getParent();
        return entity instanceof AquaticEntity ||
                variant.is(ChangedAddonTags.TransfurVariants.SHARK_LIKE) ||
                variant.is(ChangedAddonTags.TransfurVariants.AQUATIC_LIKE) ||
                variant.is(ChangedAddonTags.TransfurVariants.HAS_AQUATIC_DIET);
    }

    public static List<TransfurVariant<?>> getRemovedVariantsList(Level level) {
        if (REMOVED_VARS == null) {

            HashSet<TransfurVariant<?>> variants = new HashSet<>(
                    TagKeyUtil.getTagContents(level,
                            ChangedAddonTags.TransfurVariants.REMOVED_FROM_RANDOM_VARIANT_FUNCTION).toList()
            );
            variants.addAll(getHardCodedRemovedVariantsList());

            REMOVED_VARS = variants.stream().toList();
        }
        return REMOVED_VARS;
    }

    public static List<TransfurVariant<?>> getHardCodedRemovedVariantsList() {
        return List.of(VOID_FOX.get(),
                REYN.get(),
                FENGQI_WOLF.get(),
                EXPERIMENT_009.get(),
                EXPERIMENT_10.get(),
                EXPERIMENT_009_BOSS.get(),
                EXPERIMENT_10_BOSS.get(),
                LATEX_SNEP_FERAL_FORM.get(),
                LUMINARCTIC_LEOPARD_MALE.get(),
                LUMINARCTIC_LEOPARD_FEMALE.get()
        );
    }

    public static List<TransfurVariant<?>> getVariantsRemovedFromSyringes(Level level) {
        if (REMOVED_FROM_SYRINGES == null) {
//            List<TransfurVariant<?>> tmp = new ArrayList<>(getRemovedVariantsList(level));
//            tmp.add(LUMINARA_FLOWER_BEAST.get());
            REMOVED_FROM_SYRINGES = TagKeyUtil.getTagContents(level, ChangedAddonTags.TransfurVariants.REMOVED_FROM_GROUNDED_SYRINGES).toList();
//            List.copyOf(tmp);
        }
        return REMOVED_FROM_SYRINGES;
    }

    public static List<TransfurVariant<?>> getBossVariants(Level level) {
        if (BOSS_VARS == null) {
            BOSS_VARS = TagKeyUtil.getTagContents(level, ChangedAddonTags.TransfurVariants.BOSS_VARIANTS).toList();
            //List.of(EXPERIMENT_009_BOSS.get(), EXPERIMENT_10_BOSS.get(), EXPERIMENT_009.get(), EXPERIMENT_10.get(), VOID_FOX.get());
        }
        return BOSS_VARS;
    }

    public static Map<TransfurVariant<?>, TransfurVariant<?>> bossVariantsMap() {
        if (BOSS_VARS_MAP == null) {
            BOSS_VARS_MAP = Map.of(
                    EXPERIMENT_009.get(), EXPERIMENT_009_BOSS.get(),
                    EXPERIMENT_10.get(), EXPERIMENT_10_BOSS.get()
            );
        }
        return BOSS_VARS_MAP;
    }

    public static boolean isBossVariant(TransfurVariant<?> transfurVariant) {
        return bossVariantsMap().containsValue(transfurVariant) || transfurVariant.is(ChangedAddonTags.TransfurVariants.BOSS_VARIANTS);
    }

    public static TransfurVariant<?> getBossVersionOf(TransfurVariant<?> transfurVariant) {
        TransfurVariant<?> variant = bossVariantsMap().get(transfurVariant);
        return variant != null ? variant : transfurVariant;
    }

    @Nullable
    public static List<Component> getOcVariantComponent(TransfurVariant<?> transfurVariant) {
        return OCS.get().get(transfurVariant);
    }

    @NotNull
    public static List<Component> getVariantComponentIfAny(TransfurVariant<?> transfurVariant, Level level) {
        if (isVariantOC(transfurVariant, level)) {
            if (ChangedEntities.getCachedEntity(level, transfurVariant.getEntityType()) instanceof IOriginalCharacterEntity iOriginalCharacterEntity) {
                return Objects.requireNonNullElse(iOriginalCharacterEntity.getOcVariantComponents(), List.of());
            }
            return Objects.requireNonNullElse(OCS.get().get(transfurVariant), List.of());
        }
        return List.of();
    }


    public static boolean isVariantOC(TransfurVariant<?> transfurVariant, @Nullable Level level) {
        ChangedEntity cachedEntity = ChangedEntities.getCachedEntity(level, transfurVariant.getEntityType());
        if (level != null && cachedEntity instanceof PatronOC) {
            return true;
        } else if (level != null && cachedEntity instanceof IOriginalCharacterEntity) {
            return true;
        } else return OCS.get().containsKey(transfurVariant);
    }

    public static boolean isVariantOC(ResourceLocation transfurVariantID, @Nullable Level level) {
        TransfurVariant<?> variantFromID = ChangedRegistry.TRANSFUR_VARIANT.get()
                .getValue(transfurVariantID);
        if (variantFromID != null) {
            return isVariantOC(variantFromID, level);
        }
        return false;
    }

    public static List<TransfurVariant<?>> getHumanForms() {
        return new ArrayList<>(humanForms.stream().map(Supplier::get).toList());
    }

    // WHY THE F### isn't this working before?. IS A STATIC FINAL JAVA!.. IS SUPPOSED TO LOAD IN THE START-UP AHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    // I HAD TO F###ING ADD THE ANNOTATION FOR IT TO WORK!!!!!
    @Mod.EventBusSubscriber
    public static class Gendered {
        public static final GenderedPair<PuroKindMaleEntity, PuroKindFemaleEntity> PURO_KIND = registerPair(PURO_KIND_MALE, PURO_KIND_FEMALE);
        public static final GenderedPair<SnowLeopardMaleOrganicEntity, SnowLeopardFemaleOrganicEntity> ORGANIC_SNOW_LEOPARD = registerPair(ORGANIC_SNOW_LEOPARD_MALE, ORGANIC_SNOW_LEOPARD_FEMALE);
        public static final GenderedPair<LatexSnowFoxMaleEntity, LatexSnowFoxFemaleEntity> LATEX_SNOW_FOX = registerPair(LATEX_SNOW_FOX_MALE, LATEX_SNOW_FOX_FEMALE);
        public static final GenderedPair<Exp1MaleEntity, Exp1FemaleEntity> EXP1 = registerPair(EXP1_MALE, EXP1_FEMALE);
        public static final GenderedPair<Exp2MaleEntity, Exp2FemaleEntity> EXP2 = registerPair(EXP2_MALE, EXP2_FEMALE);
        public static final GenderedPair<LuminarcticLeopardMaleEntity, LuminarcticLeopardFemaleEntity> LUMINARCTIC_LEOPARDS = registerPair(LUMINARCTIC_LEOPARD_MALE, LUMINARCTIC_LEOPARD_FEMALE);
        public static final GenderedPair<LatexWhiteSnowLeopardMale, LatexWhiteSnowLeopardFemale> WHITE_SNOW_LEOPARDS = registerPair(LATEX_WHITE_SNOW_LEOPARD_MALE, LATEX_WHITE_SNOW_LEOPARD_FEMALE);
        public static final GenderedPair<LatexWindCatMaleEntity, LatexWindCatFemaleEntity> WIND_CATS = registerPair(LATEX_WIND_CAT_MALE, LATEX_WIND_CAT_FEMALE);
        public static final GenderedPair<CrystalGasCatMaleEntity, CrystalGasCatFemaleEntity> HIMALAYAN_CRYSTAL_GAS_CAT = registerPair(HIMALAYAN_CRYSTAL_GAS_CAT_MALE, HIMALAYAN_CRYSTAL_GAS_CAT_FEMALE);
        public static final GenderedPair<LatexKitsuneMaleEntity, LatexKitsuneFemaleEntity> KITSUNES = registerPair(LATEX_KITSUNE_MALE, LATEX_KITSUNE_FEMALE);
        public static final GenderedPair<BorealisMaleEntity, BorealisFemaleEntity> BOREALIS = registerPair(BOREALIS_MALE, BOREALIS_FEMALE);
    }
}

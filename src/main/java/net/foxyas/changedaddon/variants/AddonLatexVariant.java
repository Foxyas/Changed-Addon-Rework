package net.foxyas.changedaddon.variants;

//import net.foxyas.changedaddon.ChangedAddonAbilitys;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ChangedAddonRegisters;
import net.foxyas.changedaddon.client.model.DazedLatexModel;
import net.foxyas.changedaddon.entity.*;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.HoldEntityAbility;
import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.UseItemMode;
import net.ltxprogrammer.changed.entity.beast.*;
import net.ltxprogrammer.changed.entity.variant.GenderedVariant;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.fml.common.Mod;
import net.foxyas.changedaddon.ChangedAddonRegisters;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.monster.Creeper;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AddonLatexVariant {
    //public static UseItemMode ABO = UseItemMode.create("ABO",false,true,true,true,true);
	//this is For Not Show The Hot Bar
	//.itemUseMode(ABO)
    public static final LatexVariant<LightLatexWolfOrganic> ADDON_ORGANIC_LIGHT_LATEX_WOLF = register(LatexVariant.Builder.of(LatexVariant.LIGHT_LATEX_WOLF_ORGANIC, ChangedEntities.LIGHT_LATEX_WOLF_ORGANIC)
            .groundSpeed(1.05F).swimSpeed(1.0F).build(new ResourceLocation("changed_addon", "form_organic_light_latex_wolf")));
    public static final LatexVariant<AerosolLatexWolf> ADDON_ORGANIC_GAS_WOLF = register(LatexVariant.Builder.of(LatexVariant.AEROSOL_LATEX_WOLF, ChangedEntities.AEROSOL_LATEX_WOLF)
            .groundSpeed(1.05F).swimSpeed(1.0F).build(new ResourceLocation("changed_addon", "form_aerosol_latex_wolf")));
    public static final LatexVariant<DarkLatexDragon> ADDON_ORGANIC_DARK_LATEX_DRAGON = register(LatexVariant.Builder.of(LatexVariant.DARK_LATEX_DRAGON, ChangedEntities.DARK_LATEX_DRAGON)
            .groundSpeed(1.05F).swimSpeed(1.0F).build(new ResourceLocation("changed_addon", "form_dark_latex_dragon")));
    public static final LatexVariant<LatexSniperDog> ADDON_ORGANIC_LATEX_SNIPER_DOG = register(LatexVariant.Builder.of(LatexVariant.LATEX_SNIPER_DOG, ChangedEntities.LATEX_SNIPER_DOG)
            .groundSpeed(1.05F).swimSpeed(1.0F).build(new ResourceLocation("changed_addon", "form_latex_sniper_dog")));
    public static final LatexVariant<LatexCrystalWolf> ADDON_ORGANIC_LATEX_CRYSTAL_WOLF = register(LatexVariant.Builder.of(LatexVariant.LATEX_CRYSTAL_WOLF, ChangedEntities.LATEX_CRYSTAL_WOLF)
            .groundSpeed(1.05F).swimSpeed(1.0F).build(new ResourceLocation("changed_addon", "form_latex_crystal_wolf")));
    public static final LatexVariant<LatexCrystalWolfHorned> ADDON_ORGANIC_LATEX_CRYSTAL_WOLF_HORNED = register(LatexVariant.Builder.of(LatexVariant.LATEX_CRYSTAL_WOLF_HORNED, ChangedEntities.LATEX_CRYSTAL_WOLF_HORNED)
            .groundSpeed(1.05F).swimSpeed(1.0F).build(new ResourceLocation("changed_addon", "form_horned_latex_crystal_wolf")));
    public static final GenderedVariant<PuroKindEntity,PuroKindFemaleEntity> ADDON_PURO_KIND = LatexVariant.register(GenderedVariant.Builder.of(ChangedAddonModEntities.PURO_KIND,ChangedAddonModEntities.PURO_KIND_FEMALE)
            .groundSpeed(1.08F).swimSpeed(1.0F).faction(LatexType.DARK_LATEX).scares(List.of()).additionalHealth(4).split(LatexVariant.Builder::replicating, LatexVariant.Builder::absorbing).buildGendered(new ResourceLocation("changed_addon", "form_latex_puro_kind")));
    public static final LatexVariant<LightLatexWolfMale> ADDON_LIGHT_LATEX_WOLF = register(LatexVariant.Builder.of(ChangedEntities.LIGHT_LATEX_WOLF_MALE)
          	.groundSpeed(1.05F).swimSpeed(1.0F).transfurMode(TransfurMode.NONE).scares(List.of()).additionalHealth(6).build(new ResourceLocation("changed_addon", "form_light_latex_wolf_peaceful")));
	public static final GenderedVariant<LatexSnowLeopardMale,LatexSnowLeopardFemale> ADDON_SNOW_LEOPARD = LatexVariant.register(GenderedVariant.Builder.of(ChangedEntities.LATEX_SNOW_LEOPARD_MALE,ChangedEntities.LATEX_SNOW_LEOPARD_FEMALE)
          	.groundSpeed(1.08F).swimSpeed(1.0F).transfurMode(TransfurMode.NONE).scares(List.of(Creeper.class)).additionalHealth(6).nightVision().split(LatexVariant.Builder::replicating, LatexVariant.Builder::absorbing).buildGendered(new ResourceLocation("changed_addon", "form_latex_snow_leopard_peaceful")));
    public static final GenderedVariant<SnowLeopardMaleOrganicEntity,SnowLeopardFemaleOrganicEntity> ORGANIC_SNOW_LEOPARD = LatexVariant.register(GenderedVariant.Builder.of(LatexVariant.LATEX_SNOW_LEOPARD,ChangedAddonModEntities.SNOW_LEOPARD_MALE_ORGANIC,ChangedAddonModEntities.SNOW_LEOPARD_FEMALE_ORGANIC)
            .groundSpeed(1.17F).swimSpeed(1.0F).breatheMode(LatexVariant.BreatheMode.NORMAL).transfurMode(TransfurMode.NONE).scares(List.of(Creeper.class)).additionalHealth(4).nightVision().split(LatexVariant.Builder::replicating, LatexVariant.Builder::absorbing).buildGendered(new ResourceLocation("changed_addon", "form_biosynth_snow_leopard")));
    public static final GenderedVariant<LatexSnowFoxEntity, LatexSnowFoxFemaleEntity> ADDON_LATEX_SNOW_FOX = LatexVariant.register(GenderedVariant.Builder.of(ChangedAddonModEntities.LATEX_SNOW_FOX, ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE)
            .groundSpeed(1.1F).swimSpeed(0.95F).additionalHealth(6).nightVision().split(LatexVariant.Builder::replicating, LatexVariant.Builder::absorbing).buildGendered(new ResourceLocation("changed_addon", "form_latex_snow_fox")));
    public static final LatexVariant<DazedEntity> DAZED_LATEX = LatexVariant.register(LatexVariant.Builder.of(ChangedAddonModEntities.DAZED)
            .groundSpeed(1.1F).swimSpeed(1.07F).transfurMode(TransfurMode.ABSORPTION).scares(List.of()).additionalHealth(6).nightVision().build(new ResourceLocation("changed_addon", "form_dazed_latex")));
    public static final LatexVariant<BunyEntity> BUNY = LatexVariant.register(LatexVariant.Builder.of(ChangedAddonModEntities.BUNY)
            .groundSpeed(1.15F).swimSpeed(0.75F).jumpStrength(1.75F).transfurMode(TransfurMode.ABSORPTION).scares(List.of()).additionalHealth(10).build(new ResourceLocation("changed_addon", "form_buny")));

	//Anotation Dazed Maybe is of .faction(LatexType.WHITE_LATEX) 

    private static <T extends LatexEntity> LatexVariant<T> register(LatexVariant<T> variant) {
        return LatexVariant.register(variant);
    }


}
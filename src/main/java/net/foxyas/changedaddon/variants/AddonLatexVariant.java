package net.foxyas.changedaddon.variants;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.FoxyasEntity;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.ability.HoldEntityAbility;
import net.ltxprogrammer.changed.entity.LatexEntity;
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

import java.util.List;

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
   	public static final LatexVariant<DarkLatexWolfMale> ADDON_PURO_KIND = register(LatexVariant.Builder.of(ChangedEntities.DARK_LATEX_WOLF_MALE)
           	.groundSpeed(1.08F).swimSpeed(1.0F).transfurMode(TransfurMode.NONE).scares(List.of()).additionalHealth(8).build(new ResourceLocation("changed_addon", "form_puro_kind")));
	public static final LatexVariant<LightLatexWolfMale> ADDON_LIGHT_LATEX_WOLF = register(LatexVariant.Builder.of(ChangedEntities.LIGHT_LATEX_WOLF_MALE)
          	.groundSpeed(1.05F).swimSpeed(1.0F).transfurMode(TransfurMode.NONE).scares(List.of()).additionalHealth(6).build(new ResourceLocation("changed_addon", "form_light_latex_wolf")));
	public static final LatexVariant<LatexSnowLeopardMale> ADDON_SNOW_LEOPARD = register(LatexVariant.Builder.of(ChangedEntities.LATEX_SNOW_LEOPARD_MALE)
          	.groundSpeed(1.08F).swimSpeed(1.0F).transfurMode(TransfurMode.NONE).scares(List.of()).additionalHealth(6).nightVision().build(new ResourceLocation("changed_addon", "form_latex_snow_leopard")));
    public static final LatexVariant<FoxyasEntity> ADDON_LATEX_SNOW_FOX = register(LatexVariant.Builder.of(ChangedAddonModEntities.FOXYAS)
            .groundSpeed(1.08F).swimSpeed(1.0F).transfurMode(TransfurMode.NONE).scares(List.of()).additionalHealth(6).nightVision().build(new ResourceLocation("changed_addon", "form_latex_snow_fox")));


    private static <T extends LatexEntity> LatexVariant<T> register(LatexVariant<T> variant) {
        return LatexVariant.register(variant);
    }


}
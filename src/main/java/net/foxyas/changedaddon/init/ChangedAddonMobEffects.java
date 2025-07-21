
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import net.foxyas.changedaddon.potion.UntransfurMobEffect;
import net.foxyas.changedaddon.potion.TransfurSicknessMobEffect;
import net.foxyas.changedaddon.potion.LatexSolventMobEffect;
import net.foxyas.changedaddon.potion.LatexExposureMobEffect;
import net.foxyas.changedaddon.potion.LatexContaminationMobEffect;
import net.foxyas.changedaddon.potion.FadigeMobEffect;
import net.foxyas.changedaddon.ChangedAddonMod;

public class ChangedAddonMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ChangedAddonMod.MODID);
	public static final RegistryObject<MobEffect> FADIGE = REGISTRY.register("fadige", FadigeMobEffect::new);
	public static final RegistryObject<MobEffect> UNTRANSFUR = REGISTRY.register("untransfur", UntransfurMobEffect::new);
	public static final RegistryObject<MobEffect> LATEX_SOLVENT = REGISTRY.register("latex_solvent", LatexSolventMobEffect::new);
	public static final RegistryObject<MobEffect> LATEX_CONTAMINATION = REGISTRY.register("latex_contamination", LatexContaminationMobEffect::new);
	public static final RegistryObject<MobEffect> TRANSFUR_SICKNESS = REGISTRY.register("transfur_sickness", TransfurSicknessMobEffect::new);
	public static final RegistryObject<MobEffect> LATEX_EXPOSURE = REGISTRY.register("latex_exposure", LatexExposureMobEffect::new);
}

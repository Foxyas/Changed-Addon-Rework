
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import net.foxyas.changedaddon.potion.UntransfurMobEffect;
import net.foxyas.changedaddon.potion.LatexSolventMobEffect;
import net.foxyas.changedaddon.potion.LatexContaminationMobEffect;
import net.foxyas.changedaddon.potion.InfriendlygrabeffectMobEffect;
import net.foxyas.changedaddon.potion.InfriendlygrabMobEffect;
import net.foxyas.changedaddon.potion.GrabeffectMobEffect;
import net.foxyas.changedaddon.potion.FadigeMobEffect;
import net.foxyas.changedaddon.ChangedAddonMod;

public class ChangedAddonModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ChangedAddonMod.MODID);
	public static final RegistryObject<MobEffect> GRABEFFECT = REGISTRY.register("grabeffect", () -> new GrabeffectMobEffect());
	public static final RegistryObject<MobEffect> FADIGE = REGISTRY.register("fadige", () -> new FadigeMobEffect());
	public static final RegistryObject<MobEffect> UNTRANSFUR = REGISTRY.register("untransfur", () -> new UntransfurMobEffect());
	public static final RegistryObject<MobEffect> INFRIENDLYGRAB = REGISTRY.register("infriendlygrab", () -> new InfriendlygrabMobEffect());
	public static final RegistryObject<MobEffect> INFRIENDLYGRABEFFECT = REGISTRY.register("infriendlygrabeffect", () -> new InfriendlygrabeffectMobEffect());
	public static final RegistryObject<MobEffect> LATEX_SOLVENT = REGISTRY.register("latex_solvent", () -> new LatexSolventMobEffect());
	public static final RegistryObject<MobEffect> LATEX_CONTAMINATION = REGISTRY.register("latex_contamination", () -> new LatexContaminationMobEffect());
}

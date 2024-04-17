
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.effect.MobEffectInstance;

import net.foxyas.changedaddon.ChangedAddonMod;

public class ChangedAddonModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, ChangedAddonMod.MODID);
	public static final RegistryObject<Potion> LITIXCAMONIAEFFECT = REGISTRY.register("litixcamoniaeffect", () -> new Potion(new MobEffectInstance(ChangedAddonModMobEffects.UNTRANSFUR.get(), 2700, 0, false, true)));
	public static final RegistryObject<Potion> TRANSFUR_SICKNESS_POTION = REGISTRY.register("transfur_sickness_potion", () -> new Potion(new MobEffectInstance(ChangedAddonModMobEffects.TRANSFUR_SICKNESS.get(), 2632, 0, true, false)));
}


/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import net.foxyas.changedaddon.enchantment.SolventEnchantment;
import net.foxyas.changedaddon.ChangedAddonMod;

public class ChangedAddonModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ChangedAddonMod.MODID);
	public static final RegistryObject<Enchantment> SOLVENT = REGISTRY.register("solvent", () -> new SolventEnchantment());
}

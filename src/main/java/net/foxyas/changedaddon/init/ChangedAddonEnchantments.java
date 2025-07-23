package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.enchantment.ChangedLureEnchantment;
import net.foxyas.changedaddon.enchantment.SolventEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChangedAddonEnchantments {
    public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ChangedAddonMod.MODID);
    public static final RegistryObject<Enchantment> SOLVENT = REGISTRY.register("solvent", () -> new SolventEnchantment());
    public static final RegistryObject<Enchantment> CHANGED_LURE = REGISTRY.register("changed_lure", () -> new ChangedLureEnchantment());
}

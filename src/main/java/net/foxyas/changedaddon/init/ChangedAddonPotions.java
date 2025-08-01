package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChangedAddonPotions {
    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, ChangedAddonMod.MODID);
    public static final RegistryObject<Potion> LITIX_CAMMONIA_EFFECT = REGISTRY.register("litix_cammonia_effect", () -> new Potion(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 2700, 0, false, true)));
    public static final RegistryObject<Potion> TRANSFUR_SICKNESS_POTION = REGISTRY.register("transfur_sickness_potion", () -> new Potion(new MobEffectInstance(ChangedAddonMobEffects.TRANSFUR_SICKNESS.get(), 2632, 0, true, false)));
}

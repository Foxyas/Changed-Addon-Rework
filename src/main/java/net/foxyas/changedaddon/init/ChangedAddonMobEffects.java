
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.potion.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChangedAddonMobEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ChangedAddonMod.MODID);
    public static final RegistryObject<MobEffect> FADIGE = REGISTRY.register("fadige", FadigeMobEffect::new);
    public static final RegistryObject<MobEffect> UNTRANSFUR = REGISTRY.register("untransfur", UntransfurMobEffect::new);
    public static final RegistryObject<MobEffect> LATEX_SOLVENT = REGISTRY.register("latex_solvent", LatexSolventMobEffect::new);
    public static final RegistryObject<MobEffect> LATEX_CONTAMINATION = REGISTRY.register("latex_contamination", LatexContaminationMobEffect::new);
    public static final RegistryObject<MobEffect> TRANSFUR_SICKNESS = REGISTRY.register("transfur_sickness", TransfurSicknessMobEffect::new);
    public static final RegistryObject<MobEffect> LATEX_EXPOSURE = REGISTRY.register("latex_exposure", LatexExposureMobEffect::new);
}

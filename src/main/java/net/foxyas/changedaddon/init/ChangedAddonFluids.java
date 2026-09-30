package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.fluid.LitixCammoniaFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChangedAddonFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ChangedAddonMod.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ChangedAddonMod.MODID);

    public static final RegistryObject<FluidType> LITIX_CAMMONIA_FLUID_TYPE = FLUID_TYPES.register("litix_cammonia_fluid_type", LitixCammoniaFluid.FluidType::new);

    public static final RegistryObject<Fluid> LITIX_CAMMONIA_FLUID = FLUIDS.register("litix_cammonia_fluid", LitixCammoniaFluid.Source::new);
    public static final RegistryObject<Fluid> FLOWING_LITIX_CAMMONIA_FLUID = FLUIDS.register("flowing_litix_cammonia_fluid", LitixCammoniaFluid.Flowing::new);

}

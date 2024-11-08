
package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonModBlockEntities;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.ltxprogrammer.changed.init.ChangedRegistry.ABILITY;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonAbilitys /*extends ChangedAbilities*/ {

	public static final DeferredRegister<AbstractAbility<?>> REGISTRY = ABILITY.createDeferred(ChangedAddonMod.MODID);
	public static final RegistryObject<LeapAbility> LEAP = REGISTRY.register("leap", LeapAbility::new);
	public static final RegistryObject<ThunderBoltAbility> THUNDERBOLT = REGISTRY.register("thunderbolt",ThunderBoltAbility::new);
	public static final RegistryObject<PsychicPulseAbility> PSYCHIC_PULSE = REGISTRY.register("psychic_pulse",PsychicPulseAbility::new);
	public static final RegistryObject<PsychicHoldAbility> PSYCHIC_HOLD = REGISTRY.register("psychic_hold",PsychicHoldAbility::new);
	public static final RegistryObject<ShockWaveAbility> SHOCKWAVE = REGISTRY.register("shock_wave",ShockWaveAbility::new);
	public static final RegistryObject<DodgeAbility> DODGE = REGISTRY.register("dodge",DodgeAbility::new);
	public static final RegistryObject<CarryAbility> CARRY = REGISTRY.register("carry",CarryAbility::new);
	public static final RegistryObject<DissolveAbility> DISSOLVE = REGISTRY.register("warp",DissolveAbility::new);
	public static final RegistryObject<WitherWaveAbility> WITHER_WAVE = REGISTRY.register("wither_wave",WitherWaveAbility::new);
	public static final RegistryObject<DazedPuddleAbility> DAZED_PUDDLE_ABILITY = REGISTRY.register("dazed_puddle",DazedPuddleAbility::new);

	@SubscribeEvent
	public static void registerAbiltiys(FMLConstructModEvent event) {
		ChangedAddonAbilitys.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
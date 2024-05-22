
package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonModBlockEntities;
import net.foxyas.changedaddon.procedures.LeapAbilityProcedure;
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
	public static final RegistryObject<LeapAbilityProcedure> LEAP = REGISTRY.register("leap", LeapAbilityProcedure::new);
	public static final RegistryObject<ThunderBoltAbility> THUNDERBOLT = REGISTRY.register("thunderbolt",ThunderBoltAbility::new);

	@SubscribeEvent
	public static void registerAbiltiys(FMLConstructModEvent event) {
		ChangedAddonAbilitys.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
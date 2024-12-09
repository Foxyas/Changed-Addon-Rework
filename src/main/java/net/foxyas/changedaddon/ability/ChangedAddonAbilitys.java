
package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

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
	public static final RegistryObject<SoftenAbility> SOFTEN_ABILITY = REGISTRY.register("soften",SoftenAbility::new);
	public static final RegistryObject<CustomInteraction> CUSTOM_INTERACTION = REGISTRY.register("custom_interaction",CustomInteraction::new);
	public static final RegistryObject<TurnFeralSnepAbility> TURN_FERAL_SNEP = REGISTRY.register("turn_feral", TurnFeralSnepAbility::new);
	public static final RegistryObject<WingFlapAbility> WING_FLAP_ABILITY = REGISTRY.register("wing_flap", WingFlapAbility::new);

	public static List<EntityType<?>> getCanGlideEntites(){
		//["form_dark_dragon", "form_dark_latex_yufeng", "form_latex_pink_yuin_dragon", "form_latex_red_dragon"]
		return List.of(ChangedEntities.DARK_LATEX_YUFENG.get(),ChangedEntities.LATEX_PINK_YUIN_DRAGON.get(),ChangedEntities.DARK_DRAGON.get(),ChangedEntities.LATEX_RED_DRAGON.get());
	}
	public static void addUniversalAbilities(TransfurVariant.UniversalAbilitiesEvent event){
		event.addAbility(event.isOfTag(ChangedTags.EntityTypes.LATEX).and(event.isNotOfTag(ChangedTags.EntityTypes.PARTIAL_LATEX)), SOFTEN_ABILITY);
		event.addAbility(entityType -> getCanGlideEntites().contains(entityType), WING_FLAP_ABILITY);
	}

	@SubscribeEvent
	public static void registerAbilities(FMLConstructModEvent event) {
		ChangedAddonAbilitys.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
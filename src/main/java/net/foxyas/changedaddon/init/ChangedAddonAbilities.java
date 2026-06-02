package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.*;
import net.foxyas.changedaddon.ability.handle.dodgeTypes.TeleportDodgeType;
import net.foxyas.changedaddon.ability.handle.dodgeTypes.CounterDodgeType;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.client.AbilityColors;
import net.ltxprogrammer.changed.client.ChangedClient;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.ltxprogrammer.changed.init.ChangedRegistry.ABILITY;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonAbilities {

    public static final DeferredRegister<AbstractAbility<?>> REGISTRY = ABILITY.createDeferred(ChangedAddonMod.MODID);

    public static final RegistryObject<DashAbility> DASH = REGISTRY.register("dash", DashAbility::new);
    public static final RegistryObject<LeapAbility> LEAP = REGISTRY.register("leap", LeapAbility::new);
    public static final RegistryObject<ThunderBoltAbility> THUNDERBOLT = REGISTRY.register("thunderbolt", ThunderBoltAbility::new);
    public static final RegistryObject<ThunderPathAbility> THUNDER_PATH = REGISTRY.register("thunder_path", ThunderPathAbility::new);
    public static final RegistryObject<PsychicPulseAbility> PSYCHIC_PULSE = REGISTRY.register("psychic_pulse", PsychicPulseAbility::new);
    public static final RegistryObject<PsychicHoldAbility> PSYCHIC_HOLD = REGISTRY.register("psychic_hold", PsychicHoldAbility::new);
    public static final RegistryObject<ShockWaveAbility> SHOCKWAVE = REGISTRY.register("shock_wave", ShockWaveAbility::new);
    public static final RegistryObject<DodgeAbility> DODGE = REGISTRY.register("dodge", DodgeAbility::new);
    public static final RegistryObject<DodgeAbility> TELEPORT_DODGE = REGISTRY.register("teleport_dodge", () -> new DodgeAbility(TeleportDodgeType.INSTANCE));
    public static final RegistryObject<DodgeAbility> COUNTER_DODGE = REGISTRY.register("counter_dodge", () -> new DodgeAbility(1, CounterDodgeType.COUNTER));
    public static final RegistryObject<CarryAbility> CARRY = REGISTRY.register("carry", CarryAbility::new);
    public static final RegistryObject<DissolveAbility> DISSOLVE = REGISTRY.register("warp", DissolveAbility::new);
    public static final RegistryObject<WitherWaveAbility> WITHER_WAVE = REGISTRY.register("wither_wave", WitherWaveAbility::new);
    public static final RegistryObject<DazedPuddleAbility> DAZED_PUDDLE = REGISTRY.register("dazed_puddle", DazedPuddleAbility::new);
    public static final RegistryObject<SoftenAbility> SOFTEN_ABILITY = REGISTRY.register("soften", SoftenAbility::new);
    public static final RegistryObject<CustomInteraction> CUSTOM_INTERACTION = REGISTRY.register("custom_interaction", CustomInteraction::new);
    public static final RegistryObject<TurnFeralSnepAbility> TURN_FERAL_SNEP = REGISTRY.register("turn_feral", TurnFeralSnepAbility::new);
    public static final RegistryObject<WingFlapAbility> WING_FLAP_ABILITY = REGISTRY.register("wing_flap", WingFlapAbility::new);
    public static final RegistryObject<ClawsAbility> CLAWS = REGISTRY.register("claws", ClawsAbility::new);
    public static final RegistryObject<PsychicGrab> PSYCHIC_GRAB = REGISTRY.register("psychic_grab", PsychicGrab::new);
    public static final RegistryObject<AdvancedHearingAbility> ADVANCED_HEARING = REGISTRY.register("advanced_hearing", AdvancedHearingAbility::new);
    public static final RegistryObject<TeleportAbility> TELEPORT = REGISTRY.register("teleport", TeleportAbility::new);
    public static final RegistryObject<UnfuseAbility> UNFUSE = REGISTRY.register("unfuse", UnfuseAbility::new);
    public static final RegistryObject<ToggleClimbAbility> TOGGLE_CLIMB = REGISTRY.register("toggle_climb", ToggleClimbAbility::new);
    public static final RegistryObject<PassiveAbility> APPLY_REGENERATION_PASSIVE = REGISTRY.register("passive_regeneration", () -> new PassiveAbility((entity) -> PassiveAbility.ApplyMobEffect(entity, new MobEffectInstance(MobEffects.REGENERATION, 60, 0, false, false, true))));
    public static final RegistryObject<WindControlAbility> WIND_CONTROL = REGISTRY.register("wind_control", WindControlAbility::new);
    public static final RegistryObject<WindPassiveAbility> WIND_PASSIVE = REGISTRY.register("wind_control_passive", WindPassiveAbility::new);
    public static final RegistryObject<WitherGrenadeAbility> WITHER_GRENADE = REGISTRY.register("wither_grenade", WitherGrenadeAbility::new);
    public static final RegistryObject<SonarAbility> SONAR = REGISTRY.register("sonar", SonarAbility::new);
    public static final RegistryObject<LuminaraFireballAbility> LUMINARA_FIREBALL = REGISTRY.register("luminara_fireball", LuminaraFireballAbility::new);
    public static final RegistryObject<PollenCarryAbility> POLLEN_CARRY = REGISTRY.register("pollen_carry", PollenCarryAbility::new);
    public static final RegistryObject<SummonDLPupAbility> SUMMON_DL_PUP = REGISTRY.register("summon_dl_pup", SummonDLPupAbility::new);

    public static void addUniversalAbilities(TransfurVariant.UniversalAbilitiesEvent event) {
        event.addAbility(event.isOfTag(ChangedTags.EntityTypes.LATEX).and(event.isNotOfTag(ChangedTags.EntityTypes.PARTIAL_LATEX)), SOFTEN_ABILITY);
        event.addAbility(event.isOfTag(ChangedAddonTags.EntityTypes.HAS_CLAWS), CLAWS);
        event.addAbility(event.isOfTag(ChangedAddonTags.EntityTypes.DRAGON_ENTITIES), WING_FLAP_ABILITY);
        event.addAbility(entityType ->
                entityType.equals(ChangedEntities.LATEX_BEE.get())
                        || entityType.is(ChangedAddonTags.EntityTypes.BEE_ENTITIES), POLLEN_CARRY);
    }

    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        AbilityColors colors = ChangedClient.abilityColors.getOrThrow();
        colors.register(WingFlapAbility::getColor, WING_FLAP_ABILITY.get());
        colors.register(UnfuseAbility::getColor, UNFUSE.get());
        colors.register(ClawsAbility::getColor, CLAWS.get());
        colors.register(LuminaraFireballAbility::getColor, LUMINARA_FIREBALL.get());
    }
}
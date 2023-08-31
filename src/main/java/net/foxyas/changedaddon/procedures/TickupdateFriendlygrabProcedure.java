package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

import javax.annotation.Nullable;

import java.util.function.Function;

import com.mojang.util.UUIDTypeAdapter;

@Mod.EventBusSubscriber
public class TickupdateFriendlygrabProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level, event.player);
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		String KeyBind = "";
		String TransLate = "";
		String TransfurText = "";
		String HumanText = "";
		Entity entityFriendlyGrabbing = null;
		HumanText = new TranslatableComponent("translation.FriendlyGrab.HumanText").getString();
		TransfurText = new TranslatableComponent("translation.FriendlyGrab.TransfurText").getString();
		entityFriendlyGrabbing = world instanceof ServerLevel _serverLevelForGettingEntity ? (new Function<String, Entity>() {
			@Override
			public Entity apply(String _uuidForEntity) {
				Entity _entityFromUUID = null;
				try {
					_entityFromUUID = _serverLevelForGettingEntity.getEntity(UUIDTypeAdapter.fromString(_uuidForEntity));
				} catch (Exception e) {
					_entityFromUUID = null;
				}
				return _entityFromUUID;
			}
		}).apply(((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).FriendlyGrabbing)) : null;
		if ((entity instanceof ServerPlayer || entity instanceof Player) && new Object() {
			public boolean checkGamemode(Entity _ent) {
				if (_ent instanceof ServerPlayer _serverPlayer) {
					return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
				} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
					return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
				}
				return false;
			}
		}.checkGamemode(entity)) {
			if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.INFRIENDLYGRABEFFECT.get()) : false) {
				if (!(entityFriendlyGrabbing == null)) {
					if ((entityFriendlyGrabbing instanceof ServerPlayer || entityFriendlyGrabbing instanceof Player)
							&& (entityFriendlyGrabbing instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.INFRIENDLYGRAB.get()) : false)
							&& (entityFriendlyGrabbing.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true && !(new Object() {
								public boolean checkGamemode(Entity _ent) {
									if (_ent instanceof ServerPlayer _serverPlayer) {
										return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
									} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
										return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
												&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
									}
									return false;
								}
							}.checkGamemode(entityFriendlyGrabbing)) && (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == false) {
						if (!(entity == entityFriendlyGrabbing)) {
							{
								Entity _ent = entity;
								if (!_ent.level.isClientSide() && _ent.getServer() != null)
									_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
											("spectate " + entityFriendlyGrabbing.getDisplayName().getString() + " " + entity.getDisplayName().getString()));
							}
						}
						if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
							if (!((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).FriendlyGrabKeybind).equals("")) {
								if (entity instanceof Player _player && !_player.level.isClientSide())
									_player.displayClientMessage(new TextComponent(
											(HumanText.replace("(key)", (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).FriendlyGrabKeybind))), true);
							} else {
								if (entity instanceof Player _player && !_player.level.isClientSide())
									_player.displayClientMessage(new TextComponent((HumanText.replace("(key)", "Friendly Graboff"))), true);
							}
						}
						if ((entityFriendlyGrabbing.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
							if (!((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).FriendlyGrabKeybind).equals("")) {
								if (entityFriendlyGrabbing instanceof Player _player && !_player.level.isClientSide())
									_player.displayClientMessage(
											new TextComponent(
													(TransfurText.replace("(key)", (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).FriendlyGrabKeybind))),
											true);
							} else {
								if (entityFriendlyGrabbing instanceof Player _player && !_player.level.isClientSide())
									_player.displayClientMessage(new TextComponent((TransfurText.replace("(key)", "Friendly Graboff"))), true);
							}
						}
						if (entityFriendlyGrabbing instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, false, false));
						if (entityFriendlyGrabbing instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0, false, false));
						if (entityFriendlyGrabbing instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false));
						if (entityFriendlyGrabbing instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, false, false));
						if (entityFriendlyGrabbing instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 100, 0, false, false));
						if (entityFriendlyGrabbing instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.INFRIENDLYGRAB.get(), 100, 0, false, false));
						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.INFRIENDLYGRAB.get(), 200, 0, false, false));
						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.INFRIENDLYGRABEFFECT.get(), 100, 0, false, false));
						if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.REGENERATION) : false)) {
							if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
								_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, false, false));
						}
					}
				} else {
					FriendlyGraboffOnKeyPressedProcedure.execute(world, entity);
				}
			}
		}
	}
}

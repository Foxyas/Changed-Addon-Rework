package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.MenuProvider;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import net.foxyas.changedaddon.world.inventory.FriendlyTransfurGuiMenu;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

import javax.annotation.Nullable;

import java.util.Iterator;

import io.netty.buffer.Unpooled;

@Mod.EventBusSubscriber
public class RightclickFriendlyGrabProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getHand() != event.getPlayer().getUsedItemHand())
			return;
		execute(event, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getTarget(), event.getPlayer());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		execute(null, world, x, y, z, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (!(new Object() {
			public boolean checkGamemode(Entity _ent) {
				if (_ent instanceof ServerPlayer _serverPlayer) {
					return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
				} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
					return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
				}
				return false;
			}
		}.checkGamemode(entity)) && !(new Object() {
			public boolean checkGamemode(Entity _ent) {
				if (_ent instanceof ServerPlayer _serverPlayer) {
					return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
				} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
					return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
				}
				return false;
			}
		}.checkGamemode(entity))) {
			if (entity instanceof Player || entity instanceof ServerPlayer) {
				if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Friendly_mode == true
						&& (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true
						&& (sourceentity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == false) {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
						{
							Entity _ent = sourceentity;
							_ent.teleportTo((entity.getX()), (entity.getY()), (entity.getZ()));
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport((entity.getX()), (entity.getY()), (entity.getZ()), _ent.getYRot(), _ent.getXRot());
						}
						if (sourceentity instanceof ServerPlayer _player)
							_player.setGameMode(GameType.SPECTATOR);
						if (sourceentity instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.INFRIENDLYGRABEFFECT.get(), 50, 0, false, false));
						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.INFRIENDLYGRAB.get(), 10000000, 0, false, false));
						if (sourceentity instanceof LivingEntity _entity && !_entity.level.isClientSide())
							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.INFRIENDLYGRAB.get(), 10000000, 0, false, false));
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, new BlockPos(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
					} else {
						if (sourceentity instanceof Player _player && !_player.level.isClientSide())
							_player.displayClientMessage(new TextComponent("they are organic so you just \u00A7chug\u00A7r they"), true);
						if (entity instanceof Player _player && !_player.level.isClientSide())
							_player.displayClientMessage(new TextComponent((ForgeRegistries.ENTITIES.getKey(sourceentity.getType()).toString() + " \u00A7chug\u00A7r you")), true);
						if (entity instanceof ServerPlayer _player) {
							Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:hug"));
							AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
							if (!_ap.isDone()) {
								Iterator _iterator = _ap.getRemainingCriteria().iterator();
								while (_iterator.hasNext())
									_player.getAdvancements().award(_adv, (String) _iterator.next());
							}
						}
						if (sourceentity instanceof ServerPlayer _player) {
							Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:hug"));
							AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
							if (!_ap.isDone()) {
								Iterator _iterator = _ap.getRemainingCriteria().iterator();
								while (_iterator.hasNext())
									_player.getAdvancements().award(_adv, (String) _iterator.next());
							}
						}
					}
				}
				if ((sourceentity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Friendly_mode == true
						&& (sourceentity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true
						&& (sourceentity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).can_grab == true
						&& (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == false) {
					if ((sourceentity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
						if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).wantfriendlygrab == true) {
							{
								if (entity instanceof ServerPlayer _ent) {
									BlockPos _bpos = new BlockPos(x, y, z);
									NetworkHooks.openGui((ServerPlayer) _ent, new MenuProvider() {
										@Override
										public Component getDisplayName() {
											return new TextComponent("FriendlyTransfurGui");
										}

										@Override
										public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
											return new FriendlyTransfurGuiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
										}
									}, _bpos);
								}
							}
						} else {
							if (sourceentity instanceof Player _player && !_player.level.isClientSide())
								_player.displayClientMessage(new TextComponent("they dont want"), true);
						}
					} else {
						if (sourceentity instanceof Player _player && !_player.level.isClientSide())
							_player.displayClientMessage(new TextComponent("you are organic so you just \u00A7chug\u00A7r they"), true);
						if (entity instanceof Player _player && !_player.level.isClientSide())
							_player.displayClientMessage(new TextComponent((ForgeRegistries.ENTITIES.getKey(sourceentity.getType()).toString() + " \u00A7chug\u00A7r you")), true);
						if (entity instanceof ServerPlayer _player) {
							Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:hug"));
							AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
							if (!_ap.isDone()) {
								Iterator _iterator = _ap.getRemainingCriteria().iterator();
								while (_iterator.hasNext())
									_player.getAdvancements().award(_adv, (String) _iterator.next());
							}
						}
						if (sourceentity instanceof ServerPlayer _player) {
							Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:hug"));
							AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
							if (!_ap.isDone()) {
								Iterator _iterator = _ap.getRemainingCriteria().iterator();
								while (_iterator.hasNext())
									_player.getAdvancements().award(_adv, (String) _iterator.next());
							}
						}
					}
				}
			}
		} else if (new Object() {
			public boolean checkGamemode(Entity _ent) {
				if (_ent instanceof ServerPlayer _serverPlayer) {
					return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
				} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
					return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
				}
				return false;
			}
		}.checkGamemode(entity)) {
			if (sourceentity instanceof Player _player && !_player.level.isClientSide())
				_player.displayClientMessage(new TextComponent("you cant transfur they"), true);
		}
	}
}

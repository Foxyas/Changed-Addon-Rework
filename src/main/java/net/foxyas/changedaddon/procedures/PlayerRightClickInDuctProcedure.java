package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModGameRules;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PlayerRightClickInDuctProcedure {
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getHand() != event.getPlayer().getUsedItemHand())
			return;
		execute(event, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getWorld().getBlockState(event.getPos()), event.getPlayer());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		execute(null, world, x, y, z, blockstate, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		boolean can_leave = false;
		if (world.getLevelData().getGameRules().getBoolean(ChangedAddonModGameRules.CHANGED_ADDON_DUCT_MECANIC) == true) {
			if (blockstate.getBlock() == ForgeRegistries.BLOCKS.getValue(new ResourceLocation("changed:duct"))) {
				if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("vented") instanceof BooleanProperty _withbp6 ? blockstate.setValue(_withbp6, true) : blockstate)) {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true
							&& (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
						if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).enter_in_duct == false) {
							if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.FADIGE.get()) : false)) {
								{
									Entity _ent = entity;
									_ent.teleportTo((x + 0.5), (y + 0.2), (z + 0.5));
									if (_ent instanceof ServerPlayer _serverPlayer)
										_serverPlayer.connection.teleport((x + 0.5), (y + 0.2), (z + 0.5), _ent.getYRot(), _ent.getXRot());
								}
								entity.setSprinting(false);
								if (world instanceof Level _level) {
									if (!_level.isClientSide()) {
										_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1);
									} else {
										_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1, false);
									}
								}
								{
									boolean _setval = true;
									entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
										capability.enter_in_duct = _setval;
										capability.syncPlayerVariables(entity);
									});
								}
								if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
									_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
							} else {
								if (entity instanceof Player _player && !_player.level.isClientSide())
									_player.displayClientMessage(new TextComponent("You are too tired for this"), true);
							}
						} else {
							if ((world.getBlockState(new BlockPos(entity.getX(), entity.getY(), entity.getZ()))) == (blockstate.getBlock().getStateDefinition().getProperty("vented") instanceof BooleanProperty _withbp19
									? blockstate.setValue(_withbp19, true)
									: blockstate)) {
								if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.FADIGE.get()) : false)) {
									if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("up") instanceof BooleanProperty _withbp24 ? blockstate.setValue(_withbp24, true) : blockstate)
											&& blockstate == (blockstate.getBlock().getStateDefinition().getProperty("down") instanceof BooleanProperty _withbp28 ? blockstate.setValue(_withbp28, true) : blockstate)) {
										if ((entity.getDirection()) == Direction.EAST) {
											if ((world.getBlockState(new BlockPos(entity.getX() + 1, entity.getY(), entity.getZ()))).getBlock() == Blocks.AIR) {
												{
													Entity _ent = entity;
													_ent.teleportTo((entity.getX() + 0.65), (entity.getY()), (entity.getZ()));
													if (_ent instanceof ServerPlayer _serverPlayer)
														_serverPlayer.connection.teleport((entity.getX() + 0.65), (entity.getY()), (entity.getZ()), _ent.getYRot(), _ent.getXRot());
												}
												if (world instanceof Level _level) {
													if (!_level.isClientSide()) {
														_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1);
													} else {
														_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1, false);
													}
												}
												if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
											}
										}
										if ((entity.getDirection()) == Direction.WEST) {
											if ((world.getBlockState(new BlockPos(entity.getX() - 1, entity.getY(), entity.getZ()))).getBlock() == Blocks.AIR) {
												{
													Entity _ent = entity;
													_ent.teleportTo((entity.getX() - 0.65), (entity.getY()), (entity.getZ()));
													if (_ent instanceof ServerPlayer _serverPlayer)
														_serverPlayer.connection.teleport((entity.getX() - 0.65), (entity.getY()), (entity.getZ()), _ent.getYRot(), _ent.getXRot());
												}
												if (world instanceof Level _level) {
													if (!_level.isClientSide()) {
														_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1);
													} else {
														_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1, false);
													}
												}
												if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
											}
										}
									}
									if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("north") instanceof BooleanProperty _withbp58 ? blockstate.setValue(_withbp58, true) : blockstate)
											&& blockstate == (blockstate.getBlock().getStateDefinition().getProperty("south") instanceof BooleanProperty _withbp62 ? blockstate.setValue(_withbp62, true) : blockstate)) {
										if ((entity.getDirection()) == Direction.EAST) {
											if ((world.getBlockState(new BlockPos(entity.getX() + 1, entity.getY(), entity.getZ()))).getBlock() == Blocks.AIR) {
												{
													Entity _ent = entity;
													_ent.teleportTo((entity.getX() + 0.65), (entity.getY()), (entity.getZ()));
													if (_ent instanceof ServerPlayer _serverPlayer)
														_serverPlayer.connection.teleport((entity.getX() + 0.65), (entity.getY()), (entity.getZ()), _ent.getYRot(), _ent.getXRot());
												}
												if (world instanceof Level _level) {
													if (!_level.isClientSide()) {
														_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1);
													} else {
														_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1, false);
													}
												}
												if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
											}
										}
										if ((entity.getDirection()) == Direction.WEST) {
											if ((world.getBlockState(new BlockPos(entity.getX() - 1, entity.getY(), entity.getZ()))).getBlock() == Blocks.AIR) {
												{
													Entity _ent = entity;
													_ent.teleportTo((entity.getX() - 0.65), (entity.getY()), (entity.getZ()));
													if (_ent instanceof ServerPlayer _serverPlayer)
														_serverPlayer.connection.teleport((entity.getX() - 0.65), (entity.getY()), (entity.getZ()), _ent.getYRot(), _ent.getXRot());
												}
												if (world instanceof Level _level) {
													if (!_level.isClientSide()) {
														_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1);
													} else {
														_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1, false);
													}
												}
												if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
											}
										}
									}
									if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("east") instanceof BooleanProperty _withbp92 ? blockstate.setValue(_withbp92, true) : blockstate)
											&& blockstate == (blockstate.getBlock().getStateDefinition().getProperty("west") instanceof BooleanProperty _withbp96 ? blockstate.setValue(_withbp96, true) : blockstate)) {
										if ((entity.getDirection()) == Direction.SOUTH) {
											if ((world.getBlockState(new BlockPos(entity.getX(), entity.getY(), entity.getZ() + 1))).getBlock() == Blocks.AIR) {
												{
													Entity _ent = entity;
													_ent.teleportTo((entity.getX()), (entity.getY()), (entity.getZ() + 0.65));
													if (_ent instanceof ServerPlayer _serverPlayer)
														_serverPlayer.connection.teleport((entity.getX()), (entity.getY()), (entity.getZ() + 0.65), _ent.getYRot(), _ent.getXRot());
												}
												if (world instanceof Level _level) {
													if (!_level.isClientSide()) {
														_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1);
													} else {
														_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1, false);
													}
												}
												if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
											}
										}
										if ((entity.getDirection()) == Direction.NORTH) {
											if ((world.getBlockState(new BlockPos(entity.getX(), entity.getY(), entity.getZ() - 1))).getBlock() == Blocks.AIR) {
												{
													Entity _ent = entity;
													_ent.teleportTo((entity.getX()), (entity.getY()), (entity.getZ() - 0.65));
													if (_ent instanceof ServerPlayer _serverPlayer)
														_serverPlayer.connection.teleport((entity.getX()), (entity.getY()), (entity.getZ() - 0.65), _ent.getYRot(), _ent.getXRot());
												}
												if (world instanceof Level _level) {
													if (!_level.isClientSide()) {
														_level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1);
													} else {
														_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.NEUTRAL, 2, 1, false);
													}
												}
												if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
											}
										}
									}
								} else {
									if (entity instanceof Player _player && !_player.level.isClientSide())
										_player.displayClientMessage(new TextComponent("You are too tired for this"), true);
								}
							}
						}
					}
				}
			}
		}
	}
}

package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.GrabclickguiMenu;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModGameRules;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

import io.netty.buffer.Unpooled;

public class GrabProcedureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		DamageSource assimilation = null;
		{
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (!((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).equals("")) {
					if (world.getLevelData().getGameRules().getBoolean(ChangedAddonModGameRules.ALLOW_PLAYER_GRAB) == true) {
						if (entityiterator.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:humanoids")))) {
							if (!(entityiterator == entity)) {
								if ((new Object() {
									public Entity func(Entity player, double entityReach) {
										double distance = entityReach * entityReach;
										Vec3 eyePos = player.getEyePosition(1.0f);
										HitResult hitResult = entity.pick(entityReach, 1.0f, false);
										if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
											distance = hitResult.getLocation().distanceToSqr(eyePos);
											double blockReach = 5;
											if (distance > blockReach * blockReach) {
												Vec3 pos = hitResult.getLocation();
												hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), new BlockPos(pos));
											}
										}
										Vec3 viewVec = player.getViewVector(1.0F);
										Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
										AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
										EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> {
											return !p_234237_.isSpectator();
										}, distance);
										if (entityhitresult != null) {
											Entity entity1 = entityhitresult.getEntity();
											Vec3 targetPos = entityhitresult.getLocation();
											double distanceToTarget = eyePos.distanceToSqr(targetPos);
											if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
												hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), new BlockPos(targetPos));
											} else if (distanceToTarget < distance) {
												hitResult = entityhitresult;
											}
										}
										if (hitResult.getType() == HitResult.Type.ENTITY) {
											return ((EntityHitResult) hitResult).getEntity();
										}
										return null;
									}
								}.func(entity, 3)) == null) {
									{
										return;
									}
								} else {
									if (entityiterator == (new Object() {
										public Entity func(Entity player, double entityReach) {
											double distance = entityReach * entityReach;
											Vec3 eyePos = player.getEyePosition(1.0f);
											HitResult hitResult = entity.pick(entityReach, 1.0f, false);
											if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
												distance = hitResult.getLocation().distanceToSqr(eyePos);
												double blockReach = 5;
												if (distance > blockReach * blockReach) {
													Vec3 pos = hitResult.getLocation();
													hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), new BlockPos(pos));
												}
											}
											Vec3 viewVec = player.getViewVector(1.0F);
											Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
											AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
											EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> {
												return !p_234237_.isSpectator();
											}, distance);
											if (entityhitresult != null) {
												Entity entity1 = entityhitresult.getEntity();
												Vec3 targetPos = entityhitresult.getLocation();
												double distanceToTarget = eyePos.distanceToSqr(targetPos);
												if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
													hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), new BlockPos(targetPos));
												} else if (distanceToTarget < distance) {
													hitResult = entityhitresult;
												}
											}
											if (hitResult.getType() == HitResult.Type.ENTITY) {
												return ((EntityHitResult) hitResult).getEntity();
											}
											return null;
										}
									}.func(entity, 3))) {
										if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.FADIGE.get()) : false)) {
											if (!(new Object() {
												public boolean checkGamemode(Entity _ent) {
													if (_ent instanceof ServerPlayer _serverPlayer) {
														return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
													} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
														return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
																&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
													}
													return false;
												}
											}.checkGamemode(entityiterator) || new Object() {
												public boolean checkGamemode(Entity _ent) {
													if (_ent instanceof ServerPlayer _serverPlayer) {
														return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
													} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
														return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
																&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
													}
													return false;
												}
											}.checkGamemode(entityiterator) || (entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true)) {
												if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).can_grab == true) {
													if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
														if (!((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm)
																.equals("changed:form_dark_latex_pup")) {
															if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Friendly_mode == false) {
																if (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.GRAB_RESISTANCE.get(),
																		(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)) < 4) {
																	{
																		Entity _ent = entityiterator;
																		_ent.teleportTo((entity.getX()), (entity.getY()), (entity.getZ()));
																		if (_ent instanceof ServerPlayer _serverPlayer)
																			_serverPlayer.connection.teleport((entity.getX()), (entity.getY()), (entity.getZ()), _ent.getYRot(), _ent.getXRot());
																	}
																	if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																		_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 150, 1, false, false));
																	if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																		_entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 150, 4, false, false));
																	if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																		_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.GRABEFFECT.get(), 150, 4, false, false));
																	if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
																		_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.GRABEFFECT.get(), 150, 4, false, false));
																	if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
																		_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 150, 4, false, false));
																	if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
																		_entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 150, 4, false, false));
																	if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																		_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 300, 1, false, false));
																	{
																		Entity _ent = entity;
																		if (!_ent.level.isClientSide() && _ent.getServer() != null)
																			_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
																					"playsound changed_addon:grab_start_sound neutral Dev ~ ~ ~ 2 1 0");
																	}
																	{
																		if (entityiterator instanceof ServerPlayer _ent) {
																			BlockPos _bpos = new BlockPos(x, y, z);
																			NetworkHooks.openGui((ServerPlayer) _ent, new MenuProvider() {
																				@Override
																				public Component getDisplayName() {
																					return new TextComponent("Grabclickgui");
																				}

																				@Override
																				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
																					return new GrabclickguiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
																				}
																			}, _bpos);
																		}
																	}
																	assimilation = (new EntityDamageSource("assimilation" + ".player", entity)).bypassArmor();
																	new Object() {
																		private int ticks = 0;
																		private float waitTicks;
																		private LevelAccessor world;

																		public void start(LevelAccessor world, int waitTicks) {
																			this.waitTicks = waitTicks;
																			MinecraftForge.EVENT_BUS.register(this);
																			this.world = world;
																		}

																		@SubscribeEvent
																		public void tick(TickEvent.ServerTickEvent event) {
																			if (event.phase == TickEvent.Phase.END) {
																				this.ticks += 1;
																				if (this.ticks >= this.waitTicks)
																					run();
																			}
																		}

																		private void run() {
																			if (entityiterator.isAlive()) {
																				if (entity.isAlive()) {
																					if (entityiterator.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:humanoids")))
																							&& !(entityiterator instanceof Player || entityiterator instanceof ServerPlayer)) {
																						if (entityiterator instanceof LivingEntity _entity)
																							_entity.hurt(new DamageSource("assimilation").bypassArmor(), 100);
																						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																							_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 7200, 1, false, false));
																						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																							_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 7200, 1, false, false));
																						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																							_entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 7200, 4, false, false));
																						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																							_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 7200, 1, false, false));
																						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																							_entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 7200, 4, false, false));
																						if (entity instanceof Player _player)
																							_player.getFoodData().setFoodLevel((int) ((entity instanceof Player _plr ? _plr.getFoodData().getFoodLevel() : 0) + 5));
																						if (entity instanceof Player _player)
																							_player.getFoodData().setSaturation((float) ((entity instanceof Player _plr ? _plr.getFoodData().getSaturationLevel() : 0) + 10));
																						if (entity instanceof LivingEntity _entity)
																							_entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
																						if (entity instanceof LivingEntity _entity)
																							_entity.removeEffect(MobEffects.WEAKNESS);
																						if (entity instanceof LivingEntity _entity)
																							_entity.removeEffect(ChangedAddonModMobEffects.GRABEFFECT.get());
																						if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																							_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 300, 1, false, false));
																						if (world instanceof Level _level) {
																							if (!_level.isClientSide()) {
																								_level.playSound(null, new BlockPos(entity.getX(), entity.getY(), entity.getZ()),
																										ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.PLAYERS, 2, 1);
																							} else {
																								_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()),
																										ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.PLAYERS, 2, 1, false);
																							}
																						}
																					}
																					if (entityiterator instanceof Player || entityiterator instanceof ServerPlayer) {
																						if ((entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
																								.orElse(new ChangedAddonModVariables.PlayerVariables())).escape_progress >= (entityiterator
																										.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
																										.orElse(new ChangedAddonModVariables.PlayerVariables())).GrabEscapeClick) {
																							if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																								_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 300, 1, false, false));
																							if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																								_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, false, false));
																							for (int index0 = 0; index0 < 4; index0++) {
																								entity.setDeltaMovement(new Vec3(0, 0, 0));
																							}
																							if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
																								_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false));
																							new Object() {
																								private int ticks = 0;
																								private float waitTicks;
																								private LevelAccessor world;

																								public void start(LevelAccessor world, int waitTicks) {
																									this.waitTicks = waitTicks;
																									MinecraftForge.EVENT_BUS.register(this);
																									this.world = world;
																								}

																								@SubscribeEvent
																								public void tick(TickEvent.ServerTickEvent event) {
																									if (event.phase == TickEvent.Phase.END) {
																										this.ticks += 1;
																										if (this.ticks >= this.waitTicks)
																											run();
																									}
																								}

																								private void run() {
																									if (entity instanceof LivingEntity _entity)
																										_entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
																									if (entity instanceof LivingEntity _entity)
																										_entity.removeEffect(MobEffects.WEAKNESS);
																									MinecraftForge.EVENT_BUS.unregister(this);
																								}
																							}.start(world, 80);
																							if (entity instanceof LivingEntity _entity)
																								_entity.removeEffect(ChangedAddonModMobEffects.GRABEFFECT.get());
																							if (entityiterator instanceof LivingEntity _entity)
																								_entity.removeEffect(ChangedAddonModMobEffects.GRABEFFECT.get());
																							if (entityiterator instanceof LivingEntity _entity)
																								_entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
																							if (entityiterator instanceof LivingEntity _entity)
																								_entity.removeEffect(MobEffects.WEAKNESS);
																						} else {
																							if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
																									.orElse(new ChangedAddonModVariables.PlayerVariables())).assmilation == false
																									&& !((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
																											.orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:special")) {
																								{
																									Entity _ent = entityiterator;
																									if (!_ent.level.isClientSide() && _ent.getServer() != null)
																										_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
																												("transfur @s " + (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
																														.orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
																								}
																								if (entity instanceof Player _player)
																									_player.getFoodData().setFoodLevel((int) ((entity instanceof Player _plr ? _plr.getFoodData().getFoodLevel() : 0) - 4));
																								if (entity instanceof Player _player)
																									_player.getFoodData().setSaturation((float) ((entity instanceof Player _plr ? _plr.getFoodData().getSaturationLevel() : 0) - 4));
																							} else {
																								entityiterator.hurt(((new DamageSource("assimilation")).bypassArmor()), 100);
																								if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																									_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 7200, 1, false, false));
																								if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																									_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 7200, 1, false, false));
																								if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																									_entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 7200, 4, false, false));
																								if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																									_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 7200, 1, false, false));
																								if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																									_entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 7200, 4, false, false));
																								if (entity instanceof Player _player)
																									_player.getFoodData().setFoodLevel((int) ((entity instanceof Player _plr ? _plr.getFoodData().getFoodLevel() : 0) + 5));
																								if (entity instanceof Player _player)
																									_player.getFoodData().setSaturation((float) ((entity instanceof Player _plr ? _plr.getFoodData().getSaturationLevel() : 0) + 10));
																								if (world instanceof Level _level) {
																									if (!_level.isClientSide()) {
																										_level.playSound(null, new BlockPos(entity.getX(), entity.getY(), entity.getZ()),
																												ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.PLAYERS, 1, 1);
																									} else {
																										_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()),
																												ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:enter_in_friendly_grab")), SoundSource.PLAYERS, 1, 1, false);
																									}
																								}
																							}
																							if (entity instanceof LivingEntity _entity)
																								_entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
																							if (entity instanceof LivingEntity _entity)
																								_entity.removeEffect(MobEffects.WEAKNESS);
																							if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																								_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 300, 1, false, false));
																							if (entity instanceof LivingEntity _entity)
																								_entity.removeEffect(ChangedAddonModMobEffects.GRABEFFECT.get());
																							if (entityiterator instanceof LivingEntity _entity)
																								_entity.removeEffect(ChangedAddonModMobEffects.GRABEFFECT.get());
																							if (entityiterator instanceof LivingEntity _entity)
																								_entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
																							if (entityiterator instanceof LivingEntity _entity)
																								_entity.removeEffect(MobEffects.WEAKNESS);
																						}
																					}
																				} else {
																					if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
																						_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false));
																					if (entityiterator instanceof LivingEntity _entity)
																						_entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
																					if (entityiterator instanceof LivingEntity _entity)
																						_entity.removeEffect(MobEffects.WEAKNESS);
																					if (entity instanceof LivingEntity _entity)
																						_entity.removeEffect(ChangedAddonModMobEffects.GRABEFFECT.get());
																					if (entityiterator instanceof LivingEntity _entity)
																						_entity.removeEffect(ChangedAddonModMobEffects.GRABEFFECT.get());
																					if (entityiterator instanceof Player _player && !_player.level.isClientSide())
																						_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.event.grab.when.you.die").getString())), true);
																				}
																			} else {
																				if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																					_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 300, 1, false, false));
																				if (entity instanceof LivingEntity _entity)
																					_entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
																				if (entity instanceof LivingEntity _entity)
																					_entity.removeEffect(MobEffects.WEAKNESS);
																				if (entity instanceof LivingEntity _entity)
																					_entity.removeEffect(ChangedAddonModMobEffects.GRABEFFECT.get());
																				if (entityiterator instanceof LivingEntity _entity)
																					_entity.removeEffect(ChangedAddonModMobEffects.GRABEFFECT.get());
																				if (entity instanceof Player _player && !_player.level.isClientSide())
																					_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.event.grab.when.entity.die").getString())), true);
																			}
																			MinecraftForge.EVENT_BUS.unregister(this);
																		}
																	}.start(world, (int) (80 + EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.GRAB_RESISTANCE.get(),
																			(entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)) * 7));
																} else {
																	if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																		_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, false));
																	if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
																		_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 300, 1, false, false));
																	if (entity instanceof Player _player && !_player.level.isClientSide())
																		_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.event.grab.when.grab.immune").getString())), true);
																}
															} else {
																FriendlyGrabProcedure.execute(world, x, y, z, entity);
															}
														} else {
															if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
																if (entity instanceof Player _player && !_player.level.isClientSide())
																	_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.event.grab.when.small").getString())), true);
															}
														}
													} else {
														if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Friendly_mode == false) {
															if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
																if (entity instanceof Player _player && !_player.level.isClientSide())
																	_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.when_is.organic.grab").getString())), true);
															}
														} else {
															FriendlyGrabProcedure.execute(world, x, y, z, entity);
														}
													}
												}
											} else {
												if (new Object() {
													public boolean checkGamemode(Entity _ent) {
														if (_ent instanceof ServerPlayer _serverPlayer) {
															return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
														} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
															return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
																	&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
														}
														return false;
													}
												}.checkGamemode(entityiterator)) {
													if (entity instanceof Player _player && !_player.level.isClientSide())
														_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.grab.when.cant").getString())), true);
												}
												if ((entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true
														&& (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Friendly_mode == true) {
													FriendlyGrabProcedure.execute(world, x, y, z, entity);
												} else if ((entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true
														&& (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Friendly_mode == false) {
													if (entity instanceof Player _player && !_player.level.isClientSide())
														_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.grab.when.cant").getString())), true);
												}
											}
										} else {
											if (entity instanceof Player _player && !_player.level.isClientSide())
												_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.grab.tired").getString())), true);
										}
									}
								}
							}
						}
					} else {
						if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
							if (entity instanceof Player _player && !_player.level.isClientSide())
								_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.feature.grab.isoff").getString())), true);
						}
					}
				}
			}
		}
	}
}

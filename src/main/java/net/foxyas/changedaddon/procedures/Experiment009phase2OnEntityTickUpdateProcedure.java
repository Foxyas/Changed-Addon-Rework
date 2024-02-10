package net.foxyas.changedaddon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class Experiment009phase2OnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		Entity TpTarget = null;
		double randown = 0;
		double Pz = 0;
		double Px = 0;
		double Py = 0;
		double motionZ = 0;
		double deltaZ = 0;
		double distance = 0;
		double deltaX = 0;
		double motionY = 0;
		double deltaY = 0;
		double motionX = 0;
		double maxSpeed = 0;
		double speed = 0;
		double IAATTACK = 0;
		entity.getPersistentData().putDouble("IA", (entity.getPersistentData().getDouble("IA") + 1));
		if (entity.getPersistentData().getDouble("IA") >= 100) {
			IAATTACK = Math.random();
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= 200) {
				if (IAATTACK >= 0.5) {
					if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
						{
							Entity _ent = entity;
							_ent.teleportTo(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
									((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()));
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
										((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()), _ent.getYRot(), _ent.getXRot());
						}
						if (world instanceof ServerLevel _level) {
							LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
							entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(entity.getX(), entity.getY(), entity.getZ())));
							entityToSpawn.setVisualOnly(true);
							_level.addFreshEntity(entityToSpawn);
						}
						world.addParticle(ParticleTypes.FLASH, (entity.getX()), (entity.getY()), (entity.getZ()), 0, 1, 0);
						entity.getPersistentData().putDouble("IA", 0);
					}
				} else if (IAATTACK < 0.5) {
					if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
						entity.getPersistentData().putDouble("IA", 0);
						int horizontalRadiusHemiTop = (int) 3 - 1;
						int verticalRadiusHemiTop = (int) 1;
						int yIterationsHemiTop = verticalRadiusHemiTop;
						for (int i = 0; i < yIterationsHemiTop; i++) {
							if (i == verticalRadiusHemiTop) {
								continue;
							}
							for (int xi = -horizontalRadiusHemiTop; xi <= horizontalRadiusHemiTop; xi++) {
								for (int zi = -horizontalRadiusHemiTop; zi <= horizontalRadiusHemiTop; zi++) {
									double distanceSq = (xi * xi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop) + (i * i) / (double) (verticalRadiusHemiTop * verticalRadiusHemiTop)
											+ (zi * zi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop);
									if (distanceSq <= 1.0) {
										{
											final Vec3 _center = new Vec3(x + xi, y + i, z + zi);
											List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1, 1, 1), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
													.collect(Collectors.toList());
											for (Entity entityiterator : _entfound) {
												entityiterator.hurt(((new EntityDamageSource("lightningBolt", entity) {
													@Override
													public Component getLocalizedDeathMessage(LivingEntity _livingEntity) {
														Component _attackerName = null;
														Component _entityName = _livingEntity.getDisplayName();
														Component _itemName = null;
														Entity _attacker = this.getEntity();
														ItemStack _itemStack = ItemStack.EMPTY;
														if (_attacker != null) {
															_attackerName = _attacker.getDisplayName();
														}
														if (_attacker instanceof LivingEntity _livingAttacker) {
															_itemStack = _livingAttacker.getMainHandItem();
														}
														if (!_itemStack.isEmpty() && _itemStack.hasCustomHoverName()) {
															_itemName = _itemStack.getDisplayName();
														}
														if (_attacker != null && _itemName != null) {
															return new TranslatableComponent("death.attack." + "lightningBolt.player", _entityName, _attackerName, _itemName);
														} else if (_attacker != null) {
															return new TranslatableComponent("death.attack." + "lightningBolt.player", _entityName, _attackerName);
														} else {
															return new TranslatableComponent("death.attack." + "lightningBolt", _entityName);
														}
													}
												})), (float) 1.5);
												world.addParticle(ParticleTypes.FLASH, x + xi, y + i, z + zi, 0, 0.5, 0);
												if (world instanceof ServerLevel _level) {
													LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
													entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(x + xi, y + i, z + zi)));
													entityToSpawn.setVisualOnly(true);
													_level.addFreshEntity(entityToSpawn);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= 300 && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 200) {
				if (IAATTACK >= 0.25) {
					if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
						{
							Entity _ent = entity;
							_ent.teleportTo(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
									((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()));
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
										((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()), _ent.getYRot(), _ent.getXRot());
						}
						world.addParticle(ParticleTypes.FLASH, (entity.getX()), (entity.getY()), (entity.getZ()), 0, 1, 0);
						if (world instanceof ServerLevel _level) {
							LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
							entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(entity.getX(), entity.getY(), entity.getZ())));
							entityToSpawn.setVisualOnly(true);
							_level.addFreshEntity(entityToSpawn);
						}
						entity.getPersistentData().putDouble("IA", 0);
					}
				} else if (IAATTACK < 0.25) {
					if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
						entity.getPersistentData().putDouble("IA", 0);
						int horizontalRadiusHemiTop = (int) 3 - 1;
						int verticalRadiusHemiTop = (int) 1;
						int yIterationsHemiTop = verticalRadiusHemiTop;
						for (int i = 0; i < yIterationsHemiTop; i++) {
							if (i == verticalRadiusHemiTop) {
								continue;
							}
							for (int xi = -horizontalRadiusHemiTop; xi <= horizontalRadiusHemiTop; xi++) {
								for (int zi = -horizontalRadiusHemiTop; zi <= horizontalRadiusHemiTop; zi++) {
									double distanceSq = (xi * xi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop) + (i * i) / (double) (verticalRadiusHemiTop * verticalRadiusHemiTop)
											+ (zi * zi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop);
									if (distanceSq <= 1.0) {
										{
											final Vec3 _center = new Vec3(x + xi, y + i, z + zi);
											List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1, 1, 1), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
													.collect(Collectors.toList());
											for (Entity entityiterator : _entfound) {
												entityiterator.hurt(((new EntityDamageSource("lightningBolt", entity) {
													@Override
													public Component getLocalizedDeathMessage(LivingEntity _livingEntity) {
														Component _attackerName = null;
														Component _entityName = _livingEntity.getDisplayName();
														Component _itemName = null;
														Entity _attacker = this.getEntity();
														ItemStack _itemStack = ItemStack.EMPTY;
														if (_attacker != null) {
															_attackerName = _attacker.getDisplayName();
														}
														if (_attacker instanceof LivingEntity _livingAttacker) {
															_itemStack = _livingAttacker.getMainHandItem();
														}
														if (!_itemStack.isEmpty() && _itemStack.hasCustomHoverName()) {
															_itemName = _itemStack.getDisplayName();
														}
														if (_attacker != null && _itemName != null) {
															return new TranslatableComponent("death.attack." + "lightningBolt.player", _entityName, _attackerName, _itemName);
														} else if (_attacker != null) {
															return new TranslatableComponent("death.attack." + "lightningBolt.player", _entityName, _attackerName);
														} else {
															return new TranslatableComponent("death.attack." + "lightningBolt", _entityName);
														}
													}
												})), (float) 1.5);
												world.addParticle(ParticleTypes.FLASH, x + xi, y + i, z + zi, 0, 0.5, 0);
												if (world instanceof ServerLevel _level) {
													LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
													entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(x + xi, y + i, z + zi)));
													entityToSpawn.setVisualOnly(true);
													_level.addFreshEntity(entityToSpawn);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= 175) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null)
					_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "particle dust_color_transition 0 0.57 0.82 1 0 0.69 0.78 ~ ~1 ~ 0.2 0.5 0.2 -0 10");
			}
		}
		if (!world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 20, 20, 20), e -> true).isEmpty()) {
			TpTarget = (Entity) world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3((entity.getX()), (entity.getY()), (entity.getZ())), 20, 20, 20), e -> true).stream().sorted(new Object() {
				Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
					return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
				}
			}.compareDistOf((entity.getX()), (entity.getY()), (entity.getZ()))).findFirst().orElse(null);
		} else {
			TpTarget = null;
		}
		if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == (null)) {
			if (entity.getPersistentData().getDouble("BossTp") >= 5) {
				if (!(TpTarget == null)) {
					if (!(new Object() {
						public boolean checkGamemode(Entity _ent) {
							if (_ent instanceof ServerPlayer _serverPlayer) {
								return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
							} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
								return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
										&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
							}
							return false;
						}
					}.checkGamemode(TpTarget)) && !(new Object() {
						public boolean checkGamemode(Entity _ent) {
							if (_ent instanceof ServerPlayer _serverPlayer) {
								return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
							} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
								return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
										&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
							}
							return false;
						}
					}.checkGamemode(TpTarget))) {
						{
							Entity _ent = entity;
							_ent.teleportTo((TpTarget.getX()), (TpTarget.getY()), (TpTarget.getZ()));
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport((TpTarget.getX()), (TpTarget.getY()), (TpTarget.getZ()), _ent.getYRot(), _ent.getXRot());
						}
						if (world instanceof ServerLevel _level) {
							LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
							entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(entity.getX(), entity.getY(), entity.getZ())));
							entityToSpawn.setVisualOnly(true);
							_level.addFreshEntity(entityToSpawn);
						}
						entity.getPersistentData().putDouble("BossTp", 0);
					}
				}
			}
		} else {
			if (entity.getPersistentData().getDouble("BossTp") >= 5) {
				entity.getPersistentData().putDouble("BossTp", 0);
			}
		}
		if (entity.isInWater()) {
			if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == (null))) {
				deltaX = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX() - entity.getX();
				deltaY = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY() - entity.getY();
				deltaZ = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ() - entity.getZ();
				distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
			}
			if (distance > 0) {
				speed = 0.04;
				motionX = deltaX / distance * speed;
				motionY = deltaY / distance * speed;
				motionZ = deltaZ / distance * speed;
				maxSpeed = 0.2;
				if (motionX > maxSpeed) {
					motionX = maxSpeed;
				}
				if (motionY > maxSpeed) {
					motionY = maxSpeed;
				}
				if (motionZ > maxSpeed) {
					motionZ = maxSpeed;
				}
				{
					Entity _ent = entity;
					if (!_ent.level.isClientSide() && _ent.getServer() != null)
						_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
								("execute as " + entity.getStringUUID() + " at @s run tp @s ~ ~ ~ facing entity " + (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getStringUUID()));
				}
				entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
			}
		}
	}
}

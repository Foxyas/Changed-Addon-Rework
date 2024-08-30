package net.foxyas.changedaddon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.init.ChangedAddonModGameRules;

import java.util.stream.Collectors;
import java.util.UUID;
import java.util.List;
import java.util.Comparator;

public class Exp009IAProcedure {
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
		double attackDmg = 0;
		double attackDmg2 = 0;
		double weakattackdmg = 0;
		double strongattackdmg = 0;
		double attackDmg3 = 0;
		double attackbuff = 0;
		if ((new Object() {
			public boolean getValue() {
				CompoundTag dataIndex0 = new CompoundTag();
				entity.saveWithoutId(dataIndex0);
				return dataIndex0.getBoolean("NoAI");
			}
		}.getValue()) == false) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.666) {
				entity.getPersistentData().putDouble("IA", (entity.getPersistentData().getDouble("IA") + 2.5 + 0.1 * ((world.getLevelData().getGameRules().getInt(ChangedAddonModGameRules.CHANGED_ADDON_HARD_MODE_BOSSES)) / 100)));
			} else {
				entity.getPersistentData().putDouble("IA", (entity.getPersistentData().getDouble("IA") + 2 + 0.1 * ((world.getLevelData().getGameRules().getInt(ChangedAddonModGameRules.CHANGED_ADDON_HARD_MODE_BOSSES)) / 100)));
			}
			if (entity.getPersistentData().getDouble("IA") >= 100) {
				if (entity.isAlive()) {
					IAATTACK = Math.random();
					attackbuff = (world.getLevelData().getGameRules().getInt(ChangedAddonModGameRules.CHANGED_ADDON_HARD_MODE_BOSSES)) / 100;
					attackDmg = 1.5 + 1.5 * attackbuff;
					attackDmg2 = 1.5 + 1.5 * attackbuff;
					attackDmg3 = 2.5 + 2.5 * attackbuff;
					weakattackdmg = 1 + 1 * attackbuff;
					strongattackdmg = 2 + 2 * attackbuff;
					if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.666) {
						if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
							deltaX = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX() - entity.getX();
							deltaY = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY() - entity.getY();
							deltaZ = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ() - entity.getZ();
							distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
							entity.getPersistentData().putDouble("IA", 0);
							if (distance >= 3) {
								if (IAATTACK > 0.5) {
									{
										Entity _ent = entity;
										_ent.teleportTo(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
												((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()));
										if (_ent instanceof ServerPlayer _serverPlayer)
											_serverPlayer.connection.teleport(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
													((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()), _ent.getYRot(), _ent.getXRot());
									}
									if (entity.isAlive()) {
										if (world instanceof ServerLevel _level) {
											LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
											entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(entity.getX(), entity.getY(), entity.getZ())));
											entityToSpawn.setVisualOnly(true);
											_level.addFreshEntity(entityToSpawn);
										}
									}
									{
										final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
										List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2, 2, 2), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
												.collect(Collectors.toList());
										for (Entity entityiterator : _entfound) {
											if (!(entityiterator == entity)) {
												if (entityiterator instanceof Player _player && !_player.level.isClientSide())
													_player.displayClientMessage(new TextComponent("\u00A7l\u00A7o\u00A73You CANT ESCAPE FROM ME!!"), true);
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
												})), (float) attackDmg);
												entityiterator.setDeltaMovement(new Vec3(0, 1, 0));
											}
										}
									}
									world.addParticle(ParticleTypes.FLASH, (entity.getX()), (entity.getY()), (entity.getZ()), 0, 1, 0);
									entity.getPersistentData().putDouble("IA", 0);
								} else {
									{
										Entity _ent = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null);
										_ent.teleportTo((entity.getX()), (entity.getY()), (entity.getZ()));
										if (_ent instanceof ServerPlayer _serverPlayer)
											_serverPlayer.connection.teleport((entity.getX()), (entity.getY()), (entity.getZ()), _ent.getYRot(), _ent.getXRot());
									}
									if (entity.isAlive()) {
										if (world instanceof ServerLevel _level) {
											LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
											entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(entity.getX(), entity.getY(), entity.getZ())));
											entityToSpawn.setVisualOnly(true);
											_level.addFreshEntity(entityToSpawn);
										}
									}
									{
										final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
										List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2, 2, 2), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
												.collect(Collectors.toList());
										for (Entity entityiterator : _entfound) {
											if (!(entityiterator == entity)) {
												if (entityiterator instanceof Player _player && !_player.level.isClientSide())
													_player.displayClientMessage(new TextComponent("\u00A7o\u00A73Come Back Here \u00A7lNOW!"), true);
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
												})), (float) attackDmg2);
												entityiterator.setDeltaMovement(new Vec3(0, (-1), 0));
											}
										}
									}
									world.addParticle(ParticleTypes.FLASH, (entity.getX()), (entity.getY()), (entity.getZ()), 0, 1, 0);
									entity.getPersistentData().putDouble("IA", 0);
								}
							}
							if (distance <= 2) {
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
													List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1, 2, 1), e -> true).stream()
															.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
													for (Entity entityiterator : _entfound) {
														if (!(entityiterator == entity)) {
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
															})), (float) attackDmg3);
															entityiterator.setDeltaMovement(new Vec3(0, 1.5, 0));
															world.addParticle(ParticleTypes.FLASH, x + xi, y + i, z + zi, 0, 0.5, 0);
															if (entity.isAlive()) {
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
					} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1)
							&& (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.666) {
						if (IAATTACK >= 0.25) {
							if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
								deltaX = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX() - entity.getX();
								deltaY = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY() - entity.getY();
								deltaZ = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ() - entity.getZ();
								distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
								entity.getPersistentData().putDouble("IA", 0);
								if (distance >= 5) {
									{
										Entity _ent = entity;
										_ent.teleportTo(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
												((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()));
										if (_ent instanceof ServerPlayer _serverPlayer)
											_serverPlayer.connection.teleport(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
													((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()), _ent.getYRot(), _ent.getXRot());
									}
									if (entity.isAlive()) {
										if (world instanceof ServerLevel _level) {
											LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
											entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(entity.getX(), entity.getY(), entity.getZ())));
											entityToSpawn.setVisualOnly(true);
											_level.addFreshEntity(entityToSpawn);
										}
									}
									{
										final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
										List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2, 2, 2), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
												.collect(Collectors.toList());
										for (Entity entityiterator : _entfound) {
											if (!(entityiterator == entity)) {
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
												})), (float) weakattackdmg);
												entityiterator.setDeltaMovement(new Vec3(0, 1, 0));
											}
										}
									}
									world.addParticle(ParticleTypes.FLASH, (entity.getX()), (entity.getY()), (entity.getZ()), 0, 1, 0);
								}
							}
						} else if (IAATTACK < 0.25) {
							if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
								entity.getPersistentData().putDouble("IA", 0);
								deltaX = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX() - entity.getX();
								deltaY = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY() - entity.getY();
								deltaZ = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ() - entity.getZ();
								distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
								if (distance < 2) {
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
														List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1, 2, 1), e -> true).stream()
																.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
														for (Entity entityiterator : _entfound) {
															if (!(entityiterator == entity)) {
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
																})), (float) strongattackdmg);
																world.addParticle(ParticleTypes.FLASH, x + xi, y + i, z + zi, 0, 0.5, 0);
																if (entity.isAlive()) {
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
						}
					}
				}
			}
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.5833) {// Summon Particle
				PlayerUtilProcedure.ParticlesUtil.sendColorTransitionParticles(entity.getLevel(), entity.getX(), entity.getY(), entity.getZ(), 0.0f, 0.57f, 0.82f, // Cores inicial em escala 0-1
						0.0f, 0.69f, 0.78f, // Cores final em escala 0-1
						1.0f, // Tamanho da partícula
						0.2f, // Velocidade X
						0.5f, // Velocidade Y
						0.2f, // Velocidade Z
						10, // Quantidade de partículas
						0);
				if (!((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
						.hasModifier((new AttributeModifier(UUID.fromString("dd2e9e59-9ca0-46f0-86bf-89c26480a34c"), "Attack Buff", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL)))) {
					if (!(((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
							.hasModifier((new AttributeModifier(UUID.fromString("dd2e9e59-9ca0-46f0-86bf-89c26480a34c"), "Attack Buff", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL)))))
						((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
								.addTransientModifier((new AttributeModifier(UUID.fromString("dd2e9e59-9ca0-46f0-86bf-89c26480a34c"), "Attack Buff", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL)));
				} else if (!((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR)
						.hasModifier((new AttributeModifier(UUID.fromString("4faaf81d-dbb4-4ec3-8783-d812b02b8cbb"), "Defense Buff", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL)))) {
					if (!(((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR)
							.hasModifier((new AttributeModifier(UUID.fromString("4faaf81d-dbb4-4ec3-8783-d812b02b8cbb"), "Defense Buff", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL)))))
						((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR)
								.addTransientModifier((new AttributeModifier(UUID.fromString("4faaf81d-dbb4-4ec3-8783-d812b02b8cbb"), "Defense Buff", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL)));
				}
			} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.5833) {
				if (((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
						.hasModifier((new AttributeModifier(UUID.fromString("dd2e9e59-9ca0-46f0-86bf-89c26480a34c"), "Attack Buff", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL)))) {
					((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
							.removeModifier((new AttributeModifier(UUID.fromString("dd2e9e59-9ca0-46f0-86bf-89c26480a34c"), "Attack Buff", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL)));
				} else if (((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR)
						.hasModifier((new AttributeModifier(UUID.fromString("4faaf81d-dbb4-4ec3-8783-d812b02b8cbb"), "Defense Buff", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL)))) {
					((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR)
							.removeModifier((new AttributeModifier(UUID.fromString("4faaf81d-dbb4-4ec3-8783-d812b02b8cbb"), "Defense Buff", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL)));
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
		}
	}
}

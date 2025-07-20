package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class LeapProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        double motionZ = 0;
        double deltaZ = 0;
        double distance = 0;
        double deltaX = 0;
        double motionY = 0;
        double deltaY = 0;
        double motionX = 0;
        double maxSpeed = 0;
        double speed = 0;
        double Yspeed = 0;
        if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
            if (CanLeapProcedure.execute(entity)) {
                if (!(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.FADIGE.get()))) {
                    if ((entity instanceof Player _plr ? _plr.getFoodData().getFoodLevel() : 0) > 6) {
                        if (entity.isOnGround() && !entity.isInWater()) {
                            if (!entity.isShiftKeyDown()) {
                                deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
                                deltaY = -Math.sin((entity.getXRot() / 180) * (float) Math.PI);
                                deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
                                speed = 0.4;
                                motionX = deltaX * speed;
                                motionY = deltaY * speed;
                                motionZ = deltaZ * speed;
                                entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
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
                                }.checkGamemode(entity))) {
                                    if (entity instanceof Player _player)
                                        _player.causeFoodExhaustion((float) 0.3);
                                    if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                        _entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
                                }
                                {
                                    Entity _ent = entity;
                                    if (!_ent.level.isClientSide() && _ent.getServer() != null)
                                        _ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
                                }
                            }
                            if (entity.isShiftKeyDown()) {
                                deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
                                deltaY = entity.level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(1)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()
                                        - entity.getY();
                                deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
                                speed = 0.15;
                                Yspeed = 0.5;
                                motionX = deltaX * speed;
                                motionY = deltaY * Yspeed;
                                motionZ = deltaZ * speed;
                                entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
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
                                }.checkGamemode(entity))) {
                                    if (entity instanceof Player _player)
                                        _player.causeFoodExhaustion((float) (motionY * 1.25));
                                    if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                        _entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
                                }
                                if (!(entity instanceof ServerPlayer _plr28 && _plr28.level instanceof ServerLevel
                                        && _plr28.getAdvancements().getOrStartProgress(_plr28.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:leaper"))).isDone())) {
                                    if (motionY >= 0.75) {
                                        if (entity instanceof ServerPlayer _player) {
                                            Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:leaper"));
                                            AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                                            if (!_ap.isDone()) {
                                                Iterator _iterator = _ap.getRemainingCriteria().iterator();
                                                while (_iterator.hasNext())
                                                    _player.getAdvancements().award(_adv, (String) _iterator.next());
                                            }
                                        }
                                    }
                                }
                                {
                                    Entity _ent = entity;
                                    if (!_ent.level.isClientSide() && _ent.getServer() != null)
                                        _ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
                                }
                            }
                        }
                    }
                }
            } else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).equals("changed_addon:form_ket_experiment009_boss")) {
                if (!(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.FADIGE.get()))) {
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
                    }.checkGamemode(entity))) {
                        deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
                        deltaY = -Math.sin((entity.getXRot() / 180) * (float) Math.PI);
                        deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
                        speed = 2;
                        motionX = deltaX * speed;
                        motionY = deltaY * 1;
                        motionZ = deltaZ * speed;
                        if (entity.isOnGround() && !entity.isInWater()) {
                            if (!entity.isShiftKeyDown()) {
                                world.levelEvent(2001, new BlockPos(entity.getX(), entity.getY() - 1, entity.getZ()), Block.getId((world.getBlockState(new BlockPos(entity.getX(), entity.getY() - 1, entity.getZ())))));
                                entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
                                {
                                    Entity _ent = entity;
                                    if (!_ent.level.isClientSide() && _ent.getServer() != null)
                                        _ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
                                }
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
                                }.checkGamemode(entity))) {
                                    if (entity instanceof Player _player)
                                        _player.causeFoodExhaustion((float) 0.1);
                                    if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                        _entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 60, 0));
                                }
                            }
                        }
                        int horizontalRadiusHemiTop = 4 - 1;
                        int verticalRadiusHemiTop = 1;
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
                                            List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1, 2, 1), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
                                                    .collect(Collectors.toList());
                                            for (Entity entityiterator : _entfound) {
                                                if (!(entityiterator == entity)) {
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
                                                    }.checkGamemode(entity))) {
                                                        if (entity instanceof Player _player)
                                                            _player.causeFoodExhaustion((float) 0.1);
                                                        if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                                            _entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 100, 0));
                                                    }
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
                                                    }.checkGamemode(entityiterator)) && !(new Object() {
                                                        public boolean checkGamemode(Entity _ent) {
                                                            if (_ent instanceof ServerPlayer _serverPlayer) {
                                                                return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
                                                            } else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
                                                                return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                                                        && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
                                                            }
                                                            return false;
                                                        }
                                                    }.checkGamemode(entityiterator))) {
                                                        entityiterator.setDeltaMovement(new Vec3(0, 0.7, 0));
                                                        world.addParticle(ParticleTypes.FLASH, x + xi, y + i, z + zi, 0, 0.7, 0);
                                                        if (!(entityiterator instanceof ItemEntity)) {
                                                            if (entity instanceof LivingEntity _entity)
                                                                _entity.swing(InteractionHand.MAIN_HAND, true);
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
                                                            })), 3);
                                                            if (world instanceof Level _level) {
                                                                if (!_level.isClientSide()) {
                                                                    _level.playSound(null, new BlockPos(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                                                            ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.lightning_bolt.impact")), SoundSource.NEUTRAL, 1, 0);
                                                                } else {
                                                                    _level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()),
                                                                            ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.lightning_bolt.impact")), SoundSource.NEUTRAL, 1, 0, false);
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
            } else if (CanLeapProcedure.flyentity(entity)) {
                if (!(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.FADIGE.get()))) {
                    if ((entity instanceof Player _plr ? _plr.getFoodData().getFoodLevel() : 0) > 8) {
                        if (entity instanceof Player player && player.getAbilities().flying && !(entity instanceof LivingEntity _livEnt && _livEnt.isFallFlying())) {
                            deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
                            deltaY = -Math.sin((entity.getXRot() / 180) * (float) Math.PI);
                            deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
                            speed = 2;
                            motionX = deltaX * speed;
                            motionY = deltaY * speed;
                            motionZ = deltaZ * speed;
                            entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
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
                            }.checkGamemode(entity))) {
                                if (entity instanceof Player _player)
                                    _player.causeFoodExhaustion((float) 0.8);
                                if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                    _entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 30, 0, false, false));
                            }
                            {
                                Entity _ent = entity;
                                if (!_ent.level.isClientSide() && _ent.getServer() != null)
                                    _ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
                            }
                        } else if (entity instanceof LivingEntity _livEnt && _livEnt.isFallFlying()) {
                            deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
                            deltaY = -Math.sin((entity.getXRot() / 180) * (float) Math.PI);
                            deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
                            speed = 2;
                            motionX = deltaX * speed;
                            motionY = deltaY * speed;
                            motionZ = deltaZ * speed;
                            entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
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
                            }.checkGamemode(entity))) {
                                if (entity instanceof Player _player)
                                    _player.causeFoodExhaustion(4);
                                if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                    _entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 60, 0, false, false));
                            }
                            {
                                Entity _ent = entity;
                                if (!_ent.level.isClientSide() && _ent.getServer() != null)
                                    _ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
                            }
                        }
                    }
                }
            }
        }
    }
}

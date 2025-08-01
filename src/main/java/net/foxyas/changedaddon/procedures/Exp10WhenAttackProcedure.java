package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.bosses.Experiment10BossEntity;
import net.foxyas.changedaddon.entity.bosses.Experiment10Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class Exp10WhenAttackProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        Entity entity = event.getEntity();
        if (event != null && entity != null) {
            execute(event, entity.getLevel(), entity, event.getSource().getDirectEntity());
        }
    }

    public static void execute(LevelAccessor world, Entity entity, Entity immediatesourceentity) {
        execute(null, world, entity, immediatesourceentity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity immediatesourceentity) {
        if (entity == null || immediatesourceentity == null)
            return;
        if (immediatesourceentity instanceof Experiment10Entity || immediatesourceentity instanceof Experiment10BossEntity) {
            if (entity instanceof LivingEntity _livEnt && _livEnt.isBlocking()) {
                if (_livEnt.getMainHandItem().getItem() instanceof ShieldItem) {
                    if (entity instanceof Player _player)
                        _player.getCooldowns().addCooldown(_livEnt.getMainHandItem().getItem(), 150);
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(immediatesourceentity.getX(), immediatesourceentity.getY() + 1, immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.NEUTRAL, (float) 1.5, 1);
                        } else {
                            _level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY() + 1), (immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.NEUTRAL, (float) 1.5, 1, false);
                        }
                    }
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5);
                        } else {
                            _level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5, false);
                        }
                    }
                } else if (_livEnt.getOffhandItem().getItem() instanceof ShieldItem) {
                    if (entity instanceof Player _player)
                        _player.getCooldowns().addCooldown(_livEnt.getOffhandItem().getItem(), 150);
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(immediatesourceentity.getX(), immediatesourceentity.getY() + 1, immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1);
                        } else {
                            _level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY() + 1), (immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1, false);
                        }
                    }
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5);
                        } else {
                            _level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5, false);
                        }
                    }
                } else if (_livEnt.getMainHandItem().getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:tsc_shield"))) {
                    if (entity instanceof Player _player)
                        _player.getCooldowns().addCooldown(_livEnt.getMainHandItem().getItem(), 150);
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(immediatesourceentity.getX(), immediatesourceentity.getY() + 1, immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1);
                        } else {
                            _level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY() + 1), (immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1, false);
                        }
                    }
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5);
                        } else {
                            _level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5, false);
                        }
                    }
                } else if (_livEnt.getOffhandItem().getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:tsc_shield"))) {
                    if (entity instanceof Player _player)
                        _player.getCooldowns().addCooldown(_livEnt.getOffhandItem().getItem(), 150);
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(immediatesourceentity.getX(), immediatesourceentity.getY() + 1, immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1);
                        } else {
                            _level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY() + 1), (immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1, false);
                        }
                    }
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5);
                        } else {
                            _level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5, false);
                        }
                    }
                } else if ((ForgeRegistries.ITEMS.getKey(_livEnt.getMainHandItem().getItem()).toString()).contains("shield")
                        && !(_livEnt.getMainHandItem().getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:tsc_shield")))) {
                    if (entity instanceof Player _player)
                        _player.getCooldowns().addCooldown(_livEnt.getMainHandItem().getItem(), 150);
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(immediatesourceentity.getX(), immediatesourceentity.getY() + 1, immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1);
                        } else {
                            _level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY() + 1), (immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1, false);
                        }
                    }
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5);
                        } else {
                            _level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5, false);
                        }
                    }
                } else if ((ForgeRegistries.ITEMS.getKey(_livEnt.getOffhandItem().getItem()).toString()).contains("shield")
                        && !(_livEnt.getOffhandItem().getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:tsc_shield")))) {
                    if (entity instanceof Player _player)
                        _player.getCooldowns().addCooldown(_livEnt.getOffhandItem().getItem(), 150);
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(immediatesourceentity.getX(), immediatesourceentity.getY() + 1, immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1);
                        } else {
                            _level.playLocalSound((immediatesourceentity.getX()), (immediatesourceentity.getY() + 1), (immediatesourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit")),
                                    SoundSource.HOSTILE, (float) 1.5, 1, false);
                        }
                    }
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5);
                        } else {
                            _level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.break")), SoundSource.PLAYERS, (float) 1.5, (float) 0.5, false);
                        }
                    }
                }
            }
        }
    }
}

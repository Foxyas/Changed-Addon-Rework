package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.entity.*;

import java.util.Objects;

@Mod.EventBusSubscriber
public class Phase2EntitysHandleProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        Level _level = event.getEntity().level;
        float Damage = event.getAmount();
        if (entity != null) {
            if (entity instanceof KetExperiment009BossEntity target) {
                if (target.isPhase2()) {
                    return;
                } else if (target.getUnderlyingPlayer() != null) {
                    return;
                }
                float Health = target.getHealth();
                float MaxHealth = target.getMaxHealth();
                if (Health - Damage <= MaxHealth * 0.75) {
                    target.setPhase2(true);
                    target.SpawnThunderBolt(target.position());

                    if (!_level.isClientSide()) {
                        _level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.power_select"))), SoundSource.HOSTILE, 500,
                                0);
                    } else {
                        _level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.power_select"))), SoundSource.HOSTILE, 500, 0,
                                false);
                    }
                }
            } else if (entity instanceof Experiment10BossEntity target) {
                if (target.isPhase2()) {
                    return;
                } else if (target.getUnderlyingPlayer() != null) {
                    return;
                }

                float Health = target.getHealth();
                float MaxHealth = target.getMaxHealth();
                if (Health - Damage <= MaxHealth * 0.5) {
                    target.setPhase2(true);
                    if (!_level.isClientSide()) {
                        _level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit"))), SoundSource.HOSTILE, 1,
                                1);
                    } else {
                        _level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit"))), SoundSource.HOSTILE, 1, 1, false);
                    }
                }
            } else if (entity instanceof Experiment10Entity target) {
                if (target.isPhase2()) {
                    return;
                } else if (target.getUnderlyingPlayer() != null) {
                    return;
                }

                float Health = target.getHealth();
                float MaxHealth = target.getMaxHealth();
                if (Health - Damage <= MaxHealth * 0.5) {
                    target.setPhase2(true);
                    if (!_level.isClientSide()) {
                        _level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit"))), SoundSource.HOSTILE, 1,
                                1);
                    } else {
                        _level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.attack.crit"))), SoundSource.HOSTILE, 1, 1, false);
                    }
                }
            } else if (entity instanceof KetExperiment009Entity target) {
				if (target.isPhase2()) {
					return;
				} else if (target.getUnderlyingPlayer() != null) {
					return;
				}
				float Health = target.getHealth();
				float MaxHealth = target.getMaxHealth();
				if (Health - Damage <= MaxHealth * 0.8) {
					target.setPhase2(true);
					target.SpawnThunderBolt(target.position());

					if (!_level.isClientSide()) {
						_level.playSound(null, new BlockPos(entity.getX(), entity.getY() + 1, entity.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.power_select"))), SoundSource.HOSTILE, 500,
								0);
					} else {
						_level.playLocalSound((entity.getX()), (entity.getY() + 1), (entity.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.power_select"))), SoundSource.HOSTILE, 500, 0,
								false);
					}
				}
			}

        }
    }
}

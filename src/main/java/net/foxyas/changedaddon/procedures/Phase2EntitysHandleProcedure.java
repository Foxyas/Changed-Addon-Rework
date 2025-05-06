package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import net.minecraft.world.phys.AABB;

@Mod.EventBusSubscriber
public class Phase2EntitysHandleProcedure {

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) return;

        Level level = entity.level;
        float damage = event.getAmount();
        float currentHealth = livingEntity.getHealth();
        float maxHealth = livingEntity.getMaxHealth();

        // ======== KetExperiment009BossEntity ============ \\
        if (entity instanceof KetExperiment009BossEntity boss) {
            if (boss.isPhase2()) {
                float healthAfterDamage = currentHealth - damage;
                float ratio = boss.computeHealthRatio();
                if (healthAfterDamage <= maxHealth * 0.4f && ratio > 0.4f && !boss.isPhase3()) {
                    boss.setPhase3(true);
                    knockbackNearbyEntities(boss);
                    playSound(level, entity.blockPosition().above(), "block.beacon.power_select", SoundSource.HOSTILE, 500, 0);
                }
            } else if (boss.getUnderlyingPlayer() == null && currentHealth - damage <= maxHealth * 0.75f) {
                boss.setPhase2(true);
                boss.SpawnThunderBolt(boss.position());
                playSound(level, entity.blockPosition().above(), "block.beacon.power_select", SoundSource.HOSTILE, 500, 0);
            }

            // ======== Experiment10BossEntity ============ \\
        } else if (entity instanceof Experiment10BossEntity boss) {
            if (!boss.isPhase2() && boss.getUnderlyingPlayer() == null && currentHealth - damage <= maxHealth * 0.5f) {
                boss.setPhase2(true);
                playSound(level, entity.blockPosition().above(), "entity.player.attack.crit", SoundSource.HOSTILE, 1, 1);
            }

            // ======== Experiment10Entity ============ \\
        } else if (entity instanceof Experiment10Entity e10) {
            if (!e10.isPhase2() && e10.getUnderlyingPlayer() == null && currentHealth - damage <= maxHealth * 0.5f) {
                e10.setPhase2(true);
                playSound(level, entity.blockPosition().above(), "entity.player.attack.crit", SoundSource.HOSTILE, 1, 1);
            }

            // ======== KetExperiment009Entity ============ \\
        } else if (entity instanceof KetExperiment009Entity ket) {
            if (!ket.isPhase2() && ket.getUnderlyingPlayer() == null && currentHealth - damage <= maxHealth * 0.8f) {
                ket.setPhase2(true);
                ket.SpawnThunderBolt(ket.position());
                playSound(level, entity.blockPosition().above(), "block.beacon.power_select", SoundSource.HOSTILE, 500, 0);
            }
        }
    }

    private static void knockbackNearbyEntities(LivingEntity source) {
     	AABB attackArea = source.getBoundingBox().inflate(6);
        List<LivingEntity> nearby = source.level.getEntitiesOfClass(LivingEntity.class, attackArea);


        for (LivingEntity target : nearby) {
        	if (target != source && source.canAttack(target)) {
            	double xForce = Mth.sin(source.getYRot() * ((float) Math.PI / 180F));
            	double zForce = -Mth.cos(source.getYRot() * ((float) Math.PI / 180F));
            	target.knockback(5, xForce, zForce);
            }
        }
    }

    private static void playSound(Level level, BlockPos pos, String soundId, SoundSource source, float volume, float pitch) {
        var sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundId));
        if (sound == null) return;

        if (!level.isClientSide()) {
            level.playSound(null, pos, sound, source, volume, pitch);
        } else {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), sound, source, volume, pitch, false);
        }
    }
}

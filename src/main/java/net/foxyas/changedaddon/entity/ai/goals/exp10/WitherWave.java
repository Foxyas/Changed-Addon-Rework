package net.foxyas.changedaddon.entity.ai.goals.exp10;

import net.foxyas.changedaddon.util.ParticlesUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class WitherWave extends Goal {

    private final Mob mob;
    private final UniformInt cooldownProvider;
    private int cooldown;

    public WitherWave(Mob mob, UniformInt cooldownProvider) {
        this.mob = mob;
        this.cooldownProvider = cooldownProvider;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        return mob.getTarget() != null && mob.distanceToSqr(mob.getTarget()) >= 16;
    }

    @Override
    public void start() {
        super.start();
        double radius = 1f;
        double angle = 25f;
        for (double theta = 0; theta < 360; theta += angle) {
            double angleTheta = Math.toRadians(theta);
            for (double phi = 0; phi <= 180; phi += angle) {
                double anglePhi = Math.toRadians(phi);
                double x = mob.getX() + Math.sin(anglePhi) * Math.cos(angleTheta) * radius;
                double y = mob.getY() + Math.cos(anglePhi) * radius;
                double z = mob.getZ() + Math.sin(anglePhi) * Math.sin(angleTheta) * radius;
                Vec3 pos = new Vec3(x, y, z);
                ParticlesUtil.sendParticlesWithMotion(
                        mob.level(),
                        ParticleTypes.DAMAGE_INDICATOR,
                        pos,
                        Vec3.ZERO,
                        mob.position().subtract(pos),
                        1, 1
                );
            }
        }

    }

    @Override
    public void tick() {
        if (mob.level() instanceof ServerLevel serverLevel) {
            // pegar todas as entidades vivas em volta
            List<LivingEntity> nearby = serverLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    mob.getBoundingBox().inflate(16),
                    EntitySelector.LIVING_ENTITY_STILL_ALIVE
            );

            for (LivingEntity entity : nearby) {
                if (entity == mob) continue; // não aplicar no próprio mob
                if (entity.hasEffect(MobEffects.WITHER)) continue; // já tem wither, pular

                // aplicar efeito
                entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1)); // 5s de Wither II

                // partículas de "dano"
                serverLevel.sendParticles(
                        ParticleTypes.DAMAGE_INDICATOR,
                        entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                        8, 0.5, 0.5, 0.5, 0.1
                );
            }
        }

        // resetar cooldown
        cooldown = cooldownProvider.sample(mob.getRandom());
    }
}

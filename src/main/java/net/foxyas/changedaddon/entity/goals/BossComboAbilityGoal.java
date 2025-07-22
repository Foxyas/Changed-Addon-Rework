package net.foxyas.changedaddon.entity.goals;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Random;
import java.util.function.Consumer;

public class BossComboAbilityGoal extends Goal {
    private final Mob entity;
    private final Random random = new Random();
    // Configurable parameters
    private final int maxPhases;
    private final float minDistance;
    private final float maxDistance;
    private final float activationChance;
    private final Consumer<LivingEntity>[] phaseActions;
    private final Runnable onStart;
    private final Runnable onStop;
    private LivingEntity target;
    private int phase = 0;
    private int ticks = 0;
    private final int delay = 5;

    @SafeVarargs
    public BossComboAbilityGoal(Mob entity, int maxPhases, float minDistance, float maxDistance, float activationChance,
                                Runnable onStart, Runnable onStop, Consumer<LivingEntity>... phaseActions) {
        this.entity = entity;
        this.maxPhases = maxPhases;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.activationChance = activationChance;
        this.phaseActions = phaseActions;
        this.onStart = onStart;
        this.onStop = onStop;
    }

    @Override
    public boolean canUse() {
        this.target = entity.getTarget();

        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return false;
        }

        return target != null && target.isOnGround() &&
                (entity.distanceTo(target) >= minDistance && entity.distanceTo(target) <= maxDistance) &&
                random.nextFloat() <= activationChance;
    }

    @Override
    public boolean canContinueToUse() {
        if (target instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return false;
            }
        }

        return phase < maxPhases && target != null && target.isAlive();
    }

    @Override
    public void start() {
        super.start();
        phase = 0;
        ticks = 0;
        if (onStart != null) {
            onStart.run();
        }

        // Execute first phase action immediately
        if (phaseActions.length > 0) {
            phaseActions[0].accept(target);
            phase++;
        }
    }

    @Override
    public void tick() {
        super.tick();
        ticks++;

        if (ticks % delay == 0 && phase < phaseActions.length) {
            phaseActions[phase].accept(target);
            phase++;
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (onStop != null) {
            onStop.run();
        }
    }

    public static class DefaultCombos {

        private final LivingEntity attacker;
        private final LivingEntity target;
        private final float damage;

        private final SoundEvent[] impactSound;
        private final ParticleOptions[] impactParticle;

        public DefaultCombos(LivingEntity attacker, LivingEntity target, float damage, SoundEvent[] impactSound, ParticleOptions[] impactParticle) {
            this.attacker = attacker;
            this.target = target;
            this.damage = damage;
            this.impactSound = impactSound;
            this.impactParticle = impactParticle;
        }

        public void teleportToTarget() {
            if (target == null) return;
            attacker.teleportTo(target.getX(), target.getY(), target.getZ());
            if (attacker instanceof Mob mob) {
                mob.lookAt(target, 1, 1);
            }
            attacker.swing(InteractionHand.MAIN_HAND);
            removeIframesFromTarget();
            target.hurt(DamageSource.mobAttack(attacker), damage);
            spawnImpactEffect(target.position(), 0);
            spawnImpactParticleEffect(target.position(), 0);
        }

        public void removeIframesFromTarget() {
            target.invulnerableTime = 0;
            target.hurtDuration = 1;
            target.hurtDir = 1;
            target.hurtTime = 1;
        }

        public void teleportAndKnockback(float strength) {
            if (target == null) return;
            attacker.teleportTo(target.getX(), target.getY(), target.getZ());
            Vec3 knockDir = attacker.getLookAngle().scale(strength).add(0, 0.2, 0);
            target.setDeltaMovement(knockDir);
            if (attacker instanceof Mob mob) {
                mob.lookAt(target, 1, 1);
            }
            attacker.swing(InteractionHand.MAIN_HAND);
            removeIframesFromTarget();
            target.hurt(DamageSource.mobAttack(attacker), damage / 2);
            spawnImpactEffect(target.position(), 0);
            spawnImpactParticleEffect(target.position(), 0);
        }

        public void teleportAndKnockbackInAir(float strength) {
            if (target == null) return;
            attacker.teleportTo(target.getX(), target.getY(), target.getZ());
            Vec3 knockDir = attacker.getLookAngle().scale(strength).add(0, 0.2, 0);
            target.setDeltaMovement(knockDir);
            if (attacker instanceof Mob mob) {
                mob.lookAt(target, 1, 1);
            }
            attacker.swing(InteractionHand.MAIN_HAND);
            removeIframesFromTarget();
            target.hurt(DamageSource.mobAttack(attacker), damage / 2);
            applySlowFalling(target);
            spawnImpactEffect(target.position(), 0);
            spawnImpactParticleEffect(target.position(), 0);
        }

        public void uppercut() {
            if (target == null) return;
            attacker.teleportTo(target.getX(), target.getY(), target.getZ());
            target.setDeltaMovement(0, 1.5, 0);
            if (attacker instanceof Mob mob) {
                mob.lookAt(target, 1, 1);
            }
            attacker.swing(InteractionHand.MAIN_HAND);
            removeIframesFromTarget();
            target.hurt(DamageSource.mobAttack(attacker), damage);
            applySlowFalling(target);
            spawnImpactEffect(target.position(), 1);
            spawnImpactParticleEffect(target.position(), 1);
        }

        public void slam() {
            if (target == null) return;
            attacker.teleportTo(target.getX(), target.getY(), target.getZ());
            target.setDeltaMovement(0, -2, 0);
            if (attacker instanceof Mob mob) {
                mob.lookAt(target, 1, 1);
            }
            attacker.swing(InteractionHand.MAIN_HAND);
            removeIframesFromTarget();
            target.hurt(DamageSource.mobAttack(attacker), damage);
            spawnImpactEffect(target.position(), 2);
            spawnImpactParticleEffect(target.position(), 2);
            removeSlowFalling();
        }

        public void removeSlowFalling() {
            target.removeEffect(MobEffects.SLOW_FALLING);
            attacker.removeEffect(MobEffects.SLOW_FALLING);
        }

        public void applySlowFalling(LivingEntity entity) {
            MobEffectInstance slowFalling = new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, false, false);
            entity.addEffect(slowFalling);
            attacker.addEffect(slowFalling);
        }

        public void spawnImpactEffect(Vec3 pos, int type) {
            if (attacker.level instanceof ServerLevel serverLevel) {
            /*if (type <= impactParticle.length) {
                serverLevel.sendParticles(impactParticle[type], pos.x, pos.y, pos.z, 10, 0.5, 0.5, 0.5, 0);
            }*/
                if (type <= impactSound.length) {
                    attacker.playSound(impactSound[type], 2, 1);
                }
            }
        }

        public void spawnImpactParticleEffect(Vec3 pos, int type) {
            if (attacker.level instanceof ServerLevel serverLevel) {
                if (type <= impactParticle.length) {
                    serverLevel.sendParticles(impactParticle[type], pos.x, pos.y, pos.z, 3, 0, 0, 0, 0);
                }
            }
        }
    }
}
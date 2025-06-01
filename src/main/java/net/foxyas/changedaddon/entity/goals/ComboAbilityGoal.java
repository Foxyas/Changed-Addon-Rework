package net.foxyas.changedaddon.entity.goals;

import net.foxyas.changedaddon.entity.VoidFoxEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ComboAbilityGoal extends Goal {
    private final Mob attacker;
    private LivingEntity target;
    private int phase = 0;
    private int ticks = 0;
    private int delay = 5;
    private final Random random = new Random();
    private final float minRange;
    private final float maxRange;
    private final float damage;
    private final SoundEvent[] impactSound;
    private final ParticleOptions[] impactParticle;
    private boolean shouldEnd = false;

    public ComboAbilityGoal(Mob attacker, float minRange, float maxRange, float damage, int delay, SoundEvent[] impactSounds, ParticleOptions[] impactParticle) {
        this.attacker = attacker;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.damage = damage;
        this.delay = delay;
        this.impactSound = impactSounds;
        this.impactParticle = impactParticle;
    }

    @Override
    public boolean canUse() {
        target = attacker.getTarget();

        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        return target != null && target.isOnGround() &&
                attacker.distanceTo(target) >= minRange && attacker.distanceTo(target) <= maxRange &&
                attacker.getRandom().nextFloat() < 0.5f;
    }

    @Override
    public boolean canContinueToUse() {
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        if (shouldEnd) {
            return false;
        }
        return phase <= 22 && target != null && target.isAlive();
    }

    @Override
    public void start() {
        phase = 0;
        ticks = 0;
        teleportToTarget();
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % delay == 0) {
            switch (phase) {
                case 0, 1, 2, 3, 4 -> teleportAndKnockback(4f / Math.max(1, phase));
                case 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 18, 19, 20, 21 -> teleportAndKnockbackInAir(1);
                case 5, 11, 17 -> uppercut();
                case 22 -> slam();
            }
            phase++;
        }
    }

    @Override
    public void stop() {
        if (shouldEnd && phase < 22) {
            slam();
        }
        if (!attacker.isOnGround()) {
            BlockPos pos = attacker.blockPosition();
            int groundY = attacker.level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
            attacker.teleportTo(pos.getX() + 0.5, groundY + 0.5, pos.getZ() + 0.5);
            spawnImpactEffect(attacker.position(), 3);
            spawnImpactParticleEffect(target.position(), 2);
        }

        shouldEnd = false;
    }

    private void teleportToTarget() {
        if (target == null) return;
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        attacker.swing(InteractionHand.MAIN_HAND);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(DamageSource.mobAttack(attacker), damage);
        } else {
            target.getLevel().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
            this.shouldEnd = true;
        }
        spawnImpactEffect(target.position(), 0);
        spawnImpactParticleEffect(target.position(), 0);
    }

    private void removeIframesFromTarget() {
        target.invulnerableTime = 0;
        target.hurtDuration = 1;
        target.hurtDir = 1;
        target.hurtTime = 1;
    }

    private void teleportAndKnockback(float strength) {
        if (target == null) return;
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        Vec3 knockDir = attacker.getLookAngle().scale(strength).add(0, 0.2, 0);
        target.setDeltaMovement(knockDir);
        attacker.swing(InteractionHand.MAIN_HAND);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(DamageSource.mobAttack(attacker), damage / 2);
        } else {
            target.getLevel().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
            this.shouldEnd = true;
        }
        spawnImpactEffect(target.position(), 0);
        spawnImpactParticleEffect(target.position(), 0);
    }

    private void teleportAndKnockbackInAir(float strength) {
        if (target == null) return;
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        Vec3 knockDir = attacker.getLookAngle().scale(strength).add(0, 0.2, 0);
        target.setDeltaMovement(knockDir);
        attacker.swing(InteractionHand.MAIN_HAND);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(DamageSource.mobAttack(attacker), damage / 2);
        } else {
            target.getLevel().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
            this.shouldEnd = true;
        }
        applySlowFalling(target);
        spawnImpactEffect(target.position(), 0);
        spawnImpactParticleEffect(target.position(), 0);
    }

    private void uppercut() {
        if (target == null) return;
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        target.setDeltaMovement(0, 1.5, 0);
        attacker.swing(InteractionHand.MAIN_HAND);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(DamageSource.mobAttack(attacker), damage);
        } else {
            target.getLevel().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
            this.shouldEnd = true;
        }
        applySlowFalling(target);
        spawnImpactEffect(target.position(), 1);
        spawnImpactParticleEffect(target.position(), 1);
    }

    private void slam() {
        if (target == null) return;
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        target.setDeltaMovement(0, -2, 0);
        attacker.swing(InteractionHand.MAIN_HAND);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(DamageSource.mobAttack(attacker), damage);
        } else {
            target.getLevel().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
        }
        spawnImpactEffect(target.position(), 2);
        spawnImpactParticleEffect(target.position(), 2);
        removeSlowFalling();
    }

    private void removeSlowFalling() {
        target.removeEffect(MobEffects.SLOW_FALLING);
        attacker.removeEffect(MobEffects.SLOW_FALLING);
    }

    private void applySlowFalling(LivingEntity entity) {
        MobEffectInstance slowFalling = new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, false, false);
        entity.addEffect(slowFalling);
        attacker.addEffect(slowFalling);
    }

    private void spawnImpactEffect(Vec3 pos, int type) {
        if (attacker.level instanceof ServerLevel serverLevel) {
            /*if (type <= impactParticle.length) {
                serverLevel.sendParticles(impactParticle[type], pos.x, pos.y, pos.z, 10, 0.5, 0.5, 0.5, 0);
            }*/
            if (type <= impactSound.length) {
                attacker.playSound(impactSound[type], 2, 1);
            }
        }
    }

    private void spawnImpactParticleEffect(Vec3 pos, int type) {
        if (attacker.level instanceof ServerLevel serverLevel) {
            if (type <= impactParticle.length) {
                serverLevel.sendParticles(impactParticle[type], pos.x, pos.y, pos.z, 3, 0, 0, 0, 0);
            }
        }
    }
}

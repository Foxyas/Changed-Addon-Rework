package net.foxyas.changedaddon.entity.goals;

import net.foxyas.changedaddon.entity.VoidFoxEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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

public class SImpleFlyingSlamAttack extends Goal {
    private final Mob attacker;
    private LivingEntity target;
    private int ticks = 0;
    private int delay = 5;
    private final Random random = new Random();
    private final float minRange;
    private final float maxRange;
    private final float damage;
    private boolean shouldEnd = false;

    public SImpleFlyingSlamAttack(Mob attacker, float minRange, float maxRange, float damage, int delay) {
        this.attacker = attacker;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.damage = damage;
        this.delay = delay;
    }

    public Mob getAttacker() {
        return attacker;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public float getMinRange() {
        return minRange;
    }

    public float getMaxRange() {
        return maxRange;
    }

    @Override
    public boolean canUse() {
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        if (target instanceof Player player) {
            return player.getAbilities().flying &&
                    attacker.distanceTo(player) >= minRange && attacker.distanceTo(player) <= maxRange;
        }
        return target != null && !target.isOnGround() &&
                attacker.distanceTo(target) >= minRange && attacker.distanceTo(target) <= maxRange;
    }

    @Override
    public boolean canContinueToUse() {
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        ticks = 0;
        shouldEnd = false;
        teleportAndKnockbackInAir(1);
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % delay == 0) {
            if (!attacker.isOnGround() && !target.isOnGround()) {
                slam();
            }
        }
    }

    @Override
    public void stop() {
        if (shouldEnd) {
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

    private void removeIframesFromTarget() {
        target.invulnerableTime = 0;
        target.hurtDuration = 1;
        target.hurtDir = 1;
        target.hurtTime = 1;
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

    private void slam() {
        if (target == null) return;
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        target.setDeltaMovement(0, -4, 0);
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
            attacker.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 2, 1);
        }
    }

    private void spawnImpactParticleEffect(Vec3 pos, int type) {
        if (attacker.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 3, 0, 0, 0, 0);
            if (type == 0 && attacker instanceof VoidFoxEntity voidFoxEntity) {
                if (voidFoxEntity.getMainHandItem().isEmpty()) {
                    voidFoxEntity.doClawsAttackEffect();
                }
            }
        }
    }
}

package net.foxyas.changedaddon.entity.ai.goals.generic.attacks;

import net.foxyas.changedaddon.entity.bosses.VoidFoxEntity;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class SimpleComboAbilityGoal extends Goal {
    private final Mob attacker;
    private final int maxPhases;
    private final Random random = new Random();
    private final float minRange;
    private final float maxRange;
    private final float damage;
    private final SoundEvent[] impactSound;
    private final ParticleOptions[] impactParticle;
    private LivingEntity target;
    private int phase = 0;
    private int ticks = 0;
    private int delay = 5;
    private boolean shouldEnd = false;

    public SimpleComboAbilityGoal(Mob attacker, int phases, float minRange, float maxRange, float damage, int delay, SoundEvent[] impactSounds, ParticleOptions[] impactParticle) {
        this.attacker = attacker;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.maxPhases = phases;
        this.damage = damage;
        this.delay = delay;
        this.impactSound = impactSounds;
        this.impactParticle = impactParticle;
    }

    public boolean isShouldEnd() {
        return shouldEnd;
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
        target = attacker.getTarget();

        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        return target != null && target.onGround() &&
                attacker.distanceTo(target) >= minRange && attacker.distanceTo(target) <= maxRange &&
                attacker.getRandom().nextFloat() < 0.5f;
    }

    @Override
    public boolean canContinueToUse() {
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        if (shouldEnd) {
            return false;
        }
        return phase <= maxPhases && target != null && target.isAlive();
    }

    @Override
    public void start() {
        phase = 0;
        ticks = 0;
        shouldEnd = false;
        teleportToTarget();
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % delay == 0) {
            if (phase == 0) {
                if (attacker.getRandom().nextFloat() >= 0.5f) {
                    uppercut();
                } else {
                    teleportAndKnockback(1);
                }
            } else if (phase == maxPhases) {
                slam();
            } else {
                if (!target.onGround()) {
                    teleportAndKnockbackInAir(1);
                } else {
                    if (attacker.getRandom().nextFloat() >= 0.5f) {
                        uppercut();
                    } else {
                        teleportAndKnockback(1);
                    }
                }
            }
            phase++;
        }
    }

    @Override
    public void stop() {
        if (shouldEnd && phase < maxPhases) {
            slam();
        }

        if (!attacker.onGround()) {
            Level world = attacker.level;
            BlockPos startPos = attacker.blockPosition();

            int entityHeight = Mth.ceil(attacker.getBbHeight());

            int y = startPos.getY();
            int minY = world.getMinBuildHeight() + 1;

            BlockPos validPos = null;

            while (y > minY) {
                BlockPos feetPos = new BlockPos(startPos.getX(), y, startPos.getZ());
                BlockPos belowPos = feetPos.below();

                boolean hasSpace = true;

                for (int i = 0; i < entityHeight; i++) {
                    if (!world.isEmptyBlock(feetPos.above(i))) {
                        hasSpace = false;
                        break;
                    }
                }

                if (hasSpace && !world.isEmptyBlock(belowPos)) {
                    validPos = feetPos;
                    break;
                }

                y--;
            }

            if (validPos != null) {
                attacker.teleportTo(
                        validPos.getX() + 0.5,
                        validPos.getY(),
                        validPos.getZ() + 0.5
                );

                spawnImpactEffect(attacker.position(), 3);
                spawnImpactParticleEffect(target.position(), 2);
            }
        }

        shouldEnd = false;
    }

    private void teleportToTarget() {
        if (target == null) return;
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        attacker.swing(InteractionHand.MAIN_HAND);
        attacker.lookAt(target, 1, 1);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(this.attacker.level().damageSources().mobAttack(attacker), damage);
        } else {
            target.level().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
            this.shouldEnd = true;
        }
        spawnImpactEffect(target.position(), 0);
        spawnImpactParticleEffect(target.position(), 0);
    }

    private void removeIframesFromTarget() {
        target.invulnerableTime = 0;
        target.hurtDuration = 1;
        target.hurtTime = 1;
    }

    private void teleportAndKnockback(float strength) {
        if (target == null) return;
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        Vec3 knockDir = attacker.getLookAngle().scale(strength).add(0, 0.2, 0);
        target.setDeltaMovement(knockDir);
        attacker.swing(InteractionHand.MAIN_HAND);
        attacker.lookAt(target, 1, 1);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(this.attacker.level().damageSources().mobAttack(attacker), damage / 2);
        } else {
            target.level().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
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
        attacker.lookAt(target, 1, 1);

        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(this.attacker.level().damageSources().mobAttack(attacker), damage / 2);
        } else {
            target.level().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
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
        attacker.lookAt(target, 1, 1);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(this.attacker.level().damageSources().mobAttack(attacker), damage);
        } else {
            target.level().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
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
        attacker.lookAt(target, 1, 1);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(this.attacker.level().damageSources().mobAttack(attacker), damage);
        } else {
            target.level().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
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

            if (type != 1 && type != 2 && attacker instanceof ChangedEntity changedEntity) {
                if (changedEntity.getSelfVariant() != null && changedEntity.getSelfVariant().is(ChangedAddonTags.TransfurTypes.HAS_CLAWS)) {
                    if (changedEntity.getMainHandItem().isEmpty()) {
                        doClawsAttackEffect(changedEntity);
                    }
                }
            }

            if (type != 1 && type != 2 && attacker instanceof VoidFoxEntity voidFoxEntity) {
                if (voidFoxEntity.getMainHandItem().isEmpty()) {
                    voidFoxEntity.doClawsAttackEffect();
                }
            }
        }
    }

    public void doClawsAttackEffect(LivingEntity boss) {// Efeito visual
        double d0 = (double) (-Mth.sin(boss.getYRot() * 0.017453292F)) * 1;
        double d1 = (double) Mth.cos(boss.getYRot() * 0.017453292F) * 1;
        if (boss.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, boss.getX() + d0, boss.getY(0.5), boss.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, boss.getX() + d0, boss.getY(0.6), boss.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, boss.getX() + d0, boss.getY(0.7), boss.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            boss.level.playSound(null, boss.getX(), boss.getY(), boss.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1f, 0.75f);
        }
    }
}

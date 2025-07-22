package net.foxyas.changedaddon.entity.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class LeapSmashGoal extends Goal {
    private final Mob mob;
    public int cooldown;
    private LivingEntity target;
    private int leapTicks;
    private boolean wasInAir;

    public LeapSmashGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        target = mob.getTarget();
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        if (target == null || !target.isAlive()) return false;
        if (!mob.isOnGround()) return false;
        return mob.distanceTo(target) >= 3; // only if target within 10 blocks
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        leapTicks = 0;
        wasInAir = false;

        Vec3 dir = target.position().subtract(mob.position()).normalize();
        double horizontalBoost = 1.5;
        double verticalBoost = 1.5;

        mob.setDeltaMovement(dir.x * horizontalBoost, verticalBoost, dir.z * horizontalBoost);
        mob.getNavigation().stop();
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.getLevel().playSound(null, mob.blockPosition(), SoundEvents.GOAT_LONG_JUMP, SoundSource.HOSTILE, 1.0F, 0.9F);

        mob.getLevel().broadcastEntityEvent(mob, (byte) 4); // Play jump animation
        mob.getJumpControl().jump();
    }

    @Override
    public void tick() {
        leapTicks++;

        if (mob.isOnGround() && wasInAir) {
            performSmash();
            stop();
        }

        if (!mob.isOnGround()) {
            wasInAir = true;
            mob.getNavigation().stop();
            if (leapTicks >= 40) {
                Vec3 motion = mob.getDeltaMovement();
                double verticalBoost = -0.5f;

                mob.setDeltaMovement(motion.x, verticalBoost, motion.z);
            }
        }


    }

    private void performSmash() {
        BlockPos center = mob.blockPosition();
        int radius = 4;

        for (BlockPos pos : BlockPos.betweenClosedStream(new AABB(center.below()).inflate(radius, 0, radius)).toList()) {
            double dx = (center.getX() - pos.getX()) / (double) radius;
            double dy = (center.getY() - pos.getY());
            double dz = (center.getZ() - pos.getZ()) / (double) radius;
            double distanceSq = dx * dx + dy * dy + dz * dz;

            if (distanceSq <= radius * radius) {
                BlockState blockState = mob.level.getBlockState(pos);
                mob.level.levelEvent(2001, pos, Block.getId(blockState));
                // Server-side Break Effect
                if (mob.getLevel() instanceof ServerLevel serverLevel) {
                    int breakerId = mob.getId();
                    int stage = 5;
                    serverLevel.destroyBlockProgress(breakerId, pos, stage);
                }

            }
        }

        if (mob.getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, center.getX(), center.getY(), center.getZ(), 1, 0, 0, 0, 1);
        }


        AABB smashArea = mob.getBoundingBox().inflate(4.0D);
        List<LivingEntity> hitEntities = mob.getLevel().getEntitiesOfClass(LivingEntity.class, smashArea, e -> e != mob && e.isAlive());

        for (LivingEntity entity : hitEntities) {
            if (entity.hurt(DamageSource.mobAttack(mob), 4.0F)) {
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0)); // Podridão?
                Vec3 knockback = entity.position().subtract(mob.position()).normalize().scale(1.2);
                entity.push(knockback.x, 0.5, knockback.z);
            }
        }

        mob.getLevel().playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.HOSTILE, 1.5F, 0.8F);
    }

    @Override
    public boolean canContinueToUse() {
        return leapTicks < 60 && !mob.isOnGround();
    }

    @Override
    public void stop() {
        leapTicks = 0;
        wasInAir = false;
        cooldown = 40;
    }
}

package net.foxyas.changedaddon.entity.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DashPunchGoal extends Goal {
    private enum Phase {
        IDLE,
        CHARGING,
        DASHING
    }

    private final Mob mob;
    private Phase phase = Phase.IDLE;
    private int chargeTicks = 0;
    private int dashTicks = 0;
    private int cooldown = 0;
    private LivingEntity target;

    public DashPunchGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        target = mob.getTarget();
        return target != null && target.isAlive() && mob.distanceTo(target) < 16 && mob.isOnGround();
    }

    @Override
    public void start() {
        phase = Phase.CHARGING;
        chargeTicks = 0;
        dashTicks = 0;

        mob.getNavigation().stop();
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.getLevel().playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.HOSTILE, 1.0F, 1.0F);
        for (LivingEntity living : mob.getLevel().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(4), (livingEntity -> !livingEntity.isSpectator() && !livingEntity.is(mob)))) {
            Vec3 knock = living.position().subtract(mob.position()).normalize().scale(1.2);
            living.push(knock.x, knock.y, knock.z);
        }
    }

    @Override
    public void tick() {
        if (target == null || !target.isAlive()) {
            stop();
            return;
        }

        switch (phase) {
            case CHARGING:
                handleCharging();
                break;
            case DASHING:
                handleDashing();
                break;
            default:
                break;
        }
    }

    private void handleCharging() {
        chargeTicks++;
        mob.getNavigation().stop();
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Charge particles
        if (mob.level instanceof ServerLevel server) {
            server.sendParticles(
                    ParticleTypes.ENTITY_EFFECT,
                    mob.getX(), mob.getEyeY(), mob.getZ(),
                    2, 0.2, 0.2, 0.2, 0.0
            );
        }

        if (chargeTicks >= 40) {
            beginDash();
        }
    }

    private void beginDash() {
        phase = Phase.DASHING;
        dashTicks = 0;

        mob.getNavigation().stop();
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.getLevel().playSound(null, mob.blockPosition(), SoundEvents.GOAT_LONG_JUMP, SoundSource.HOSTILE, 1.0F, 0.9F);
    }

    private void handleDashing() {
        dashTicks++;
        Vec3 direction = mob.getDeltaMovement().add(target.position().subtract(mob.position()).normalize().scale(1));
        mob.setDeltaMovement(direction.x, direction.y, direction.z);
        mob.hurtMarked = true;  // Forces client update

        // Check for impact
        if (mob.distanceTo(target) < 2.5) {
            applyImpact();
            stop();
        }

        // Safety timeout
        if (dashTicks > 40) {
            stop();
        }
    }

    private void applyImpact() {
        Level level = mob.getLevel();

        // Reverse knockback on self
        Vec3 reverse = mob.position().subtract(target.position()).normalize().scale(2);
        mob.push(reverse.x, reverse.y, reverse.z);
        mob.hurtMarked = true;

        // Knockback on target
        Vec3 knock = target.position().subtract(mob.position()).normalize().scale(2);
        target.push(knock.x, knock.y, knock.z);
        // Damage
        target.hurt(DamageSource.mobAttack(mob), 6.0F);

        // Particles
        if (level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION, mob.getX(), mob.getY(), mob.getZ(), 10, 0.5, 0.5, 0.5, 0.1);
        }

        // Sound
        level.playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.HOSTILE, 1.5F, 0.8F);
        level.playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.HOSTILE, 1.5F, 0.8F);
    }

    @Override
    public boolean canContinueToUse() {
        return phase != Phase.IDLE;
    }

    @Override
    public void stop() {
        phase = Phase.IDLE;
        cooldown = 40;
        chargeTicks = 0;
        dashTicks = 0;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}

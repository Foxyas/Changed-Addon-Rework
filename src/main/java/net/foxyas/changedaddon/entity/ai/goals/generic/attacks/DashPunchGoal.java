package net.foxyas.changedaddon.entity.ai.goals.generic.attacks;

import com.mojang.datafixers.util.Pair;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;

public class DashPunchGoal extends Goal {

    protected final Mob mob;
    protected Phase phase = Phase.IDLE;
    protected int chargeTicks = 0;
    protected int dashTicks = 0;
    protected int cooldown = 0;
    protected LivingEntity target;

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
        if (target == null || target.isRemoved() && target.isDeadOrDying()) return false;
        return target != null && target.isAlive() && mob.distanceTo(target) < 16 && mob.onGround();
    }

    @Override
    public void start() {
        phase = Phase.CHARGING;
        chargeTicks = 0;
        dashTicks = 0;

        mob.getNavigation().stop();
        if (target.isRemoved() && target.isDeadOrDying()) return;
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.level().playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.HOSTILE, 1.0F, 1.0F);
        if (mob.level instanceof ServerLevel server) {
            server.sendParticles(
                    ParticleTypes.EXPLOSION_EMITTER,
                    mob.getX(), mob.getEyeY(), mob.getZ(),
                    1, 0, 0, 0, 0
            );
        }
        for (LivingEntity living : mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(8), (livingEntity -> !livingEntity.isSpectator() && !livingEntity.is(mob)))) {
            Vec3 knock = living.position().subtract(mob.position()).normalize().scale(1.2);
            living.push(knock.x, knock.y * 1.25f, knock.z);
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
                handleBlockBreaking();
                break;
            default:
                break;
        }
    }

    protected void handleCharging() {
        chargeTicks++;
        mob.getNavigation().stop();
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Charge particles
        if (mob.level instanceof ServerLevel server) {
            if (mob instanceof ChangedEntity changedEntity) {
                Pair<Color3, Color3> entityColor = ChangedEntities.getEntityColor(changedEntity);
                Color3 first = entityColor.getFirst();
                Color3 second = entityColor.getSecond();
                RandomSource randomSource = mob.getRandom();
                server.sendParticles(
                        ParticleTypes.ENTITY_EFFECT,
                        mob.getX() + randomSource.nextGaussian() * 2, mob.getEyeY() + randomSource.nextGaussian() * 0.2, mob.getZ() + randomSource.nextGaussian() * 2, 0, first.red() / 255, first.green() / 255, first.blue() / 255, 0.0
                );
                server.sendParticles(
                        ParticleTypes.ENTITY_EFFECT,
                        mob.getX() + randomSource.nextGaussian() * 2, mob.getEyeY() + randomSource.nextGaussian() * 0.2, mob.getZ() + randomSource.nextGaussian() * 2, 0, second.red() / 255, second.green() / 255, second.blue() / 255, 0.0
                );
            } else {
                server.sendParticles(
                        ParticleTypes.ENTITY_EFFECT,
                        mob.getX(), mob.getEyeY(), mob.getZ(), 0, 0.2, 0.2, 0.2, 0.0
                );
            }
        }

        if (chargeTicks >= 40) {
            beginDash();
        }
    }

    protected void beginDash() {
        phase = Phase.DASHING;
        dashTicks = 0;

        mob.getNavigation().stop();
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.level().playSound(null, mob.blockPosition(), SoundEvents.GOAT_LONG_JUMP, SoundSource.HOSTILE, 1.0F, 0.9F);
    }

    protected void handleDashing() {
        dashTicks++;
        mob.getLookControl().setLookAt(target);
        Vec3 direction = mob.getDeltaMovement().add(target.position().subtract(mob.position()).normalize().scale(0.6));
        mob.setDeltaMovement(direction.x, direction.y, direction.z);
        mob.hurtMarked = true;  // Forces client update

        // Check for impact
        if (mob.distanceTo(target) < 2.5) {
            applyImpact();
            stop();
        }

        // Safety timeout
        if (dashTicks > 25) {
            stop();
        }
    }

    protected void handleBlockBreaking() {
        if (!(mob.level() instanceof ServerLevel serverLevel)) return;

        if (dashTicks % 5 != 0) return;

        BlockPos mobPos = mob.blockPosition();
        int horizontalRadius = 3;
        int verticalRadius = 3;

        BlockPos.betweenClosedStream(
                        mobPos.offset(-horizontalRadius, 0, -horizontalRadius),
                        mobPos.offset(horizontalRadius, verticalRadius, horizontalRadius))
                .map(BlockPos::immutable)
                .filter(pos -> {
                    int xi = pos.getX() - mobPos.getX();
                    int yi = pos.getY() - mobPos.getY();
                    int zi = pos.getZ() - mobPos.getZ();
                    double distanceSq = (xi * xi) / (double) (9) + (yi * yi) / (double) (9) + (zi * zi) / (double) (9);
                    return distanceSq <= 1.0;
                })
                .forEach(pos -> {
                    if (pos.equals(mobPos.below())) return;

                    var state = serverLevel.getBlockState(pos);

                    if (isIronTierOrLower(state, serverLevel, pos)) {
                        serverLevel.destroyBlock(pos, true, mob);
                    }
                });
    }

    /**
     * Verifica se o bloco é destrutível por ferramentas de ferro ou inferiores.
     */
    protected boolean isIronTierOrLower(BlockState state, ServerLevel serverLevel, BlockPos pos) {
        if (state.isAir()) return false;
        // 1. Evitar Bedrock e indestrutíveis (Dureza negativa)
        if (state.getDestroySpeed(serverLevel, pos) < 0) return false;

        if (state.is(Tags.Blocks.NEEDS_NETHERITE_TOOL)) {
            return false;
        }

        return true;
    }

    protected void applyImpact() {
        Level level = mob.level();

        // Reverse knockback on self
        Vec3 reverse = mob.position().subtract(target.position()).normalize().scale(2);
        mob.setDeltaMovement(reverse.x, reverse.y * 1.25f, reverse.z);
        mob.hurtMarked = true;
        mob.hasImpulse = true;

        // Knockback on target
        Vec3 knock = target.position().subtract(mob.position()).normalize().scale(2);
        target.push(knock.x, knock.y * 1.25f, knock.z);
        // Damage
        target.hurt(mob.damageSources().mobAttack(mob), 6.0F);

        // Particles
        if (level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION, mob.getX(), mob.getEyeY(), mob.getZ(), 10, 0.5, 0.5, 0.5, 0.1);
        }

        // Sound
        level.playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.HOSTILE, 1.5F, 0.8F);
        level.playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.HOSTILE, 1.5F, 0.8F);
    }

    @Override
    public boolean canContinueToUse() {
        if (target.isSpectator() || target.isInvulnerable() || (target instanceof Player player && player.isCreative())) {
            return false;
        }
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

    protected enum Phase {
        IDLE,
        CHARGING,
        DASHING
    }
}

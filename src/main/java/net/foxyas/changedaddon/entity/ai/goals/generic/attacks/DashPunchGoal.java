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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;

public class DashPunchGoal extends Goal {

    public static final int MAX_DASH_TICKS = 30;
    public static final int MAX_CHARGE_TICKS = 40;
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
        // Fail-safe: Verificação de nulidade e estado da entidade
        if (target == null || !target.isAlive() || target.isRemoved()) return false;

        return mob.distanceTo(target) <= 16 && mob.onGround();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.phase == Phase.IDLE) {
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        phase = Phase.CHARGING;
        chargeTicks = 0;
        dashTicks = 0;

        // Para outros comportamentos de movimento para evitar conflitos físicos
        mob.getNavigation().stop();

        // Efeitos visuais de início
        if (mob.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, mob.getX(), mob.getEyeY(), mob.getZ(), 1, 0, 0, 0, 0);

            // Impacto inicial (Knockback em área)
            for (LivingEntity living : mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(4),
                    (e) -> e != mob && !e.isSpectator())) {

                Vec3 delta = living.position().subtract(mob.position());
                // Fail-safe: Se estiverem na mesma posição, empurra para frente da entidade
                Vec3 knockDirection = delta.lengthSqr() < 1.0E-4D ? Vec3.directionFromRotation(0, mob.getYRot()) : delta.normalize();

                living.push(knockDirection.x * 1.2, 0.5, knockDirection.z * 1.2);
            }
        }

        mob.level().playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.HOSTILE, 1.0F, 1.0F);
    }

    @Override
    public void tick() {
        if (target == null || !target.isAlive()) {
            stop();
            return;
        }

        switch (phase) {
            case CHARGING -> handleCharging();
            case DASHING -> {
                handleDashing();
                handleBlockBreaking();
            }
        }
    }

    protected void handleCharging() {
        chargeTicks++;
        mob.getNavigation().stop();
        if (target.distanceTo(mob) > 0) {
            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }

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
            if (chargeTicks == 1) {
                server.playSound(null, mob, SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.HOSTILE, 2, 1);
            }
        }

        if (chargeTicks >= MAX_CHARGE_TICKS) {
            beginDash();
        }
    }

    protected void beginDash() {
        phase = Phase.DASHING;
        dashTicks = 0;
        mob.level().playSound(null, mob.blockPosition(), SoundEvents.GOAT_LONG_JUMP, SoundSource.HOSTILE, 1.0F, 0.9F);
        mob.level().playSound(null, mob, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 2, 1);
    }

    protected void handleDashing() {
        dashTicks++;
        if (target.distanceTo(mob) > 0) {
            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            mob.yBodyRot = mob.getYRot();
        }

        Vec3 targetPos = target.position();
        Vec3 mobPos = mob.position();
        Vec3 relativeVec = targetPos.subtract(mobPos);
        double distSq = relativeVec.lengthSqr();

        // FAIL-SAFE CRÍTICO: Se a distância for quase zero, usa a rotação do mob para evitar NaN
        Vec3 direction;
        if (distSq < 1.0E-4D) {
            direction = Vec3.directionFromRotation(0, mob.getYRot());
        } else {
            direction = relativeVec.normalize();
        }

        // Aplica o movimento
        Vec3 movement = direction.scale(0.45f);

        mob.setDeltaMovement(movement.x, movement.y, movement.z);
        mob.hasImpulse = true;
        mob.hurtMarked = true;

        if (isDashReachingTarget() || dashTicks > MAX_DASH_TICKS) {
            if (dashTicks > MAX_DASH_TICKS) stop();
            applyImpact();
        }
    }

    public boolean isDashReachingTarget() {
        return mob.getBoundingBox().inflate(6).intersects(target.getBoundingBox());
    }

    protected void handleBlockBreaking() {
        if (!(mob.level() instanceof ServerLevel serverLevel) || dashTicks % 4 != 0) return;

        BlockPos center = mob.blockPosition().above();
        // Reduzi o raio para 2 para melhorar performance e evitar "buracos" gigantes inúteis
        BlockPos.betweenClosedStream(center.offset(-2, -1, -2), center.offset(2, 2, 2)).forEach(pos -> {
            BlockState state = serverLevel.getBlockState(pos);
            if (!state.isAir() && state.getDestroySpeed(serverLevel, pos) >= 0 && !state.is(Tags.Blocks.NEEDS_NETHERITE_TOOL)) {
                serverLevel.destroyBlock(pos, true, mob);
            }
        });
    }

    protected void applyImpact() {
        DamageSource pSource = mob.damageSources().mobAttack(mob);
        if (target.isDamageSourceBlocked(pSource)) {
            target.hurt(pSource, 8.0F);
        }

        Vec3 knock = target.position().subtract(mob.position());
        Vec3 knockDir = knock.lengthSqr() < 1.0E-4D ? Vec3.directionFromRotation(0, mob.getYRot()) : knock.normalize();

        target.push(knockDir.x * 1.5, 0.4, knockDir.z * 1.5);

        if (mob.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION, target.getX(), target.getY(), target.getZ(), 5, 0.2, 0.2, 0.2, 0.0);
        }
        mob.level().playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.HOSTILE, 1.0F, 1.0F);
    }

    @Override
    public void stop() {
        phase = Phase.IDLE;
        cooldown = 50; // Aumentado um pouco o tempo de recarga
    }

    @Override
    public boolean isInterruptable() {
        return false; // Mantém o dash até o fim ou timeout
    }


    protected enum Phase {
        IDLE,
        CHARGING,
        DASHING
    }
}

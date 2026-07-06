package net.foxyas.changedaddon.entity.ai.goals.exp9;

import net.foxyas.changedaddon.entity.ai.goals.IReactiveGoal;
import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class ThunderDashAttack extends Goal implements IReactiveGoal {

    private static final int PREPARE_TIME = 60; // 3 seconds
    private static final int MAX_DASH_TICKS = 20;
    private static final double DETECTION_DISTANCE = 3.5D;
    private static final double KNOCKBACK_MULTIPLIER = 1.5;

    private final Experiment009BossEntity dasher;
    private LivingEntity target;

    private int tickCount = 0;
    private boolean isDashing = false;

    private Vec3 dashDirection = Vec3.ZERO;
    private float dashSpeed = 1.0f;
    private float strength = 1.0f;

    public ThunderDashAttack(Experiment009BossEntity dasher) {
        this.dasher = dasher;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public int getTickCount() {
        return tickCount;
    }

    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean canUse() {
        this.target = dasher.getTarget();
        if (target == null || target.isRemoved() && target.isDeadOrDying()) return false;
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        return target != null && target.isAlive() && target.distanceTo(dasher) >= 3.5f;
    }

    @Override
    public boolean canContinueToUse() {
        return isDashing || tickCount < (PREPARE_TIME + MAX_DASH_TICKS);
    }

    public Vec3 getDashDirection() {
        return dashDirection;
    }

    public void setDashDirection(Vec3 dashDirection) {
        this.dashDirection = dashDirection;
    }

    public float getDashSpeed() {
        return this.dashSpeed;
    }

    public void setDashSpeed(float dashSpeed) {
        this.dashSpeed = dashSpeed;
    }

    @Override
    public void start() {
        if (tickCount >= PREPARE_TIME + MAX_DASH_TICKS) {
            tickCount = 0;
        }
        isDashing = false;
        dasher.getViewVector(1).scale(strength).multiply(1, 0, 1);
        this.target = dasher.getTarget();
    }

    public boolean isChargingDash() {
        return tickCount < PREPARE_TIME;
    }

    public boolean isDashing() {
        return isDashing || (tickCount <= PREPARE_TIME + MAX_DASH_TICKS);
    }

    @Override
    public void tick() {
        tickCount++;

        this.dashDirection.scale(this.dashSpeed);

        // Preparando o dash
        if (tickCount < PREPARE_TIME) {
            if (tickCount == 0) {
                dasher.level().playSound(null, dasher, SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.HOSTILE, 2, 1);
            }
            dasher.getNavigation().stop();
            if (target == null || target.isRemoved() || target.isDeadOrDying()) return;
            if (target.distanceTo(dasher) > 0) {
                dasher.getLookControl().setLookAt(target, 90f, 90f);
            }
            dashDirection = dasher.getViewVector(1).scale(strength).multiply(1, 0, 1);
            dasher.level().playSound(null, dasher, SoundEvents.BEACON_AMBIENT, SoundSource.HOSTILE, 2, (float) tickCount / PREPARE_TIME);
            if (dasher.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ENCHANT, dasher.getX(), dasher.getEyeY(), dasher.getZ(), 4, 0.25, 0.5, 0.25, 0.5);
                serverLevel.sendParticles(ParticleTypes.END_ROD, dasher.getX(), dasher.getEyeY(), dasher.getZ(), 4, 0.25, 0.5, 0.25, 0.05f);
            }
            isDashing = false;
            return;
        }

        if (!isDashing) {
            isDashing = true;
        }

        if (tickCount <= PREPARE_TIME + MAX_DASH_TICKS) {
            dasher.getNavigation().stop();
            if (tickCount == PREPARE_TIME) {
                dasher.level().playSound(null, dasher, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 2, 1);
            }

            // Aplica o movimento
            dasher.setDeltaMovement(dashDirection);
            Vec3 lookAt = dasher.getEyePosition(0).add(dashDirection);
            dasher.getLookControl().setLookAt(lookAt.x, lookAt.y, lookAt.z, 180, 180);
            dasher.yBodyRot = dasher.yHeadRot;
            dasher.yBodyRotO = dasher.yHeadRotO;

            if (dasher.horizontalCollision || dasher.minorHorizontalCollision) {
                tickCount += 5;
            }

            // Detecta entidades na frente
            Vec3 forward = dasher.getLookAngle();
            Vec3 origin = dasher.position();
            AABB detectionBox = dasher.getBoundingBox().expandTowards(forward.scale(DETECTION_DISTANCE)).inflate(1.0);

            List<LivingEntity> entities = dasher.level.getEntitiesOfClass(LivingEntity.class, detectionBox, e -> e != dasher && e.isAlive());

            for (LivingEntity entity : entities) {
                // Aplica dano e knockback baseado na distância
                Vec3 difference = entity.position().subtract(dasher.position());
                double distance = difference.length();
                if (distance > 0.1) {
                    Vec3 knockback = difference.normalize().scale(distance * KNOCKBACK_MULTIPLIER);
                    dasher.swing(InteractionHand.MAIN_HAND);
                    DamageSource pSource = dasher.level().damageSources().mobAttack(dasher);
                    if (!entity.isDamageSourceBlocked(pSource)) {
                        entity.hurt(pSource, 6.0F);
                        dasher.level().playSound(null, entity, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 1, 1);
                    } else {
                        dasher.level().playSound(null, entity, SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1, 1);
                    }
                    entity.setDeltaMovement(entity.getDeltaMovement().add(knockback.multiply(1f, 0.0025f, 1f)));
                    if (entity instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(
                                serverPlayer.getId(),
                                serverPlayer.getDeltaMovement())
                        );
                    }
                }
            }
            this.isDashing = false;
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.tickCount = 0;
        isDashing = false;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public Mob getDasher() {
        return dasher;
    }

    @Override
    public void onHurt(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float pDamageAmount) {
        if (this.isChargingDash()) {
            this.tickCount += (int) (PREPARE_TIME * 0.25);
        }
    }

    @Override
    public void onDamage(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float amount, boolean willCauseDamage) {

    }

    @Override
    public void onHeal(LivingEntity livingEntity, float amount) {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void setCanceledTo(boolean canceled) {

    }
}

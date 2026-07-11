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

    private static final int MAX_CHARGE_TICKS = 60; // 3 seconds
    private static final int MAX_DASH_TICKS = 20;
    private static final double DETECTION_DISTANCE = 3.5D;
    private static final double KNOCKBACK_MULTIPLIER = 1.5;

    protected final Experiment009BossEntity dasher;
    protected LivingEntity target;

    protected int dashingTickCounter = 0;
    protected int chargingTickCounter = 0;

    protected Phase phase = Phase.IDLE;
    protected Vec3 dashDirection = Vec3.ZERO;
    protected float dashSpeed = 1.0f;
    protected float strength = 1.0f;

    public ThunderDashAttack(Experiment009BossEntity dasher) {
        this.dasher = dasher;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public int getDashingTickCounter() {
        return dashingTickCounter;
    }

    public void setDashingTickCounter(int dashingTickCounter) {
        this.dashingTickCounter = dashingTickCounter;
    }

    public int getChargingTickCounter() {
        return chargingTickCounter;
    }

    public void setChargingTickCounter(int chargingTickCounter) {
        this.chargingTickCounter = chargingTickCounter;
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
    public boolean canUse() {
        this.target = dasher.getTarget();
        if (target == null || target.isRemoved() && target.isDeadOrDying()) return false;
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        return target != null && target.isAlive() && target.distanceTo(dasher) >= 3.5f;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.phase == Phase.IDLE) {
            return false;
        }

        return this.isDashing() || this.isChargingDash();
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
        chargingTickCounter = 0;
        dashingTickCounter = 0;
        dasher.getViewVector(1).scale(strength).multiply(1, 0, 1);
        this.target = dasher.getTarget();
        this.phase = Phase.CHARGING;
    }

    public boolean isChargingDash() {
        return this.phase == Phase.CHARGING;
    }

    public boolean isDashing() {
        return this.phase == Phase.DASHING;
    }

    @Override
    public void tick() {
        switch (phase) {
            case IDLE -> {
                return;
            }
            // Preparando o dash
            case CHARGING -> handleCharging();
            case DASHING -> handleDashing();
        }
    }

    protected void handleDashing() {
        dashingTickCounter++;
        if (dashingTickCounter <= MAX_CHARGE_TICKS + MAX_DASH_TICKS) {
            dasher.getNavigation().stop();

            // Aplica o movimento
            dasher.setDeltaMovement(dashDirection);
            Vec3 lookAt = dasher.getEyePosition(0).add(dashDirection.normalize());
            dasher.getLookControl().setLookAt(lookAt.x, lookAt.y, lookAt.z, 180, 180);
            dasher.yBodyRot = dasher.getYRot();

            if (dasher.horizontalCollision || dasher.minorHorizontalCollision) {
                dashingTickCounter += 5;
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
        }
    }

    protected void handleCharging() {
        if (chargingTickCounter == 0) {
            dasher.level().playSound(null, dasher, SoundEvents.WARDEN_SONIC_CHARGE, SoundSource.HOSTILE, 2, 1);
        }
        chargingTickCounter++;
        if (chargingTickCounter <= MAX_CHARGE_TICKS) {
            if (chargingTickCounter == MAX_CHARGE_TICKS) {
                onStartDashing();
                return;
            }
            dasher.getNavigation().stop();
            if (target == null || target.isRemoved() || target.isDeadOrDying()) return;
            if (target.distanceTo(dasher) > 0) {
                dasher.getLookControl().setLookAt(target, 90f, 90f);
            }
            dashDirection = dasher.getViewVector(0).scale(strength).multiply(1, 0, 1);
            dasher.level().playSound(null, dasher, SoundEvents.BEACON_AMBIENT, SoundSource.HOSTILE, 2, (float) chargingTickCounter / MAX_CHARGE_TICKS);
            if (dasher.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ENCHANT, dasher.getX(), dasher.getEyeY(), dasher.getZ(), 4, 0.25, 0.5, 0.25, 0.5);
                serverLevel.sendParticles(ParticleTypes.END_ROD, dasher.getX(), dasher.getEyeY(), dasher.getZ(), 4, 0.25, 0.5, 0.25, 0.05f);
            }
        }
    }

    protected void onStartDashing() {
        this.phase = Phase.DASHING;
        dasher.level().playSound(null, dasher, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 2, 1);
    }

    @Override
    public void stop() {
        super.stop();
        this.dashingTickCounter = 0;
        this.chargingTickCounter = 0;
        this.phase = Phase.IDLE;
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
            this.chargingTickCounter += (int) (MAX_CHARGE_TICKS * 0.25);
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

    protected enum Phase {
        IDLE,
        CHARGING,
        DASHING
    }
}

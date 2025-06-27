package net.foxyas.changedaddon.entity.goals;

import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;

import java.util.EnumSet;
import java.util.List;

public class DashAttack extends Goal {

    private static final int PREPARE_TIME = 60; // 3 seconds
    private static final int MAX_DASH_TICKS = 20;
    private static final double DETECTION_DISTANCE = 3.5D;
    private static final double KNOCKBACK_MULTIPLIER = 1.5;

    private final Mob dasher;
    private LivingEntity target;

    private int tickCount = 0;
    private boolean isDashing = false;

    private Vec3 dashDirection = Vec3.ZERO;
    private float dashSpeed = 1.0f;
    private float strength = 1.0f;

    public DashAttack(Mob dasher) {
        this.dasher = dasher;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public int getTickCount() {
        return tickCount;
    }

    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    @Override
    public boolean canUse() {
        this.target = dasher.getTarget();
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
        dashDirection = Vec3.ZERO;
    }

    public boolean isChargingDash(){
        return !(tickCount > PREPARE_TIME);
    }

    public boolean isDashing() {
        return isDashing;
    }

    @Override
    public void tick() {
        tickCount++;

        this.dashDirection.scale(this.dashSpeed);
        
        if (target instanceof Player player) {
            //player.displayClientMessage(new TextComponent("Ticks = " + tickCount), true);
        }

        // Preparando o dash
        if (tickCount < PREPARE_TIME) {
            dasher.getNavigation().stop();
            dasher.getLookControl().setLookAt(target, 30.0F, 30.0F);
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
            // Aplica o movimento
            dasher.setDeltaMovement(dashDirection);
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
                    if (!entity.isBlocking()) {
                        entity.hurt(DamageSource.mobAttack(dasher), 6.0F);
                        dasher.level().playSound(null, entity, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 1, 1);
                    } else {
                        dasher.level().playSound(null, entity, SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1, 1);
                    }
                    entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));
                }
            }
            this.isDashing = false;
        }
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
}

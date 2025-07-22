package net.foxyas.changedaddon.entity.goals;

import net.foxyas.changedaddon.entity.projectile.ParticleProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class VoidFoxDashAttack extends Goal {

    public static final int PREPARE_TIME = 60; // 3 seconds
    public static final int MAX_DASH_TICKS = 20;
    private static final double DETECTION_DISTANCE = 3.5D;
    private static final double KNOCKBACK_MULTIPLIER = 1.5;

    private final Mob dasher;
    private final EntityType<? extends ParticleProjectile> projectileType;
    private LivingEntity target;

    private int tickCount = 0;
    private boolean isDashing = false;

    private Vec3 dashDirection = Vec3.ZERO;
    private float dashSpeed = 1.0f;
    private float strength = 1.0f;

    public VoidFoxDashAttack(Mob dasher, EntityType<? extends ParticleProjectile> projectileType) {
        this.dasher = dasher;
        this.projectileType = projectileType;
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
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) return false;
        return target != null && target.isAlive() && target.distanceTo(dasher) >= 3.5f;
    }

    @Override
    public boolean canContinueToUse() {
        return isDashing || tickCount < (PREPARE_TIME + MAX_DASH_TICKS);
    }

    @Override
    public void start() {
        if (tickCount >= PREPARE_TIME + MAX_DASH_TICKS) {
            tickCount = 0;
        }
        isDashing = false;
        dashDirection = Vec3.ZERO;
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
            if (tickCount % 20 == 0) {
                shootProjectile(target);
                if (!dasher.getLevel().isClientSide()) {
                    dasher.getLevel().playSound(null, dasher, SoundEvents.EVOKER_PREPARE_ATTACK, SoundSource.HOSTILE, 2, (float) tickCount / PREPARE_TIME);
                }
            }
            if (dasher.getLevel() instanceof ServerLevel serverLevel) {
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
            dasher.getLookControl().setLookAt(dasher.getEyePosition().add(dashDirection));
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
                    if (entity.hurt(DamageSource.mobAttack(dasher), 6.0F)) {
                        dasher.getLevel().playSound(null, entity, SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.HOSTILE, 1, 1);
                        dasher.getLevel().playSound(null, entity, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 1, 1);
                    } else {
                        if (dasher.getLevel() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.CRIT, entity.getX(), entity.getY(0.5), entity.getZ(), 4, 0.25, 0.25, 0.25, 0.05);
                        }
                        if (entity.isBlocking()) {
                            if (dasher.getLevel().isClientSide()) {
                                dasher.getLevel().playSound(null, entity, SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1, 1);
                            }
                        }
                    }

                    entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));
                }
            }
            this.isDashing = false;
        }
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


    public boolean isChargingDash() {
        return !(tickCount > PREPARE_TIME);
    }

    public boolean isDashing() {
        return isDashing;
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

    private void shootProjectile(LivingEntity target) {
        Level level = dasher.level;
        switch (dasher.getRandom().nextInt(3)) {
            case 0 -> spawnProjectilesInCircle(dasher, target, new Vec3(0, 0.5, 0), 2, 4, level);
            case 1 -> spawnProjectilesInXCircleTargetInPlayer(dasher, target, new Vec3(0, 0, 0), 2, 8, level);
            case 2 -> spawnProjectilesInXCircleTargetInPlayerPos(dasher, target, new Vec3(0, 0, 0), 2, 8, level);
        }
    }

    public void spawnProjectilesInCircle(LivingEntity entity, LivingEntity target, Vec3 offset, double radius, int count, Level level) {
        Vec3 center = entity.getEyePosition().add(offset); // centro do círculo com offset

        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            Vec3 dir = new Vec3(1, 0, 0).zRot((float) angle)
                    .yRot((float) Math.toRadians(-dasher.getYRot()))
                    .normalize();
            Vec3 spawnPos = center.add(dir.scale(radius));

            // Spawn do projétil
            if (!level.isClientSide()) {
                ParticleProjectile projectile = new ParticleProjectile(projectileType, dasher, dasher.getLevel(), target);
                projectile.setPos(spawnPos);
                projectile.setNoGravity(true);
                projectile.setOwner(dasher);
                projectile.teleport = false;

                Vec3 direction = target.getEyePosition(1.0F).subtract(dasher.getEyePosition(0.5f)).normalize();
                projectile.shoot(direction.x, direction.y, direction.z, 1.5f, 0.0f);


                entity.level.addFreshEntity(projectile);
            }
        }
        dasher.swing(InteractionHand.MAIN_HAND);
    }

    public void spawnProjectilesInXCircleTargetInPlayer(LivingEntity entity, LivingEntity target, Vec3 offset, double radius, int count, Level level) {
        Vec3 center = target.getEyePosition().add(offset); // centro do círculo com offset

        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            Vec3 dir = new Vec3(1, 0, 0).yRot((float) angle)
                    .normalize();
            Vec3 spawnPos = center.add(dir.scale(radius));

            // Spawn do projétil
            if (!level.isClientSide()) {
                ParticleProjectile projectile = new ParticleProjectile(projectileType, dasher, dasher.getLevel(), target);
                projectile.setPos(spawnPos);
                projectile.setNoGravity(true);
                projectile.setOwner(dasher);
                projectile.teleport = false;

                Vec3 direction = target.getEyePosition(1.0F).subtract(dasher.getEyePosition(0.5f)).normalize();
                projectile.shoot(direction.x, direction.y, direction.z, 1.5f, 0.0f);


                entity.level.addFreshEntity(projectile);
            }
        }
        dasher.swing(InteractionHand.MAIN_HAND);
    }

    public void spawnProjectilesInXCircleTargetInPlayerPos(LivingEntity entity, LivingEntity target, Vec3 offset, double radius, int count, Level level) {
        Vec3 center = target.getEyePosition().add(offset); // centro do círculo com offset

        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            Vec3 dir = new Vec3(1, 0, 0).xRot((float) angle)
                    .normalize();
            Vec3 spawnPos = center.add(dir.scale(radius));

            // Spawn do projétil
            if (!level.isClientSide()) {
                ParticleProjectile projectile = new ParticleProjectile(projectileType, dasher, dasher.getLevel(), null);
                projectile.setTargetPos(target.position());
                projectile.setPos(spawnPos);
                projectile.setNoGravity(true);
                projectile.setOwner(dasher);
                projectile.teleport = false;

                Vec3 direction = target.getEyePosition(1.0F).subtract(dasher.getEyePosition(0.5f)).normalize();
                projectile.shoot(direction.x, direction.y, direction.z, 1.5f, 0.0f);


                entity.level.addFreshEntity(projectile);
            }
        }
        dasher.swing(InteractionHand.MAIN_HAND);
    }
}

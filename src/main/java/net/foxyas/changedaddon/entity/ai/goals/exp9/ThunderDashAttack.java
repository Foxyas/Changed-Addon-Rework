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
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class ThunderDashAttack extends Goal implements IReactiveGoal {

    private static final int MAX_CHARGE_TICKS = 60; // 3 seconds
    private static final int MAX_DASH_TICKS = 20;
    private static final int FAIL_SAFE_TICKS = MAX_CHARGE_TICKS + MAX_DASH_TICKS + 40;
    private static final double DETECTION_DISTANCE = 3.5D;
    private static final double KNOCKBACK_MULTIPLIER = 1.5;

    protected final Experiment009BossEntity dasher;
    protected LivingEntity target;

    protected int dashingTickCounter = 0;
    protected int chargingTickCounter = 0;
    protected int ticks = 0;

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
        return ticks >= FAIL_SAFE_TICKS;
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
        ticks = 0;
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
        ticks++;
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
        if (dashingTickCounter <= MAX_DASH_TICKS) {
            dasher.getNavigation().stop();

            // Aplica o movimento
            dasher.setDeltaMovement(dashDirection);
            Vec3 lookAt = dasher.getEyePosition(1).add(dashDirection.scale(1.5f));
            dasher.getLookControl().setLookAt(lookAt.x, lookAt.y, lookAt.z, 180, 180);
            dasher.setYBodyRot(dasher.getYHeadRot());

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
                        if (isTargetDoingCorrectSwingParry(entity, pSource)) {
                            onParriedAttemptToHitTarget(entity);
                        } else {
                            if (entity.hurt(pSource, 6.0F)) {
                                dasher.level().playSound(null, entity, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 1, 1);
                            }
                        }
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

    protected void onParriedAttemptToHitTarget(LivingEntity target) {
        Level level = dasher.level();
        if (level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.FLASH, target.getX(), target.getY(), target.getZ(), 5, 0.2, 0.2, 0.2, 0.0);
        }
        level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.HOSTILE, 1.0F, 1.0F);
        level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.HOSTILE, 1.0F, 1.0F);

        Vec3 targetPos = target.position();
        Vec3 mobPos = dasher.position();
        Vec3 relativeVec = targetPos.subtract(mobPos);
        Vec3 direction = relativeVec.normalize();

        // Aplica o movimento
        Vec3 movement = direction.scale(1.25f).add(0f, 0.5f, 0f).multiply(0.75f, 1.25f, 0.75f);
        target.setDeltaMovement(movement.x, movement.y, movement.z);
    }


    public int getCurrentSwingDurationFor(LivingEntity self) {
        if (MobEffectUtil.hasDigSpeed(self)) {
            return 6 - (1 + MobEffectUtil.getDigSpeedAmplification(self));
        } else {
            return self.hasEffect(MobEffects.DIG_SLOWDOWN) ? 6 + (1 + self.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) * 2 : 6;
        }
    }

    protected boolean isTargetDoingCorrectSwingParry(LivingEntity target, DamageSource damageSource) {
        return isTargetDoingCorrectSwingParry(target, damageSource, 0.5f, 0.5f);
    }

    protected boolean isTargetDoingCorrectSwingParry(LivingEntity target, DamageSource damageSource, float viewPrecision, float swingPrecision) {
        // 1. Checa se o alvo está executando um swing/ataque no momento
        // swingPrecision entre 0.0f e 1.0f (ex: 0.5f = primeiros 50% do swing)
        int maxDuration = getCurrentSwingDurationFor(target);
        float allowedParryTicks = maxDuration * swingPrecision;

        // Se o swingTime passou da janela permitida, falha o parry
        if (!target.swinging || target.swingTime > allowedParryTicks) {
            return false;
        }

        Vec3 sourcePosition = damageSource.getSourcePosition();
        if (sourcePosition != null) {
            // 2. Vetor para onde o jogador está olhando (visão)
            Vec3 viewVector = target.getViewVector(1.0F);

            // 3. Vetor que vai do JOGADOR para a FONTE do dano
            Vec3 targetToSource = sourcePosition.subtract(target.getEyePosition());

            // Se quiser ignorar a diferença de altura (parry 2D/horizontal):
            // viewVector = new Vec3(viewVector.x, 0.0D, viewVector.z).normalize();
            // targetToSource = new Vec3(targetToSource.x, 0.0D, targetToSource.z).normalize();

            viewVector = viewVector.normalize();
            targetToSource = targetToSource.normalize();

            // 4. Produto escalar (dot product):
            //  1.0 = olhando EXATAMENTE para a fonte
            //  0.0 = olhando 90 graus para o lado
            // -1.0 = olhando de costas para a fonte
            double dotProduct = targetToSource.dot(viewVector);

            return dotProduct >= viewPrecision;
        }

        return false;
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
        } else {
            onStartDashing();
        }
    }

    protected void onStartDashing() {
        this.phase = Phase.DASHING;
        dasher.level().playSound(null, dasher, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 2, 1);
    }

    @Override
    public void stop() {
        super.stop();
        this.ticks = 0;
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

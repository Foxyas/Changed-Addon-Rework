package net.foxyas.changedaddon.entity.ai.goals.generic.attacks;

import com.mojang.datafixers.util.Pair;
import net.foxyas.changedaddon.entity.bosses.Experiment10BossEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;

public class LastDashPunchGoal extends Goal {

    public static final int MAX_DASH_TICKS = 30;
    public static final int MAX_CHARGE_TICKS = 40;
    protected final Mob mob;
    protected Phase phase = Phase.IDLE;
    protected int chargeTicks = 0;
    protected int dashTicks = 0;
    protected int cooldown = 0;
    protected LivingEntity target;

    // --- NEW FIELDS FOR MULTI-DASH PARRY CHAIN ---
    protected int currentParries = 0;
    protected int requiredParries = 3;
    protected int successfulHitsLanded = 0;
    protected boolean normalModeShieldUsed = false;

    public boolean canTriggerDashPunch = false;

    public LastDashPunchGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        
        if (!canTriggerDashPunch) return false;
        target = mob.getTarget();
        if (target == null || !target.isAlive() || target.isRemoved()) return false;

        return mob.distanceTo(target) <= 16 && mob.onGround();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.phase == Phase.IDLE && cooldown > 0) {
            return false;
        }
        if (this.phase != Phase.IDLE && target != null) {
            return true;
        }
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        phase = Phase.CHARGING;
        chargeTicks = 0;
        dashTicks = 0;
        currentParries = 0;
        successfulHitsLanded = 0;
        normalModeShieldUsed = false;

        // Determine required parries based on difficulty
        Difficulty difficulty = mob.level().getDifficulty();
        if (difficulty == Difficulty.HARD) {
            requiredParries = 5;
        } else if (difficulty == Difficulty.NORMAL) {
            requiredParries = 4;
        } else {
            requiredParries = 3; // Easy / Peaceful fallback
        }

        mob.getNavigation().stop();

        if (mob.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, mob.getX(), mob.getEyeY(), mob.getZ(), 1, 0, 0, 0, 0);

            for (LivingEntity living : mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(4),
                    (e) -> e != mob && !e.isSpectator())) {
                Vec3 delta = living.position().subtract(mob.position());
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

        if (mob.level() instanceof ServerLevel server) {
            if (mob instanceof ChangedEntity changedEntity) {
                Pair<Color3, Color3> entityColor = ChangedEntities.getEntityColor(changedEntity);
                Color3 first = entityColor.getFirst();
                Color3 second = entityColor.getSecond();
                RandomSource randomSource = mob.getRandom();
                server.sendParticles(ParticleTypes.ENTITY_EFFECT, mob.getX() + randomSource.nextGaussian() * 2, mob.getEyeY() + randomSource.nextGaussian() * 0.2, mob.getZ() + randomSource.nextGaussian() * 2, 0, first.red() / 255, first.green() / 255, first.blue() / 255, 0.0);
                server.sendParticles(ParticleTypes.ENTITY_EFFECT, mob.getX() + randomSource.nextGaussian() * 2, mob.getEyeY() + randomSource.nextGaussian() * 0.2, mob.getZ() + randomSource.nextGaussian() * 2, 0, second.red() / 255, second.green() / 255, second.blue() / 255, 0.0);
            } else {
                server.sendParticles(ParticleTypes.ENTITY_EFFECT, mob.getX(), mob.getEyeY(), mob.getZ(), 0, 0.2, 0.2, 0.2, 0.0);
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
        if (mob instanceof Experiment10BossEntity e) e.setDashing(true);
        dashTicks++;
        if (target.distanceTo(mob) > 0) {
            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            mob.setYBodyRot(mob.getYRot());
        }

        Vec3 targetPos = target.position();
        Vec3 mobPos = mob.position();
        Vec3 relativeVec = targetPos.subtract(mobPos);
        double distSq = relativeVec.lengthSqr();

        Vec3 direction = distSq < 1.0E-4D ? Vec3.directionFromRotation(0, mob.getYRot()) : relativeVec.normalize();
        Vec3 movement = direction.scale(0.85f);

        mob.setDeltaMovement(movement.x, movement.y * 0, movement.z);
        mob.hasImpulse = true;
        mob.hurtMarked = true;

        if (dashTicks > MAX_DASH_TICKS) {
            // If dash runs out of time without hitting or being parried, treat as a missed attack / reset or stop
            stop();
            return;
        }

        if (isDasherReachingTarget()) {
            applyImpact(target);
        }
    }

    public boolean isDasherReachingTarget() {
        return mob.getBoundingBox().inflate(3).intersects(target.getBoundingBox());
    }

    protected void handleBlockBreaking() {
        if (!(mob.level() instanceof ServerLevel serverLevel) || dashTicks % 4 != 0) return;

        BlockPos center = mob.blockPosition().above();
        BlockPos.betweenClosedStream(center.offset(-2, -1, -2), center.offset(2, 2, 2)).forEach(pos -> {
            BlockState state = serverLevel.getBlockState(pos);
            if (!state.isAir() && state.getDestroySpeed(serverLevel, pos) >= 0 && !state.is(Tags.Blocks.NEEDS_NETHERITE_TOOL)) {
                serverLevel.destroyBlock(pos, true, mob);
            }
        });
    }

    protected void applyImpact(LivingEntity target) {
        DamageSource pSource = mob.damageSources().mobAttack(mob);
        Difficulty difficulty = mob.level().getDifficulty();

        boolean blockHandled = false;

        // --- DIFFICULTY SHIELD HANDLING ---
        if (target.isUsingItem() && target.getUseItem().is(Items.SHIELD)) {
            if (difficulty == Difficulty.HARD) {
                // Hard mode: Shield blocking does NOT work, straight bypass and apply cooldown + hint
                if (target instanceof Player player) {
                    player.disableShield(true);
                    player.displayClientMessage(Component.translatable("text.changed_addon.boss_attacks.shield_bypass_hard"), true);
                }
                blockHandled = false; // Bypass shield completely
            } else if (difficulty == Difficulty.NORMAL) {
                // Normal mode: Shield blocking works just once, then applies cooldown + hint
                if (!normalModeShieldUsed) {
                    normalModeShieldUsed = true;
                    if (target instanceof Player player) {
                        player.disableShield(true);
                        player.displayClientMessage(Component.translatable("text.changed_addon.boss_attacks.shield_broken_normal"), true);
                    }
                    onBlockedAttemptToHitTarget(target);
                    return;
                } else {
                    // Subsequent blocks in normal mode fail/bypass
                    if (target instanceof Player player) {
                        player.displayClientMessage(Component.translatable("text.changed_addon.boss_attacks.shield_bypass_hard"), true);
                    }
                    blockHandled = false;
                }
            } else {
                // Easy mode: Shield blocking works fully as a parry/block
                if (target.isDamageSourceBlocked(pSource)) {
                    onBlockedAttemptToHitTarget(target);
                    return;
                }
            }
        }

        // --- PARRY & HIT LOGIC ---
        if (!blockHandled && target.isDamageSourceBlocked(pSource)) {
            onBlockedAttemptToHitTarget(target);
            return;
        }

        // Check for manual weapon swing parry (Easy/Normal/Hard active parry system)
        if (isTargetDoingCorrectSwingParry(target, pSource)) {
            handleSuccessfulParry(target);
        } else {
            // Boss successfully lands a hit!
            if (target.hurt(pSource, 10.0F)) {
                onHitTarget(target);
            }
        }
    }

    protected void handleSuccessfulParry(LivingEntity target) {
        currentParries++;
        
        if (mob.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLASH, mob.getX(), mob.getEyeY(), mob.getZ(), 5, 0.2, 0.2, 0.2, 0.25f);
            ExperienceOrb experienceOrb = new ExperienceOrb(serverLevel, mob.getX(), mob.getY(), mob.getZ(), 3);
            Vec3 targetPos = target.getBoundingBox().getCenter();
            Vec3 direction = targetPos.subtract(mob.getEyePosition()).normalize();
            experienceOrb.setDeltaMovement(direction.scale(0.8D));
            serverLevel.addFreshEntity(experienceOrb);
        }
        
        mob.level().playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.HOSTILE, 1.0F, 1.0F);
        mob.level().playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.HOSTILE, 1.0F, 1.0F);

        // Recoil boss back
        dashTicks = MAX_DASH_TICKS;
        
        Vec3 targetPos = target.position();
        Vec3 mobPos = mob.position();
        Vec3 movement = mobPos.subtract(targetPos).normalize().scale(1.25f).multiply(1f, 0f, 1f).add(0f, 0.5f, 0f).scale(2.75f);
        mob.setDeltaMovement(movement);

        // CHECK IF MAX PARRIES REACHED
        if (currentParries >= requiredParries) {
            // Boss is completely parried successfully to the max! Punish the boss.
            // Todo: make it kill the boss
            stop();
        } else {
            // Not enough parries yet! Reset state to CHARGING for the next rapid dash
            this.phase = Phase.CHARGING;
            chargeTicks = 0;
            dashTicks = 0;
            if (target instanceof Player player) {
                player.displayClientMessage(Component.translatable("text.changed_addon.boss_attacks.dash_recharge_hint", (requiredParries - currentParries)), true);
            }
        }
    }

    protected void onBlockedAttemptToHitTarget(LivingEntity target) {
        if (mob.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION, target.getX(), target.getY(), target.getZ(), 5, 0.2, 0.2, 0.2, 0.0);
        }
        mob.level().playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.HOSTILE, 1.0F, 1.0F);
        stop();
    }

    protected void onHitTarget(LivingEntity target) {
        successfulHitsLanded++;

        // Heal boss by 5% of max health
        float maxHealth = mob.getMaxHealth();
        mob.heal(maxHealth * 0.05F);

        if (target instanceof Player player) {
            player.displayClientMessage(Component.translatable("text.changed_addon.boss_attacks.isParryable"), true);
        }
        
        if (mob.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION, target.getX(), target.getY(), target.getZ(), 5, 0.2, 0.2, 0.2, 0.0);
        }
        mob.level().playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.HOSTILE, 1.0F, 1.0F);

        // Check if boss has landed 5 hits total -> disable the canTrigger flag
        if (successfulHitsLanded >= 5) {
            this.canTriggerDashPunch = false;
            stop();
            return;
        }

        // If hit lands, reset or continue depending on your flow. Let's bounce back and recharge for another try or stop.
        dashTicks = MAX_DASH_TICKS;
        this.phase = Phase.IDLE;
        stop();
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
        int maxDuration = getCurrentSwingDurationFor(target);
        float allowedParryTicks = maxDuration * swingPrecision;

        if (!target.swinging || target.swingTime > allowedParryTicks) {
            return false;
        }

        Vec3 sourcePosition = damageSource.getSourcePosition();
        if (sourcePosition != null) {
            Vec3 viewVector = target.getViewVector(1.0F).normalize();
            Vec3 targetToSource = sourcePosition.subtract(target.getEyePosition()).normalize();
            double dotProduct = targetToSource.dot(viewVector);
            return dotProduct >= viewPrecision;
        }
        return false;
    }

    @Override
    public void stop() {
        phase = Phase.IDLE;
        cooldown = 60; 
        if (mob instanceof Experiment10BossEntity e) e.setDashing(false);
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
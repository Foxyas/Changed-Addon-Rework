package net.foxyas.changedaddon.entity.ai.goals.exp9;

import net.foxyas.changedaddon.entity.ai.goals.IAbilityGoal;
import net.foxyas.changedaddon.entity.ai.goals.IReactiveGoal;
import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class TeleportRandomlyGoal extends Goal implements IAbilityGoal, IReactiveGoal {
    private final Experiment009BossEntity boss;
    private final UniformInt cooldownProvider;
    private final double teleportRadius;
    private int cooldownTicks;

    public TeleportRandomlyGoal(Experiment009BossEntity boss, UniformInt cooldownProvider, double teleportRadius) {
        this.boss = boss;
        this.cooldownProvider = cooldownProvider;
        this.teleportRadius = teleportRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public TeleportRandomlyGoal(Experiment009BossEntity boss, UniformInt cooldownProvider) {
        this(boss, cooldownProvider, 16.0D);
    }

    @Override
    public boolean canUse() {
        if (this.boss.getTarget() == null || !this.boss.getTarget().isAlive()) {
            return false;
        }

        // Don't teleport while executing channel/cast animations
        if (this.boss.isCastingAttack()) {
            return false;
        }

        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
            return false;
        }

        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return false; // Executes instantly in one tick
    }

    @Override
    public void start() {
        this.cooldownTicks = this.cooldownProvider.sample(this.boss.getRandom());

        double currentX = this.boss.getX();
        double currentY = this.boss.getY();
        double currentZ = this.boss.getZ();

        double targetX = currentX + (this.boss.getRandom().nextDouble() - 0.5D) * this.teleportRadius * 2.0D;
        double targetY = currentY + (this.boss.getRandom().nextInt(5) - 2); // -2 to +2 vertical range
        double targetZ = currentZ + (this.boss.getRandom().nextDouble() - 0.5D) * this.teleportRadius * 2.0D;

        // Visual / Audio effect before teleport
        spawnTeleportParticles(currentX, currentY, currentZ);

        if (this.boss.randomTeleport(targetX, targetY, targetZ, false)) {
            this.boss.level().playSound(
                    null,
                    currentX, currentY, currentZ,
                    SoundEvents.ITEM_PICKUP, // Replace with appropriate sound, e.g. SoundEvents.ENDERMAN_TELEPORT
                    SoundSource.HOSTILE,
                    1.0F,
                    1.2F
            );

            this.boss.level().playSound(
                    null,
                    this.boss.getX(), this.boss.getY(), this.boss.getZ(),
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.HOSTILE,
                    1.0F,
                    1.0F
            );

            // Face target after teleporting
            if (this.boss.getTarget() != null) {
                this.boss.getLookControl().setLookAt(this.boss.getTarget(), 180.0F, 180.0F);
            }

            spawnTeleportParticles(this.boss.getX(), this.boss.getY(), this.boss.getZ());
        }
    }

    private void spawnTeleportParticles(double x, double y, double z) {
        if (this.boss.level().isClientSide) return;
        
        // Spawn lightning sparks/particles around the source/destination
        this.boss.spawnThunderBoltsSpark(10, 1.5D, 6, 8.0F, 0.5F);
    }

    @Override
    public void onHurt(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float pDamageAmount) {

    }

    @Override
    public void onDamage(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float amount, boolean willCauseDamage) {
        if (willCauseDamage) {
            cooldownTicks -= 20;
        }
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
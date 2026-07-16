package net.foxyas.changedaddon.entity.ai.goals.simple;

import net.foxyas.changedaddon.process.features.PatFeatureHandle;
import net.foxyas.changedaddon.util.EntityUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class PatNearbyEntity extends Goal {

    public final PathfinderMob mob;
    private final float speed;
    private LivingEntity target;
    private int patCooldown;

    public PatNearbyEntity(PathfinderMob mob, float speed) {
        this.mob = mob;
        this.speed = speed;
        // This tells the AI system this goal controls movement and looking,
        // preventing conflicts with other goals like wandering.
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (patCooldown > 0) {
            patCooldown--;
            return false;
        }
        // Don't look for someone to pat if the mob is currently attacking someone
        if (this.mob.getTarget() != null) {
            return false;
        }

        // Define a search area (e.g., 8 blocks in all directions)
        AABB searchBox = this.mob.getBoundingBox().inflate(8.0D, 3.0D, 8.0D);

        // Set up conditions to only find non-combat targets that match our safety check
        TargetingConditions conditions = TargetingConditions.forNonCombat().range(8.0).selector(this::isSafeToPat);

        // Find the nearest entity that matches our conditions
        // Note: Use `this.mob.level` instead of `this.mob.level()` if you are on 1.19.4 or older
        this.target = this.mob.level().getNearestEntity(
                LivingEntity.class,
                conditions,
                this.mob,
                this.mob.getX(),
                this.mob.getY(),
                this.mob.getZ(),
                searchBox
        );

        // The goal can start if we successfully found a target
        return this.target != null && mob.getRandom().nextFloat() <= 0.10f;
    }

    // Helper method to define what a "safe" entity is
    private boolean isSafeToPat(LivingEntity entity) {
        if (!entity.isAlive() || entity == this.mob) return false; // Must be alive and not itself

        // If the entity recently hurt our mob, or our mob recently hurt it, it's not safe
        if (entity.getLastHurtByMob() == this.mob || this.mob.getLastHurtByMob() == entity) return false;

        if (mob instanceof AbstractVillager villager && entity instanceof ChangedEntity changedEntity) {
            boolean shouldScareVillager = TransfurVariant.shouldScareVillager(changedEntity, villager);
            return !shouldScareVillager && EntityUtil.isCuteEnoughToReceivePatsFromVillagers(villager, changedEntity);
        }
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        // Continue running the goal as long as the target is alive, and we haven't finished moving
        PathNavigation pathNavigation = this.mob.getNavigation();
        return this.target != null && this.target.isAlive()
                && (pathNavigation.getPath() != null && pathNavigation.getPath().canReach())
                && !pathNavigation.isDone();
    }

    @Override
    public void start() {
        // Start pathfinding to the target
        this.mob.getNavigation().moveTo(this.target, this.speed);
        this.patCooldown = 0;
    }

    @Override
    public void stop() {
        // Clean up when the goal finishes or is interrupted
        this.target = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        // Make the mob look at the target
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        // Calculate distance squared to the target
        double distanceSq = this.mob.distanceToSqr(this.target);

        // If within range (e.g., ~2 blocks = 4.0 distance squared)
        if (distanceSq <= 4.0D) {
            this.mob.getNavigation().stop(); // Stop walking

            if (this.patCooldown <= 0) {
                performPat(this.target);
                this.patCooldown = 260;
                this.target = null;
            }
        } else {
            // Keep updating the path if the target moves
            this.mob.getNavigation().moveTo(this.target, this.speed);
        }

        // Decrease cooldown timer
        if (this.patCooldown > 0) {
            this.patCooldown--;
        }
    }

    private void performPat(LivingEntity target) {
        PatFeatureHandle.patEntity(mob, target, target.getUsedItemHand());
    }
}
package net.foxyas.changedaddon.entity.customHandle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class BurstAbilityHandle {

    protected final LivingEntity mob;
    protected float burstProgress;
    protected final float maxBurstProgress;

    @Nullable
    protected LivingEntity currentTarget;
    protected long lastHitGameTime;
    protected final int timeoutTicks; // Ticks without hitting before accumulating progress

    protected boolean isDestructive;
    protected Predicate<BlockState> canDestroyBlock;

    /**
     * @param mob The entity owning this burst handle.
     * @param maxBurstProgress Maximum progress capacity for the burst.
     * @param timeoutTicks Time elapsed without hitting target (in ticks) to award progress (e.g., 60 ticks = 3 seconds).
     */
    public BurstAbilityHandle(LivingEntity mob, float maxBurstProgress, int timeoutTicks) {
        this(mob, maxBurstProgress, timeoutTicks, false, state -> !state.isAir() && state.getDestroySpeed(null, null) >= 0.0F);
    }

    /**
     * Overloaded constructor for destructive bursts.
     *
     * @param mob The entity owning this burst handle.
     * @param maxBurstProgress Maximum progress capacity for the burst.
     * @param timeoutTicks Time elapsed without hitting target (in ticks) to award progress.
     * @param isDestructive Whether this burst capability can destroy blocks.
     * @param canDestroyBlock Predicate that determines if a BlockState can be destroyed.
     */
    public BurstAbilityHandle(LivingEntity mob, float maxBurstProgress, int timeoutTicks, boolean isDestructive, Predicate<BlockState> canDestroyBlock) {
        this.mob = mob;
        this.maxBurstProgress = maxBurstProgress;
        this.timeoutTicks = timeoutTicks;
        this.burstProgress = 0.0f;
        this.lastHitGameTime = mob.level().getGameTime();
        this.isDestructive = isDestructive;
        this.canDestroyBlock = canDestroyBlock;
    }

    /**
     * Updates the attacker's current target.
     */
    public void setTarget(@Nullable LivingEntity target) {
        if (this.currentTarget != target) {
            this.currentTarget = target;
            this.lastHitGameTime = mob.level().getGameTime(); // Reset timer for the new target
        }
    }

    /**
     * Should be called on every entity tick (e.g., inside LivingEntity#tick).
     */
    public void tick() {
        if (currentTarget == null || !currentTarget.isAlive()) {
            return;
        }

        long currentGameTime = mob.level().getGameTime();

        // Check if the timeout threshold without hitting the target was reached
        long elapsedTicks = currentGameTime - lastHitGameTime;
        if (elapsedTicks >= timeoutTicks && mob.getCombatTracker().takingDamage) {
            // Add burst progress due to inactivity/failing to hit the target
            addBurstProgress(0.5f); // Adjust value as needed

            // Update time to avoid continuous stacking without interval
            this.lastHitGameTime = currentGameTime;
        }
    }

    /**
     * Should be called whenever the attacker deals damage to any entity.
     *
     * @param actualTarget Entity that actually took damage.
     * @param amount Damage amount.
     */
    public void onDamageDealt(LivingEntity actualTarget, float amount) {
        if (this.currentTarget == null) {
            return;
        }

        long currentGameTime = mob.level().getGameTime();

        // CASE 1: Damage WAS dealt to the correct target
        if (actualTarget == this.currentTarget) {
            // Reset the inactivity timer
            this.lastHitGameTime = currentGameTime;
        }
        // CASE 2: Damage WAS NOT dealt to the intended target (hit another entity instead)
        else {
            addBurstProgress(1.0f); // Add burst progress for missing the main target
        }
    }

    /**
     * Destroys blocks in a radius around the mob if the burst is destructive and ready.
     * Called automatically when burst hits 100% or invoked manually upon burst trigger.
     *
     * @param radius Radius around the mob to destroy blocks.
     */
    public void destroyBlocks(int radius) {
        Level level = mob.level();

        // Must run on server side, burst must be destructive, and mob griefing rule must be allowed
        if (level.isClientSide() || !this.isDestructive || !ForgeEventFactory.getMobGriefingEvent(level, mob)) {
            return;
        }

        BlockPos center = mob.blockPosition();

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -radius, -radius), center.offset(radius, radius, radius))) {
            if (pos.distSqr(center) <= radius * radius) {
                BlockState state = level.getBlockState(pos);

                if (canDestroyBlock.test(state)) {
                    level.destroyBlock(pos, true, mob); // Drops items and triggers block break particles/sounds
                }
            }
        }
    }

    /**
     * Registers/Adds progress to the burst up to the max cap.
     * Automatically triggers block destruction if configured when reaching maximum capacity.
     */
    public void addBurstProgress(float amount) {
        this.burstProgress = Math.min(this.maxBurstProgress, this.burstProgress + amount);

        if (isBurstReady() && isDestructive) {
            destroyBlocks(3); // Default radius, can be overridden or called explicitly
        }
    }

    public float getBurstProgress() {
        return burstProgress;
    }

    public boolean isBurstReady() {
        return burstProgress >= maxBurstProgress;
    }

    public void resetBurst() {
        this.burstProgress = 0.0f;
    }

    public boolean isDestructive() {
        return isDestructive;
    }

    public void setDestructive(boolean destructive) {
        this.isDestructive = destructive;
    }

    public Predicate<BlockState> getCanDestroyBlock() {
        return canDestroyBlock;
    }

    public void setCanDestroyBlock(Predicate<BlockState> canDestroyBlock) {
        this.canDestroyBlock = canDestroyBlock;
    }

    public LivingEntity getMob() {
        return mob;
    }

    @Nullable
    public LivingEntity getCurrentTarget() {
        return currentTarget;
    }
}
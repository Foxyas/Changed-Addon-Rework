package net.foxyas.changedaddon.entity.customHandle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class BurstAbilityHandle<T extends LivingEntity> {

    protected final T mob;
    protected float burstProgress;
    protected BiFunction<T, BurstAbilityHandle<T>, Float> maxBurstProgressSupplier;

    @Nullable
    protected LivingEntity currentTarget;
    protected long lastHitGameTime;
    protected final int timeoutTicks;

    protected boolean isDestructive;
    protected boolean isEvasive;
    protected boolean useGameTimeInsteadOfEntityTicks;
    protected BiPredicate<BlockState, BlockPos> canDestroyBlock;

    @Nullable
    protected BiConsumer<T, BurstAbilityHandle<T>> customDestructiveHandle;
    @Nullable
    protected BiConsumer<T, BurstAbilityHandle<T>> customEvasiveHandle;
    @Nullable
    protected BiConsumer<T, BurstAbilityHandle<T>> customHandle;

    /**
     * @param mob                      The entity owning this burst handle.
     * @param maxBurstProgressSupplier Dynamic calculation for the max burst limit.
     * @param timeoutTicks             Time elapsed without hitting target (in ticks) to award progress.
     */
    public BurstAbilityHandle(T mob, BiFunction<T, BurstAbilityHandle<T>, Float> maxBurstProgressSupplier, int timeoutTicks) {
        this(mob, maxBurstProgressSupplier, timeoutTicks, false, false, false, (state, pos) -> !state.isAir() && state.getDestroySpeed(mob.level(), pos) >= 0.0F, null, null, null);
    }

    /**
     * Overloaded constructor for full capability customization.
     */
    public BurstAbilityHandle(T mob,
                              BiFunction<T, BurstAbilityHandle<T>, Float> maxBurstProgressSupplier,
                              int timeoutTicks,
                              boolean isDestructive,
                              boolean isEvasive,
                              boolean useGameTimeInsteadOfEntityTicks,
                              BiPredicate<BlockState, BlockPos> canDestroyBlock,
                              @Nullable BiConsumer<T, BurstAbilityHandle<T>> customDestructiveHandle,
                              @Nullable BiConsumer<T, BurstAbilityHandle<T>> customEvasiveHandle,
                              @Nullable BiConsumer<T, BurstAbilityHandle<T>> customHandle
    ) {
        this.mob = mob;
        this.maxBurstProgressSupplier = maxBurstProgressSupplier;
        this.timeoutTicks = timeoutTicks;
        this.burstProgress = 0.0f;
        this.lastHitGameTime = mob.level().getGameTime();
        this.isDestructive = isDestructive;
        this.isEvasive = isEvasive;
        this.useGameTimeInsteadOfEntityTicks = useGameTimeInsteadOfEntityTicks;
        this.canDestroyBlock = canDestroyBlock;
        this.customDestructiveHandle = customDestructiveHandle;
        this.customEvasiveHandle = customEvasiveHandle;
        this.customHandle = customHandle;
    }

    /**
     * Static helper method to instantiate a new Builder.
     */
    public static <T extends LivingEntity> Builder<T> builder(T mob) {
        return new Builder<>(mob);
    }

    /**
     * Computes the current maximum burst progress dynamically.
     */
    public float getMaxBurstProgress() {
        return Math.max(1.0f, this.maxBurstProgressSupplier.apply(this.mob, this));
    }

    public void setMaxBurstProgressSupplier(BiFunction<T, BurstAbilityHandle<T>, Float> maxBurstProgressSupplier) {
        this.maxBurstProgressSupplier = maxBurstProgressSupplier;
    }

    // --- Custom Action Handlers ---

    public void setCustomDestructiveHandle(@Nullable BiConsumer<T, BurstAbilityHandle<T>> customDestructiveHandle) {
        this.customDestructiveHandle = customDestructiveHandle;
    }

    public void setCustomEvasiveHandle(@Nullable BiConsumer<T, BurstAbilityHandle<T>> customEvasiveHandle) {
        this.customEvasiveHandle = customEvasiveHandle;
    }

    public void setCustomHandle(@Nullable BiConsumer<T, BurstAbilityHandle<T>> customHandle) {
        this.customHandle = customHandle;
    }

    public void appendCustomHandle(BiConsumer<T, BurstAbilityHandle<T>> handle) {
        if (this.customHandle == null) {
            this.customHandle = handle;
        } else {
            this.customHandle = this.customHandle.andThen(handle);
        }
    }

    @Nullable
    public BiConsumer<T, BurstAbilityHandle<T>> getCustomDestructiveHandle() {
        return customDestructiveHandle;
    }

    @Nullable
    public BiConsumer<T, BurstAbilityHandle<T>> getCustomEvasiveHandle() {
        return customEvasiveHandle;
    }

    @Nullable
    public BiConsumer<T, BurstAbilityHandle<T>> getCustomHandle() {
        return customHandle;
    }

    /**
     * Updates the attacker's current target.
     */
    public void setTarget(@Nullable LivingEntity target) {
        if (this.currentTarget != target) {
            this.currentTarget = target;
            this.lastHitGameTime = mob.level().getGameTime();
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

        if (mob.getLastAttacker() != currentTarget) {
            addBurstProgress(0.5f);
        }

        if (useGameTimeInsteadOfEntityTicks) {
            // Check if the timeout threshold without hitting the target was reached
            long elapsedTicks = currentGameTime - lastHitGameTime;
            if (elapsedTicks >= timeoutTicks && mob.getCombatTracker().takingDamage) {
                addBurstProgress(0.5f);
                this.lastHitGameTime = currentGameTime;
            }
        } else {
            if (mob.getLastHurtMobTimestamp() >= timeoutTicks && mob.getCombatTracker().takingDamage) {
                addBurstProgress(0.5f);
            }
        }


    }

    /**
     * Should be called whenever the  deals damage to any entity.
     *
     * @param source DamageSource.
     * @param amount Damage amount.
     */
    public void onDamageTaken(DamageSource source, float amount) {
        Entity sourceEntity = source.getEntity();
        if (this.currentTarget == null) {
            return;
        }

        // Damage WASN'T dealt by the correct target
        if (sourceEntity != this.currentTarget) {
            float damageRatio = amount / Math.max(mob.getHealth(), 1);
            addBurstProgress(damageRatio * getMaxBurstProgress());
        }
    }

    /**
     * Should be called whenever the  deals damage to any entity.
     *
     * @param source DamageSource.
     * @param amount Damage amount.
     */
    public void onDamageDealt(DamageSource source, float amount) {
        Entity sourceEntity = source.getEntity();
        if (this.currentTarget == null) {
            return;
        }

        long currentGameTime = mob.level().getGameTime();

        // CASE 1: Damage WAS dealt to the correct target
        if (sourceEntity == this.currentTarget) {
            this.lastHitGameTime = currentGameTime;
        }
    }

    /**
     * Evaluates the burst readiness and triggers configured effects.
     */
    public void checkBurstState() {
        if (!isBurstReady()) {
            return;
        }

        // Destructive check
        if (this.isDestructive) {
            if (this.customDestructiveHandle != null) {
                this.customDestructiveHandle.accept(this.mob, this);
            } else {
                destroyBlocks(3);
            }
        }

        // Evasive check
        if (this.isEvasive) {
            if (this.customEvasiveHandle != null) {
                this.customEvasiveHandle.accept(this.mob, this);
            } else {
                applyEvasiveKnockback(5.0D, 1.25D);
            }
        }

        // General custom action handle
        if (this.customHandle != null) {
            this.customHandle.accept(this.mob, this);
        }
        this.burstProgress = 0;
    }

    /**
     * Default generic method to destroy blocks in a radius around the mob.
     */
    public void destroyBlocks(int radius) {
        Level level = mob.level();

        if (level.isClientSide() || !this.isDestructive || !ForgeEventFactory.getMobGriefingEvent(level, mob)) {
            return;
        }

        BlockPos center = mob.blockPosition();

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, 0, -radius), center.offset(radius, radius, radius))) {
            if (pos.distSqr(center) <= radius * radius) {
                BlockState state = level.getBlockState(pos);

                if (canDestroyBlock.test(state, pos)) {
                    level.destroyBlock(pos, true, mob);
                }
            }
        }
    }

    /**
     * Default generic method to push nearby entities away from the mob to create distance.
     */
    public void applyEvasiveKnockback(double radius, double strength) {
        Level level = mob.level();
        if (level.isClientSide()) {
            return;
        }

        AABB area = mob.getBoundingBox().inflate(radius);
        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, area, entity -> entity != mob && entity.isAlive());

        level.playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.HOSTILE, 1.0F, 2.0F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, mob.getX(), mob.getEyeY(), mob.getZ(), 0, 0, 0, 0, 1);
        }

        for (LivingEntity target : nearby) {
            Vec3 pushVec = target.position().subtract(mob.position()).normalize();

            target.knockback(strength, -pushVec.x, -pushVec.z);
            target.hasImpulse = true;
            if (target instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer.getId(), serverPlayer.getDeltaMovement()));
            }
        }
    }

    /**
     * Registers/Adds progress to the burst up to the dynamically computed max cap.
     */
    public void addBurstProgress(float amount) {
        float maxCap = getMaxBurstProgress();
        this.burstProgress = Math.min(maxCap, this.burstProgress + amount);

        if (isBurstReady()) {
            checkBurstState();
        }
    }

    public float getBurstProgress() {
        return burstProgress;
    }

    public boolean isBurstReady() {
        return burstProgress >= getMaxBurstProgress();
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

    public boolean isEvasive() {
        return isEvasive;
    }

    public void setEvasive(boolean evasive) {
        this.isEvasive = evasive;
    }

    public BiPredicate<BlockState, BlockPos> getCanDestroyBlock() {
        return canDestroyBlock;
    }

    public void setCanDestroyBlock(BiPredicate<BlockState, BlockPos> canDestroyBlock) {
        this.canDestroyBlock = canDestroyBlock;
    }

    public void appendCanDestroyBlockPredicate(BiPredicate<BlockState, BlockPos> predicate) {
        this.canDestroyBlock = this.canDestroyBlock.and(predicate);
    }

    public T getMob() {
        return mob;
    }

    @Nullable
    public LivingEntity getCurrentTarget() {
        return currentTarget;
    }

    // ==========================================
    // BUILDER CLASS
    // ==========================================

    public static class Builder<T extends LivingEntity> {
        private final T mob;
        private BiFunction<T, BurstAbilityHandle<T>, Float> maxBurstProgressSupplier = (entity, handle) -> 100.0f;
        private int timeoutTicks = 60;
        private boolean isDestructive = false;
        private boolean isEvasive = false;
        private boolean useGameTimeInsteadOfEntityTicks = false;
        private BiPredicate<BlockState, BlockPos> canDestroyBlock;

        @Nullable
        private BiConsumer<T, BurstAbilityHandle<T>> customDestructiveHandle = null;
        @Nullable
        private BiConsumer<T, BurstAbilityHandle<T>> customEvasiveHandle = null;
        @Nullable
        private BiConsumer<T, BurstAbilityHandle<T>> customHandle = null;

        public Builder(T mob) {
            this.mob = Objects.requireNonNull(mob, "Mob entity cannot be null");
            this.canDestroyBlock = (state, pos) -> !state.isAir() && state.getDestroySpeed(mob.level(), pos) >= 0.0F;
        }

        /**
         * Sets a fixed maximum progress capacity.
         */
        public Builder<T> maxProgress(float maxBurstProgress) {
            this.maxBurstProgressSupplier = (entity, handle) -> maxBurstProgress;
            return this;
        }

        /**
         * Sets a dynamic maximum progress supplier based on mob phase, health, etc.
         */
        public Builder<T> maxProgress(BiFunction<T, BurstAbilityHandle<T>, Float> maxBurstProgressSupplier) {
            this.maxBurstProgressSupplier = maxBurstProgressSupplier;
            return this;
        }

        public Builder<T> timeoutTicks(int timeoutTicks) {
            this.timeoutTicks = timeoutTicks;
            return this;
        }

        public Builder<T> destructive(boolean isDestructive) {
            this.isDestructive = isDestructive;
            return this;
        }

        public Builder<T> setDestructive() {
            this.isDestructive = true;
            return this;
        }

        public Builder<T> evasive(boolean isEvasive) {
            this.isEvasive = isEvasive;
            return this;
        }

        public Builder<T> setEvasive() {
            this.isEvasive = true;
            return this;
        }

        public Builder<T> useGameTimeInsteadOfEntityTicks() {
            this.useGameTimeInsteadOfEntityTicks = true;
            return this;
        }

        public Builder<T> setUseGameTimeInsteadOfEntityTicks(boolean useGameTimeInsteadOfEntityTicks) {
            this.useGameTimeInsteadOfEntityTicks = useGameTimeInsteadOfEntityTicks;
            return this;
        }

        public Builder<T> canDestroyBlock(BiPredicate<BlockState, BlockPos> predicate) {
            this.canDestroyBlock = predicate;
            return this;
        }

        public Builder<T> addBlockFilter(BiPredicate<BlockState, BlockPos> predicate) {
            this.canDestroyBlock = this.canDestroyBlock.and(predicate);
            return this;
        }

        public Builder<T> onDestructiveBurst(BiConsumer<T, BurstAbilityHandle<T>> customDestructiveHandle) {
            this.customDestructiveHandle = customDestructiveHandle;
            this.isDestructive = true;
            return this;
        }

        public Builder<T> onEvasiveBurst(BiConsumer<T, BurstAbilityHandle<T>> customEvasiveHandle) {
            this.customEvasiveHandle = customEvasiveHandle;
            this.isEvasive = true;
            return this;
        }

        public Builder<T> onBurst(BiConsumer<T, BurstAbilityHandle<T>> customHandle) {
            this.customHandle = customHandle;
            return this;
        }

        public Builder<T> addBurstAction(BiConsumer<T, BurstAbilityHandle<T>> handle) {
            if (this.customHandle == null) {
                this.customHandle = handle;
            } else {
                this.customHandle = this.customHandle.andThen(handle);
            }
            return this;
        }

        public BurstAbilityHandle<T> build() {
            return new BurstAbilityHandle<>(
                    mob,
                    maxBurstProgressSupplier,
                    timeoutTicks,
                    isDestructive,
                    isEvasive,
                    useGameTimeInsteadOfEntityTicks,
                    canDestroyBlock,
                    customDestructiveHandle,
                    customEvasiveHandle,
                    customHandle
            );
        }
    }
}
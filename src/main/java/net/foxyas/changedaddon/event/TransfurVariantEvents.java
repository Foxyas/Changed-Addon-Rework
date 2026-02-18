package net.foxyas.changedaddon.event;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TransfurVariantEvents {


    public static class KillAfterTransfurredEvent extends Event {

        public final LivingEntity targetEntity;
        public final LivingEntity sourceEntity;

        public KillAfterTransfurredEvent(LivingEntity targetEntity, LivingEntity sourceEntity) {
            this.targetEntity = targetEntity;
            this.sourceEntity = sourceEntity;
        }

        public LivingEntity getTargetEntity() {
            return targetEntity;
        }

        public LivingEntity getSourceEntity() {
            return sourceEntity;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class SpawnAtTransfurredEntityEvent extends Event {

        public final LivingEntity spawnAt;
        @Nullable
        public ChangedEntity changedEntity;

        public SpawnAtTransfurredEntityEvent(LivingEntity spawnAt, @Nullable ChangedEntity changedEntity) {
            this.spawnAt = spawnAt;
            this.changedEntity = changedEntity;
        }

        public LivingEntity getSpawnAt() {
            return spawnAt;
        }

        public @Nullable ChangedEntity getChangedEntity() {
            return changedEntity;
        }

        public void setChangedEntity(@Nullable ChangedEntity changedEntity) {
            this.changedEntity = changedEntity;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class KillAfterTransfurredSpecificEvent extends Event {

        private final LivingEntity targetEntity;
        protected final IAbstractChangedEntity originalCause;
        protected IAbstractChangedEntity newReplacement;

        public KillAfterTransfurredSpecificEvent(LivingEntity targetEntity, @Nullable IAbstractChangedEntity iAbstractChangedEntity) {
            this.targetEntity = targetEntity;
            this.originalCause = iAbstractChangedEntity;
            this.newReplacement = originalCause;
        }

        public IAbstractChangedEntity getOriginalCause() {
            return originalCause;
        }

        public IAbstractChangedEntity getNewReplacement() {
            return newReplacement;
        }

        public void setNewReplacement(IAbstractChangedEntity newReplacement) {
            this.newReplacement = newReplacement;
        }

        public LivingEntity getTargetEntity() {
            return targetEntity;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class OnPlayerFuseWithOther extends Event {

        private final LivingEntity target;
        private final LivingEntity source;
        private final TransfurVariant<?> originalFusionVariant;
        private final ChangedEntity sourceChangedEntity;

        public OnPlayerFuseWithOther(LivingEntity target,
                                     LivingEntity source,
                                     ChangedEntity sourceChangedEntity,
                                     TransfurVariant<?> originalFusionVariant
        ) {
            this.target = target;
            this.source = source;
            this.originalFusionVariant = originalFusionVariant;
            this.sourceChangedEntity = sourceChangedEntity;
        }

        @Override
        public boolean isCancelable() {
            return false;
        }

        public TransfurVariant<?> getOriginalFusionVariant() {
            return originalFusionVariant;
        }

        public LivingEntity getSource() {
            return source;
        }

        public LivingEntity getTarget() {
            return target;
        }

        public ChangedEntity getSourceChangedEntity() {
            return sourceChangedEntity;
        }
    }

    public static class OnEntityFuseWithOther extends Event {

        private final LivingEntity target;
        private final ChangedEntity source;
        private final TransfurVariant<?> originalFusionVariant;
        @Nullable private final TransfurVariantInstance<?> oldVariantInstance;

        public OnEntityFuseWithOther(LivingEntity target,
                                     @Nullable TransfurVariantInstance<?> oldVariantInstance,
                                     ChangedEntity source,
                                     TransfurVariant<?> originalFusionVariant
        ) {
            this.target = target;
            this.oldVariantInstance = oldVariantInstance;
            this.source = source;
            this.originalFusionVariant = originalFusionVariant;
        }

        @Override
        public boolean isCancelable() {
            return false;
        }

        public TransfurVariant<?> getOriginalFusionVariant() {
            return originalFusionVariant;
        }

        public ChangedEntity getSource() {
            return source;
        }

        public LivingEntity getTarget() {
            return target;
        }

        public @Nullable TransfurVariantInstance<?> getOldVariantInstance() {
            return oldVariantInstance;
        }
    }

    public static class KillAfterTransfurredFinalEvent extends Event {

        private final LivingEntity targetEntity;
        private final LivingEntity originalSource;
        private LivingEntity source;

        public KillAfterTransfurredFinalEvent(LivingEntity targetEntity, LivingEntity source) {
            this.targetEntity = targetEntity;
            this.originalSource = source;
            this.source = source;
        }

        public LivingEntity getSource() {
            return source;
        }

        public void setSource(LivingEntity source) {
            this.source = source;
        }

        public LivingEntity getOriginalSource() {
            return originalSource;
        }

        public LivingEntity getTargetEntity() {
            return targetEntity;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    /**
     * Event fired to allow overriding the {@link TransfurVariant} used as the
     * source variant during an absorption or replication attempt.
     *
     * <p>This event is fired before the absorption/replication logic continues,
     * allowing listeners to replace the original source variant based on the
     * current context, such as the target entity, source entity, damage amount,
     * or possible mob fusions.</p>
     *
     * <p><b>Cancel behavior:</b><br>
     * If this event is cancelled, the absorption or replication will proceed using
     * the <b>original (default) TransfurVariant</b>. Any variant set via
     * {@link #setVariant(TransfurVariant)} will be ignored.</p>
     *
     * <p>By default, the value returned by {@link #getVariant()} is initialized
     * to the original variant provided by the source entity.</p>
     *
     * <p><b>Fusion context:</b><br>
     * The value returned by {@link #getPossibleMobFusions()} represents the set of
     * possible fusion variants available during an absorption attempt.
     * If this value is {@code null}, the event is most likely being fired in a
     * <b>replication context</b> rather than a standard mob fusion scenario.</p>
     */
    public static class OverrideSourceTransfurVariantEvent extends Event {

        public enum TransfurType {
            ABSORPTION,
            REPLICATION
        }

        private final TransfurType transfurType;
        private final TransfurVariant<?> original;
        private final ChangedEntity changedEntity;
        private final LivingEntity target;
        private final IAbstractChangedEntity source;
        private final float amount;

        @Nullable
        private final List<TransfurVariant<?>> possibleMobFusions;

        private TransfurVariant<?> variant;

        public OverrideSourceTransfurVariantEvent(
                TransfurType transfurType,
                TransfurVariant<?> original,
                ChangedEntity changedEntity, LivingEntity target,
                IAbstractChangedEntity source,
                float amount,
                @Nullable List<TransfurVariant<?>> possibleMobFusions
        ) {
            this.transfurType = transfurType;
            this.original = original;
            this.changedEntity = changedEntity;
            this.target = target;
            this.source = source;
            this.amount = amount;
            this.possibleMobFusions = possibleMobFusions;
            this.variant = original;
        }

        public TransfurType getTransfurType() {
            return transfurType;
        }

        /**
         * Returns the {@link IAbstractChangedEntity} instance representing the
         * <b>current (self)</b> entity performing the absorption or replication.
         *
         * <p>This is the primary entity whose transfur logic is currently being
         * executed. It should be used for context-sensitive logic where the
         * behavior depends on the state, variant, or properties of the entity
         * initiating the process.</p>
         *
         * <p>This value is guaranteed to be non-null for the lifetime of this event.</p>
         *
         * <p><b>Important:</b><br>
         * This is <b>not</b> the target being absorbed or replicated. To access the
         * affected entity, use {@link #getTarget()} instead.</p>
         *
         * @return The {@link IAbstractChangedEntity} representing the entity
         * currently executing the absorption or replication logic.
         */
        public ChangedEntity getChangedEntity() {
            return changedEntity;
        }

        /**
         * Sets a new {@link TransfurVariant} to be used as the source variant.
         *
         * <p>This value will only take effect if the event is <b>not cancelled</b>.</p>
         */
        public void setVariant(TransfurVariant<?> variant) {
            this.variant = variant;
        }

        /**
         * @return The {@link TransfurVariant} that will be used as the source variant.
         * If the event was cancelled, this will effectively be the original variant.
         */
        public TransfurVariant<?> getVariant() {
            return variant;
        }

        /**
         * @return {@code true}, indicating this event can be cancelled.
         */
        @Override
        public boolean isCancelable() {
            return true;
        }

        /**
         * Returns the list of possible mob fusion variants available for this event.
         *
         * <p>If this value is {@code null}, the event is most likely being processed
         * in a <b>replication context</b> rather than a standard absorption with
         * mob fusion selection.</p>
         *
         * @return A list of possible fusion {@link TransfurVariant}s, or {@code null}
         * if no fusion context is present.
         */
        public @Nullable List<TransfurVariant<?>> getPossibleMobFusions() {
            return possibleMobFusions;
        }

        /**
         * @return The damage amount contributing to the absorption or replication.
         */
        public float getAmount() {
            return amount;
        }

        /**
         * @return The abstract representation of the source (attacking) entity.
         */
        public IAbstractChangedEntity getSource() {
            return source;
        }

        /**
         * @return The target entity being absorbed or replicated.
         */
        public LivingEntity getTarget() {
            return target;
        }

        /**
         * @return The original {@link TransfurVariant} provided by the source entity
         * before any overrides were applied.
         */
        public TransfurVariant<?> getOriginal() {
            return original;
        }
    }
}

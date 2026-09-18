package net.foxyas.changedaddon.entity.ai.behaviors;

import com.google.common.collect.ImmutableMap;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.process.features.PatFeatureHandle;
import net.foxyas.changedaddon.util.EntityUtil;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class PatNearbyEntityBehavior extends Behavior<Villager> {

    private final float speedModifier;
    private final int searchRadius;
    private LivingEntity target;

    public PatNearbyEntityBehavior(float speedModifier, int searchRadius) {
        super(ImmutableMap.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, // Don't interrupt existing walk targets
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT
        ), 150);

        this.speedModifier = speedModifier;
        this.searchRadius = searchRadius;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Villager owner) {
        if (owner.getRandom().nextFloat() > 0.10f) return false;

        Brain<Villager> brain = owner.getBrain();
        Optional<NearestVisibleLivingEntities> nearestVisible = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        if (nearestVisible.isEmpty()) return false;

        // Correctly query the NearestVisibleLivingEntities wrapper
        this.target = nearestVisible.get()
                .find(entity -> entity.closerThan(owner, searchRadius) && isSafeToPat(owner, entity))
                .findFirst().orElse(null);

        return this.target != null;
    }

    @Override
    protected void start(ServerLevel level, Villager entity, long gameTime) {
        setPathAndLookTarget(entity);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Villager entity, long gameTime) {
        return this.target != null && this.target.isAlive() && entity.closerThan(this.target, searchRadius + 2);
    }

    @Override
    protected void tick(ServerLevel level, Villager owner, long gameTime) {
        if (this.target == null) return;

        // Continuously update look target and navigation destination
        setPathAndLookTarget(owner);

        // If close enough (~2.5 blocks)
        if (owner.closerThan(this.target, 2.5D)) {
            if (PatFeatureHandle.patEntity(owner, this.target, owner.getUsedItemHand())) {
                this.doStop(level, owner, gameTime);
            }
        }
    }

    @Override
    protected void stop(ServerLevel level, Villager entity, long gameTime) {
        this.target = null;
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    private void setPathAndLookTarget(Villager entity) {
        Brain<?> brain = entity.getBrain();
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.target, true));
        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(this.target, false), this.speedModifier, 1));
    }

    private boolean isSafeToPat(AbstractVillager villager, LivingEntity entity) {
        if (!entity.isAlive() || entity == villager) return false;
        if (entity.getLastHurtByMob() == villager || villager.getLastHurtByMob() == entity) return false;

        Optional<IAbstractChangedEntity> optional = IAbstractChangedEntity.forEitherSafe(entity);
        if (optional.isPresent()) {
            ChangedEntity cEntity = optional.get().getChangedEntity();
            return !TransfurVariant.shouldScareVillager(cEntity, villager)
                    && EntityUtil.isCuteEnoughToReceivePatsFromVillagers(villager, cEntity);
        }

        return entity.getType().is(ChangedAddonTags.EntityTypes.PATABLE);
    }

    private boolean hasPositiveGossip(Villager villager, LivingEntity entity) {
        if (!entity.isAlive() || entity == villager) return false;

        if (entity instanceof Player player) {
            int playerReputation = villager.getPlayerReputation(player);
            return playerReputation > 0;
        }

        return false;
    }
}
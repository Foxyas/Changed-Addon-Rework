package net.foxyas.changedaddon.entity.ai.goals;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.api.SyncTrackMotion;
import net.foxyas.changedaddon.network.packet.RequestMovementCheckPacket;
import net.ltxprogrammer.changed.block.Pillow;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.EnumSet;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

public class AlphaSleepGoal extends Goal {

    private static final EnumSet<Goal.Flag> FLAGS = EnumSet.allOf(Goal.Flag.class);

    protected final PathfinderMob holder;
    protected final float scanRange;
    protected final IntPredicate fluffyBlocksRequired;
    protected final float noWalkingRange;
    protected final float noWalkingRangeSqr;
    protected final IntProvider sleepDurationProvider;

    protected int sleepCooldown;
    protected static final int MAX_SLEEP_COOLDOWN = 20 * 60;

    protected int lastScan;
    protected boolean enoughFluffyBlocks;
    public int sleepDuration;

    public AlphaSleepGoal(PathfinderMob holder, float scanRange, IntPredicate fluffyBlocksRequired, float noWalkingRange, IntProvider sleepDurationProvider) {
        this.holder = holder;
        this.scanRange = scanRange;
        this.fluffyBlocksRequired = fluffyBlocksRequired;
        this.noWalkingRange = noWalkingRange;
        noWalkingRangeSqr = noWalkingRange * noWalkingRange;
        this.sleepDurationProvider = sleepDurationProvider;

        setFlags(FLAGS);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    protected void scanForFluffyBlocks() {
        if (holder.tickCount - lastScan < 20) return;

        lastScan = holder.tickCount;
        Level level = holder.level;

        int count = 0;
        for (BlockPos pos : BlockPos.betweenClosed(
                holder.blockPosition().offset((int) -scanRange, -1, (int) -scanRange),
                holder.blockPosition().offset((int) scanRange, 1, (int) scanRange))) {

            BlockState state = level.getBlockState(pos);
            if (state.is(BlockTags.BEDS) || state.is(BlockTags.WOOL) || state.getBlock() instanceof Pillow) {
                count++;
                if (fluffyBlocksRequired.test(count)) break;
            }
        }

        enoughFluffyBlocks = fluffyBlocksRequired.test(count);
    }

    @Override
    public boolean canUse() {
        if (sleepCooldown > 0) {
            sleepCooldown--;
            return false;
        }
        if (holder.getTarget() != null) return false;

        scanForFluffyBlocks();
        return enoughFluffyBlocks;
    }

    @Override
    public void start() {
        holder.startSleeping(holder.blockPosition());
        sleepDuration = sleepDurationProvider.sample(holder.getRandom());
        holder.noCulling = true;
        holder.goalSelector.getRunningGoals().filter(wrappedGoal -> wrappedGoal.getGoal() != this).forEach(WrappedGoal::stop);
        holder.targetSelector.getRunningGoals().filter(wrappedGoal -> wrappedGoal.getGoal() != this).forEach(WrappedGoal::stop);
        holder.setTarget(null);
    }

    @Override
    public void tick() {
        holder.goalSelector.getRunningGoals().filter(wrappedGoal -> wrappedGoal.getGoal() != this).forEach(WrappedGoal::stop);
        holder.targetSelector.getRunningGoals().filter(wrappedGoal -> wrappedGoal.getGoal() != this).forEach(WrappedGoal::stop);
        holder.setTarget(null);
        if (sleepDuration > 0) {
            sleepDuration--;
        }
    }

    public static boolean hasValidAlphaSleepGoal(PathfinderMob mob) {
        return mob.goalSelector.getAvailableGoals().stream()
                .map(WrappedGoal::getGoal)
                .map(goal -> goal instanceof AlphaSleepGoal sleepGoal ? sleepGoal : null)
                .anyMatch(sleepGoal -> sleepGoal != null && sleepGoal.enoughFluffyBlocks && sleepGoal.sleepCooldown <= 0);
    }

    @Override
    public boolean canContinueToUse() {
        if (sleepDuration <= 0) {
            return false;
        }

        boolean takingDamage = holder.getCombatTracker().takingDamage;
        if (takingDamage) {
            return false;
        }

        Level level = holder.level;
        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                holder.getBoundingBox().inflate(noWalkingRange),
                EntitySelector.NO_SPECTATORS.and(e -> e != holder)
        );

        for (LivingEntity entity : entities) {
            if (entity.isSleeping() || entity.isCrouching()) continue;

            if (entity.isSprinting()) return false;

            ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new RequestMovementCheckPacket(true));

            Vec3 movement = entity.getDeltaMovement();
            if (entity instanceof SyncTrackMotion syncTrackMotion) {
                boolean flag = syncTrackMotion.getLastKnownMotion() != null;
                if (flag) movement = syncTrackMotion.getLastKnownMotion();
            }

            if (movement.lengthSqr() < 0.05D) continue;

            if (entity.distanceToSqr(holder) < noWalkingRangeSqr * 0.25D) {
                return false; // alguém chegou muito perto
            }

            if (movement.lengthSqr() >= 0.05D) {
                return false; // alguém correndo
            }

        }

        return true;
    }

    @Override
    public void stop() {
        holder.stopSleeping();
        sleepCooldown = MAX_SLEEP_COOLDOWN;
    }

    public static List<AlphaSleepGoal> getAllSleepGoalsFromEntity(PathfinderMob living) {
        Stream<AlphaSleepGoal> goalStream = living.goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal).filter(goal -> goal instanceof AlphaSleepGoal).map(goal -> goal instanceof AlphaSleepGoal alphaSleepGoal ? alphaSleepGoal : null);
        return goalStream.toList();
    }
}

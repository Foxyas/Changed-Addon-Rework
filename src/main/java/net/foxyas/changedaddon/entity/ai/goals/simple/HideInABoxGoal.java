package net.foxyas.changedaddon.entity.ai.goals.simple;

import net.ltxprogrammer.changed.block.entity.CardboardBoxTallBlockEntity;
import net.ltxprogrammer.changed.entity.SeatEntity;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;
import java.util.Optional;

public class HideInABoxGoal extends Goal {

    private static final int SEARCH_RANGE = 10;

    protected final PathfinderMob holder;
    protected final IntProvider searchCooldownProvider;
    protected final IntProvider maxInBoxTicksProvider;

    protected int hideInBoxCooldown = 0;
    protected BlockPos boxPos;
    protected int noPathTimeout;
    protected int inBoxTicks;
    protected int targetInBoxTicks = 60 * 20;

    public HideInABoxGoal(PathfinderMob holder, IntProvider searchCooldownProvider, IntProvider maxInBoxTicksProvider) {
        this.holder = holder;
        this.searchCooldownProvider = searchCooldownProvider;
        this.maxInBoxTicksProvider = maxInBoxTicksProvider;

        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (hideInBoxCooldown > 0) {
            hideInBoxCooldown--;
            return false;
        }

        if (holder.getTarget() != null) {
            return false;
        }

        if (boxPos == null) {
            tryFindBox();
        }

        return boxPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (boxPos == null || holder.getTarget() != null) {
            return false;
        }

        return inBoxTicks < targetInBoxTicks;
    }

    @Override
    public void start() {
        if (boxPos == null) {
            tryFindBox();
        }
        if (boxPos != null) {
            holder.getNavigation().moveTo(boxPos.getX() + 0.5, boxPos.getY(), boxPos.getZ() + 0.5, 0.25f);
            this.inBoxTicks = 0;
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    protected boolean isHidedInBox() {
        return holder.getVehicle() instanceof SeatEntity && boxPos != null;
    }

    @Override
    public void tick() {
        Level level = holder.level();

        if (isHidedInBox()) {
            // Prevent other code context from assigning a target while hiding inside the box
            if (holder.getTarget() != null) {
                holder.setTarget(null);
            }

            inBoxTicks++;
            if (!(holder.getVehicle() instanceof SeatEntity seatEntity) || seatEntity.isRemoved()) {
                invalidateCurrentBox();
            }
            return;
        }

        PathNavigation navigation = holder.getNavigation();
        if (boxPos == null || isBlockInvalid(level, boxPos, level.getBlockState(boxPos))) {
            tryFindBox();
            if (boxPos == null) return;
            navigation.moveTo(boxPos.getX() + 0.5, boxPos.getY(), boxPos.getZ() + 0.5, 0.25f);
        }

        holder.getLookControl().setLookAt(
                boxPos.getX(), boxPos.getY(), boxPos.getZ(),
                30.0F,
                30.0F
        );

        if (holder.blockPosition().closerThan(boxPos, 2.5)) {
            if (level.getBlockEntity(boxPos) instanceof CardboardBoxTallBlockEntity box) {
                if (box.hideEntity(holder)) {
                    this.targetInBoxTicks = maxInBoxTicksProvider.sample(holder.getRandom());
                    this.inBoxTicks = 1;
                    return;
                }
            }
        }

        if (navigation.isStuck() || (navigation.getPath() != null && !navigation.getPath().canReach())) {
            noPathTimeout--;
            if (noPathTimeout <= 0) {
                applySearchCooldown();
                boxPos = null;
            } else if (noPathTimeout % 25 == 0) {
                navigation.recomputePath();
            }
            return;
        }

        noPathTimeout = 100;
    }

    private void invalidateCurrentBox() {
        boxPos = null;
    }

    protected void tryFindBox() {
        Optional<BlockPos> nearestValidBox = getNearestValidBox();
        if (nearestValidBox.isPresent()) {
            boxPos = nearestValidBox.get();
        } else {
            boxPos = null;
            applySearchCooldown();
        }
    }

    protected void applySearchCooldown() {
        this.hideInBoxCooldown = searchCooldownProvider.sample(holder.getRandom());
    }

    protected Optional<BlockPos> getNearestValidBox() {
        BlockPos center = holder.blockPosition();
        BlockPos closestBox = null;
        float closestDist = SEARCH_RANGE * SEARCH_RANGE + .01f;
        Level level = holder.level();

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-SEARCH_RANGE, -SEARCH_RANGE, -SEARCH_RANGE), center.offset(SEARCH_RANGE, SEARCH_RANGE, SEARCH_RANGE))) {
            float dist = (float) pos.distSqr(center);
            if (dist >= closestDist || isBlockInvalid(level, pos, level.getBlockState(pos))) continue;
            Path path = this.holder.getNavigation().createPath(pos, 0);
            if (path == null) continue;
            closestDist = dist;
            closestBox = pos.immutable();
        }

        return Optional.ofNullable(closestBox);
    }

    protected boolean isBlockInvalid(Level level, BlockPos pos, BlockState state) {
        return !state.is(ChangedBlocks.CARDBOARD_BOX_TALL.get())
                || state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) != DoubleBlockHalf.UPPER
                || !(level.getBlockEntity(pos) instanceof CardboardBoxTallBlockEntity box)
                || box.getSeatedEntity() != null;
    }

    @Override
    public void stop() {
        holder.getNavigation().stop();
        boxPos = null;
        noPathTimeout = 100;
        inBoxTicks = 0;

        if (holder.getVehicle() instanceof SeatEntity) {
            holder.stopRiding();
        }
    }
}
package net.foxyas.changedaddon.entity.ai.goals.simple;

import net.foxyas.changedaddon.util.DelayedTask;
import net.ltxprogrammer.changed.block.entity.CardboardBoxTallBlockEntity;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.EnumSet;

public class HideInABoxGoal extends Goal {

    private static final int searchRange = 10;

    protected final PathfinderMob holder;

    protected boolean lock;
    protected BlockPos boxPos;
    protected int noPathTimeout;
    protected boolean inBox;

    public HideInABoxGoal(PathfinderMob holder) {
        this.holder = holder;

        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (lock) return false;

        return holder.getTarget() == null && !inBox;
    }

    @Override
    public boolean canContinueToUse() {
        if (boxPos == null) {
            lock = true;
            new DelayedTask(200, () -> lock = false);
            return false;
        }

        return holder.getTarget() == null && !inBox;
    }

    @Override
    public void start() {
        tryFindBox();
        if (boxPos == null) return;

        holder.getNavigation().moveTo(boxPos.getX() + 0.5, boxPos.getY(), boxPos.getZ() + 0.5, 0.25f);
    }

    @Override
    public void tick() {
        Level level = holder.level();
        PathNavigation navigation = holder.getNavigation();
        if (boxPos == null || isBlockInvalid(level, boxPos, level.getBlockState(boxPos))) {
            tryFindBox();
            if (boxPos == null) return;//cancel goal - no boxes
            navigation.moveTo(boxPos.getX() + 0.5, boxPos.getY(), boxPos.getZ() + 0.5, 0.25f);
        }

        holder.getLookControl().setLookAt(
                boxPos.getX(), boxPos.getY(), boxPos.getZ(),
                30.0F, // yaw change speed (degrees per tick)
                30.0F  // pitch change speed
        );

        if (holder.blockPosition().closerThan(boxPos, 2.5)) {
            //get in box
            if (level.getBlockEntity(boxPos) instanceof CardboardBoxTallBlockEntity box) {
                box.hideEntity(holder);
            }

            inBox = true;//assume instanceof always true
            return;
        }

        if (navigation.isStuck() || (navigation.getPath() != null && !navigation.getPath().canReach())) {
            noPathTimeout--;
            if (noPathTimeout <= 0) {//No path, try again later
                boxPos = null;
            } else if (noPathTimeout % 25 == 0) navigation.recomputePath();
            return;
        }

        noPathTimeout = 100;
    }

    protected void tryFindBox() {
        BlockPos center = holder.blockPosition(), closestCrop = null;
        float closestDist = searchRange * searchRange + .01f, dist;
        Level level = holder.level();
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-searchRange, -searchRange, -searchRange), center.offset(searchRange, searchRange, searchRange))) {
            dist = (float) pos.distSqr(center);
            if (dist >= closestDist || isBlockInvalid(level, pos, level.getBlockState(pos))) continue;

            closestDist = dist;
            closestCrop = pos.immutable();
        }

        boxPos = closestCrop;
    }

    protected boolean isBlockInvalid(Level level, BlockPos pos, BlockState state) {
        return !state.is(ChangedBlocks.CARDBOARD_BOX_TALL.get())
                || state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) != DoubleBlockHalf.UPPER
                || !(level.getBlockEntity(pos) instanceof CardboardBoxTallBlockEntity box) || box.getSeatedEntity() != null;
    }

    @Override
    public void stop() {
        holder.getNavigation().stop();
        boxPos = null;
        noPathTimeout = 100;
        inBox = false;
    }
}

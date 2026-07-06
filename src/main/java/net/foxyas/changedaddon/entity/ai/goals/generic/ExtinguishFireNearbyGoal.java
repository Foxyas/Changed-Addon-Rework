package net.foxyas.changedaddon.entity.ai.goals.generic;

import net.foxyas.changedaddon.util.FoxyasUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import static net.minecraft.tags.BlockTags.FIRE;

public class ExtinguishFireNearbyGoal extends Goal {

    private final PathfinderMob mob;

    public ExtinguishFireNearbyGoal(PathfinderMob pathfinderMob) {
        this.mob = pathfinderMob;
    }

    @Override
    public boolean canUse() {
        Level level = mob.level();
        BlockPos mobPos = mob.blockPosition();
        return BlockPos.betweenClosedStream(
                mobPos.offset(-16, -16, -16),
                mobPos.offset(16, 16, 16)
        ).count() >= 8;
    }

    @Override
    public void start() {
        super.start();
        BlockPos mobPos = mob.blockPosition();
        Level level = mob.level();
        for (BlockPos blockPos : FoxyasUtil.betweenClosedStreamSphere(mobPos.offset(-16, -16, -16), mobPos.offset(16, 16, 16)).map(BlockPos::immutable).filter(pos -> level.getBlockState(pos).is(FIRE)).toList()) {
            level.removeBlock(blockPos, false);
            level.levelEvent(1009, blockPos, 0);
        }
    }
}

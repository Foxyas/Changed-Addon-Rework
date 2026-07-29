package net.foxyas.changedaddon.entity.ai.goals;

import net.foxyas.changedaddon.entity.api.IFlyableChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.block.state.BlockState;

public class ToggleFlightGoal<T extends PathfinderMob & IFlyableChangedEntity> extends Goal {
    private final T entity;

    public ToggleFlightGoal(T entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (entity.getTarget() != null) {
            return false; //There's no need to stop or start flying randomly if it has a target
        }

        BlockPos currentPos = this.entity.blockPosition();

        // Procura o chão manualmente descendo bloco por bloco a uma distância máxima (ex: 32 blocos)
        int maxScanDistance = 32;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int i = 1; i <= maxScanDistance; i++) {
            mutablePos.set(currentPos.getX(), currentPos.getY() - i, currentPos.getZ());
            if (mutablePos.getY() < this.entity.level().getMinBuildHeight()) {
                break;
            }

            BlockState state = this.entity.level().getBlockState(mutablePos);
            if (!state.isAir() && GoalUtils.isSolid(entity, mutablePos)) {
                break; // stop the loop with the right block pos
            }
        }

        if (mutablePos.distSqr(currentPos) > 9.0f) {
            return false;
        }

        return this.entity.getRandom().nextInt(200) == 0;
    }

    @Override
    public void start() {
        boolean currentlyFlying = this.entity.isFlyingMode();
        this.entity.setFlyingMode(!currentlyFlying);
    }
}
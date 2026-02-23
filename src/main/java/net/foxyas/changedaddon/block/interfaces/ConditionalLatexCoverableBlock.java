package net.foxyas.changedaddon.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface ConditionalLatexCoverableBlock {

    default boolean canBeSpread(LevelAccessor level, BlockState state, BlockPos pos) {
        return true;
    }

    interface NonLatexCoverableBlock extends ConditionalLatexCoverableBlock {
        @Override
        default boolean canBeSpread(LevelAccessor level, BlockState state, BlockPos pos) {
            return false;
        }
    }
}

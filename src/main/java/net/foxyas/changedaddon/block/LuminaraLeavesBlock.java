package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class LuminaraLeavesBlock extends LeavesBlock {

    public LuminaraLeavesBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CHERRY_LEAVES));
    }

    public List<Color3> getPossibleColorsForFallingLeaves(@NotNull BlockState pState,
                                                          @NotNull Level pLevel,
                                                          @NotNull BlockPos pPos,
                                                          @NotNull RandomSource pRandom) {
        return Stream.of(
                        "#8901f9",
                        "#f5daef",
                        "#1e1635",
                        "#fa95e9"
                )
                .map(Color3::getColor)
                .toList();
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    @Override
    public void animateTick(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        if (pRandom.nextInt(10) == 0) {
            BlockPos below = pPos.below();
            BlockState blockstate = pLevel.getBlockState(below);
            if (!isFaceFull(blockstate.getCollisionShape(pLevel, below), Direction.UP)) {
                List<Color3> colors = getPossibleColorsForFallingLeaves(pState, pLevel, pPos, pRandom);
                ParticleUtils.spawnParticleBelow(pLevel, pPos, pRandom, ChangedAddonParticleTypes.fallingLeaf(Util.getRandom(colors, pRandom)));
            }
        }
    }
}
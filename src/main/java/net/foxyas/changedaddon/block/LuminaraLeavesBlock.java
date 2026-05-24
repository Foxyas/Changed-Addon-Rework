package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class LuminaraLeavesBlock extends LeavesBlock {
    public LuminaraLeavesBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CHERRY_LEAVES));
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
                MapColor mapColor = this.getMapColor(pState, pLevel, pPos, this.defaultMapColor());
                ParticleUtils.spawnParticleBelow(pLevel, pPos, pRandom, ChangedAddonParticleTypes.fallingLeaf(Color3.fromInt(mapColor.col)));
            }
        }
    }
}
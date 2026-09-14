package net.foxyas.changedaddon.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static net.foxyas.changedaddon.block.interfaces.ConditionalLatexCoverableBlock.NonLatexCoverableBlock;

public class WolfCrystalPillarBlock extends RotatedPillarBlock implements NonLatexCoverableBlock {

    public WolfCrystalPillarBlock() {
        super(Properties.copy(Blocks.BLUE_ICE)
                .friction(0.98F)
                .sound(SoundType.AMETHYST)
                .strength(2.0F, 2.0F).noOcclusion().requiresCorrectToolForDrops());
    }

    @Override
    public boolean skipRendering(@NotNull BlockState pState, BlockState pAdjacentBlockState, @NotNull Direction pSide) {
        return pAdjacentBlockState.is(this) || super.skipRendering(pState, pAdjacentBlockState, pSide);
    }

    @Override
    public float getShadeBrightness(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pReader, @NotNull BlockPos pPos) {
        return true;
    }

    @Override
    public int getLightBlock(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return 1;
    }
}

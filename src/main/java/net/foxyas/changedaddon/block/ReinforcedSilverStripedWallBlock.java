package net.foxyas.changedaddon.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static net.foxyas.changedaddon.block.interfaces.ConditionalLatexCoverableBlock.NonLatexCoverableBlock;

public class ReinforcedSilverStripedWallBlock extends Block implements NonLatexCoverableBlock {

    public ReinforcedSilverStripedWallBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).strength(6f, 30f));
    }

    @Override
    public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        return 15;
    }
}

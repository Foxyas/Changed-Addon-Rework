package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.ltxprogrammer.changed.block.DarkLatexBlock;
import net.ltxprogrammer.changed.block.WolfCrystalBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.ltxprogrammer.changed.block.AbstractLatexBlock.getLatexed;

public class WhiteWolfCrystalSmallBlock extends AbstractWolfCrystalExtender.AbstractWolfCrystalSmall {
    public WhiteWolfCrystalSmallBlock() {
        super(ChangedAddonModItems.WHITE_WOLF_CRYSTAL_FRAGMENT);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderLayer() {
        ItemBlockRenderTypes.setRenderLayer(ChangedAddonModBlocks.WHITE_WOLF_CRYSTAL_SMALL.get(), renderType -> renderType == RenderType.cutout());
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter p_51043_, BlockPos p_51044_) {
        return blockState.getBlock() == ChangedAddonModBlocks.WHITE_WOLF_CRYSTAL_BLOCK.get()
                || blockState.getBlock() instanceof WolfCrystalBlock
                || blockState.getBlock() instanceof DarkLatexBlock
                || getLatexed(blockState) == LatexType.DARK_LATEX;
    }

    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        BlockState blockStateOn = level.getBlockState(blockPos.below());
        if (!canSupportRigidBlock(level, blockPos.below()))
            return false;
        return blockStateOn.getBlock() == ChangedAddonModBlocks.WHITE_WOLF_CRYSTAL_BLOCK.get()
                || blockStateOn.getBlock() instanceof WolfCrystalBlock
                || blockStateOn.getBlock() instanceof DarkLatexBlock
                || getLatexed(blockStateOn) == LatexType.DARK_LATEX;
    }
}

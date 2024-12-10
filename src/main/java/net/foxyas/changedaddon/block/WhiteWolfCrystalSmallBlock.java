
package net.foxyas.changedaddon.block;

import net.ltxprogrammer.changed.block.DarkLatexBlock;
import net.ltxprogrammer.changed.block.WolfCrystalBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

import net.foxyas.changedaddon.init.ChangedAddonModBlocks;

import java.util.List;
import java.util.Collections;
import java.util.Random;

import static net.ltxprogrammer.changed.block.AbstractLatexBlock.getLatexed;
import net.minecraft.world.level.LevelReader;

public class WhiteWolfCrystalSmallBlock extends AbstractWolfCrystalExtender.AbstractWolfCrystalSmall {
	public WhiteWolfCrystalSmallBlock() {
		super();
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

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderLayer() {
		ItemBlockRenderTypes.setRenderLayer(ChangedAddonModBlocks.WHITE_WOLF_CRYSTAL_SMALL.get(), renderType -> renderType == RenderType.cutout());
	}
}

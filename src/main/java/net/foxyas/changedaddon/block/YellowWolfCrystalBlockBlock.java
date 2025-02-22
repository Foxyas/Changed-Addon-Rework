
package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.ltxprogrammer.changed.block.WolfCrystal;
import net.ltxprogrammer.changed.init.ChangedMaterials;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class YellowWolfCrystalBlockBlock extends AbstractWolfCrystalExtender.AbstractWolfCrystalBlock {
	public YellowWolfCrystalBlockBlock() {
		super();
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {

		BlockState plant = plantable.getPlant(world, pos.relative(facing));
		if (plant.getBlock() instanceof YellowWolfCrystalSmallBlock)
			return true;
		else
			return super.canSustainPlant(state, world, pos, facing, plantable);
	}

	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		super.tick(state, level, pos, random);
		BlockPos above = pos.above();
		if (level.getBlockState(above).is(Blocks.AIR)) {
			level.setBlock(above, ChangedAddonModBlocks.YELLOW_WOLF_CRYSTAL_SMALL.get().defaultBlockState(), 3);
			level.playSound(null, pos, ChangedSounds.ICE2, SoundSource.BLOCKS, 1.0f, 1.0f);
		}
	}

}

package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.init.ChangedAddonModBlocks;

public class Show0porcentbarProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(new BlockPos(x, y, z))).getBlock() == ChangedAddonModBlocks.CATLYZER.get()) {
			if (new Object() {
				public double getValue(LevelAccessor world, BlockPos pos, String tag) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity != null)
						return blockEntity.getTileData().getDouble(tag);
					return -1;
				}
			}.getValue(world, new BlockPos(x, y, z), "recipe_progress") >= 10) {
				return false;
			}
		} else if ((world.getBlockState(new BlockPos(x, y, z))).getBlock() == ChangedAddonModBlocks.UNIFUSER.get()) {
			if (new Object() {
				public double getValue(LevelAccessor world, BlockPos pos, String tag) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity != null)
						return blockEntity.getTileData().getDouble(tag);
					return -1;
				}
			}.getValue(world, new BlockPos(x, y, z), "recipe_progress") >= 20) {
				return false;
			}
		}
		return true;
	}
}

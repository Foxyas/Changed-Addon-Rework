package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.init.ChangedAddonModGameRules;

public class PainiteOreAdditionalGenerationConditionProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if (world.getLevelData().getGameRules().getBoolean(ChangedAddonModGameRules.PAINITE_GENERATION) == true) {
			if (!(world.isEmptyBlock(new BlockPos(x + 1, y, z)) && world.isEmptyBlock(new BlockPos(x - 1, y, z)) && world.isEmptyBlock(new BlockPos(x, y + 1, z)) && world.isEmptyBlock(new BlockPos(x, y - 1, z))
					&& world.isEmptyBlock(new BlockPos(x, y, z + 1)) && world.isEmptyBlock(new BlockPos(x, y, z - 1)))) {
				return true;
			}
		}
		return false;
	}
}

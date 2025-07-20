package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public class PainiteOreAdditionalGenerationConditionProcedure {
    public static boolean execute(LevelAccessor world, double x, double y, double z) {
        if (world.getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.PAINITE_GENERATION)) {
            return !(world.isEmptyBlock(new BlockPos(x + 1, y, z)) && world.isEmptyBlock(new BlockPos(x - 1, y, z)) && world.isEmptyBlock(new BlockPos(x, y + 1, z)) && world.isEmptyBlock(new BlockPos(x, y - 1, z))
                    && world.isEmptyBlock(new BlockPos(x, y, z + 1)) && world.isEmptyBlock(new BlockPos(x, y, z - 1)));
        }
        return false;
    }
}

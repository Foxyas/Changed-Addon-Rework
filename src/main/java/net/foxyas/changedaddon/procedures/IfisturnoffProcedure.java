package net.foxyas.changedaddon.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

public class IfisturnoffProcedure {
    public static boolean execute(LevelAccessor world, double x, double y, double z) {
        return !(new Object() {
            public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity != null)
                    return blockEntity.getTileData().getBoolean(tag);
                return false;
            }
        }.getValue(world, new BlockPos(x, y, z), "turn_on"));
    }
}

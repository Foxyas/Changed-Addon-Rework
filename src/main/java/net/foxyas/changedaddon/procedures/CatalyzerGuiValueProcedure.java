package net.foxyas.changedaddon.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CatalyzerGuiValueProcedure {
    public static String execute(LevelAccessor world, double x, double y, double z) {
        return "Nitrogen % = " + (new Object() {
            public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity != null)
                    return blockEntity.getTileData().getDouble(tag);
                return -1;
            }
        }.getValue(world, new BlockPos(x, y, z), "nitrogen_power")) + "%";
    }
}

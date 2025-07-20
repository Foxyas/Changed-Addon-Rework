package net.foxyas.changedaddon.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class LitixCamoniaFluidUpdateTickProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        {
            String _value = "neutral";
            BlockPos _pos = new BlockPos(x, y, z);
            BlockState _bs = world.getBlockState(_pos);
            if (_bs.getBlock().getStateDefinition().getProperty("covered_with") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
                world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
        }
        {
            String _value = "neutral";
            BlockPos _pos = new BlockPos(x + 1, y, z);
            BlockState _bs = world.getBlockState(_pos);
            if (_bs.getBlock().getStateDefinition().getProperty("covered_with") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
                world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
        }
        {
            String _value = "neutral";
            BlockPos _pos = new BlockPos(x - 1, y, z);
            BlockState _bs = world.getBlockState(_pos);
            if (_bs.getBlock().getStateDefinition().getProperty("covered_with") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
                world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
        }
        {
            String _value = "neutral";
            BlockPos _pos = new BlockPos(x, y, z + 1);
            BlockState _bs = world.getBlockState(_pos);
            if (_bs.getBlock().getStateDefinition().getProperty("covered_with") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
                world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
        }
        {
            String _value = "neutral";
            BlockPos _pos = new BlockPos(x, y, z - 1);
            BlockState _bs = world.getBlockState(_pos);
            if (_bs.getBlock().getStateDefinition().getProperty("covered_with") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
                world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
        }
        {
            String _value = "neutral";
            BlockPos _pos = new BlockPos(x, y + 1, z);
            BlockState _bs = world.getBlockState(_pos);
            if (_bs.getBlock().getStateDefinition().getProperty("covered_with") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
                world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
        }
        {
            String _value = "neutral";
            BlockPos _pos = new BlockPos(x, y - 1, z);
            BlockState _bs = world.getBlockState(_pos);
            if (_bs.getBlock().getStateDefinition().getProperty("covered_with") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
                world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
        }
    }
}

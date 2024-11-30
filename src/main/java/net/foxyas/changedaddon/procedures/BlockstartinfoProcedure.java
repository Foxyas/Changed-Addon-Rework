package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.BlockPos;

public class BlockstartinfoProcedure {
	public static String execute(LevelAccessor world, double x, double y, double z) {
		String block = "";
		block = new TranslatableComponent(("block." + (ForgeRegistries.BLOCKS.getKey((world.getBlockState(new BlockPos(x, y, z))).getBlock()).toString()).replace(":", "."))).getString();
		if ((new Object() {
			public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity != null)
					return blockEntity.getTileData().getBoolean(tag);
				return false;
			}
		}.getValue(world, new BlockPos(x, y, z), "start_recipe")) == true) {
			return block + " is activated";
		}
		return block + " is deactivated";
	}
}

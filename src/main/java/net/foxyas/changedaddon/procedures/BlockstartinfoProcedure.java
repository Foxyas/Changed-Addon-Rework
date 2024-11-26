package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.init.ChangedAddonModBlocks;

public class BlockstartinfoProcedure {
	public static String execute(LevelAccessor world, double x, double y, double z) {
		String block = "";
		if ((world.getBlockState(new BlockPos(x, y, z))).getBlock() == ChangedAddonModBlocks.CATLYZER.get()) {
			block = new TranslatableComponent("block.changed_addon.catlyzer").getString();
		} else if ((world.getBlockState(new BlockPos(x, y, z))).getBlock() == ChangedAddonModBlocks.UNIFUSER.get()) {
			block = new TranslatableComponent("block.changed_addon.unifuser").getString();
		}
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

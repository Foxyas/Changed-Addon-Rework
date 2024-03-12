package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;

public class SignalBlockFeatureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean found = false;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ChangedAddonModItems.SIGNAL_CATCHER.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == ChangedAddonModItems.SIGNAL_CATCHER.get()) {
			sx = -32;
			found = false;
			for (int index0 = 0; index0 < 64; index0++) {
				sy = -32;
				for (int index1 = 0; index1 < 64; index1++) {
					sz = -32;
					for (int index2 = 0; index2 < 64; index2++) {
						if ((world.getBlockState(new BlockPos(x + sx, y + sy, z + sz))).getBlock() == ChangedAddonModBlocks.SIGNAL_BLOCK.get()) {
							found = true;
							if (found == true) {
								if (entity instanceof Player _player && !_player.level.isClientSide())
									_player.displayClientMessage(new TextComponent(("Location Found in " + Math.floor(x + sx) + " " + Math.floor(y + sy) + " " + Math.floor(z + sz))), false);
							}
							break;
						}
						sz = sz + 1;
					}
					sy = sy + 1;
				}
				sx = sx + 1;
			}
		}
	}
}

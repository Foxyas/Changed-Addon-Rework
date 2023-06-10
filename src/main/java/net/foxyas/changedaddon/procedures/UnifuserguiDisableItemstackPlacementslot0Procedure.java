package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;

import net.foxyas.changedaddon.init.ChangedAddonModItems;

public class UnifuserguiDisableItemstackPlacementslot0Procedure {
	public static boolean execute(ItemStack itemstack) {
		if (itemstack.getItem() == ChangedAddonModItems.AMMONIA.get() || itemstack.getItem() == ChangedAddonModItems.LITIX_CAMONIA.get() || itemstack.getItem() == Blocks.TINTED_GLASS.asItem()
				|| itemstack.getItem() == ChangedAddonModItems.UNLATEXBASE.get()) {
			return false;
		}
		return true;
	}
}

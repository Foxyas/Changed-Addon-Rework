package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;

import net.foxyas.changedaddon.init.ChangedAddonModItems;

public class UnifuserguiDisableItemstackPlacementslot0Procedure {
	public static boolean execute(ItemStack itemstack) {
		if (itemstack.getItem() == ChangedAddonModItems.AMMONIA.get() || itemstack.getItem() == ChangedAddonModItems.LITIX_CAMONIA.get() || itemstack.getItem() == ChangedAddonModItems.LAETHIN.get()
				|| itemstack.getItem() == Blocks.TINTED_GLASS.asItem() || itemstack.getItem() == ChangedAddonModItems.ANTI_LATEX_BASE.get()) {
			return false;
		}
		return false;
	}
}

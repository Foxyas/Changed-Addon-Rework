package net.foxyas.changedaddon.procedures;

import net.minecraft.world.item.ItemStack;

public class IsSignalCatcherCordsSetProcedure {
	public static double execute(ItemStack itemstack) {
		if (itemstack.hasTag()
				&& (itemstack.getOrCreateTag().contains("x")
				&& itemstack.getOrCreateTag().contains("y")
				&& itemstack.getOrCreateTag().contains("z"))){
			return 1;
		}


		return 0;
	}
}

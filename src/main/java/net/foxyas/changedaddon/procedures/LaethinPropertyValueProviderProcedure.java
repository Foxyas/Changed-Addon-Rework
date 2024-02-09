package net.foxyas.changedaddon.procedures;

import net.minecraft.world.item.ItemStack;

public class LaethinPropertyValueProviderProcedure {
	public static double execute(ItemStack itemstack) {
		if ((itemstack.getOrCreateTag().getString("type")).equals("dark_latex")) {
			return 1;
		}
		return 0;
	}
}

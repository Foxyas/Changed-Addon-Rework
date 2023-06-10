package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class FoxyasguiDisableItemstackPlacementProcedure {
	public static boolean execute(ItemStack itemstack) {
		if (itemstack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:orange"))) {
			return false;
		}
		return true;
	}
}

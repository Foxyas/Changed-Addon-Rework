package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.init.ChangedAddonModItems;

public class UnifuserguiDisableItemstackPlacementProcedure {
	public static boolean execute(ItemStack itemstack) {
		if (itemstack.getItem() == ChangedAddonModItems.CATALYZEDDNA.get() || itemstack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:blood_syringe"))
				|| itemstack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_syringe"))) {
			return false;
		}
		return true;
	}
}

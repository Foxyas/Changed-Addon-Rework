
package net.foxyas.changedaddon.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.procedures.PainiteItemInInventoryTickProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;

public class PainiteItem extends Item {
	public PainiteItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).stacksTo(64).rarity(Rarity.COMMON));
	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);
		PainiteItemInInventoryTickProcedure.execute(entity);
	}
}

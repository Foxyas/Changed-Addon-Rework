
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;

import net.foxyas.changedaddon.procedures.WhenHitWithAColoredWolfCrystalFragmentProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;

public class WhiteWolfCrystalFragmentItem extends Item {
	public WhiteWolfCrystalFragmentItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).stacksTo(64).rarity(Rarity.COMMON));
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		WhenHitWithAColoredWolfCrystalFragmentProcedure.execute(entity, sourceentity);
		return retval;
	}
}

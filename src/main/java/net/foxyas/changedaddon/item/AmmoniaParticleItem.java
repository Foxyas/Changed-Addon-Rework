
package net.foxyas.changedaddon.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;

import net.foxyas.changedaddon.procedures.AmmoniaparticleItemIsCraftedsmeltedProcedure;
import net.foxyas.changedaddon.init.ChangedAddonTabs;

public class AmmoniaParticleItem extends Item {
	public AmmoniaParticleItem() {
		super(new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON).stacksTo(64).rarity(Rarity.COMMON));
	}

	@Override
	public void onCraftedBy(ItemStack itemstack, Level world, Player entity) {
		super.onCraftedBy(itemstack, world, entity);
		AmmoniaparticleItemIsCraftedsmeltedProcedure.execute(entity);
	}
}

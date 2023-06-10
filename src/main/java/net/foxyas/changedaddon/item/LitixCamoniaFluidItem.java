
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.init.ChangedAddonModFluids;

public class LitixCamoniaFluidItem extends BucketItem {
	public LitixCamoniaFluidItem() {
		super(ChangedAddonModFluids.LITIX_CAMONIA_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.COMMON).tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
	}
}

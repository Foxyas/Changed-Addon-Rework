
package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ChangedBookItem extends Item {
	public ChangedBookItem() {
		super(new Item.Properties().stacksTo(1).tab(ChangedAddonTabs.TAB_CHANGED_ADDON).rarity(Rarity.RARE));
	}
}

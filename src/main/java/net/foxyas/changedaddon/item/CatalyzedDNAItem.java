
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

import net.foxyas.changedaddon.init.ChangedAddonTabs;

public class CatalyzedDNAItem extends Item {
	public CatalyzedDNAItem() {
		super(new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON).stacksTo(1).rarity(Rarity.RARE));
	}
}

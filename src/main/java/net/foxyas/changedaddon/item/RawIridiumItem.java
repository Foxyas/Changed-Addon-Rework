
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

import net.foxyas.changedaddon.init.ChangedAddonTabs;

public class RawIridiumItem extends Item {
	public RawIridiumItem() {
		super(new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON).stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON));
	}
}

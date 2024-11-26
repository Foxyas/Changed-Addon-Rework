
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;

public class IridiumItem extends Item {
	public IridiumItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON));
	}
}


package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ChangedBookItem extends Item {
	public ChangedBookItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
	}
}

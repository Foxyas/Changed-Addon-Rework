
package net.foxyas.changedaddon.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import net.minecraft.network.chat.Component;

import java.util.List;

public class SnepIconItem extends Item {
	public SnepIconItem() {
		super(new Item.Properties().tab(null).stacksTo(64).rarity(Rarity.EPIC));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(Component.literal("\u00A74THIS IS ONE DEV ITEM YOU CAN'T DO ANYTHING WITH THIS"));
	}
}

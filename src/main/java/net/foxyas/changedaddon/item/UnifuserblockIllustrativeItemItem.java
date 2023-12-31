
package net.foxyas.changedaddon.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;

import java.util.List;

public class UnifuserblockIllustrativeItemItem extends Item {
	public UnifuserblockIllustrativeItemItem() {
		super(new Item.Properties().tab(null).stacksTo(1).rarity(Rarity.RARE));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(new TextComponent("Unifuser Block Illustrative Item for a Jei Support"));
		list.add(new TextComponent("This mean that craft is in a Unifuser Craft Style"));
	}
}

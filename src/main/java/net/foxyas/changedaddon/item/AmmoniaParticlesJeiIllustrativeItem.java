
package net.foxyas.changedaddon.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import net.minecraft.network.chat.Component;

import java.util.List;

public class AmmoniaParticlesJeiIllustrativeItem extends Item {
	public AmmoniaParticlesJeiIllustrativeItem() {
		super(new Item.Properties().tab(null).stacksTo(16).rarity(Rarity.RARE));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(new TextComponent("Illustrative item of 16 Ammonia Particles for Jei support"));
	}
}

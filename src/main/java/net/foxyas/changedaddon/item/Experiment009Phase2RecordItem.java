
package net.foxyas.changedaddon.item;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;

import net.foxyas.changedaddon.init.ChangedAddonModSounds;

import java.util.List;

public class Experiment009Phase2RecordItem extends RecordItem {
	public Experiment009Phase2RecordItem() {
		super(0, ChangedAddonModSounds.REGISTRY.get(new ResourceLocation("changed_addon:experiment009_theme_phase2")), new Item.Properties().tab(null).stacksTo(1).rarity(Rarity.RARE));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(new TextComponent("You did it!!"));
	}
}

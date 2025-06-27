
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.init.ChangedAddonModSounds;

public class MeaninglessstrafemusicdiscItem extends RecordItem {
	public MeaninglessstrafemusicdiscItem() {
		super(15, ChangedAddonModSounds.REGISTRY.get(ResourceLocation.parse("changed_addon:music.boss.exp9")), new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).stacksTo(1).rarity(Rarity.RARE));
	}
}

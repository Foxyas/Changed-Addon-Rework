
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.foxyas.changedaddon.init.ChangedAddonSounds;

public class MeaninglessStrafeMusicDiscItem extends RecordItem {
	public MeaninglessStrafeMusicDiscItem() {
		super(15, ChangedAddonSounds.REGISTRY.get(new ResourceLocation("changed_addon:music.boss.exp9")), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON).stacksTo(1).rarity(Rarity.RARE));
	}
}

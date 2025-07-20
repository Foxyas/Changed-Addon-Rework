
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;

public class ChangedAddonModTabs {
	public static CreativeModeTab TAB_CHANGED_ADDON;
	public static CreativeModeTab TAB_CHANGED_ADDON_COMBAT_OPTIONAL;

	public static void load() {
		TAB_CHANGED_ADDON = new CreativeModeTab("tabchanged_addon") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(ChangedAddonItems.CHANGEDBOOK.get());
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
		TAB_CHANGED_ADDON_COMBAT_OPTIONAL = new CreativeModeTab("tabchanged_addon_combat_optional") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(ChangedAddonItems.ELECTRIC_KATANA.get());
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
}

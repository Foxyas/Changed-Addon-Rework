package net.foxyas.changedaddon.item.armor;

import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.ltxprogrammer.changed.init.ChangedTabs;
import net.ltxprogrammer.changed.item.ClothingItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class DyeableClothingItem extends ClothingItem implements DyeableLeatherItem {
    public DyeableClothingItem() {
        super();
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        if (this.allowdedIn(tab)) {
            for (DyeableShorts.DefaultColors color : DyeableShorts.DefaultColors.values()) {
                ItemStack stack = new ItemStack(this);
                this.setColor(stack, color.getColorToInt());
                items.add(stack);
            }
        }
    }

    @Override
    protected boolean allowdedIn(CreativeModeTab tab) {
        if (tab == ChangedTabs.TAB_CHANGED_ITEMS) {
            return false;
        } else if (tab == ChangedAddonTabs.TAB_CHANGED_ADDON) {
            return true;
        }
        return super.allowdedIn(tab);
    }
}
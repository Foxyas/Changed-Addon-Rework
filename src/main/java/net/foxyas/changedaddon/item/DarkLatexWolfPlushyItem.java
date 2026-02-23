package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class DarkLatexWolfPlushyItem extends BlockItem {
    public DarkLatexWolfPlushyItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    public DarkLatexWolfPlushyItem() {
        this(ChangedAddonBlocks.DARK_LATEX_WOLF_PLUSHY.get(), new Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
        );
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        if (armorType == EquipmentSlot.HEAD) {
            return true;
        }

        return super.canEquip(stack, armorType, entity);
    }
}

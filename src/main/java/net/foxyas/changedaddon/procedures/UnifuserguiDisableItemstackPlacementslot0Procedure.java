package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class UnifuserguiDisableItemstackPlacementslot0Procedure {
    public static boolean execute(ItemStack itemstack) {
        if (itemstack.getItem() == ChangedAddonItems.AMMONIA.get() || itemstack.getItem() == ChangedAddonItems.LITIX_CAMONIA.get() || itemstack.getItem() == ChangedAddonItems.LAETHIN.get()
                || itemstack.getItem() == Blocks.TINTED_GLASS.asItem() || itemstack.getItem() == ChangedAddonItems.ANTI_LATEX_BASE.get()) {
            return false;
        }
        return false;
    }
}

package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class UnifuserguiDisableItemstackPlacementProcedure {
    public static boolean execute(ItemStack itemstack) {
        return itemstack.getItem() != ChangedAddonItems.CATALYZEDDNA.get() && itemstack.getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:blood_syringe"))
                && itemstack.getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_syringe"));
    }
}

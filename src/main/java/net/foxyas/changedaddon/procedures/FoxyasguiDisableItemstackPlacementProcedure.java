package net.foxyas.changedaddon.procedures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class FoxyasguiDisableItemstackPlacementProcedure {
    public static boolean execute(ItemStack itemstack) {
        return itemstack.getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:orange"));
    }
}

package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.minecraft.world.item.ItemStack;

public class TransfurTotemMakeItemGlowProcedure {
    public static boolean execute(ItemStack itemstack) {
        if ((itemstack.getOrCreateTag().getString("form")).isEmpty()) {
            return false;
        } else if ((itemstack.getOrCreateTag().getString("form")).startsWith("changed:form")) {
            return true;
        } else return (itemstack.getOrCreateTag().getString("form")).startsWith("changed_addon:form") && ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get() == true;
    }
}

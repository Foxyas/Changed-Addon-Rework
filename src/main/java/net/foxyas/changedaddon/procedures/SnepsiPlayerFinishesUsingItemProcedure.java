package net.foxyas.changedaddon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

public class SnepsiPlayerFinishesUsingItemProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		Entity target = null;
		if (!(entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
			if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_latex_snow_leopard_partial")) {
				AddTransfurProgressProcedure.SnepsiTransfur(entity, true, 1);
			} else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_exp2/male")) {
				AddTransfurProgressProcedure.SnepsiTransfur(entity, true, 2);
			} else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_exp2/female")) {
				AddTransfurProgressProcedure.SnepsiTransfur(entity, true, 3);
			} else {
				AddTransfurProgressProcedure.SnepsiTransfur(entity, true, 1);
			}
		}
	}
}

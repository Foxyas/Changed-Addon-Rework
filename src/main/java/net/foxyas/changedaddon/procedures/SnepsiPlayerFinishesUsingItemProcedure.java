package net.foxyas.changedaddon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import java.util.Iterator;

public class SnepsiPlayerFinishesUsingItemProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		Entity target = null;
		double Snepsi_Drink_Amount = 0;
		Snepsi_Drink_Amount = (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Snepsi_Drink;
		{
			double _setval = 1 + Snepsi_Drink_Amount;
			entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.Snepsi_Drink = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
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
		if (Snepsi_Drink_Amount >= 100) {
			if (entity instanceof ServerPlayer _player) {
				Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:snepsi_adctive"));
				AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
				if (!_ap.isDone()) {
					Iterator _iterator = _ap.getRemainingCriteria().iterator();
					while (_iterator.hasNext())
						_player.getAdvancements().award(_adv, (String) _iterator.next());
				}
			}
		}
	}
}

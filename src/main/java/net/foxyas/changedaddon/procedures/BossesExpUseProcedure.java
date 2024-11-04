package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.network.chat.TranslatableComponent;

import net.ltxprogrammer.changed.item.LatexSyringe;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;

@Mod.EventBusSubscriber
public class BossesExpUseProcedure {

	@SubscribeEvent
	public static void VariantGet(LatexSyringe.UsedOnBlock event) {
		TransfurVariant<?> SyringeVariant;
		SyringeVariant = event.syringeVariant;
		var PlayerVariant = event.syringeVariant;
		if (PlayerVariant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009.get() 
		|| PlayerVariant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get() 
		|| PlayerVariant == ChangedAddonTransfurVariants.EXPERIMENT_10.get()
		|| PlayerVariant == ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get()) {
			event.setCanceled(true);
			event.player.displayClientMessage(new TranslatableComponent("changed_addon.latex_syringe.not_valid"), true);
		}
	}
}

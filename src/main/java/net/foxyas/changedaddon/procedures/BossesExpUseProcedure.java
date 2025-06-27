package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.network.chat.Component;

import net.ltxprogrammer.changed.item.LatexSyringe;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;

@Mod.EventBusSubscriber
public class BossesExpUseProcedure {

	@SubscribeEvent
	public static void VariantGet(LatexSyringe.UsedOnBlock event) {
		TransfurVariant<?> variant = event.syringeVariant;
		if (event.player.isCreative()) {
			return;
		}
		
		if (variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009.get()
		|| variant == ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get()
		|| variant == ChangedAddonTransfurVariants.EXPERIMENT_10.get()
		|| variant == ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get()) {
			event.setCanceled(true);
			event.player.displayClientMessage(Component.translatable("changed_addon.latex_syringe.not_valid"), true);
		}
	}
}

package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.network.chat.TranslatableComponent;

import net.ltxprogrammer.changed.item.LatexSyringe;

import net.foxyas.changedaddon.variants.AddonLatexVariant;

@Mod.EventBusSubscriber
public class BossesExpUseProcedure {
	public static TransfurVariant<?> SyringeVariant;

	@SubscribeEvent
	public static void VariantGet(LatexSyringe.UsedOnBlock event) {
		SyringeVariant = event.syringeVariant;
		var PlayerVariant = event.syringeVariant;
		if (PlayerVariant == AddonLatexVariant.KET_EXPERIMENT_009.get() || PlayerVariant == AddonLatexVariant.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get() || PlayerVariant == AddonLatexVariant.EXPERIMENT_10.get()) {
			event.setCanceled(true);
			event.player.displayClientMessage(new TranslatableComponent("changed_addon.latex_syringe.not_valid"), true);
		}
	}
}

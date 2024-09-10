package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.network.chat.TranslatableComponent;

import net.ltxprogrammer.changed.item.LatexSyringe;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;

import net.foxyas.changedaddon.variants.AddonLatexVariant;

@Mod.EventBusSubscriber
public class BossesExpUseProcedure {
	public static LatexVariant<?> SyringeVariant;

	@SubscribeEvent
	public static void VariantGet(LatexSyringe.UsedOnBlock event) {
		SyringeVariant = event.syringeVariant;
		var PlayerVariant = event.syringeVariant;
		if (PlayerVariant == AddonLatexVariant.KET_EXPERIMENT_009 || PlayerVariant == AddonLatexVariant.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT || PlayerVariant == AddonLatexVariant.EXPERIMENT_10_LATEX_VARIANT) {
			event.setCanceled(true);
			event.player.displayClientMessage(new TranslatableComponent("changed_addon.latex_syringe.not_valid"), true);
		}
	}
}

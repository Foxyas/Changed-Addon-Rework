package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CanLeapProcedure {
	public static boolean execute(Entity entity) {
		Player player = (Player) entity;
		LatexVariantInstance LatexInstace = ProcessTransfur.getPlayerLatexVariant(player);
		LatexVariant Variant = LatexVariant.getEntityVariant(LatexInstace.getLatexEntity());
		if (Variant.is(ChangedTags.LatexVariants.CAT_LIKE )|| Variant.is(ChangedTags.LatexVariants.LEOPARD_LIKE)){
			return true;
		}
		return false;
	}

	public static boolean flyentity(Entity entity) {
		Player player = (Player) entity;
		LatexVariantInstance LatexInstace = ProcessTransfur.getPlayerLatexVariant(player);
		LatexVariant Variant = LatexVariant.getEntityVariant(LatexInstace.getLatexEntity());
		if (Variant.canGlide){
			return true;
		}
		return false;
	}
}

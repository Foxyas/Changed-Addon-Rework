package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CanLeapProcedure {
	public static boolean execute(Entity entity) {
		Player player = (Player) entity;
		TransfurVariantInstance LatexInstace = ProcessTransfur.getPlayerTransfurVariant(player);
		TransfurVariant Variant = TransfurVariant.getEntityVariant(LatexInstace.getChangedEntity());
		if (Variant.is(ChangedTags.TransfurVariants.CAT_LIKE )|| Variant.is(ChangedTags.TransfurVariants.LEOPARD_LIKE)){
			return true;
		}
		return false;
	}

	public static boolean flyentity(Entity entity) {
		Player player = (Player) entity;
		TransfurVariantInstance LatexInstace = ProcessTransfur.getPlayerTransfurVariant(player);
		TransfurVariant Variant = TransfurVariant.getEntityVariant(LatexInstace.getChangedEntity());
		if (Variant.canGlide){
			return true;
		}
		return false;
	}
}

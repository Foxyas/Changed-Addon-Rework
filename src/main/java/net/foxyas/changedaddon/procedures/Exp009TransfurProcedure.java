package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Exp009TransfurProcedure {
	public static void execute(Entity entity) {
		Player entity1 = (Player) entity;
		ProcessTransfur.setPlayerTransfurVariant(entity1, ChangedAddonTransfurVariants.KET_EXPERIMENT_009.get(), TransfurCause.GRAB_REPLICATE);
	}

	public static void exp10(Entity entity) {
		Player entity1 = (Player) entity;
		ProcessTransfur.setPlayerTransfurVariant(entity1, ChangedAddonTransfurVariants.EXPERIMENT_10.get(), TransfurCause.GRAB_REPLICATE);
	}

}

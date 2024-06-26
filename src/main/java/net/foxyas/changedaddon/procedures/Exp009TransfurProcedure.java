package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Exp009TransfurProcedure {
	public static void execute(Entity entity) {
		Player entity1 = (Player) entity;
		ProcessTransfur.setPlayerLatexVariant(entity1, AddonLatexVariant.KET_EXPERIMENT_009);
	}
}

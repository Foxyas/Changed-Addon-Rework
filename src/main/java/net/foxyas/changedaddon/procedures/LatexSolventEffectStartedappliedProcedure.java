package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

public class LatexSolventEffectStartedappliedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double LatexSolvent_level = 0;
		LatexSolvent_level = (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) == 0
				? 0.1
				: (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) * 0.1 + 0.1;
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
						("attribute @s " + "changed_addon:latexresistance " + "modifier add f9ff3894-a234-4994-ac8f-d84f45d1827c \"Solvent Effect Attribute Change\" " + LatexSolvent_level + " add"));
		}
	}
}

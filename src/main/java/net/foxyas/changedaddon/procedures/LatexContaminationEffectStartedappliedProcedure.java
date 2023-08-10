package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

public class LatexContaminationEffectStartedappliedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double LatexSolvent_level = 0;
		double LatexContamination_level = 0;
		LatexContamination_level = (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()).getAmplifier() : 0) == 0
				? 0.1
				: (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()).getAmplifier() : 0) * 0.1 + 0.1;
		float LatexC = (float) LatexContamination_level;
		float Math = 0.5f * LatexC;
		float PlayerTransfurProgress = new Object() {
			public float getValue() {
				CompoundTag dataIndex0 = new CompoundTag();
				entity.saveWithoutId(dataIndex0);
				return dataIndex0.getFloat("TransfurProgress");
			}
		}.getValue();
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
						("attribute @s " + "changed_addon:latexinfection " + "modifier add 0-0-0-0-1 \"Latex Contamination Effect Attribute Change\" " + LatexContamination_level + " add"));
		}
		CompoundTag dataIndex1 = new CompoundTag();
		entity.saveWithoutId(dataIndex1);
		dataIndex1.putFloat("TransfurProgress", PlayerTransfurProgress + Math);
		entity.load(dataIndex1);
	}
}

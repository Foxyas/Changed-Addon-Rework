package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.core.Registry;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

public class LatexInsulatorEntityCollidesInTheBlockProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.fallDistance = 0;
		if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("changed_addon:latexentity")))) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 1) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.LATEX_SOLVENT.get(), 300, 0, false, false));
				if (entity instanceof LivingEntity _entity)
					_entity.hurt(new DamageSource("latex_solvent").bypassArmor(), 1);
			}
		}
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true) {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.LATEX_SOLVENT.get(), 120, 0, false, false));
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 1) {
					if (entity instanceof LivingEntity _entity)
						_entity.hurt(new DamageSource("latex_solvent").bypassArmor(), 1);
				}
			} else {
				if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal((Component.translatable("changedaddon.untransfur.Immune").getString())), true);
				}
			}
		}
	}
}

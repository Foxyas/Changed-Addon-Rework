package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.Registry;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

public class LitixCamoniaFluidMobplayerCollidesBlockProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true) {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
				if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
					_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.LATEX_SOLVENT.get(), 500, 0, false, false));
			} else {
				if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
					if (entity instanceof Player _player && !_player.level.isClientSide())
						_player.displayClientMessage(new TextComponent("you are immune to this fluid"), true);
				}
			}
		} else {
			if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
				_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.LATEX_SOLVENT.get(), 500, 0, false, false));
		}
		if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:latexentity")))) {
			if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
				_entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.LATEX_SOLVENT.get(), 200, 0, false, false));
		}
	}
}

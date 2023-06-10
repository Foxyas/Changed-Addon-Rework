package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

public class InfriendlygrabeffectOnEffectActiveTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.INFRIENDLYGRAB.get()) : false) {
			if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.INFRIENDLYGRABEFFECT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.INFRIENDLYGRABEFFECT.get()).getDuration() : 0) == 1) {
				if (entity instanceof ServerPlayer _player)
					_player.setGameMode(GameType.SURVIVAL);
			}
		}
	}
}

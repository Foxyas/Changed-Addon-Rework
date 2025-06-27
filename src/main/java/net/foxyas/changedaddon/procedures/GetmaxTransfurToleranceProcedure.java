package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;


public class GetmaxTransfurToleranceProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(new TextComponent(("The maximum Transfur Tolerance is \u00A76" + ReturnMaxTransfurToleranceProcedure.execute((LivingEntity) entity))), false);
	}
}

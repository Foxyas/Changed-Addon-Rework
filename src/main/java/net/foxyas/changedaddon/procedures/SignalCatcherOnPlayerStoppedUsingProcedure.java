package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;

public class SignalCatcherOnPlayerStoppedUsingProcedure {
	public static void execute(Entity entity, double time) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && !_player.level.isClientSide())
			_player.displayClientMessage(new TextComponent(("Not enough time " + time + "/15")), true);
	}
}

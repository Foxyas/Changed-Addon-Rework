package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;
import net.ltxprogrammer.changed.Changed;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

public class SetDefaultValueProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double DefaultValue = 20.0;
		Changed.config.server.transfurTolerance.set(DefaultValue);
		if (entity instanceof Player _player && !_player.level.isClientSide())
			_player.displayClientMessage(new TextComponent("Value has been set to default \u00A76<20>"), false);

		if (entity instanceof Player) {
			ChangedAddonMod.LOGGER.info((entity.getDisplayName().getString() + " Set the max Transfur Tolerance to " + "default \u00A76<20>"));
		}

	}
}

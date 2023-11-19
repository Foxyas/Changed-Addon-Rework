package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.ltxprogrammer.changed.Changed;
import net.minecraft.server.level.ServerPlayer;

public class SetmaxTransfurToleranceProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		double MaxNumber = 0;
		MaxNumber = DoubleArgumentType.getDouble(arguments, "MaxNumber");
		Changed.config.server.transfurTolerance.set(MaxNumber);
		if (entity instanceof Player _player && !_player.level.isClientSide())
			_player.displayClientMessage(new TextComponent(("The Maximum Transfur Tolerance has been set to \u00A76" + ReturnMaxTransfurToleranceProcedure.execute())), false);

		if (entity instanceof Player || entity instanceof ServerPlayer) {
			ChangedAddonMod.LOGGER.info((entity.getDisplayName().getString() + " Set the max Transfur Tolerance to " + MaxNumber));
		}

	}
}

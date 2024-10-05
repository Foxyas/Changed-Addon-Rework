package net.foxyas.changedaddon.procedures;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.ltxprogrammer.changed.Changed;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class SetmaxTransfurToleranceProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		double MaxNumber;
		MaxNumber = DoubleArgumentType.getDouble(arguments, "MaxNumber");
		LivingEntity livingEntity;
		try {
			livingEntity = (LivingEntity) EntityArgument.getEntity(arguments,"target");
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}
		Objects.requireNonNull(livingEntity.getAttributes().getInstance(ChangedAttributes.TRANSFUR_TOLERANCE.get())).setBaseValue(MaxNumber);
		if (entity instanceof Player _player && !_player.level.isClientSide())
			_player.displayClientMessage(new TextComponent(("The Maximum Transfur Tolerance has been set to §6" + ReturnMaxTransfurToleranceProcedure.execute(livingEntity))), false);

		if (entity instanceof Player || entity instanceof ServerPlayer) {
			ChangedAddonMod.LOGGER.info((entity.getDisplayName().getString() + " Set the max Transfur Tolerance to " + MaxNumber));
		}

	}
}

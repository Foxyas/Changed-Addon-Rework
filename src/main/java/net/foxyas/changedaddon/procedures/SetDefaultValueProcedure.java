package net.foxyas.changedaddon.procedures;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;
import net.ltxprogrammer.changed.Changed;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import java.util.Objects;

public class SetDefaultValueProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		double DefaultValue = 20.0;
		LivingEntity livingEntity;
		try {
			livingEntity = (LivingEntity) EntityArgument.getEntity(arguments,"target");
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}
		Objects.requireNonNull(livingEntity.getAttributes().getInstance(ChangedAttributes.TRANSFUR_TOLERANCE.get())).setBaseValue(DefaultValue);
		{
			Objects.requireNonNull(livingEntity.getAttributes().getInstance(ChangedAttributes.TRANSFUR_TOLERANCE.get())).setBaseValue(DefaultValue);
			if (entity instanceof Player _player && !_player.level.isClientSide())
				_player.displayClientMessage(new TextComponent("Value has been set to default §6<"+ DefaultValue + ">"), false);

			if (entity instanceof Player) {
				ChangedAddonMod.LOGGER.info((entity.getDisplayName().getString() + " Set the max Transfur Tolerance to " + "default §6<"+ DefaultValue +">"));
			}

		}
	}
}

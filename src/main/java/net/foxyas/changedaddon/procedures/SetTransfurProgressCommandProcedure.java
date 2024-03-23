package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class SetTransfurProgressCommandProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		double Number = 0;
		Number = DoubleArgumentType.getDouble(arguments, "Number");
		if (entity == entity) {
			AddTransfurProgressProcedure.set(entity, Number);
		}
	}
}

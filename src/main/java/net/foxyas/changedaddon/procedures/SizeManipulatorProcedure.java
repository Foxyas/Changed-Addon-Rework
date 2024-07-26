package net.foxyas.changedaddon.procedures;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.ltxprogrammer.changed.Changed;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;

public class SizeManipulatorProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		double amount = 0;
		Entity MainEntity = null;
		MainEntity = entity;
		amount = DoubleArgumentType.getDouble(arguments, "size");
		SizeChange((float) amount);
	}

	public static void SizeChange(float Amount){
		Changed.config.client.basicPlayerInfo.setSize(Amount);
	}
}

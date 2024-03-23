package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class AddTransfurProgressCommandProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		double Number = 0;
		Number = new Object() {
			public double getValue() {
				CompoundTag dataIndex0 = new CompoundTag();
				entity.saveWithoutId(dataIndex0);
				return dataIndex0.getDouble("TransfurProgress");
			}
		}.getValue() + DoubleArgumentType.getDouble(arguments, "Number");
		if (entity == entity) {
			AddTransfurProgressProcedure.set(entity, Number);
		}
	}
}

package net.foxyas.changedaddon.procedures;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class AddTransfurProgressCommandProcedure {
    public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
        if (entity == null)
            return;
        double Number = 0;
        if (entity instanceof Player player) {
            Number = ProcessTransfur.getPlayerTransfurProgress(player) + DoubleArgumentType.getDouble(arguments, "Number");
        } else {
            return;
        }
        AddTransfurProgressProcedure.set(entity, Number);
    }
}

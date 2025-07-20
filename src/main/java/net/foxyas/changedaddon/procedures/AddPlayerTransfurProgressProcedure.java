package net.foxyas.changedaddon.procedures;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class AddPlayerTransfurProgressProcedure {
    public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
        if (entity == null)
            return;
        double Number;
        Entity EntityTarget;
        EntityTarget = new Object() {
            public Entity getEntity() {
                try {
                    return EntityArgument.getEntity(arguments, "Target");
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.getEntity();
        if (EntityTarget instanceof Player player) {
            Number = ProcessTransfur.getPlayerTransfurProgress(player) + DoubleArgumentType.getDouble(arguments, "Number");
        } else {
            return;
        }
        AddTransfurProgressProcedure.set(EntityTarget, Number);
    }
}

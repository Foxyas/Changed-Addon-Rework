package net.foxyas.changedaddon.procedures;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;

public class TooglewarnsProcedure {
    public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
        if (entity == null)
            return;
        {
            boolean _setval = BoolArgumentType.getBool(arguments, "warns");
            entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.showwarns = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
    }
}

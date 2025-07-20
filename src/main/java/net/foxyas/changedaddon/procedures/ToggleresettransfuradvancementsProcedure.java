package net.foxyas.changedaddon.procedures;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ToggleresettransfuradvancementsProcedure {
    public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
        if (entity == null)
            return;
        if (BoolArgumentType.getBool(arguments, "turn")) {
            if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).reset_transfur_advancements) {
                if (entity instanceof Player _player && !_player.level.isClientSide())
                    _player.displayClientMessage(new TextComponent("\u00A7cNothing changed, it already had that value"), false);
            } else {
                if (entity instanceof Player _player && !_player.level.isClientSide())
                    _player.displayClientMessage(new TextComponent("You Activated the Transfur Reset Achievements"), false);
            }
        } else {
            if (!(entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).reset_transfur_advancements) {
                if (entity instanceof Player _player && !_player.level.isClientSide())
                    _player.displayClientMessage(new TextComponent("\u00A7cNothing changed, it already had that value"), false);
            } else {
                if (entity instanceof Player _player && !_player.level.isClientSide())
                    _player.displayClientMessage(new TextComponent("You Disabled Reset Transfur Achievements"), false);
            }
        }
        {
            boolean _setval = BoolArgumentType.getBool(arguments, "turn");
            entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.reset_transfur_advancements = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
    }
}

package net.foxyas.changedaddon.procedures;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CommandSetmaxTransfurToleranceProcedure {
    public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof Player _player && !_player.level.isClientSide())
            _player.displayClientMessage(new TextComponent(("The maximum Transfur Tolerance has been set to \u00A76" + DoubleArgumentType.getDouble(arguments, "MaxNumber"))), false);
    }
}

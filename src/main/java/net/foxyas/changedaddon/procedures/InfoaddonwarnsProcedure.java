package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class InfoaddonwarnsProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if (!(entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns) {
            if (entity instanceof Player _player && !_player.level.isClientSide())
                _player.displayClientMessage(new TextComponent(("Warns is \u00A74" + (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns)), true);
        } else {
            if (entity instanceof Player _player && !_player.level.isClientSide())
                _player.displayClientMessage(new TextComponent(("Warns is \u00A7b" + (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns)), true);
        }
    }
}

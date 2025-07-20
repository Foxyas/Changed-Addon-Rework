package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TurnOffTransfurOnKeyPressedProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
            if (ReturnTransfurModeProcedure.GetDefault.execute((Player) entity)) {
                if (ReturnTransfurModeProcedure.execute(entity) != 3) {
                    ReturnTransfurModeProcedure.setPlayerTransfurMode.execute((Player) entity, 3);
                } else {
                    ReturnTransfurModeProcedure.setPlayerTransfurMode.execute((Player) entity, (int) ReturnTransfurModeProcedure.GetDefault.GetDefaultValue(entity));
                }
            }
        }
    }
}

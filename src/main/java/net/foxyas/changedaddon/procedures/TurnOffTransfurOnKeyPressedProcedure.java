package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TurnOffTransfurOnKeyPressedProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
            if (GetDefault.execute((Player) entity)) {
                if (ReturnTransfurModeProcedure.execute(entity) != 3) {
                    setPlayerTransfurMode.execute((Player) entity, 3);
                } else {
                    setPlayerTransfurMode.execute((Player) entity, (int) GetDefault.GetDefaultValue(entity));
                }
            }
        }
    }
}

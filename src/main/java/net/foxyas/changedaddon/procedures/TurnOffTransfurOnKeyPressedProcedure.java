package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TurnOffTransfurOnKeyPressedProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if (!(entity instanceof Player player)) {
            return;
        }
        boolean transfur = ProcessTransfur.isPlayerTransfurred(player);
        if (transfur) {
        String transfurId = ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString();
            if (ReturnTransfurModeProcedure.GetDefault.execute(player)) {
                if (ReturnTransfurModeProcedure.execute(entity) != 3) {
                    ReturnTransfurModeProcedure.setPlayerTransfurMode.execute(player, 3);
                } else {
                    ReturnTransfurModeProcedure.setPlayerTransfurMode.execute(player, (int) ReturnTransfurModeProcedure.GetDefault.GetDefaultValue(entity));
                }
            }
        }
    }
}

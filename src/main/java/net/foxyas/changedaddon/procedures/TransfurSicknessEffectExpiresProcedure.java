package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TransfurSicknessEffectExpiresProcedure {
    public static void execute(Entity entity, double amplifier) {
        if (entity == null)
            return;
        if (entity instanceof Player player && ProcessTransfur.isPlayerTransfurred(player)) {
            /*
            if (ReturnTransfurModeProcedure.GetDefault.execute(player)) {
                ReturnTransfurModeProcedure.setPlayerTransfurMode.execute(player, 1);
            }*/
            //TransfurSicknessHandleProcedure.removeAttributeMod(entity, amplifier);
        }
    }
}

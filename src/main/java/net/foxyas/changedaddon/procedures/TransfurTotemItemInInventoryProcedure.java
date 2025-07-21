package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TransfurTotemItemInInventoryProcedure {
    public static double execute(Entity entity) {
        if (entity == null)
            return 0;
        double GlowTick = 0;

        if (entity instanceof Player player) {
            boolean transfur = ProcessTransfur.isPlayerTransfurred(player);
            var instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance == null) {
                return 0;
            }
            String transfurId = ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString();

            if (transfurId.equals("changed:form_latex_benign_wolf")) {
                GlowTick += 0.5;
                return GlowTick;
            }
        }
        return 0;
    }
}

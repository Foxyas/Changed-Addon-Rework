package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CanRoarProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        if (entity instanceof Player player && ProcessTransfur.isPlayerTransfurred(player)) {
            var Res = ProcessTransfur.getPlayerTransfurVariant(player).getFormId();
            if (Res != null) {
                String id = Res.toString();
                return id.contains("lion")
                        || id.contains("tiger")
                        || id.startsWith("changed_addon:form_ket_experiment009");
            }
        }
        return false;
    }
}

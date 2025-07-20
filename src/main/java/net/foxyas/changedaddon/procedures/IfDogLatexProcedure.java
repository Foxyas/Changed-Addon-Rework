package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class IfDogLatexProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance != null) {
                if (instance.getFormId().toString().contains("wolf")
                        || instance.getFormId().toString().contains("dog")
                        || instance.getFormId().toString().contains("pup")) {
                    return true;
                } else return PlayerUtil.IsWolfTransfur(player);
            }
        }
        return false;
    }
}

package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class IfPlayerArentTransfuredProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        return entity instanceof Player player && !ProcessTransfur.isPlayerTransfurred(player);
    }
}

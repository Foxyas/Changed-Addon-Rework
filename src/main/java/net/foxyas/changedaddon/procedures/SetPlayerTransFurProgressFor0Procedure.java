package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SetPlayerTransFurProgressFor0Procedure {
    public static void execute(Entity entity) {
        if (entity instanceof Player) {
            ProcessTransfur.setPlayerTransfurProgress((Player) entity, 0);
        }
    }
}

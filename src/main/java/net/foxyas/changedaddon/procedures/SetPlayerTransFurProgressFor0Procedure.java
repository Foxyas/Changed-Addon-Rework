package net.foxyas.changedaddon.procedures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


import net.ltxprogrammer.changed.process.ProcessTransfur;

public class SetPlayerTransFurProgressFor0Procedure {
    public static void execute(Entity entity) {
        if (entity instanceof Player) {
                ProcessTransfur.setPlayerTransfurProgress((Player) entity, 0);
        }
    }
}

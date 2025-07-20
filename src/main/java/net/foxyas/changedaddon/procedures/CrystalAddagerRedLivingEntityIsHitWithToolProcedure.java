package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

public class CrystalAddagerRedLivingEntityIsHitWithToolProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        Entity target = null;
        target = entity;
        AddTransfurProgressProcedure.addRed(entity, 3);
    }
}

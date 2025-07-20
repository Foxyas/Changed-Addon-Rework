package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

public class CrystalAddagerBlackLivingEntityIsHitWithToolProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        Entity target = null;
        AddTransfurProgressProcedure.addBlack(entity, 3);
    }
}

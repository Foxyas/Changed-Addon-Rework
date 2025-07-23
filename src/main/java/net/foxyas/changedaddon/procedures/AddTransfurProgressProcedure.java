package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.LivingEntity;

public class AddTransfurProgressProcedure {

    public static void addRed(LivingEntity entity, float amount) {
        ProcessTransfur.progressTransfur(entity, amount, entity.getRandom().nextInt(10) > 5
                ? ChangedTransfurVariants.CRYSTAL_WOLF.get()
                : ChangedTransfurVariants.CRYSTAL_WOLF_HORNED.get(), TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
    }
}
package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class ReturnMaxTransfurToleranceProcedure {
    public static double execute(LivingEntity entity) {
        return (float) Objects.requireNonNull(entity.getAttributes().getInstance(ChangedAttributes.TRANSFUR_TOLERANCE.get())).getValue();
    }
}

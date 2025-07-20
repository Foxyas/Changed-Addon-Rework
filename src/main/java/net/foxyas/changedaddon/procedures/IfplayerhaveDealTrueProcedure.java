package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

public class IfplayerhaveDealTrueProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        return entity.getPersistentData().getBoolean("Deal");
    }
}

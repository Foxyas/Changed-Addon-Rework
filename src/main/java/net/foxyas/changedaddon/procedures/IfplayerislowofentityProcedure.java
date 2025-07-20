package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class IfplayerislowofentityProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        if (entity.isInWater()) {
            return entity.getY() > (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY();
        }
        return false;
    }
}

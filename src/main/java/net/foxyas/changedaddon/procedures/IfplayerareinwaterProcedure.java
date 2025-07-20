package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class IfplayerareinwaterProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == (null))) {
            if (entity.isInWater() && (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).isInWater()) {
                return !(entity.getY() > (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY());
            }
        }
        return true;
    }
}

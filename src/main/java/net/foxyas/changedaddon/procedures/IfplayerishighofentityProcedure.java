package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class IfplayerishighofentityProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
            return (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY() >= entity.getY() + 1.5;
        }
        return false;
    }
}

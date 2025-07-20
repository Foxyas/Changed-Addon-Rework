package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.world.entity.Entity;

public class IfplayeraretransfurProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        return (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur;
    }
}

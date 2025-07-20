package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.world.entity.Entity;

public class FightTokeepconsciousnessminigameValueProcedure {
    public static String execute(Entity entity) {
        if (entity == null)
            return "";
        return (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).consciousness_fight_progress + "/25.0";
    }
}

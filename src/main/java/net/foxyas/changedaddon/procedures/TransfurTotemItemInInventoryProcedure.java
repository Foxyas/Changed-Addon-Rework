package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.world.entity.Entity;

public class TransfurTotemItemInInventoryProcedure {
    public static double execute(Entity entity) {
        if (entity == null)
            return 0;
        double GlowTick = 0;
        if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).equals("changed:form_latex_benign_wolf")) {
            GlowTick = GlowTick + 0.5;
            if (GlowTick == 2.5) {
                GlowTick = 0;
            }
            return GlowTick;
        } else {
            if (GlowTick != 0) {
                GlowTick = 0;
            }
        }
        return 0;
    }
}

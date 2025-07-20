package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class TransfurSicknessOnEffectActiveTickProcedure {
    public static void execute(Entity entity, double amplifier) {
        if (entity == null)
            return;
        double levelPotion = 0;
        if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
            if (ReturnTransfurModeProcedure.GetDefault.execute((Player) entity)) {
                ReturnTransfurModeProcedure.setPlayerTransfurMode.execute((Player) entity, 3);
            }
			/*levelPotion = amplifier;
			TransfurSicknessHandleProcedure.addAttributeMod(entity, levelPotion);*/
        }
    }
}

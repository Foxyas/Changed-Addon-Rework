package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class IfCatLatexProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance != null) {
                if (instance.getFormId().toString().contains("cat")
                        || instance.getFormId().toString().contains("leopard")
                        || instance.getFormId().toString().contains("lynx")
                        || instance.getFormId().toString().contains("tiger")
                        || instance.getFormId().toString().equals("changed:form_latex_snow_leopard/female")
                        || instance.getFormId().toString().equals("changed:form_latex_snow_leopard/male")
                        || instance.getFormId().toString().equals("changed:form_latex_medusa_cat")
                        || instance.getFormId().toString().equals("changed:form_latex_stiger")
                        || instance.getFormId().toString().equals("changed:form_latex_white_tiger")
                        || instance.getFormId().toString().equals("changed_addon:form_experiment_10")
                        || instance.getFormId().toString().startsWith("changed_addon:form_experiment009")
                        || instance.getFormId().toString().startsWith("changed_addon:form_exp2")) {
                    return true;
                } else return PlayerUtil.IsCatTransfur(player);
            }
        }
        return false;
    }
}

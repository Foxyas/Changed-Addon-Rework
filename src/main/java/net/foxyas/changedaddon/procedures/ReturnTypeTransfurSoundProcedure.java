package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ReturnTypeTransfurSoundProcedure {
    public static String execute(Entity entity) {
        if (!(entity instanceof Player player))
            return "";

        TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (variant == null)
            return "§fYou are a Transfur";

        String formId = variant.getFormId().toString();

        if (formId.contains("cat") ||
                formId.contains("leopard") ||
                formId.contains("lynx") ||
                formId.contains("tiger") ||
                formId.equals("changed:form_latex_snow_leopard/female") ||
                formId.equals("changed:form_latex_snow_leopard/male") ||
                formId.equals("changed:form_latex_medusa_cat") ||
                formId.equals("changed:form_latex_stiger") ||
                formId.equals("changed:form_latex_white_tiger")) {
            return "§fYou are a Cat Transfur";
        }

        if (formId.contains("wolf") || formId.contains("dog")) {
            return "§fYou are a Dog Transfur";
        }

        return "§fYou are a Transfur";
    }
}

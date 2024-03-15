package net.foxyas.changedaddon.procedures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;

public class SetPlayerTransFurProgressFor0Procedure {
    public static void execute(Entity entity) {
        if (entity instanceof Player) {
            if (ProcessTransfur.getPlayerTransfurProgress((Player) entity).variant() == LatexVariant.LIGHT_LATEX_WOLF ) {
                ProcessTransfur.setPlayerTransfurProgress((Player) entity, new ProcessTransfur.TransfurProgress(0, LatexVariant.LIGHT_LATEX_WOLF.male()));
            } else if (ProcessTransfur.getPlayerTransfurProgress((Player) entity).variant() != LatexVariant.LIGHT_LATEX_WOLF) {
                ProcessTransfur.setPlayerTransfurProgress((Player) entity, new ProcessTransfur.TransfurProgress(0, ProcessTransfur.getPlayerTransfurProgress((Player) entity).variant()));
            }
        }

    }
    public static void latexdmg(Entity entity){
        if (entity == null){
            return;
        }
        if (entity instanceof Player _entity) {

            ProcessTransfur.setPlayerTransfurProgress((Player) entity, new ProcessTransfur.TransfurProgress(ProcessTransfur.getPlayerTransfurProgress(_entity).progress()+1, LatexVariant.LIGHT_LATEX_WOLF));
        }

    }
}

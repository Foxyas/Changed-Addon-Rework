package net.foxyas.changedaddon.client.renderer.layers.animation;

import net.foxyas.changedaddon.ability.ChangedAddonAbilitys;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.LatexHumanoidModel;
import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;

public class CarryAbilityAnimation {

    public static void playAnimation(LatexEntity entity, LatexHumanoidModel<?> latexHumanoidModel) {
        boolean isWithCarryAbilitySelected = false;
        if (ProcessTransfur.getPlayerLatexVariant(entity.getUnderlyingPlayer()) != null) {
            isWithCarryAbilitySelected = ProcessTransfur.getPlayerLatexVariant(entity.getUnderlyingPlayer()).selectedAbility == ChangedAddonAbilitys.CARRY.get();
        }
        updateArmPositions(entity, latexHumanoidModel, isWithCarryAbilitySelected);
    }

    public static void updateArmPositions(LatexEntity entity, LatexHumanoidModel<?> model, boolean isWithCarryAbilitySelected) {
        if (entity.getUnderlyingPlayer() != null && entity.getUnderlyingPlayer().getFirstPassenger() != null) {
            // Levanta os braços se a habilidade de carregar está selecionada e a mão estiver vazia
            if (entity.getMainHandItem().isEmpty()) {
                // Levanta o braço principal para cima se estiver vazio
                model.getArm(entity.getMainArm()).xRot = ((float) -Math.PI / 2F) - 90; // Rotaciona para cima (180 graus)
            }
            if (entity.getOffhandItem().isEmpty()) {
                // Levanta o braço offhand para cima se estiver vazio
                model.getArm(entity.getMainArm().getOpposite()).xRot = ((float) -Math.PI / 2F) - 90; // Rotaciona para cima (180 graus)
            }
        } else if (entity.getFirstPassenger() != null) {
            // Levanta os braços se a habilidade de carregar está selecionada e a mão estiver vazia
            if (entity.getMainHandItem().isEmpty()) {
                // Levanta o braço principal para cima se estiver vazio
                model.getArm(entity.getMainArm()).xRot = ((float) -Math.PI / 2F) - 90; // Rotaciona para cima (180 graus)
            }
            if (entity.getOffhandItem().isEmpty()) {
                // Levanta o braço offhand para cima se estiver vazio
                model.getArm(entity.getMainArm().getOpposite()).xRot = ((float) -Math.PI / 2F) - 90; // Rotaciona para cima (180 graus)
            }
        }


        // Aplica animações normais aos braços com base na rotação da cabeça apenas se isWithCarryAbilitySelected for true
        if (isWithCarryAbilitySelected && PlayerUtilProcedure.getEntityPlayerLookingAt(entity.getUnderlyingPlayer(), 3) != null) {
            if (entity.getMainHandItem().isEmpty()) {
                model.getArm(entity.getMainArm()).xRot = (-(float) Math.PI / 2F) + model.getHead().xRot;
                model.getArm(entity.getMainArm()).yRot = -0.1F + model.getHead().yRot;
            }
            if (entity.getOffhandItem().isEmpty()) {
                model.getArm(entity.getMainArm().getOpposite()).yRot = 0.1F + model.getHead().yRot;
                model.getArm(entity.getMainArm().getOpposite()).xRot = (-(float) Math.PI / 2F) + model.getHead().xRot;
            }
        }
    }
}

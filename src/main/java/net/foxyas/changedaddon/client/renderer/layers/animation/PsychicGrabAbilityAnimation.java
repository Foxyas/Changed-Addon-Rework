package net.foxyas.changedaddon.client.renderer.layers.animation;

import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class PsychicGrabAbilityAnimation {
    public static void playAnimation(ChangedEntity entity,
                                     AdvancedHumanoidModel<?> model) {
        if (shouldAnimate(entity)) {
            // Detecta o braço usado com base na mão usada
            InteractionHand usedHand = entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() ? InteractionHand.MAIN_HAND : entity.getItemInHand(InteractionHand.OFF_HAND).isEmpty() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            HumanoidArm arm = entity.getMainArm();
            if (usedHand == InteractionHand.OFF_HAND) {
                arm = arm.getOpposite();
            }

            // Aplica rotação ao braço com base na cabeça
            model.getArm(arm).xRot = model.head.xRot - (float)Math.PI / 2F - (entity.isCrouching() ? 0.2617994F : 0.0F);
            model.getArm(arm).yRot = model.head.yRot;
        }
    }

    private static boolean shouldAnimate(ChangedEntity entity) {
        // Aqui você pode colocar sua lógica de ativação da animação, por exemplo:
        // return entity está usando a habilidade de agarrar psiquicamente
        boolean Work = false;
        if (entity.getUnderlyingPlayer() != null) {
            Work = ProcessTransfur.getPlayerTransfurVariantSafe(entity.getUnderlyingPlayer())
                    .map(transfurVariantInstance -> {
                        AbstractAbilityInstance abilityInstance = transfurVariantInstance.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_GRAB.get());
                        return abilityInstance != null && abilityInstance.getController().getHoldTicks() > 0;
                    }).orElse(false);
        }
        return Work;
    }
}

package net.foxyas.changedaddon.client.renderer.layers.animation;

import net.foxyas.changedaddon.ability.ChangedAddonAbilitys;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;
import java.util.Optional;

public class CarryAbilityAnimation {

    public static void playAnimation(ChangedEntity entity, AdvancedHumanoidModel<?> AdvancedHumanoidModel) {
        boolean isWithCarryAbilitySelected = false;
        if (ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer()) != null) {
            isWithCarryAbilitySelected = ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer()).selectedAbility == ChangedAddonAbilitys.CARRY.get();
        }
        updateArmPositions(entity, AdvancedHumanoidModel, isWithCarryAbilitySelected);
    }

    public static void updateArmPositions(ChangedEntity entity, AdvancedHumanoidModel<?> model, boolean isWithCarryAbilitySelected) {
        if (entity.getUnderlyingPlayer() != null && entity.getUnderlyingPlayer().getFirstPassenger() != null) {
            // Levanta os braços se a habilidade de carregar está selecionada e a mão estiver vazia
            if (entity.getMainHandItem().isEmpty()) {
                // Levanta o braço principal para cima se estiver vazio
                model.getArm(entity.getMainArm()).xRot = (float) Math.PI; // Rotaciona para cima (180 graus)
            }
            if (entity.getOffhandItem().isEmpty()) {
                // Levanta o braço offhand para cima se estiver vazio
                model.getArm(entity.getMainArm().getOpposite()).xRot = (float) Math.PI; // Rotaciona para cima (180 graus)
            }
        } else if (entity.getFirstPassenger() != null) {
            // Levanta os braços se a habilidade de carregar está selecionada e a mão estiver vazia
            if (entity.getMainHandItem().isEmpty()) {
                // Levanta o braço principal para cima se estiver vazio
                model.getArm(entity.getMainArm()).xRot = (float) Math.PI; // Rotaciona para cima (180 graus)
            }
            if (entity.getOffhandItem().isEmpty()) {
                // Levanta o braço offhand para cima se estiver vazio
                model.getArm(entity.getMainArm().getOpposite()).xRot = (float) Math.PI; // Rotaciona para cima (180 graus)
            }
        }

        // Aplica animações normais aos braços com base na rotação da cabeça apenas se isWithCarryAbilitySelected for true
        if (entity.getUnderlyingPlayer() != null && entity.getUnderlyingPlayer().getFirstPassenger() == null){
            Entity entity1 = PlayerUtilProcedure.getEntityPlayerLookingAt(entity.getUnderlyingPlayer(), 3);
            if (isWithCarryAbilitySelected && entity1 instanceof LivingEntity livingEntity) {
                if (livingEntity.getType().is(ChangedTags.EntityTypes.HUMANOIDS)
                        || livingEntity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:can_carry")))
                        && entity.getFirstPassenger() == null) {

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
    }
}

package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class TransfurSicknessHandleProcedure {
    private static final UUID TRANSFUR_SICKNESS_MODIFIER_ID = UUID.fromString("0-0-0-0-0");
    private static final String MODIFIER_NAME = "Transfur_Sickness_Debuff";

    // Adds or updates the Transfur Sickness attribute modifier
    public static void addAttributeMod(Entity entity, double amplifier) {
        if (entity instanceof Player player && ProcessTransfur.isPlayerTransfurred(player)) {
            var attribute = player.getAttribute(ChangedAttributes.TRANSFUR_DAMAGE.get());
            if (attribute == null) {
                System.err.println("Failed to retrieve TRANSFUR_DAMAGE attribute for player: " + player);
                return;
            }

            // Define the attribute modifier with an updated value
            var attributeMod = new AttributeModifier(
                    TRANSFUR_SICKNESS_MODIFIER_ID, MODIFIER_NAME, (-0.1) * (amplifier + 1), AttributeModifier.Operation.ADDITION
            );

            // Only apply the modifier if it is not already present
            if (!attribute.hasModifier(attributeMod)) {
                attribute.addTransientModifier(attributeMod);
            }
        }
        if (entity instanceof LivingEntity livingEntity) {
            var attribute = livingEntity.getAttribute(ChangedAttributes.TRANSFUR_DAMAGE.get());
            if (attribute == null) {
                System.err.println("Failed to retrieve TRANSFUR_DAMAGE attribute for entity: " + entity);
                return;
            }

            // Define the attribute modifier with an updated value
            var attributeMod = new AttributeModifier(
                    TRANSFUR_SICKNESS_MODIFIER_ID, MODIFIER_NAME, (-0.1) * (amplifier + 1), AttributeModifier.Operation.ADDITION
            );

            // Only apply the modifier if it is not already present
            if (!attribute.hasModifier(attributeMod)) {
                attribute.addTransientModifier(attributeMod);
            }
        }
    }

    // Removes the Transfur Sickness attribute modifier
    public static void removeAttributeMod(Entity entity, double level) {
        if (entity instanceof Player player && ProcessTransfur.isPlayerTransfurred(player)) {
            var attribute = player.getAttribute(ChangedAttributes.TRANSFUR_DAMAGE.get());
            if (attribute == null) {
                System.err.println("Failed to retrieve TRANSFUR_DAMAGE attribute for player: " + player);
                return;
            }

            // Remove the modifier if it exists
            if (attribute.getModifier(TRANSFUR_SICKNESS_MODIFIER_ID) != null) {
                attribute.removeModifier(TRANSFUR_SICKNESS_MODIFIER_ID);
            }
        }
        if (entity instanceof LivingEntity livingEntity) {
            var attribute = livingEntity.getAttribute(ChangedAttributes.TRANSFUR_DAMAGE.get());
            if (attribute == null) {
                System.err.println("Failed to retrieve TRANSFUR_DAMAGE attribute for entity: " + entity);
                return;
            }

            // Remove the modifier if it exists
            if (attribute.getModifier(TRANSFUR_SICKNESS_MODIFIER_ID) != null) {
                attribute.removeModifier(TRANSFUR_SICKNESS_MODIFIER_ID);
            }
        }

    }
}

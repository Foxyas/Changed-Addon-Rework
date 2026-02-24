package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public interface IBestiaryEntityData {
    Component getLore();

    default List<Component> getAttributePreview() {
        if (!(this instanceof ChangedEntity changedEntity)) {
            return List.of();
        }

        List<Component> previewList = new ArrayList<>();

        // Player base vanilla
        AttributeSupplier playerDefaults =
                DefaultAttributes.getSupplier(EntityType.PLAYER);

        AttributeMap transformedMap = changedEntity.getAttributes();

        for (AttributeInstance transformedInstance : transformedMap.getSyncableAttributes()) {
            Attribute attribute = transformedInstance.getAttribute();

            if (!playerDefaults.hasAttribute(attribute)) continue;

            double playerBase = playerDefaults.getBaseValue(attribute);
            double transformedBase = transformedInstance.getBaseValue();

            double diff = transformedBase - playerBase;

            if (diff == 0.0D) continue;

            boolean isPercent = attribute == Attributes.MOVEMENT_SPEED
                    || attribute == Attributes.ATTACK_SPEED;

            String valueString;

            if (isPercent) {
                double percent = diff * 100.0D;
                valueString = String.format("%+.0f%%", percent);
            } else {
                valueString = String.format("%+.2f", diff);
            }

            ChatFormatting color = diff > 0 ? ChatFormatting.GREEN : ChatFormatting.RED;

            Component line = Component.literal(
                    "If transformed: ")
                    .append(Component.literal(valueString).withStyle(color).append(" ").append(Component.translatable(attribute.getDescriptionId()))
            );

            previewList.add(line);
        }

        return previewList;
    }

    default List<AbstractAbility<?>> getAbilities() {
        if (this instanceof ChangedEntity changedEntity) {
            return changedEntity.getSelfVariant().abilities.stream()
                    .<AbstractAbility<?>>map((typeFunction) -> typeFunction.apply(changedEntity.getType()))
                    .toList();
        }

        return List.of();
    }

    EntityType<?> getReferencedEntityType();

    default boolean isUnlocked(Player player) {
        /*if (player instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getStats().getValue(Stats.ENTITY_KILLED.get(getReferencedEntityType())) >= 0;
        }*/
        //TODO MAYBE Make the player need to kill the entity to it to show in the bestiary?
        // this will make the player also have the option to see the boss entities info before fighting them by just putting they "spawn egg" into the block slot

        return true;
    }
}
package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IBestiaryEntityData {

    static List<Component> getAttributePreview(LivingEntity livingEntity) {
        List<Component> previewList = new ArrayList<>();

        // Player base vanilla
        AttributeSupplier playerDefaults =
                DefaultAttributes.getSupplier(EntityType.PLAYER);

        AttributeMap transformedMap = livingEntity.getAttributes();

        for (AttributeInstance transformedInstance : transformedMap.getSyncableAttributes()) {
            Attribute attribute = transformedInstance.getAttribute();
            if (!playerDefaults.hasAttribute(attribute)) continue;

            double playerBase = playerDefaults.getValue(attribute);
            double transformedBase = transformedInstance.getValue();
            if (attribute == Attributes.MOVEMENT_SPEED) {
                transformedBase *= 0.1;
            }

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
                            "")
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

    default List<BestiaryInfo> getBestiaryInfo() {
        BestiaryInfo lore = getBasicLore();

        if (!(this instanceof LivingEntity livingEntity)) {
            return new ArrayList<>(Collections.singleton(lore));
        }

        List<Component> attributes = getAttributePreview(livingEntity);

        if (attributes.isEmpty()) {
            return new ArrayList<>(Collections.singleton(lore));
        }

        MutableComponent attributeText = Component.empty();
        attributes.forEach(component -> attributeText.append("\n").append(component));

        BestiaryInfo attributeData = new BestiaryInfo(
                Component.literal("Attributes"),
                attributeText.withStyle(ChatFormatting.GREEN),
                1
        );

        return new ArrayList<>(List.of(lore, attributeData));
    }

    default BestiaryInfo getBasicLore() {
        return new BestiaryInfo(Component.literal("Lore").withStyle(ChatFormatting.YELLOW), Component.literal("N/A"), 0);
    }

    record BestiaryInfo(Component title, Component description, int order) {
    }
}
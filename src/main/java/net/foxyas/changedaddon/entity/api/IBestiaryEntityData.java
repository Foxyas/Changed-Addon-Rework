package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.Entity;
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

            if (diff <= 0.0001D) continue;

            boolean isPercent = attribute == Attributes.MOVEMENT_SPEED
                    || attribute == Attributes.ATTACK_SPEED;

            String valueString;

            if (isPercent) {
                double percentDiff = (transformedBase / playerBase) - 1;
                if (percentDiff <= 0.0001D) continue;

                double percent = percentDiff * 100.0D;
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

    default EntityType<?> getReferencedEntityType() {
        return this instanceof Entity entity ? entity.getType() : null;
    }

    default void applyBestiaryRenderState(ChangedEntity changedEntity, GuiGraphics guiGraphics) {
    }

    default boolean isUnlocked(Player player) {
        StatsCounter stats = null;
        if (player.level.isClientSide()) {
            if (player instanceof LocalPlayer localPlayer) {
                stats = localPlayer.getStats();
            }
        } else if (player instanceof ServerPlayer serverPlayer) {
            stats = serverPlayer.getStats();
        }

        //TODO MAYBE Make the player need to kill the entity to it to show in the bestiary?
        // this will make the player also have the option to see the boss entities info before fighting them by just putting they "spawn egg" into the block slot
        if (stats != null) {
            return stats.getValue(Stats.ENTITY_KILLED.get(getReferencedEntityType())) >= 5;
        }

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
        int index = 0;
        for (Component component : attributes) {
            if (index == 0) {
                attributeText.append(component);
            } else {
                attributeText.append("\n").append(component);
            }
            index++;
        }

        BestiaryInfo attributeData = new BestiaryInfo(
                Component.literal("Attributes"),
                attributeText.withStyle(ChatFormatting.GREEN),
                1,
                -60
        );

        if (livingEntity.level.isClientSide()) {
            Minecraft minecraft = Minecraft.getInstance();
            int lineCount = minecraft.font.split(attributeText, 180).size();
            int lineBreaks = attributeText.getString().split("\n", -1).length - 1;

            // Dynamic Stuff can be done here... not clue to HOW make it looks good...
            // Most of the time it just get "too upwards"...

            attributeData = new BestiaryInfo(
                    Component.literal("Attributes"),
                    attributeText.withStyle(ChatFormatting.GREEN),
                    1,
                    -60
            );
        }



        return new ArrayList<>(List.of(lore, attributeData));
    }

    default BestiaryInfo getBasicLore() {
        return new BestiaryInfo(Component.literal("Lore").withStyle(ChatFormatting.YELLOW), Component.literal("N/A"), 0);
    }

    class BestiaryInfo {

        public final Component title;
        public final Component description;
        public final int order;
        public final int heightSizeOffset;

        public BestiaryInfo(Component title, Component description, int order) {
            this.title = title;
            this.description = description;
            this.order = order;
            this.heightSizeOffset = 0;
        }

        public BestiaryInfo(Component title, Component description, int order, int heightSizeOffset) {
            this.title = title;
            this.description = description;
            this.order = order;
            this.heightSizeOffset = heightSizeOffset;
        }


        public Component title() {
            return this.title;
        }

        public Component description() {
            return this.description;
        }

        public int order() {
            return order;
        }

        public int heightSizeOffset() {
            return heightSizeOffset;
        }
    }
}
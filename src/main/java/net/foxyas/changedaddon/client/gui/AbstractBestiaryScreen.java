package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.entity.api.IBestiaryEntityData;
import net.foxyas.changedaddon.process.bestiary.BestiaryEntriesManager;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;

public abstract class AbstractBestiaryScreen extends Screen implements MouseMoveListener {

    protected AbstractBestiaryScreen(Component pTitle) {
        super(pTitle);
    }

    protected void setUnlockedColor(GuiGraphics graphics) {
        graphics.setColor(0.05f, 0.05f, 0.07f, 1.0f);
    }

    protected void resetColor(GuiGraphics graphics) {
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    protected String getVariantDisplayName(TransfurVariant<?> tf) {
        if (tf == null) return Component.translatable("gui.changed_addon.bestiary.unknown").getString();

        Player player = this.minecraft != null ? this.minecraft.player : null;

        // Obfuscated display name if target variant is not unlocked
        if (!isVariantUnlocked(tf, player)) {
            return "???";
        }

        EntityType<?> type = tf.getEntityType();
        if (type != null) {
            return Component.translatable(type.getDescriptionId()).getString();
        }
        return tf.getFormId().getPath();
    }

    protected Component getVariantDisplayNameComponent(TransfurVariant<?> tf) {
        if (tf == null) return Component.translatable("gui.changed_addon.bestiary.unknown");

        Player player = this.minecraft != null ? this.minecraft.player : null;

        // Obfuscated display name if target variant is not unlocked
        if (!isVariantUnlocked(tf, player)) {
            return Component.literal("???");
        }

        EntityType<?> type = tf.getEntityType();
        if (type != null) {
            return Component.translatable(type.getDescriptionId());
        }
        return Component.literal(tf.getFormId().getPath());
    }

    /**
     * Utility method to check if a TransfurVariant is unlocked for a given player either
     * via IBestiaryEntityData (including referenced entity types) or BestiaryEntriesManager.
     */
    public boolean isVariantUnlocked(TransfurVariant<?> variant, Player player) {
        if (variant == null) {
            return false;
        }
        if (player == null) {
            return true;
        }

        boolean managerUnlocked = BestiaryEntriesManager.isVariantBestiaryUnlocked(variant, player);
        if (managerUnlocked) {
            return true;
        }

        Level level = player.level();
        ChangedEntity entity = (ChangedEntity) ChangedEntities.getCachedEntity(level, variant.getEntityType());
        if (entity instanceof IBestiaryEntityData data) {
            if (data.isUnlocked(player)) {
                return true;
            }
            EntityType<?> refType = data.getReferencedEntityType();
            if (refType != null && refType != entity.getType()) {
                Entity cached = ChangedEntities.getCachedEntity(level, refType);
                if (cached instanceof IBestiaryEntityData refData) {
                    return refData.isUnlocked(player);
                }
            }
        }

        return false;
    }
}

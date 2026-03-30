package net.foxyas.changedaddon.client.gui.ftkc;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.packet.ServerboundProgressFTKCPacket;
import net.foxyas.changedaddon.qte.FightToKeepConsciousness;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import org.jetbrains.annotations.NotNull;

public class CircleHoverMinigameScreen extends CircleMinigameScreen {

    private static final float fullnessPerTick = 1 / 40f;

    public CircleHoverMinigameScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();
        circle.set(halfWidth, halfHeight);
        if (cursor.x == 0 && cursor.y == 0) randomizeCursorPos(width / 3f, height / 3f);
    }

    @Override
    public void tick() {
        if (ChangedAddonVariables.ofOrDefault(player).FTKCminigameType == null) {
            minecraft.setScreen(null);
            return;
        }

        struggleProgressO = struggleProgress;

        // Normal Mouse Offset happen each 2 ticks.
        if (player.tickCount % 2 == 0) cursor.add((player.getRandom().nextFloat() - .5f) * 16, (player.getRandom().nextFloat() - .5f) * 16);

        // Heavy Offset happen each 80 ticks.
        if (player.tickCount % 80 == 0) cursor.add((player.getRandom().nextFloat() - .5f) * 64, (player.getRandom().nextFloat() - .5f) * 64);

        float scale = circleScale();
        if (cursor.distanceSquared(circle) <= INTERACTION_RADIUS_SQR * scale * scale) {
            increaseStruggle();
            return;
        }

        if (struggleProgress < 0) {
            struggleProgress = 0;
            return;
        }

        if (struggleProgress > 0) {
            struggleProgress = Math.max(0, struggleProgress - fullnessPerTick * 1.75f);
        }
    }

    protected float circleScale() {
        TransfurVariantInstance<?> transfurInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        return Mth.clamp(FightToKeepConsciousness.getStruggleTime() - (transfurInstance == null ? 0 : transfurInstance.ageAsVariant) / FightToKeepConsciousness.getStruggleTime(), 0, 1) + 1;
    }

    protected void drawCircles(@NotNull GuiGraphics guiGraphics) {
        TransfurVariantInstance<?> var = ProcessTransfur.getPlayerTransfurVariant(player);
        if (var != null) {
            Color3 color = var.getTransfurColor();
            guiGraphics.setColor(1 - color.red(), 1 - color.green(), 1 - color.blue(), 1);
        } else guiGraphics.setColor(0, 0, 0, 0);

        float scale = circleScale();
        WidgetHelper.blit(CIRCLE_SLOT, guiGraphics.pose(), circle.x - 10 * scale, circle.y - 10 * scale, 19 * scale, 19 * scale, 19, 19);
        guiGraphics.setColor(1, 1, 1, 1);

        RenderSystem.setShaderTexture(0, CIRCLE_CURSOR);
        WidgetHelper.blit(CIRCLE_CURSOR, guiGraphics.pose(), cursor.x - 10, cursor.y - 10, 19, 19, 19, 19);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        drawProgressBar(guiGraphics, halfWidth, halfHeight + 25, partialTick);

        guiGraphics.drawCenteredString(font, Component.translatable("gui.changed_addon.fight_to_keep_consciousness_minigame.label_text", KeyPressMinigameScreen.getTimeRemaining(player)), (int) halfWidth, (int) (halfHeight - 50), -1);
        guiGraphics.drawCenteredString(font, KeyPressMinigameScreen.getProgressText(player), (int) halfWidth, (int) (halfHeight - 30), -1);

        drawCircles(guiGraphics);
    }

    @Override
    protected void increaseStruggle() {
        struggleProgressO = struggleProgress;

        if (struggleProgress < 1) struggleProgress += fullnessPerTick;

        if (struggleProgress >= 0.8) ChangedAddonMod.PACKET_HANDLER.sendToServer(new ServerboundProgressFTKCPacket());
    }
}

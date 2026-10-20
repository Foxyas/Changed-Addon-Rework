package net.foxyas.changedaddon.client.gui.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;

public class BestiaryModelViewportRenderer {

    public static void render(GuiGraphics graphics, Font font, int x, int y, int w, int h, 
                              int mouseX, int mouseY, LivingEntity currentEntity, 
                              boolean isUnlocked, float modelYaw, float modelPitch, 
                              float modelZoom, float modelOffsetX, float modelOffsetY, 
                              boolean isModelOnly, int[] buttonBoundsOut,
                              Runnable setUnlockedColor, Runnable resetColor) {
        
        // Inset Box background
        graphics.fill(x, y, x + w, y + h, 0xFF0D0D10);
        graphics.fill(x, y, x + w, y + 1, 0xFF16161A);
        graphics.fill(x, y, x + 1, y + h, 0xFF16161A);
        graphics.fill(x, y + h - 1, x + w, y + h, 0xFF35353D);
        graphics.fill(x + w - 1, y, x + w, y + h, 0xFF35353D);

        // Expand / Restore Model Toggle Button
        int btnW = 14;
        int btnH = 12;
        int btnX = x + w - btnW - 3;
        int btnY = y + 3;

        buttonBoundsOut[0] = btnX;
        buttonBoundsOut[1] = btnY;
        buttonBoundsOut[2] = btnW;
        buttonBoundsOut[3] = btnH;

        boolean isBtnHovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;

        int btnBg = isModelOnly ? 0xFF2A4438 : (isBtnHovered ? 0xFF282830 : 0xFF1A1A20);
        int btnBorder = isModelOnly ? 0xFF55FF55 : (isBtnHovered ? 0xFF585868 : 0xFF2A2A32);

        graphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, btnBg);
        graphics.renderOutline(btnX, btnY, btnW, btnH, btnBorder);

        String btnSymbol = isModelOnly ? "◀" : "⛶";
        int symW = font.width(btnSymbol);
        graphics.drawString(font, btnSymbol, btnX + (btnW - symW) / 2, btnY + 2, isModelOnly ? 0x55FF55 : (isBtnHovered ? 0xFFFFFF : 0xAAAAAA), false);

        if (isBtnHovered) {
            Component tooltip = Component.translatable(isModelOnly ? "gui.changed_addon.bestiary.button.restore_split" : "gui.changed_addon.bestiary.button.expand_model");
            graphics.renderTooltip(font, tooltip, mouseX, mouseY);
        }

        if (currentEntity != null) {
            int centerX = x + w / 2;
            int centerY = y + h - 26;

            graphics.fill(centerX - 24, centerY - 2, centerX + 24, centerY + 3, 0x44000000);
            graphics.fill(centerX - 18, centerY - 3, centerX + 18, centerY + 4, 0x33000000);

            if (!isUnlocked) {
                graphics.drawString(font, Component.translatable("gui.changed_addon.bestiary.locked"), centerX - 24, y + 8, 0xFF5555, false);

                String lockMsg = Component.translatable("gui.changed_addon.bestiary.locked").getString();
                int lockW = font.width(lockMsg);
                PoseStack pose = graphics.pose();
                pose.pushPose();
                pose.translate(0, 0, 200.0F);
                graphics.drawString(font, lockMsg, centerX - (lockW / 2), y + (8 * 12), 0xFF5555, false);
                pose.popPose();
            }

            float oldYBodyRot = currentEntity.yBodyRot;
            float oldYRot = currentEntity.getYRot();
            float oldXRot = currentEntity.getXRot();
            float oldYHeadRot = currentEntity.yHeadRot;
            float oldYHeadRotO = currentEntity.yHeadRotO;

            currentEntity.yBodyRot = 180.0f;
            currentEntity.setYRot(180.0f);
            currentEntity.setXRot(0.0f);
            currentEntity.yHeadRot = 180.0f;
            currentEntity.yHeadRotO = 180.0f;

            Quaternionf pose = new Quaternionf()
                    .rotateZ((float) Math.PI)
                    .rotateX(modelPitch * ((float) Math.PI / 180.0f))
                    .rotateY(modelYaw * ((float) Math.PI / 180.0f));

            boolean scissored = false;
            if (w > 4 && h > 4) {
                graphics.enableScissor(x + 2, y + 2, x + w - 2, y + h - 2);
                scissored = true;
            }

            try {
                int modelCenterX = centerX + (int) modelOffsetX;
                int modelCenterY = centerY + (int) modelOffsetY;
                graphics.pose().pushPose();
                graphics.pose().translate(0, 0, 50);

                if (currentEntity instanceof Player player && player.getPose() == Pose.CROUCHING) {
                    modelCenterY -= 7;
                }

                if (!isUnlocked) setUnlockedColor.run();

                InventoryScreen.renderEntityInInventory(graphics, modelCenterX, modelCenterY, (int) modelZoom, pose, null, currentEntity);

                if (!isUnlocked) resetColor.run();

                graphics.pose().popPose();
            } catch (Exception e) {
                graphics.drawString(font, Component.translatable("gui.changed_addon.bestiary.render_error"), centerX - 28, centerY - 10, 0xFF5555, false);
            } finally {
                if (scissored) {
                    graphics.disableScissor();
                }
                currentEntity.yBodyRot = oldYBodyRot;
                currentEntity.setYRot(oldYRot);
                currentEntity.setXRot(oldXRot);
                currentEntity.yHeadRot = oldYHeadRot;
                currentEntity.yHeadRotO = oldYHeadRotO;
            }
        } else {
            graphics.drawString(font, Component.translatable("gui.changed_addon.bestiary.no_model"), x + w / 2 - 24, y + h / 2 - 4, 0x777777, false);
        }

        Component hint = Component.translatable("gui.changed_addon.bestiary.model_hint");
        int hintW = font.width(hint);
        float hintScale = 0.75f;
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + (w - (int) (hintW * hintScale)) / 2.0, y + h - 9, 200.0);
        pose.scale(hintScale, hintScale, 1.0f);
        graphics.drawString(font, hint, 0, 0, 0x888888, false);
        pose.popPose();
    }
}
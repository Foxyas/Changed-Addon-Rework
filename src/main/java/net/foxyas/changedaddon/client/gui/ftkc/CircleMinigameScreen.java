package net.foxyas.changedaddon.client.gui.ftkc;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.qte.FightToKeepConsciousness;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Random;

public abstract class CircleMinigameScreen extends Screen {

    public static final ResourceLocation CIRCLE_SLOT = ChangedAddonMod.textureLoc(
            "textures/screens/qtes/fight_to_keep_consciousness/struggle_circle_slot");
    public static final ResourceLocation CIRCLE_CURSOR = ChangedAddonMod.textureLoc(
            "textures/screens/qtes/fight_to_keep_consciousness/struggle_circle_cursor");
    protected static final float INTERACTION_RADIUS = 15f;
    protected static final float INTERACTION_RADIUS_SQR = INTERACTION_RADIUS * INTERACTION_RADIUS;

    protected final Minecraft minecraft;
    protected final Player player;
    protected final Vector2f circle = new Vector2f();
    protected final Vector2f mouseLast = new Vector2f();
    protected final Vector2f cursor = new Vector2f();
    protected float struggleProgressO = 0;
    protected float struggleProgress = 0;
    protected float halfWidth;
    protected float halfHeight;

    protected CircleMinigameScreen(Component title) {
        super(title);
        minecraft = Minecraft.getInstance();
        player = minecraft.player;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        halfWidth = width / 2f;
        halfHeight = height / 2f;
        GLFW.glfwSetInputMode(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, InputConstants.CURSOR_DISABLED);
    }

    protected void drawProgressBar(GuiGraphics guiGraphics, float x, float y, float partialTick) {
        final int barWidth = 100;
        final int barHeight = 10;

        float filledHalfWidth = (int) (Mth.lerp(partialTick, struggleProgressO, struggleProgress) * barWidth / 2);

        guiGraphics.fill((int) ((int) x - filledHalfWidth), (int) (y - barHeight / 2f), (int) (x + filledHalfWidth), (int) (y + barHeight / 2f), Color.WHITE.getRGB());
    }

    protected void drawCircles(@NotNull GuiGraphics guiGraphics) {
        TransfurVariantInstance<?> var = ProcessTransfur.getPlayerTransfurVariant(player);
        if (var != null) {
            Color3 color = var.getTransfurColor();
            guiGraphics.setColor(1 - color.red(), 1 - color.green(), 1 - color.blue(), 1);
        } else guiGraphics.setColor(0, 0, 0, 0);

        WidgetHelper.blit(CIRCLE_SLOT, guiGraphics.pose(), circle.x - 10, circle.y - 10, 19, 19, 19, 19);
        guiGraphics.setColor(1, 1, 1, 1);

        RenderSystem.setShaderTexture(0, CIRCLE_CURSOR);
        WidgetHelper.blit(CIRCLE_CURSOR, guiGraphics.pose(), cursor.x - 10, cursor.y - 10, 19, 19, 19, 19);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(guiGraphics, pPartialTick);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    public void renderBackground(@NotNull GuiGraphics guiGraphics, float partialTick) {
        TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
        if (tf == null) {
            guiGraphics.fill(0, 0, width, height, -8355712);
            return;
        }

        double fightProgress = ChangedAddonVariables.nonNullOf(player).consciousnessFightProgress / FightToKeepConsciousness.getStruggleNeed();
        double loseProgress = Mth.lerp(partialTick, Math.max(0, tf.ageAsVariant - 1), tf.ageAsVariant) / FightToKeepConsciousness.getStruggleTime();

        int alpha = (int) (128 + 128 * (loseProgress - fightProgress));

        guiGraphics.fill(0, 0, width, height, alpha << 24 | tf.getTransfurColor().toInt());
    }

    @Override
    public void tick() {
        if (ChangedAddonVariables.ofOrDefault(player).FTKCminigameType == null) {
            minecraft.setScreen(null);
            return;
        }

        struggleProgressO = struggleProgress;
        if (cursor.distanceSquared(circle) <= INTERACTION_RADIUS_SQR) {
            increaseStruggle();
            return;
        }

        if (struggleProgress < 0) {
            struggleProgress = 0;
            return;
        }

        if (struggleProgress > 0) {
            struggleProgress = Math.max(0, struggleProgress - .1f);
        }
    }

    protected abstract void increaseStruggle();

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        cursor.add((float) mouseX - mouseLast.x, (float) mouseY - mouseLast.y);
        cursor.set(Mth.clamp(cursor.x, 5, width - 5), Mth.clamp(cursor.y, 5, height - 5));
        mouseLast.set((float) mouseX, (float) mouseY);
    }

    protected void randomizeCursorPos(float offsetX, float offsetY) {
        Random rand = new Random();
        float x = Mth.clamp(halfWidth + rand.nextFloat(-offsetX, offsetX), 5, width - 5);
        float y = Mth.clamp(halfHeight + rand.nextFloat(-offsetY, offsetY), 5, height - 5);
        mouseLast.set(x, y);
        cursor.set(x, y);

        Window window = minecraft.getWindow();
        float scale = (float) window.getGuiScale();
        GLFW.glfwSetCursorPos(window.getWindow(), x * scale, y * scale);
    }

    @Override
    public void removed() {
        GLFW.glfwSetInputMode(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, InputConstants.CURSOR_NORMAL);
    }
}

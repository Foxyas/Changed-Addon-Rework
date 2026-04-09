package net.foxyas.changedaddon.client.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;


@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PacifiedVignetteOverlay {
    private static final ResourceLocation VIGNETTE_LOCATION = ChangedAddonMod.textureLoc("textures/misc/alpha_vignette");
    private static final Color BORDER_COLOR = new Color(243, 146, 255);

    // Fade variables
    private static float vignetteFadeProgress = 0.0f;
    private static final float FADE_SPEED = 0.005f; // Adjust for faster/slower fade
    private static final float MAX_ALPHA = 0.5f;   // Your original 0.5f target alpha

    @SubscribeEvent
    public static void updateAlphaValue(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;

            // Determine if the effect SHOULD be visible
            boolean shouldVisible = ProcessTransfur.isPlayerTransfurred(player) && player.hasEffect(ChangedAddonMobEffects.PACIFIED.get());

            // Update fade progress based on state
            // If it should be visible, increase towards 1.0, otherwise decrease towards 0.0
            if (shouldVisible) {
                vignetteFadeProgress = Math.min(1.0f, vignetteFadeProgress + FADE_SPEED);
            } else {
                vignetteFadeProgress = Math.max(0.0f, vignetteFadeProgress - FADE_SPEED);
            }
        }
    }

    public static void renderPacifiedVignetteOverlay(ForgeGui forgeGui, GuiGraphics graphics, float partialTick, int width, int height) {

        // If progress is 0, we don't need to render anything
        if (vignetteFadeProgress <= 0.0f) {
            return;
        }

        forgeGui.setupOverlayRenderState(true, false);

        float r = BORDER_COLOR.getRed() / 255.0F;
        float g = BORDER_COLOR.getGreen() / 255.0F;
        float b = BORDER_COLOR.getBlue() / 255.0F;

        // Apply fade: Multiply the desired max alpha by the current fade progress
        // Use lerp for a smoother visual transition if preferred
        float dynamicAlpha = Mth.lerp(partialTick, vignetteFadeProgress - FADE_SPEED, vignetteFadeProgress) * MAX_ALPHA;
        dynamicAlpha = Mth.clamp(dynamicAlpha, 0.0f, MAX_ALPHA);

        graphics.setColor(r, g, b, dynamicAlpha);

        graphics.blit(VIGNETTE_LOCATION, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);

        // Reset state
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}
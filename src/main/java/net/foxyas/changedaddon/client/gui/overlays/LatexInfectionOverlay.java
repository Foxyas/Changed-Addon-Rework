package net.foxyas.changedaddon.client.gui.overlays;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.LatexInfection;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.client.gui.TransfurProgressOverlay.Position;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;

@OnlyIn(Dist.CLIENT)
public class LatexInfectionOverlay {
    private static final ResourceLocation INFECTION_DANGER_INDICATOR = ChangedAddonMod.resourceLoc("textures/screens/overlays/infection_danger.png");

    public static boolean getBlink(int delay, Player player) {
        int maxDelay = LatexInfection.getTickDelayForDifficulty(player);

        // Evita divisão por zero ou valores inconsistentes
        if (maxDelay <= 0) {
            maxDelay = 100;
        }

        // Calculamos o progresso de 0.0 (início) até 1.0 (perto de completar/máximo)
        float progress = (float) delay / (float) maxDelay;
        progress = Mth.clamp(progress, 0.0f, 1.0f);

        // Definimos os limites do intervalo em milissegundos
        int maxBlinkDelay = 500; // Início: pisca devagar (a cada 500ms)
        int minBlinkDelay = 100; // Fim: pisca no limite rápido (a cada 100ms)

        // Interpolação linear: quanto maior o progresso, menor o intervalo
        int blinkInterval = (int) Mth.lerp(progress, maxBlinkDelay, minBlinkDelay);

        return (System.currentTimeMillis() / blinkInterval) % 2 == 0;
    }

    public static void renderLatexInfectionDangerOverlay(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        //Logic part
        Player player = EntityUtil.playerOrNull(Minecraft.getInstance().getCameraEntity());
        if (player == null) return;

        ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.ofOrDefault(player);
        LatexInfection latexInfection = playerVariables.latexInfection;
        if (!latexInfection.isActive() || !latexInfection.isPlayerAffected(player)) {
            return;
        }

        float dangerLevel = (float) (ProcessTransfur.getPlayerTransfurProgress(player) / ProcessTransfur.getEntityTransfurTolerance(player));
        TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (dangerLevel <= 0.1F || variant != null) {
            return;
        }

        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        final Position position = Changed.config.client.transfurMeterPosition.get();

        int x = position.getX(screenWidth, player);
        int y = position.getY(screenHeight);

        int delay = latexInfection.getLatexInfectionTicksUntilDamage();
        int u = getBlink(delay, player) ? 16 : 0;
        int v = latexInfection.shouldStallTransfurProgress ? 16 : 0;

        // True Render Stuff
        gui.setupOverlayRenderState(true, false);
        int xOffset;
        if (!ChangedAddonClientConfiguration.RENDER_LATEX_INFECTION_ICONS_OUTSIDE.get()) {
            xOffset = -4;
            graphics.setColor(1.0f, 1.0f, 1.0f, 0.5f);
        } else {
            xOffset = position != Position.HOTBAR_LEFT ? 8 : -16;
        }

        int yOffset = 16;
        graphics.blit(INFECTION_DANGER_INDICATOR,
                x + xOffset,
                y + yOffset,
                u,
                v,
                16,
                16,
                32,
                32);

        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
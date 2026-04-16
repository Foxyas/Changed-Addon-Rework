package net.foxyas.changedaddon.client.gui.overlays;

import net.foxyas.changedaddon.init.ChangedAddonKeyMappings;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;

import java.awt.*;

public class StopCuddlingHint {

    public static final String ID = "stop_cuddling_hint";

    public static void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = forgeGui.getMinecraft().player;
        if (player == null || player.isDeadOrDying() || !player.isSleeping() || !ChangedAddonVariables.ofOrDefault(player).isCuddling) return;

        WidgetHelper.drawCenteredComp(guiGraphics, forgeGui.getFont(), Component.translatable("text.changed_addon.stop_cuddling", Component.keybind(ChangedAddonKeyMappings.CUDDLE_KEY.getName())), screenWidth / 2f, 30, -1, false);
    }
}

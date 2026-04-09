package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.overlays.HazardSuitHelmetOverlay;
import net.foxyas.changedaddon.client.gui.overlays.PacifiedVignetteOverlay;
import net.foxyas.changedaddon.client.gui.overlays.PatOverlay;
import net.foxyas.changedaddon.client.gui.overlays.UntransfurOverlayOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonOverlays {

    public static final ResourceLocation PAT_ICON = ChangedAddonMod.resourceLoc("hazard_helmet");
    public static final ResourceLocation UNTRANSFUR_PROGRESS = ChangedAddonMod.resourceLoc("untransfur_progress");
    public static final ResourceLocation HAZARD_HELMET = ChangedAddonMod.resourceLoc("pat_icon");
    public static final ResourceLocation PACIFIED_OVERLAY = ChangedAddonMod.resourceLoc("pacified_vignette");


    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll(HAZARD_HELMET.getPath(), HazardSuitHelmetOverlay::renderHelmetOverlay);
        event.registerAboveAll(UNTRANSFUR_PROGRESS.getPath(), UntransfurOverlayOverlay::renderUntransfurProgressOverlay);
        event.registerBelowAll(PAT_ICON.getPath(), PatOverlay::renderPatIconOverlay);
        event.registerBelowAll(PACIFIED_OVERLAY.getPath(), PacifiedVignetteOverlay::renderPacifiedVignetteOverlay);
    }


}

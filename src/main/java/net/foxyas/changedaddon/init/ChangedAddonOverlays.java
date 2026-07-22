package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.overlays.*;
import net.ltxprogrammer.changed.client.ChangedOverlays;
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
    public static final ResourceLocation INFECTION_DANGER = ChangedAddonMod.resourceLoc("infection_danger");


    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll(HAZARD_HELMET.getPath(), HazardSuitHelmetOverlay::renderHelmetOverlay);
        event.registerAboveAll(UNTRANSFUR_PROGRESS.getPath(), UntransfurOverlay::renderUntransfurProgressOverlay);
        event.registerBelowAll(PAT_ICON.getPath(), PatOverlay::renderPatIconOverlay);
        event.registerBelowAll(PACIFIED_OVERLAY.getPath(), PacifiedVignetteOverlay::renderPacifiedVignetteOverlay);
        event.registerAbove(ChangedOverlays.DANGER_OVERLAY, INFECTION_DANGER.getPath(), LatexInfectionOverlay::renderLatexInfectionDangerOverlay);
        event.registerAboveAll(StopCuddlingHint.ID, StopCuddlingHint::render);
    }


}

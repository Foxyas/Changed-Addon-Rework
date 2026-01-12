package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.overlays.HazardSuitHelmetOverlay;
import net.foxyas.changedaddon.client.gui.overlays.PatOverlay;
import net.foxyas.changedaddon.client.gui.overlays.UntransfurOverlayOverlay;
import net.ltxprogrammer.changed.client.ChangedOverlays;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = {Dist.CLIENT})
public class ChangedAddonOverlays {

    public static final IIngameOverlay HAZARD_HELMET = OverlayRegistry.registerOverlayBottom(ChangedAddonMod.resourceLocString("hazard_helmet"), HazardSuitHelmetOverlay::renderHelmetOverlay);
    public static final IIngameOverlay UNTRANSFUR_PROGRESS = OverlayRegistry.registerOverlayAbove(ChangedOverlays.DANGER_ELEMENT, ChangedAddonMod.resourceLocString("untransfur_progress"), UntransfurOverlayOverlay::renderUntransfurProgressOverlay);
    public static final IIngameOverlay PAT_ICON = OverlayRegistry.registerOverlayBottom(ChangedAddonMod.resourceLocString("pat_icon"), PatOverlay::renderPatIconOverlay);

}

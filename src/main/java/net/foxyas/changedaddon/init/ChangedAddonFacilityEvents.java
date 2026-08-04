package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityPieceEvent;
import net.minecraftforge.registries.DeferredRegister;

public class ChangedAddonFacilityEvents {

    public static final DeferredRegister<FacilityPieceEvent> REGISTRY =  ChangedRegistry.FACILITY_EVENTS.createDeferred(ChangedAddonMod.MODID);

}

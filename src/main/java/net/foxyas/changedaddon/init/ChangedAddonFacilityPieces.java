package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityPieceCollectionBuilder;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityRoomPiece;
import net.ltxprogrammer.changed.world.features.structures.facility.GatherFacilityPiecesEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonFacilityPieces {

    @SubscribeEvent
    public static void RegisterAddonFacilityPieces(GatherFacilityPiecesEvent event) {
        event.register(FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON, new FacilityRoomPiece(new ResourceLocation("changed_addon:exp009room"), new ResourceLocation("changed_addon:chests/experiment_009_loot_dna")));
        event.register(FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON, new FacilityRoomPiece(new ResourceLocation("changed_addon:exp10room"), new ResourceLocation("changed_addon:chests/experiment_10_loot_op")));
        event.register(FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON, new FacilityRoomPiece(new ResourceLocation("changed_addon:luminar_crystal_room"), new ResourceLocation("changed:chests/high_tier_lab")));
    }
}
package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.world.features.structures.FacilityPieces;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityPieceCollection;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityRoomPiece;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonFacilityPieces extends FacilityPieces {
    public static final FacilityPieceCollection Exp9FacilityRoom = FacilityPieces.ROOMS.register(new FacilityRoomPiece(new ResourceLocation("changed_addon:exp009room"), new ResourceLocation("changed_addon:chests/experiment_009_loot_dna")));
    public static final FacilityPieceCollection Exp10FacilityRoom = FacilityPieces.ROOMS.register(new FacilityRoomPiece(new ResourceLocation("changed_addon:exp10room"), new ResourceLocation("changed_addon:chests/experiment_10_loot_op")));
    public static final FacilityPieceCollection LuminarCrystalsFacilityRoom = FacilityPieces.ROOMS.register(new FacilityRoomPiece(new ResourceLocation("changed_addon:luminar_crystal_room"), new ResourceLocation("changed:chests/high_tier_lab")));
}
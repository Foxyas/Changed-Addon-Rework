package net.foxyas.changedaddon.init;

import net.ltxprogrammer.changed.world.features.structures.LootTables;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityCorridorSection;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityPieceCollectionBuilder;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityRoomPiece;
import net.ltxprogrammer.changed.world.features.structures.facility.GatherFacilityPiecesEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonFacilityPieces {

    @SubscribeEvent
    public static void RegisterAddonFacilityPieces(GatherFacilityPiecesEvent event) {
        switch (event.getPieceType()) {
            case ROOM -> registerAddonRooms(event.getBuilder());
            case CORRIDOR -> registerAddonCorridors(event.getBuilder());
        }
    }

    public static void registerAddonCorridors(FacilityPieceCollectionBuilder builder) {
        builder.register((int) (FacilityPieceCollectionBuilder.WEIGHT_COMMON * 1.5f),
                new FacilityCorridorSection(ResourceLocation.parse("changed_addon:facilities/facility_hallways/garden_containment"),
                        LootTables.HIGH_TIER_LAB));
        builder.register(FacilityPieceCollectionBuilder.WEIGHT_COMMON,
                new FacilityCorridorSection(ResourceLocation.parse("changed_addon:facilities/facility_hallways/dark_latex_plushy_hallway"),
                        LootTables.LOW_TIER_LAB));
        builder.register(9,
                new FacilityCorridorSection(ResourceLocation.parse("changed_addon:facilities/facility_hallways/closed_meteor_in_pool"),
                        LootTables.HIGH_TIER_LAB));
    }

    public static void registerAddonRooms(FacilityPieceCollectionBuilder builder) {
        builder.register(FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/exp009room"),
                                null))
                .register(FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/exp10room"),
                                ResourceLocation.parse("changed_addon:chests/experiment_10_loot_op")))
                .register(FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/luminar_crystal_room"),
                                ResourceLocation.parse("changed:chests/high_tier_lab")))
                .register(FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/luminarctic_leopards_cave"),
                                ResourceLocation.parse("changed:chests/high_tier_lab")))
                .register(FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/alpha_cave_snow_leopard"),
                                ResourceLocation.parse("changed:chests/high_tier_lab")))
                .register(FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/alpha_cave_tiger_shark"),
                                ResourceLocation.parse("changed:chests/high_tier_lab")));
//                .register(FacilityPieceCollectionBuilder.WEIGHT_COMMON,
//                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/closed_meteor"),
//                                ResourceLocation.parse("changed:chests/high_tier_lab")));
    }
}
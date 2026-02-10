package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.data.RegistryElementPredicate;
import net.ltxprogrammer.changed.init.ChangedFacilityZones;
import net.ltxprogrammer.changed.world.features.structures.LootTables;
import net.ltxprogrammer.changed.world.features.structures.facility.*;
import net.ltxprogrammer.changed.world.features.structures.facility.types.CorridorType;
import net.ltxprogrammer.changed.world.features.structures.facility.types.PieceType;
import net.ltxprogrammer.changed.world.features.structures.facility.types.RoomType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonFacilityPieces {

    @SubscribeEvent
    public static void RegisterAddonFacilityPieces(GatherFacilityPiecesEvent event) {
        PieceType<?> type = event.getPieceType();
        if (type instanceof RoomType) registerAddonRooms(event.getBuilder());
        if (type instanceof CorridorType) registerAddonCorridors(event.getBuilder());
    }

    public static void registerAddonCorridors(FacilityPieceCollectionBuilder builder) {
        Weight gardenContainmentSpawnWeight = Weight.of((int) (FacilityPieceCollectionBuilder.WEIGHT_COMMON * 1.5f));
        Weight closedMeteorInPoolSpawnWeight = Weight.of(9);

        FacilityCorridorSection gardenContainment = new FacilityCorridorSection(ResourceLocation.parse("changed_addon:facilities/facility_hallways/garden_containment"), Optional.of(LootTables.HIGH_TIER_LAB));
        FacilityCorridorSection darkLatexPlushyHallway = new FacilityCorridorSection(ResourceLocation.parse("changed_addon:facilities/facility_hallways/dark_latex_plushy_hallway"), Optional.of(LootTables.LOW_TIER_LAB));
        FacilityCorridorSection closedMeteorInPool = new FacilityCorridorSection(ResourceLocation.parse("changed_addon:facilities/facility_hallways/closed_meteor_in_pool"), Optional.of(LootTables.HIGH_TIER_LAB));

        ConfiguredFacilityPiece gardenContainmentFacilityPiece = new ConfiguredFacilityPiece(gardenContainment, gardenContainmentSpawnWeight, 1, 10, Set.of(ChangedFacilityZones.GREENHOUSE_ZONE.get(), ChangedFacilityZones.ENTRANCE_ZONE.get()), RegistryElementPredicate.forAll(ForgeRegistries.BIOMES));
        ConfiguredFacilityPiece darkLatexPlushyHallwayFacilityPiece = new ConfiguredFacilityPiece(darkLatexPlushyHallway, Weight.of(FacilityPieceCollectionBuilder.WEIGHT_COMMON), 0, 10, Set.of(ChangedFacilityZones.ENTRANCE_ZONE.get()), RegistryElementPredicate.forAll(ForgeRegistries.BIOMES));
        ConfiguredFacilityPiece closedMeteorInPoolFacilityPiece = new ConfiguredFacilityPiece(closedMeteorInPool, closedMeteorInPoolSpawnWeight, 0, 10, Set.of(ChangedFacilityZones.ENTRANCE_ZONE.get()), RegistryElementPredicate.forAll(ForgeRegistries.BIOMES));

        builder.register(gardenContainmentFacilityPiece.setName(ChangedAddonMod.resourceLoc("garden_containment")));
        builder.register(darkLatexPlushyHallwayFacilityPiece.setName(ChangedAddonMod.resourceLoc("dark_latex_plushy_hallway")));
        builder.register(closedMeteorInPoolFacilityPiece.setName(ChangedAddonMod.resourceLoc("closed_meteor_in_pool")));
    }

    public static void registerAddonRooms(FacilityPieceCollectionBuilder builder) {
        builder.register(ChangedAddonMod.resourceLoc("exp009room"), FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/exp009room"),
                                Optional.empty()))

                .register(ChangedAddonMod.resourceLoc("exp10room"), FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/exp10room"),
                                Optional.of(ResourceLocation.parse("changed_addon:chests/experiment_10_loot_op"))))

                .register(ChangedAddonMod.resourceLoc("luminar_crystal_room"), FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/luminar_crystal_room"),
                                Optional.of(ResourceLocation.parse("changed:chests/high_tier_lab"))))

                .register(ChangedAddonMod.resourceLoc("luminarctic_leopards_cave"), FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/luminarctic_leopards_cave"),
                                Optional.of(ResourceLocation.parse("changed:chests/high_tier_lab"))))

                .register(ChangedAddonMod.resourceLoc("alpha_cave_snow_leopard"), FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/alpha_cave_snow_leopard"),
                                Optional.of(ResourceLocation.parse("changed:chests/high_tier_lab"))))

                .register(ChangedAddonMod.resourceLoc("alpha_cave_tiger_shark"), FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON + 40,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/alpha_cave_tiger_shark"),
                                Optional.of(ResourceLocation.parse("changed:chests/high_tier_lab"))))

                .register(ChangedAddonMod.resourceLoc("archives_room"), FacilityPieceCollectionBuilder.WEIGHT_UNCOMMON,
                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/archives_room"),
                                Optional.empty()));

//                .register(FacilityPieceCollectionBuilder.WEIGHT_COMMON,
//                        new FacilityRoomPiece(ResourceLocation.parse("changed_addon:facilities/facility_rooms/closed_meteor"),
//                                ResourceLocation.parse("changed:chests/high_tier_lab")));
    }
}
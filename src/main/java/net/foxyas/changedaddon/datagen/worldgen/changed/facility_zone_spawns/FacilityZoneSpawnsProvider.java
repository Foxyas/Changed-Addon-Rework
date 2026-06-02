package net.foxyas.changedaddon.datagen.worldgen.changed.facility_zone_spawns;

import com.mojang.serialization.JsonOps;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.HashMap;

import static net.ltxprogrammer.changed.world.features.structures.facility.FacilityZoneEntities.ZoneEntitiesDefinition;

public class FacilityZoneSpawnsProvider extends JsonCodecProvider<ZoneEntitiesDefinition> {

    public FacilityZoneSpawnsProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modid, HashMap<ResourceLocation, ZoneEntitiesDefinition> entries) {
        super(
                output,
                existingFileHelper,
                modid,
                JsonOps.INSTANCE,
                PackType.SERVER_DATA,
                "facility_zone_spawns",
                ZoneEntitiesDefinition.CODEC,
                entries
        );
    }

    public static FacilityZoneSpawnsProvider getChangedAddonZoneSpawns(PackOutput output, ExistingFileHelper existingFileHelper) {
        FacilityZoneSpawnsProviderBuilder facilityPieceProviderBuilder = new FacilityZoneSpawnsProviderBuilder(output, existingFileHelper, ChangedAddonMod.MODID);
        return facilityPieceProviderBuilder.createFacilityPieceProvider();
    }
}
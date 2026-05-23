package net.foxyas.changedaddon.datagen.worldgen.changed.facility_zone_spawns;

import net.ltxprogrammer.changed.world.features.structures.facility.FacilityZoneEntities.ZoneEntitiesDefinition;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashMap;

public class FacilityZoneSpawnsProviderBuilder {
    private PackOutput output;
    private ExistingFileHelper existingFileHelper;
    private String modid;
    private HashMap<ResourceLocation, ZoneEntitiesDefinition> entries = new HashMap<>();

    public FacilityZoneSpawnsProviderBuilder(PackOutput output, ExistingFileHelper existingFileHelper, String modid) {
        this.output = output;
        this.existingFileHelper = existingFileHelper;
        this.modid = modid;
    }

    public FacilityZoneSpawnsProviderBuilder(PackOutput output, ExistingFileHelper existingFileHelper) {
        this.output = output;
        this.existingFileHelper = existingFileHelper;
    }

    public FacilityZoneSpawnsProviderBuilder setModid(String modid) {
        this.modid = modid;
        return this;
    }

    public FacilityZoneSpawnsProviderBuilder setEntries(HashMap<ResourceLocation, ZoneEntitiesDefinition> entries) {
        this.entries = entries;
        return this;
    }

    public FacilityZoneSpawnsProviderBuilder addEntry(ResourceLocation location, ZoneEntitiesDefinition facilityPiece) {
        this.entries.put(location, facilityPiece);
        return this;
    }

    public FacilityZoneSpawnsProvider createFacilityPieceProvider() {
        return new FacilityZoneSpawnsProvider(output, existingFileHelper, modid, entries);
    }
}
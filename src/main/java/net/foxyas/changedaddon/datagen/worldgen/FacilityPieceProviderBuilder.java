package net.foxyas.changedaddon.datagen.worldgen;

import net.ltxprogrammer.changed.world.features.structures.facility.ConfiguredFacilityPiece;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Map;

public class FacilityPieceProviderBuilder {
    private PackOutput output;
    private ExistingFileHelper existingFileHelper;
    private String modid;
    private HashMap<ResourceLocation, ConfiguredFacilityPiece> entries = new HashMap<>();

    public FacilityPieceProviderBuilder(PackOutput output, ExistingFileHelper existingFileHelper, String modid) {
        this.output = output;
        this.existingFileHelper = existingFileHelper;
        this.modid = modid;
    }

    public FacilityPieceProviderBuilder(PackOutput output, ExistingFileHelper existingFileHelper) {
        this.output = output;
        this.existingFileHelper = existingFileHelper;
    }

    public FacilityPieceProviderBuilder setModid(String modid) {
        this.modid = modid;
        return this;
    }

    public FacilityPieceProviderBuilder setEntries(HashMap<ResourceLocation, ConfiguredFacilityPiece> entries) {
        this.entries = entries;
        return this;
    }

    public FacilityPieceProviderBuilder addEntry(ResourceLocation location, ConfiguredFacilityPiece facilityPiece) {
        this.entries.put(location, facilityPiece);
        return this;
    }

    public FacilityPieceProvider createFacilityPieceProvider() {
        return new FacilityPieceProvider(output, existingFileHelper, modid, entries);
    }
}
package net.foxyas.changedaddon.datagen.worldgen.changed.facility;

import com.mojang.serialization.JsonOps;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.world.features.structures.facility.ConfiguredFacilityPiece;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.Map;

public class FacilityPieceProvider extends JsonCodecProvider<ConfiguredFacilityPiece> {

    public FacilityPieceProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modid, Map<ResourceLocation, ConfiguredFacilityPiece> entries) {
        super(
                output,
                existingFileHelper,
                modid,
                JsonOps.INSTANCE,
                PackType.SERVER_DATA,
                "worldgen/changed/facility",
                ConfiguredFacilityPiece.CODEC,
                entries
        );
    }

    public static FacilityPieceProvider getChangedAddonFacilitiesPieces(PackOutput output, ExistingFileHelper existingFileHelper) {
        FacilityPieceProviderBuilder facilityPieceProviderBuilder = new FacilityPieceProviderBuilder(output, existingFileHelper, ChangedAddonMod.MODID);

        return facilityPieceProviderBuilder.createFacilityPieceProvider();
    }
}
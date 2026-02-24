package net.foxyas.changedaddon.datagen.worldgen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.data.RegistryElementPredicate;
import net.ltxprogrammer.changed.init.ChangedFacilityZones;
import net.ltxprogrammer.changed.world.features.structures.LootTables;
import net.ltxprogrammer.changed.world.features.structures.facility.ConfiguredFacilityPiece;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityCorridorSection;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityPieceCollectionBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.random.Weight;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FacilityPieceProvider extends JsonCodecProvider<ConfiguredFacilityPiece> {

    public FacilityPieceProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modid, Map<ResourceLocation, ConfiguredFacilityPiece> entries) {
        super(
                output,
                existingFileHelper,
                modid,
                JsonOps.INSTANCE, // DynamicOps para converter o Codec em JsonElement
                PackType.SERVER_DATA,
                "worldgen/changed/facility", // O diretório que seu FacilityPieces.java lê
                ConfiguredFacilityPiece.CODEC, // O codec que você definiu na classe
                entries
        );
    }

    public static FacilityPieceProvider getChangedAddonFacilitiesPieces(PackOutput output, ExistingFileHelper existingFileHelper) {
        FacilityPieceProviderBuilder facilityPieceProviderBuilder = new FacilityPieceProviderBuilder(output, existingFileHelper, ChangedAddonMod.MODID);

        return facilityPieceProviderBuilder.createFacilityPieceProvider();
    }
}
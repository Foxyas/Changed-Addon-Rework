package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.datagen.ability_tree.AbilityTreeProviderImpl;
import net.foxyas.changedaddon.datagen.compatibility.ModTrimMapsProvider;
import net.foxyas.changedaddon.datagen.lang.ENLanguageProvider;
import net.foxyas.changedaddon.datagen.lang.HULanguageProvider;
import net.foxyas.changedaddon.datagen.patchouli.ENPatchouliBookProvider;
import net.foxyas.changedaddon.datagen.patchouli.RUPatchouliBookProvider;
import net.foxyas.changedaddon.datagen.worldgen.changed.facility.FacilityPieceProvider;
import net.foxyas.changedaddon.datagen.worldgen.changed.facility_zone_spawns.FacilityZoneSpawnsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatherData {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        BlockTagsProvider blocks = new BlockTagsProvider(packOutput, lookupProvider, helper);
        generator.addProvider(true, blocks);
        generator.addProvider(true, new ItemTagsProvider(generator, lookupProvider, blocks.contentsGetter(), existingFileHelper));
        generator.addProvider(true, new FluidTagsProvider(packOutput, lookupProvider, helper));

        generator.addProvider(true, new EntityTypeTagsProvider(packOutput, lookupProvider, helper));
        generator.addProvider(true, new TFTagsProvider(packOutput, lookupProvider, helper));
        generator.addProvider(true, new CAPaintingVariantTagsProvider(packOutput, lookupProvider, helper));
        generator.addProvider(true, new AccessoryEntityProvider(generator));
        generator.addProvider(true, new AbilityTreeProviderImpl(packOutput));
        generator.addProvider(true, new GameEventTagsProvider(packOutput, lookupProvider, existingFileHelper));

        CompletableFuture<HolderLookup.Provider> lookup0 =
                generator.addProvider(event.includeServer(), new DatapackEntriesProvider(packOutput, lookupProvider)).getRegistryProvider();
        generator.addProvider(event.includeServer(), new BiomeTagProvider(packOutput, lookup0, helper));
        generator.addProvider(event.includeServer(), new DamageTypeTagProvider(packOutput, lookup0, helper));

        generator.addProvider(event.includeServer(), FacilityZoneSpawnsProvider.getChangedAddonZoneSpawns(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), FacilityPieceProvider.getChangedAddonFacilitiesPieces(packOutput, existingFileHelper));

        generator.addProvider(true, new ModRecipeProvider(packOutput));

        generator.addProvider(true, new BlockModelProvider(packOutput, helper));
        generator.addProvider(true, new BlockStateProvider(packOutput, helper));
        generator.addProvider(true, new LatexCoverStateProvider(packOutput, helper, ChangedAddonMod.MODID));
        generator.addProvider(true, new ItemModelProvider(packOutput, helper));
        generator.addProvider(true, new ModAdvancementProvider(packOutput, lookup0, helper));
        generator.addProvider(true, new LootTableProvider(packOutput));

        generator.addProvider(true, new ModTrimMapsProvider(packOutput));
        generator.addProvider(true, new AtlasProvider(packOutput, helper));
        generator.addProvider(true, new ENLanguageProvider(packOutput));
        generator.addProvider(true, new HULanguageProvider(packOutput));

        generator.addProvider(true, new ENPatchouliBookProvider(packOutput));
        generator.addProvider(true, new RUPatchouliBookProvider(packOutput));
//        generator.addProvider(true, new ModAnimationAssociationsProvider(packOutput));
    }
}

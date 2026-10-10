package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.bestiary.BestiaryEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonBestiaryEntries {

    public static final ResourceKey<Registry<BestiaryEntry>> BESTIARY_ENTRIES_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("bestiary_entries"));

    public static Registry<BestiaryEntry> registry(Level level) {
        return level.registryAccess().registryOrThrow(BESTIARY_ENTRIES_KEY);
    }

    public static ResourceKey<BestiaryEntry> modKey(String id) {
        return ResourceKey.create(ChangedAddonBestiaryEntries.BESTIARY_ENTRIES_KEY, ChangedAddonMod.resourceLoc(id));
    }

    public static ResourceKey<BestiaryEntry> key(ResourceLocation id) {
        return ResourceKey.create(ChangedAddonBestiaryEntries.BESTIARY_ENTRIES_KEY, id);
    }

    @SubscribeEvent
    public static void onCreateDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(BESTIARY_ENTRIES_KEY, BestiaryEntry.CODEC, BestiaryEntry.CODEC);//TODO add same codec as networkCodec if sync needed
    }
}
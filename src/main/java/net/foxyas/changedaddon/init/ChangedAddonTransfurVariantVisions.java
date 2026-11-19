package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.variantsExtraStats.visions.TransfurVariantVision;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DataPackRegistryEvent;

public class ChangedAddonTransfurVariantVisions {
    
    public static final ResourceKey<Registry<TransfurVariantVision>> TRANSFUR_VARIANT_VISION_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("transfur_variant/visions"));

    public static Registry<TransfurVariantVision> registry(Level level) {
        return level.registryAccess().registryOrThrow(TRANSFUR_VARIANT_VISION_KEY);
    }

    public static ResourceKey<TransfurVariantVision> modKey(String id) {
        return ResourceKey.create(TRANSFUR_VARIANT_VISION_KEY, ChangedAddonMod.resourceLoc(id));
    }

    public static ResourceKey<TransfurVariantVision> key(ResourceLocation id) {
        return ResourceKey.create(TRANSFUR_VARIANT_VISION_KEY, id);
    }

    @SubscribeEvent
    public static void onCreateDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(TRANSFUR_VARIANT_VISION_KEY, TransfurVariantVision.CODEC, TransfurVariantVision.CODEC);//TODO add same codec as networkCodec if sync needed
    }
}

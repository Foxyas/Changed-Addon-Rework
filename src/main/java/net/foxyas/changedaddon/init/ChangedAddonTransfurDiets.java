package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonTransfurDiets {

    public static final ResourceKey<Registry<TransfurVariantDiet>> TRANSFUR_VARIANT_DIET_KEY =
            ResourceKey.createRegistryKey(ChangedAddonMod.resourceLoc("transfur_variant/diets"));

    public static Registry<TransfurVariantDiet> registry(Level level) {
        return level.registryAccess().registryOrThrow(TRANSFUR_VARIANT_DIET_KEY);
    }

    @SubscribeEvent
    public static void onCreateDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(TRANSFUR_VARIANT_DIET_KEY, TransfurVariantDiet.CODEC);//TODO add same codec as networkCodec if sync needed
    }
}
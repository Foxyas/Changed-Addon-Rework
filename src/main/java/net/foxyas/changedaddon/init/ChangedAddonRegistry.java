package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonRegistry {

    // 1. Define the ResourceKey for the custom registry
    public static final ResourceKey<Registry<TransfurVariantDiet>> TRANSFUR_VARIANT_DIETS_KEY =
            ResourceKey.createRegistryKey(ChangedAddonMod.resourceLoc("transfur_variant/diets"));

    // 2. Create the DeferredRegister using the key
    public static final DeferredRegister<TransfurVariantDiet> TRANSFUR_VARIANT_DIET_DEFERRED_REGISTER =
            DeferredRegister.create(TRANSFUR_VARIANT_DIETS_KEY, ChangedAddonMod.MODID);

    // 3. Supplier to hold the registered IForgeRegistry instance
    public static Supplier<IForgeRegistry<TransfurVariantDiet>> TRANSFUR_VARIANT_DIET_REGISTRY;

    /**
     * Call this inside your main mod class constructor to attach the DeferredRegister to the mod event bus.
     */
    public static void register(IEventBus bus) {
        TRANSFUR_VARIANT_DIET_DEFERRED_REGISTER.register(bus);
    }

    /**
     * Handles creating and registering the custom Forge Registry during mod initialization.
     */
    @SubscribeEvent
    public static void onCreateRegistries(NewRegistryEvent event) {
        TRANSFUR_VARIANT_DIET_REGISTRY = event.create(
                new RegistryBuilder<TransfurVariantDiet>()
                        .setName(TRANSFUR_VARIANT_DIETS_KEY.location())
        );
    }
}
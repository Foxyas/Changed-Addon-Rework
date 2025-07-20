package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.world.features.processors.OffSetSpawnProcessor;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonProcessors {

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS =
            DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, ChangedAddonMod.MODID);

    public static final RegistryObject<StructureProcessorType<OffSetSpawnProcessor>> OFFSET_SPAWN =
            PROCESSORS.register("offset_spawn", () -> () -> OffSetSpawnProcessor.CODEC);

    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        PROCESSORS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

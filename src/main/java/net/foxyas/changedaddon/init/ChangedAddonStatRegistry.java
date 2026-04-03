package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonStatRegistry {

    public static final DeferredRegister<ResourceLocation> STATS = DeferredRegister.create(Registries.CUSTOM_STAT, ChangedAddonMod.MODID);

    public static final RegistryObject<ResourceLocation> PATS_GIVEN = register("pats_given");
    public static final RegistryObject<ResourceLocation> PATS_RECEIVED = register("pats_received");
    public static final RegistryObject<ResourceLocation> ENTITY_ASSIMILATED = register("entity_assimilated");
    public static final RegistryObject<ResourceLocation> ENTITY_TRANSFURRED = register("entity_transfurred");

    private static RegistryObject<ResourceLocation> register(String name) {
        return STATS.register(name, () -> ChangedAddonMod.resourceLoc(name));
    }

    @SubscribeEvent
    public static void onRegistered(FMLCommonSetupEvent event) {
        STATS.getEntries().forEach(stat -> Stats.CUSTOM.get(stat.get()));
    }
}

package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.configuration.ChangedAddonCommonConfiguration;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonConfigs {
    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        event.enqueueWork(() -> {
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ChangedAddonServerConfiguration.SPEC, "changed_addon-server.toml");
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ChangedAddonClientConfiguration.SPEC, "changed_addon-client.toml");
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ChangedAddonCommonConfiguration.SPEC, "changed_addon-common.toml");
        });
    }
}

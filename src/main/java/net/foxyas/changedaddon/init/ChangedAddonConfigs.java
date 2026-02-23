package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.configuration.ChangedAddonCommonConfiguration;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonConfigs {

    public static void register(ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.SERVER, ChangedAddonServerConfiguration.SPEC, "changed_addon-server.toml");
        context.registerConfig(ModConfig.Type.CLIENT, ChangedAddonClientConfiguration.SPEC, "changed_addon-client.toml");
        context.registerConfig(ModConfig.Type.COMMON, ChangedAddonCommonConfiguration.SPEC, "changed_addon-common.toml");
    }
}

package net.foxyas.changedaddon.registers;

import net.foxyas.changedaddon.process.variantsExtraStats.visions.TransfurVisionReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class ModReloadEventHandler {
    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new TransfurVisionReloadListener());
    }
}

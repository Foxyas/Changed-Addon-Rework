package net.foxyas.changedaddon.procedures.blocksHandle;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DispenserExpansion {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(BoneMealExpansion.BoneMealDispenserHandler::registerDispenserBehavior);
        event.enqueueWork(BoneMealExpansion.GooApplyDispenserHandler::registerDispenserBehavior);
    }
}
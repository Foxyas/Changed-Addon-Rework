package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.client.gui.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonScreens {
    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ChangedAddonMenus.FOXYASGUI, FoxyasguiScreen::new);
            MenuScreens.register(ChangedAddonMenus.GENERATORGUI, GeneratorguiScreen::new);
            MenuScreens.register(ChangedAddonMenus.CATALYZERGUI, CatalyzerguiScreen::new);
            MenuScreens.register(ChangedAddonMenus.UNIFUSERGUI, UnifuserguiScreen::new);
            MenuScreens.register(ChangedAddonMenus.FIGHT_TOKEEPCONSCIOUSNESSMINIGAME, FightToKeepConsciousnessMinigameScreen::new);
            MenuScreens.register(ChangedAddonMenus.FOXYAS_GUI_2, FoxyasGui2Screen::new);
            MenuScreens.register(ChangedAddonMenus.TRANSFUR_SOUNDS_GUI, TransfurSoundsGuiScreen::new);
            MenuScreens.register(ChangedAddonMenus.TRANSFUR_TOTEM_GUI, TransfurTotemGuiScreen::new);
            MenuScreens.register(ChangedAddonMenus.INFORMANT_GUI, InformantGuiScreen::new);
        });
    }
}

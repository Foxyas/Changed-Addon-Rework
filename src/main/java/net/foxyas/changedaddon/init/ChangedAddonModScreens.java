
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.client.gui.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(ChangedAddonModMenus.FOXYASGUI, FoxyasguiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.GENERATORGUI, GeneratorguiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.CATLYZERGUI, CatlyzerguiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.UNIFUSERGUI, UnifuserguiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.FIGHT_TOKEEPCONSCIOUSNESSMINIGAME, FightToKeepConsciousnessMinigameScreen::new);
			MenuScreens.register(ChangedAddonModMenus.FOXYAS_GUI_2, FoxyasGui2Screen::new);
			MenuScreens.register(ChangedAddonModMenus.TRANFUR_SOUNDS_GUI, TranfurSoundsGuiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.TRANSFUR_TOTEM_GUI, TransfurTotemGuiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.INFORMANT_GUI, InformantGuiScreen::new);
		});
	}
}

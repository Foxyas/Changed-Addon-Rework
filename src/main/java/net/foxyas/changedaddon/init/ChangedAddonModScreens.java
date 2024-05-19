
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

import net.foxyas.changedaddon.client.gui.UnifuserguiScreen;
import net.foxyas.changedaddon.client.gui.TransfurTotemGuiScreen;
import net.foxyas.changedaddon.client.gui.TranfurSoundsGuiScreen;
import net.foxyas.changedaddon.client.gui.PagesecretScreen;
import net.foxyas.changedaddon.client.gui.InformantGuiScreen;
import net.foxyas.changedaddon.client.gui.GrabclickguiScreen;
import net.foxyas.changedaddon.client.gui.GrabRadialMenugrabScreen;
import net.foxyas.changedaddon.client.gui.GrabRadialMenuScreen;
import net.foxyas.changedaddon.client.gui.GeneratorguiScreen;
import net.foxyas.changedaddon.client.gui.FriendlyTransfurGuiScreen;
import net.foxyas.changedaddon.client.gui.FoxyasguiScreen;
import net.foxyas.changedaddon.client.gui.FoxyasGui2Screen;
import net.foxyas.changedaddon.client.gui.FightTokeepconsciousnessminigameScreen;
import net.foxyas.changedaddon.client.gui.CatlyzerguiScreen;
import net.foxyas.changedaddon.client.gui.Bookrecipepage9Screen;
import net.foxyas.changedaddon.client.gui.Bookrecipepage8Screen;
import net.foxyas.changedaddon.client.gui.Bookrecipepage7Screen;
import net.foxyas.changedaddon.client.gui.Bookrecipepage6Screen;
import net.foxyas.changedaddon.client.gui.Bookrecipepage10Screen;
import net.foxyas.changedaddon.client.gui.Bookpagenumber5Screen;
import net.foxyas.changedaddon.client.gui.Bookpagenumber4Screen;
import net.foxyas.changedaddon.client.gui.Bookpagenumber3Screen;
import net.foxyas.changedaddon.client.gui.BookRecipePage11Screen;
import net.foxyas.changedaddon.client.gui.BookPagenumber7Screen;
import net.foxyas.changedaddon.client.gui.BookPagenumber6Screen;
import net.foxyas.changedaddon.client.gui.BookPagenumber2Screen;
import net.foxyas.changedaddon.client.gui.BookPagenumber1Screen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(ChangedAddonModMenus.GRABCLICKGUI, GrabclickguiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOK_PAGENUMBER_1, BookPagenumber1Screen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOK_PAGENUMBER_2, BookPagenumber2Screen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOKPAGENUMBER_3, Bookpagenumber3Screen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOKPAGENUMBER_4, Bookpagenumber4Screen::new);
			MenuScreens.register(ChangedAddonModMenus.PAGESECRET, PagesecretScreen::new);
			MenuScreens.register(ChangedAddonModMenus.FOXYASGUI, FoxyasguiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.GENERATORGUI, GeneratorguiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.CATLYZERGUI, CatlyzerguiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.UNIFUSERGUI, UnifuserguiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOKPAGENUMBER_5, Bookpagenumber5Screen::new);
			MenuScreens.register(ChangedAddonModMenus.FIGHT_TOKEEPCONSCIOUSNESSMINIGAME, FightTokeepconsciousnessminigameScreen::new);
			MenuScreens.register(ChangedAddonModMenus.GRAB_RADIAL_MENU, GrabRadialMenuScreen::new);
			MenuScreens.register(ChangedAddonModMenus.GRAB_RADIAL_MENUGRAB, GrabRadialMenugrabScreen::new);
			MenuScreens.register(ChangedAddonModMenus.FOXYAS_GUI_2, FoxyasGui2Screen::new);
			MenuScreens.register(ChangedAddonModMenus.FRIENDLY_TRANSFUR_GUI, FriendlyTransfurGuiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.TRANFUR_SOUNDS_GUI, TranfurSoundsGuiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOK_PAGENUMBER_6, BookPagenumber6Screen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOK_PAGENUMBER_7, BookPagenumber7Screen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOKRECIPEPAGE_6, Bookrecipepage6Screen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOKRECIPEPAGE_7, Bookrecipepage7Screen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOKRECIPEPAGE_8, Bookrecipepage8Screen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOKRECIPEPAGE_9, Bookrecipepage9Screen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOKRECIPEPAGE_10, Bookrecipepage10Screen::new);
			MenuScreens.register(ChangedAddonModMenus.TRANSFUR_TOTEM_GUI, TransfurTotemGuiScreen::new);
			MenuScreens.register(ChangedAddonModMenus.BOOK_RECIPE_PAGE_11, BookRecipePage11Screen::new);
			MenuScreens.register(ChangedAddonModMenus.INFORMANT_GUI, InformantGuiScreen::new);
		});
	}
}

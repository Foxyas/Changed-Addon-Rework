
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.AbstractContainerMenu;

import net.foxyas.changedaddon.world.inventory.UnifuserguiMenu;
import net.foxyas.changedaddon.world.inventory.TranfurSoundsGuiMenu;
import net.foxyas.changedaddon.world.inventory.PagesecretMenu;
import net.foxyas.changedaddon.world.inventory.GrabclickguiMenu;
import net.foxyas.changedaddon.world.inventory.GrabRadialMenugrabMenu;
import net.foxyas.changedaddon.world.inventory.GrabRadialMenuMenu;
import net.foxyas.changedaddon.world.inventory.GeneratorguiMenu;
import net.foxyas.changedaddon.world.inventory.FriendlyTransfurGuiMenu;
import net.foxyas.changedaddon.world.inventory.FoxyasguiMenu;
import net.foxyas.changedaddon.world.inventory.FoxyasGui2Menu;
import net.foxyas.changedaddon.world.inventory.FightTokeepconsciousnessminigameMenu;
import net.foxyas.changedaddon.world.inventory.CatlyzerguiMenu;
import net.foxyas.changedaddon.world.inventory.Bookpagenumber5Menu;
import net.foxyas.changedaddon.world.inventory.Bookpagenumber4Menu;
import net.foxyas.changedaddon.world.inventory.Bookpagenumber3Menu;
import net.foxyas.changedaddon.world.inventory.BookPagenumber2Menu;
import net.foxyas.changedaddon.world.inventory.BookPagenumber1Menu;

import java.util.List;
import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonModMenus {
	private static final List<MenuType<?>> REGISTRY = new ArrayList<>();
	public static final MenuType<GrabclickguiMenu> GRABCLICKGUI = register("grabclickgui", (id, inv, extraData) -> new GrabclickguiMenu(id, inv, extraData));
	public static final MenuType<BookPagenumber1Menu> BOOK_PAGENUMBER_1 = register("book_pagenumber_1", (id, inv, extraData) -> new BookPagenumber1Menu(id, inv, extraData));
	public static final MenuType<BookPagenumber2Menu> BOOK_PAGENUMBER_2 = register("book_pagenumber_2", (id, inv, extraData) -> new BookPagenumber2Menu(id, inv, extraData));
	public static final MenuType<Bookpagenumber3Menu> BOOKPAGENUMBER_3 = register("bookpagenumber_3", (id, inv, extraData) -> new Bookpagenumber3Menu(id, inv, extraData));
	public static final MenuType<Bookpagenumber4Menu> BOOKPAGENUMBER_4 = register("bookpagenumber_4", (id, inv, extraData) -> new Bookpagenumber4Menu(id, inv, extraData));
	public static final MenuType<PagesecretMenu> PAGESECRET = register("pagesecret", (id, inv, extraData) -> new PagesecretMenu(id, inv, extraData));
	public static final MenuType<FoxyasguiMenu> FOXYASGUI = register("foxyasgui", (id, inv, extraData) -> new FoxyasguiMenu(id, inv, extraData));
	public static final MenuType<GeneratorguiMenu> GENERATORGUI = register("generatorgui", (id, inv, extraData) -> new GeneratorguiMenu(id, inv, extraData));
	public static final MenuType<CatlyzerguiMenu> CATLYZERGUI = register("catlyzergui", (id, inv, extraData) -> new CatlyzerguiMenu(id, inv, extraData));
	public static final MenuType<UnifuserguiMenu> UNIFUSERGUI = register("unifusergui", (id, inv, extraData) -> new UnifuserguiMenu(id, inv, extraData));
	public static final MenuType<Bookpagenumber5Menu> BOOKPAGENUMBER_5 = register("bookpagenumber_5", (id, inv, extraData) -> new Bookpagenumber5Menu(id, inv, extraData));
	public static final MenuType<FightTokeepconsciousnessminigameMenu> FIGHT_TOKEEPCONSCIOUSNESSMINIGAME = register("fight_tokeepconsciousnessminigame", (id, inv, extraData) -> new FightTokeepconsciousnessminigameMenu(id, inv, extraData));
	public static final MenuType<GrabRadialMenuMenu> GRAB_RADIAL_MENU = register("grab_radial_menu", (id, inv, extraData) -> new GrabRadialMenuMenu(id, inv, extraData));
	public static final MenuType<GrabRadialMenugrabMenu> GRAB_RADIAL_MENUGRAB = register("grab_radial_menugrab", (id, inv, extraData) -> new GrabRadialMenugrabMenu(id, inv, extraData));
	public static final MenuType<FoxyasGui2Menu> FOXYAS_GUI_2 = register("foxyas_gui_2", (id, inv, extraData) -> new FoxyasGui2Menu(id, inv, extraData));
	public static final MenuType<FriendlyTransfurGuiMenu> FRIENDLY_TRANSFUR_GUI = register("friendly_transfur_gui", (id, inv, extraData) -> new FriendlyTransfurGuiMenu(id, inv, extraData));
	public static final MenuType<TranfurSoundsGuiMenu> TRANFUR_SOUNDS_GUI = register("tranfur_sounds_gui", (id, inv, extraData) -> new TranfurSoundsGuiMenu(id, inv, extraData));

	private static <T extends AbstractContainerMenu> MenuType<T> register(String registryname, IContainerFactory<T> containerFactory) {
		MenuType<T> menuType = new MenuType<T>(containerFactory);
		menuType.setRegistryName(registryname);
		REGISTRY.add(menuType);
		return menuType;
	}

	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
		event.getRegistry().registerAll(REGISTRY.toArray(new MenuType[0]));
	}
}

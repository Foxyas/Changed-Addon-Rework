
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.world.inventory.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.IContainerFactory;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonModMenus {
	private static final List<MenuType<?>> REGISTRY = new ArrayList<>();
	public static final MenuType<FoxyasGuiMenu> FOXYASGUI = register("foxyasgui", (id, inv, extraData) -> new FoxyasGuiMenu(id, inv, extraData));
	public static final MenuType<GeneratorGuiMenu> GENERATORGUI = register("generatorgui", (id, inv, extraData) -> new GeneratorGuiMenu(id, inv, extraData));
	public static final MenuType<CatlyzerGuiMenu> CATLYZERGUI = register("catlyzergui", (id, inv, extraData) -> new CatlyzerGuiMenu(id, inv, extraData));
	public static final MenuType<UnifuserGuiMenu> UNIFUSERGUI = register("unifusergui", (id, inv, extraData) -> new UnifuserGuiMenu(id, inv, extraData));
	public static final MenuType<FightToKeepConsciousnessMinigameMenu> FIGHT_TOKEEPCONSCIOUSNESSMINIGAME = register("fight_tokeepconsciousnessminigame", (id, inv, extraData) -> new FightToKeepConsciousnessMinigameMenu(id, inv, extraData));
	public static final MenuType<FoxyasGui2Menu> FOXYAS_GUI_2 = register("foxyas_gui_2", (id, inv, extraData) -> new FoxyasGui2Menu(id, inv, extraData));
	public static final MenuType<TranfurSoundsGuiMenu> TRANFUR_SOUNDS_GUI = register("tranfur_sounds_gui", (id, inv, extraData) -> new TranfurSoundsGuiMenu(id, inv, extraData));
	public static final MenuType<TransfurTotemGuiMenu> TRANSFUR_TOTEM_GUI = register("transfur_totem_gui", (id, inv, extraData) -> new TransfurTotemGuiMenu(id, inv, extraData));
	public static final MenuType<InformantGuiMenu> INFORMANT_GUI = register("informant_gui", (id, inv, extraData) -> new InformantGuiMenu(id, inv, extraData));

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

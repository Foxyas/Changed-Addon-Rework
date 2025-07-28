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
public class ChangedAddonMenus {
    private static final List<MenuType<?>> REGISTRY = new ArrayList<>();

    public static final MenuType<FoxyasGuiMenu> FOXYAS_GUI = register("foxyas_gui", FoxyasGuiMenu::new);
    public static final MenuType<GeneratorGuiMenu> GENERATORGUI = register("generator_gui", GeneratorGuiMenu::new);
    public static final MenuType<CatalyzerGuiMenu> CATALYZER_GUI = register("catalyzer_gui", CatalyzerGuiMenu::new);
    public static final MenuType<UnifuserGuiMenu> UNIFUSER_GUI = register("unifuser_gui", UnifuserGuiMenu::new);
    public static final MenuType<FightToKeepConsciousnessMinigameMenu> FIGHT_TO_KEEP_CONSCIOUSNESS_MINIGAME = register("fight_to_keep_consciousness_minigame", FightToKeepConsciousnessMinigameMenu::new);
    public static final MenuType<FoxyasGui2Menu> FOXYAS_GUI_2 = register("foxyas_gui_2", FoxyasGui2Menu::new);
    public static final MenuType<TransfurSoundsGuiMenu> TRANSFUR_SOUNDS_GUI = register("transfur_sounds_gui", TransfurSoundsGuiMenu::new);
    public static final MenuType<TransfurTotemGuiMenu> TRANSFUR_TOTEM_GUI = register("transfur_totem_gui", TransfurTotemGuiMenu::new);
    public static final MenuType<InformantGuiMenu> INFORMANT_GUI = register("informant_gui", InformantGuiMenu::new);

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

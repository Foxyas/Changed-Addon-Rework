package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.*;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.foxyas.changedaddon.item.tooltip.ClientTransfurTotemTooltipComponent;
import net.foxyas.changedaddon.item.tooltip.TransfurTotemTooltipComponent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterNamedRenderTypesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientMod {

    public static boolean changedAdditionsLoaded = false;
    public static boolean changedAdditionsWarningScreenShowed = false;

    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Sheets.addWoodType(ChangedAddonBlocks.LUMINARA_WOOD_TYPE);

            MenuScreens.register(ChangedAddonMenus.CATALYZER_MENU.get(), CatalyzerGuiScreen::new);
            MenuScreens.register(ChangedAddonMenus.UNIFUSER_MENU.get(), UnifuserGuiScreen::new);
            MenuScreens.register(ChangedAddonMenus.INFORMANT_MENU.get(), InformantGuiScreen::new);
            MenuScreens.register(ChangedAddonMenus.PROTOTYPE_MENU.get(), PrototypeMenuScreen::new);
            MenuScreens.register(ChangedAddonMenus.MERCHANT_MENU.get(), CustomMerchantScreen::new);
            MenuScreens.register(ChangedAddonMenus.FOXYAS_INVENTORY_MENU.get(), FoxyasInventoryMenuScreen::new);
            MenuScreens.register(ChangedAddonMenus.TIMED_KEYPAD_TIMER.get(), TimedKeypadTimerScreen::new);
            MenuScreens.register(ChangedAddonMenus.TAMED_LATEX.get(), TamedLatexScreen::new);
            MenuScreens.register(ChangedAddonMenus.TAMED_LATEX_INVENTORY.get(), TamedLatexInventoryScreen::new);
        });
    }

    @SubscribeEvent
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
//        event.register(
//                "dynamic_light_loader",
//                DynamicLightModelLoader.INSTANCE
//        );
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
//        event.registerReloadListener(new BlocksLightEmissionRegistry());
    }

    @SubscribeEvent
    public static void showWarningScreen(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("changed_additions")) {
            changedAdditionsLoaded = true;
            changedAdditionsWarningScreenShowed = false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerToolTips(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(TransfurTotemTooltipComponent.class, ClientTransfurTotemTooltipComponent::new);
    }

    @SubscribeEvent
    public static void onRegisterModelRenderTypes(RegisterNamedRenderTypesEvent event) {
        // Use this as eyes but with fewer pipeline bugs
        event.register("energy", RenderType.cutout(), RenderType.energySwirl(InventoryMenu.BLOCK_ATLAS, 0, 0));
    }
}

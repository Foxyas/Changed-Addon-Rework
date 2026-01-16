package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.menu.CustomMerchantMenu;
import net.foxyas.changedaddon.network.ChangedAddonPackets;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.ClientPacketHandler;
import net.foxyas.changedaddon.network.ServerPacketHandler;
import net.foxyas.changedaddon.network.packet.*;
import net.foxyas.changedaddon.procedure.blocksHandle.LatexBonemealAndDispenserHandler;
import net.foxyas.changedaddon.recipe.brewing.TransfurSicknessRecipeBrewingRecipe;
import net.foxyas.changedaddon.recipe.brewing.UntransfurPotionRecipeBrewingRecipe;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedTabs;
import net.ltxprogrammer.changed.item.AbstractLatexItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonMod {

    public static final ChangedAddonPackets CHANGED_ADDON_PACKETS = new ChangedAddonPackets(ChangedAddonMod.PACKET_HANDLER);

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ChangedAddonVariables.PlayerVariables.class);
    }

//    private static final Predicate<TransfurVariant<?>> CHANGED_ADDON_ONLY_TRANSFURS = variant -> variant.getFormId().getNamespace().equals(ChangedAddonMod.MODID);
//
//    @SubscribeEvent
//    public static void applyItemsInOtherModsTabs(BuildCreativeModeTabContentsEvent event) {
//        CreativeModeTab tab = event.getTab();
//        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
//        CreativeModeTab.ItemDisplayParameters params = event.getParameters();
//        if (tab.equals(ChangedTabs.TAB_CHANGED_ITEMS.get()) || tabKey == ChangedTabs.TAB_CHANGED_ITEMS.getKey()) {
//            ChangedItems.DARK_LATEX_MASK.get().fillItemList(CHANGED_ADDON_ONLY_TRANSFURS, params, event);
//            ChangedItems.LATEX_SYRINGE.get().fillItemList(CHANGED_ADDON_ONLY_TRANSFURS, params, event);
//            ChangedItems.LATEX_FLASK.get().fillItemList(CHANGED_ADDON_ONLY_TRANSFURS, params, event);
//            ChangedItems.LATEX_TIPPED_ARROW.get().fillItemList(CHANGED_ADDON_ONLY_TRANSFURS, params, event);
//        }
//    }


    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        addPackets();

        event.enqueueWork(() -> {
            LatexBonemealAndDispenserHandler.registerBonemealDispenser();
            LatexBonemealAndDispenserHandler.registerAntiLatexDispenser(ChangedAddonItems.ANTI_LATEX_BASE.get().getDefaultInstance());
            AbstractLatexItem whiteLatexGoo = ChangedItems.WHITE_LATEX_GOO.get();
            AbstractLatexItem darkLatexGoo = ChangedItems.DARK_LATEX_GOO.get();

            LatexBonemealAndDispenserHandler.registerGooDispenser(
                    whiteLatexGoo.getLatexType(),
                    whiteLatexGoo.getDefaultInstance()
            );

            LatexBonemealAndDispenserHandler.registerGooDispenser(
                    darkLatexGoo.getLatexType(),
                    darkLatexGoo.getDefaultInstance()
            );

            BrewingRecipeRegistry.addRecipe(new UntransfurPotionRecipeBrewingRecipe());
            BrewingRecipeRegistry.addRecipe(new TransfurSicknessRecipeBrewingRecipe());
        });
    }

    private static void addPackets() {
        CHANGED_ADDON_PACKETS.registerPackets();

        ChangedAddonMod.addNetworkMessage(SafeGrabSyncPacket.class, SafeGrabSyncPacket::write,
                SafeGrabSyncPacket::new,
                (packet, contextSupplier) -> ClientPacketHandler.handleSafeGrabSync(packet, contextSupplier),
                NetworkDirection.PLAY_TO_CLIENT);

        ChangedAddonMod.addNetworkMessage(KeyPressPacket.class, KeyPressPacket::encode,
                KeyPressPacket::new, KeyPressPacket::handle);
        ChangedAddonMod.addNetworkMessage(SyncTransfurVisionsPacket.class, SyncTransfurVisionsPacket::encode,
                SyncTransfurVisionsPacket::new, SyncTransfurVisionsPacket::handle);

        ChangedAddonMod.addNetworkMessage(RequestMovementCheckPacket.class, RequestMovementCheckPacket::encode,
                RequestMovementCheckPacket::new, RequestMovementCheckPacket::handle);
        ChangedAddonMod.addNetworkMessage(ConfirmMovementPacket.class, ConfirmMovementPacket::encode,
                ConfirmMovementPacket::decode, ConfirmMovementPacket::handle);
        ChangedAddonMod.addNetworkMessage(VariantSecondAbilityActivate.class, VariantSecondAbilityActivate::write,
                VariantSecondAbilityActivate::new, VariantSecondAbilityActivate::handle);

        ChangedAddonMod.addNetworkMessage(ChangedAddonVariables.SyncPacket.class, ChangedAddonVariables.SyncPacket::encode,
                ChangedAddonVariables.SyncPacket::new, ChangedAddonVariables.SyncPacket::handler);

        ChangedAddonMod.addNetworkMessage(GeneratorGuiButtonPacket.class, GeneratorGuiButtonPacket::encode,
                GeneratorGuiButtonPacket::new, GeneratorGuiButtonPacket::handler);

        ChangedAddonMod.addNetworkMessage(OpenExtraDetailsPacket.class, OpenExtraDetailsPacket::encode,
                OpenExtraDetailsPacket::new, OpenExtraDetailsPacket::handler);

        ChangedAddonMod.addNetworkMessage(PatKeyPacket.class, PatKeyPacket::encode, PatKeyPacket::new, PatKeyPacket::handler);

        ChangedAddonMod.addNetworkMessage(TransfurSoundsGuiButtonPacket.class, TransfurSoundsGuiButtonPacket::encode,
                TransfurSoundsGuiButtonPacket::new, TransfurSoundsGuiButtonPacket::handler);

        ChangedAddonMod.addNetworkMessage(TurnOffTransfurPacket.class, TurnOffTransfurPacket::encode,
                TurnOffTransfurPacket::new, TurnOffTransfurPacket::handler);

        ChangedAddonMod.addNetworkMessage(InformantBlockGuiKeyPacket.class, InformantBlockGuiKeyPacket::encode,
                InformantBlockGuiKeyPacket::new, InformantBlockGuiKeyPacket::handle);


        ChangedAddonMod.addNetworkMessage(ServerboundProgressFTKCPacket.class, ServerboundProgressFTKCPacket::encode,
                ServerboundProgressFTKCPacket::new, ServerPacketHandler::handleProgressFTKCPacket,
                NetworkDirection.PLAY_TO_SERVER);

        ChangedAddonMod.addNetworkMessage(ClientboundOpenFTKCScreenPacket.class, ClientboundOpenFTKCScreenPacket::encode,
                ClientboundOpenFTKCScreenPacket::new,
                (packet, contextSupplier) -> ClientPacketHandler.handleOpenFTKCScreenPacket(packet, contextSupplier),
                NetworkDirection.PLAY_TO_CLIENT);

        ChangedAddonMod.addNetworkMessage(ClientboundSonarUpdatePacket.class, ClientboundSonarUpdatePacket::encode,
                ClientboundSonarUpdatePacket::new,
                (packet, contextSupplier) -> ClientPacketHandler.handleSonarUpdatePacket(packet, contextSupplier),
                NetworkDirection.PLAY_TO_CLIENT);

        ChangedAddonMod.addNetworkMessage(ServerboundCustomSelectTradePacket.class, ServerboundCustomSelectTradePacket::encode,
                ServerboundCustomSelectTradePacket::new,
                (packet, context) -> {
                    NetworkEvent.Context ctx = context.get();
                    if (ctx.getSender() == null) return;
                    ctx.enqueueWork(() -> {
                        if (ctx.getSender().containerMenu instanceof CustomMerchantMenu menu) {
                            int i = packet.shopItem();
                            menu.setSelectionHint(i);
                            menu.tryMoveItems(i);
                        }
                    });
                }, NetworkDirection.PLAY_TO_SERVER
        );

        ChangedAddonMod.addNetworkMessage(RespawnAsTransfurPacket.class, RespawnAsTransfurPacket::encode,
                RespawnAsTransfurPacket::new, RespawnAsTransfurPacket::handler,
                NetworkDirection.PLAY_TO_SERVER);

        ChangedAddonMod.addNetworkMessage(UpdateTimedKeypadTimerPacket.class, UpdateTimedKeypadTimerPacket::encode,
                UpdateTimedKeypadTimerPacket::new, UpdateTimedKeypadTimerPacket::handle);

        ChangedAddonMod.addNetworkMessage(SyncGrabberEntity.class, SyncGrabberEntity::encode,
                SyncGrabberEntity::new, SyncGrabberEntity::handle);

        ChangedAddonMod.addNetworkMessage(C2SReleaseGrabbedEntity.class, C2SReleaseGrabbedEntity::encode,
                C2SReleaseGrabbedEntity::new, C2SReleaseGrabbedEntity::handle);

        ChangedAddonMod.addNetworkMessage(S2CCheckGrabberEntity.class, S2CCheckGrabberEntity::encode,
                S2CCheckGrabberEntity::new, S2CCheckGrabberEntity::handle);
    }
}

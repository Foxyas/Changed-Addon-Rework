package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.*;
import net.foxyas.changedaddon.network.packets.KeyPressPacket;
import net.foxyas.changedaddon.network.packets.SyncTransfurVisionsPacket;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonMod {

    @SubscribeEvent
    public static void registerPackets(FMLConstructModEvent event) {
        ChangedAddonMod.addNetworkMessage(KeyPressPacket.class, KeyPressPacket::encode, KeyPressPacket::decode, KeyPressPacket::handle);
        ChangedAddonMod.addNetworkMessage(SyncTransfurVisionsPacket.class, SyncTransfurVisionsPacket::encode, SyncTransfurVisionsPacket::decode, SyncTransfurVisionsPacket::handle);

        ChangedAddonMod.addNetworkMessage(FightToKeepConsciousnessMinigameButtonMessage.class, FightToKeepConsciousnessMinigameButtonMessage::buffer, FightToKeepConsciousnessMinigameButtonMessage::new,
                FightToKeepConsciousnessMinigameButtonMessage::handler);
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ChangedAddonModVariables.PlayerVariables.class);
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        ChangedAddonMod.addNetworkMessage(ChangedAddonModVariables.PlayerVariablesSyncMessage.class, ChangedAddonModVariables.PlayerVariablesSyncMessage::buffer,
                ChangedAddonModVariables.PlayerVariablesSyncMessage::new, ChangedAddonModVariables.PlayerVariablesSyncMessage::handler);

        ChangedAddonMod.addNetworkMessage(FoxyasGui2ButtonMessage.class, FoxyasGui2ButtonMessage::buffer, FoxyasGui2ButtonMessage::new,
                FoxyasGui2ButtonMessage::handler);

        ChangedAddonMod.addNetworkMessage(FoxyasGuiButtonMessage.class, FoxyasGuiButtonMessage::buffer, FoxyasGuiButtonMessage::new,
                FoxyasGuiButtonMessage::handler);

        ChangedAddonMod.addNetworkMessage(GeneratorGuiButtonMessage.class, GeneratorGuiButtonMessage::buffer, GeneratorGuiButtonMessage::new,
                GeneratorGuiButtonMessage::handler);

        ChangedAddonMod.addNetworkMessage(LeapKeyMessage.class, LeapKeyMessage::buffer, LeapKeyMessage::new, LeapKeyMessage::handler);

        ChangedAddonMod.addNetworkMessage(OpenExtraDetailsMessage.class, OpenExtraDetailsMessage::buffer, OpenExtraDetailsMessage::new,
                OpenExtraDetailsMessage::handler);

        ChangedAddonMod.addNetworkMessage(OpenStruggleMenuMessage.class, OpenStruggleMenuMessage::buffer, OpenStruggleMenuMessage::new,
                OpenStruggleMenuMessage::handler);

        ChangedAddonMod.addNetworkMessage(PatKeyMessage.class, PatKeyMessage::buffer, PatKeyMessage::new, PatKeyMessage::handler);

        ChangedAddonMod.addNetworkMessage(TransfurSoundsGuiButtonMessage.class, TransfurSoundsGuiButtonMessage::buffer,
                TransfurSoundsGuiButtonMessage::new, TransfurSoundsGuiButtonMessage::handler);

        ChangedAddonMod.addNetworkMessage(TurnOffTransfurMessage.class, TurnOffTransfurMessage::buffer, TurnOffTransfurMessage::new,
                TurnOffTransfurMessage::handler);

        ChangedAddonMod.addNetworkMessage(InformantBlockGuiKeyMessage.class,
                InformantBlockGuiKeyMessage::encode,
                InformantBlockGuiKeyMessage::decode,
                InformantBlockGuiKeyMessage::handle);
    }
}

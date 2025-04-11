package net.foxyas.changedaddon.abilities.handle;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.abilities.PsychicGrab;
import net.foxyas.changedaddon.network.packets.KeyPressPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PsychicGrabKeyHandler {

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (PsychicGrab.Keys.contains(event.getKey()) && Minecraft.getInstance().screen == null) {
            ChangedAddonMod.PACKET_HANDLER.sendToServer(new KeyPressPacket(event.getKey()));
        }
    }
}

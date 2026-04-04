package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.network.packet.SyncAllUntransfurImmunityPacket;
import net.foxyas.changedaddon.network.packet.SyncUntransfurImmunityPacket;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SafeClientPacketHandler {

    ///  Safe Lambda Call for servers. just create the method that you want to call via "beautiful" lambda here.
    ///  Ex: ClientPacketHandler::handleUntransfurImmunitySync is unsafe to call cuz it loads Client only stuff on the server side via its imports but here there's no imports of it. soo it is sided safe

    public static void handleUntransfurImmunitySync(SyncUntransfurImmunityPacket packet, Supplier<NetworkEvent.Context> supplier) {
        ClientPacketHandler.handleUntransfurImmunitySync(packet, supplier);
    }

    public static void handleAllUntransfurImmunitySync(SyncAllUntransfurImmunityPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        ClientPacketHandler.handleAllUntransfurImmunitySync(packet, contextSupplier);
    }
}

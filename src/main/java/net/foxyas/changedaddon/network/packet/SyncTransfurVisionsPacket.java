package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.process.variantsExtraStats.visions.ClientTransfurVisionRegistry;
import net.foxyas.changedaddon.process.variantsExtraStats.visions.TransfurVariantVision;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SyncTransfurVisionsPacket {

    private final List<TransfurVariantVision> visions;

    public SyncTransfurVisionsPacket(List<TransfurVariantVision> visions) {
        this.visions = visions;
    }

    public SyncTransfurVisionsPacket(FriendlyByteBuf buf) {
        this(buf.readList(buf1 -> new TransfurVariantVision(buf1.readResourceLocation(), buf1.readResourceLocation())));
    }

    public static void handle(SyncTransfurVisionsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientTransfurVisionRegistry.clear();
            for (TransfurVariantVision v : msg.visions) {
                ClientTransfurVisionRegistry.register(v); // igual ao servidor, mas do lado do cliente
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(visions, (buf1, vision) ->
                buf1.writeResourceLocation(vision.visionEffect()).writeResourceLocation(vision.form()));
    }
}

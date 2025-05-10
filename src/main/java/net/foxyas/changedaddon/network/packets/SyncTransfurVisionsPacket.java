package net.foxyas.changedaddon.network.packets;

import net.foxyas.changedaddon.process.variantsExtraStats.visions.ClientTransfurVisionRegistry;
import net.foxyas.changedaddon.process.variantsExtraStats.visions.TransfurVariantVision;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncTransfurVisionsPacket {
    private final List<TransfurVariantVision> visions;

    public SyncTransfurVisionsPacket(List<TransfurVariantVision> visions) {
        this.visions = visions;
    }

    public static void encode(SyncTransfurVisionsPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.visions.size());
        for (TransfurVariantVision v : msg.visions) {
            buf.writeResourceLocation(v.getForm());
            buf.writeResourceLocation(v.getVisionEffect());
        }
    }

    public static SyncTransfurVisionsPacket decode(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<TransfurVariantVision> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation form = buf.readResourceLocation();
            ResourceLocation effect = buf.readResourceLocation();
            list.add(new TransfurVariantVision(effect, form));
        }
        return new SyncTransfurVisionsPacket(list);
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
}

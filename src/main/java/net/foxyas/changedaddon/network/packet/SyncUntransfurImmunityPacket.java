package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.event.UntransfurEvent.UntransfurType;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncUntransfurImmunityPacket {
    private final UntransfurType type;
    private final boolean value;

    public SyncUntransfurImmunityPacket(UntransfurType type, boolean value) {
        this.type = type;
        this.value = value;
    }

    public SyncUntransfurImmunityPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readEnum(UntransfurType.class);
        this.value = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(type);
        buffer.writeBoolean(this.value);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // No Cliente: Player é o Minecraft.getInstance().player
            // No Servidor: Player é o context.getSender()
            Player player = context.getSender(); 
            
            // Se o sender for nulo, estamos no lado Client recebendo do Server
            if (player == null) {
                player = net.minecraft.client.Minecraft.getInstance().player;
            }

            if (player != null) {
                TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
                if (variant instanceof TransfurVariantInstanceExtensor extensor) {
                    extensor.setUntransfurImmunity(this.type, this.value);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
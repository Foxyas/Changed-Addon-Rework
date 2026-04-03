package net.foxyas.changedaddon.network.packet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncAllUntransfurImmunityPacket {

    public static final Codec<SyncAllUntransfurImmunityPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("commandImmunity").forGetter(packet -> packet.commandImmunity),
            Codec.BOOL.fieldOf("survivalImmunity").forGetter(packet -> packet.survivalImmunity)
    ).apply(instance, SyncAllUntransfurImmunityPacket::new));

    private final boolean commandImmunity;
    private final boolean survivalImmunity;

    public SyncAllUntransfurImmunityPacket(boolean commandImmunity, boolean survivalImmunity) {
        this.commandImmunity = commandImmunity;
        this.survivalImmunity = survivalImmunity;
    }

    public SyncAllUntransfurImmunityPacket(FriendlyByteBuf buffer) {
        this.commandImmunity = buffer.readBoolean();
        this.survivalImmunity = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(commandImmunity);
        buffer.writeBoolean(survivalImmunity);
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
                    extensor.setUntransfurImmunity(UntransfurEvent.UntransfurType.SURVIVAL, survivalImmunity);
                    extensor.setUntransfurImmunity(UntransfurEvent.UntransfurType.COMMAND, commandImmunity);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
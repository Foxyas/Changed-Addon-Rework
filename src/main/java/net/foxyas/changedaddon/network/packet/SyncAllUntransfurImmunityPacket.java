package net.foxyas.changedaddon.network.packet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

public class SyncAllUntransfurImmunityPacket {

    public static final Codec<SyncAllUntransfurImmunityPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("playerId").forGetter(packet -> packet.playerId),
            Codec.BOOL.fieldOf("commandImmunity").forGetter(packet -> packet.commandImmunity),
            Codec.BOOL.fieldOf("survivalImmunity").forGetter(packet -> packet.survivalImmunity)
    ).apply(instance, SyncAllUntransfurImmunityPacket::new));

    public final int playerId;
    public final boolean commandImmunity;
    public final boolean survivalImmunity;

    public SyncAllUntransfurImmunityPacket(int playerId, boolean commandImmunity, boolean survivalImmunity) {
        this.playerId = playerId;
        this.commandImmunity = commandImmunity;
        this.survivalImmunity = survivalImmunity;
    }

    public SyncAllUntransfurImmunityPacket(FriendlyByteBuf buffer) {
        this.playerId = buffer.readInt();
        this.commandImmunity = buffer.readBoolean();
        this.survivalImmunity = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.playerId);
        buffer.writeBoolean(commandImmunity);
        buffer.writeBoolean(survivalImmunity);
    }
}
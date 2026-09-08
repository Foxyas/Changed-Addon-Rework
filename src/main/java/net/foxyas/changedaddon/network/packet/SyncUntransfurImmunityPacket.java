package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.process.UntransfurReason;
import net.minecraft.network.FriendlyByteBuf;

public class SyncUntransfurImmunityPacket {
    public final int playerId;
    public final UntransfurReason type;
    public final boolean value;

    public SyncUntransfurImmunityPacket(int playerId, UntransfurReason type, boolean value) {
        this.playerId = playerId;
        this.type = type;
        this.value = value;
    }

    public SyncUntransfurImmunityPacket(FriendlyByteBuf buffer) {
        this.playerId = buffer.readInt();
        this.type = buffer.readEnum(UntransfurReason.class);
        this.value = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(playerId);
        buffer.writeEnum(type);
        buffer.writeBoolean(this.value);
    }
}
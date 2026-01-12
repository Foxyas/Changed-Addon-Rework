package net.foxyas.changedaddon.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public record SafeGrabSyncPacket(int targetId, boolean safeMode) {

    public SafeGrabSyncPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(targetId).writeBoolean(safeMode);
    }
}

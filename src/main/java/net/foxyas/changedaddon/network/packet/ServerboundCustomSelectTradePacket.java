package net.foxyas.changedaddon.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public record ServerboundCustomSelectTradePacket(int shopItem) {

    public ServerboundCustomSelectTradePacket(FriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(shopItem);
    }
}

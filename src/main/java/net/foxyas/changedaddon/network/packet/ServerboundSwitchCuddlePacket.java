package net.foxyas.changedaddon.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public record ServerboundSwitchCuddlePacket() {

    public static final ServerboundSwitchCuddlePacket INSTANCE = new ServerboundSwitchCuddlePacket();

    public void encode(FriendlyByteBuf buf) {}
}

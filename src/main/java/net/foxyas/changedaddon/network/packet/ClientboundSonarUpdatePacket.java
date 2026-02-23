package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.renderer.layers.features.RenderMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public record ClientboundSonarUpdatePacket(int ticks, int lastTicks, int fadeInTicks, int fadeOutTicks, float maxDist,
                                           RenderMode mode) {

    public ClientboundSonarUpdatePacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readFloat(), buf.readEnum(RenderMode.class));
    }

    public static void update(ServerPlayer player, int ticks, int lastTicks, int fadeInTicks, int fadeOutTicks, float maxDist, RenderMode mode) {
        ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientboundSonarUpdatePacket(ticks, lastTicks, fadeInTicks, fadeOutTicks, maxDist, mode));
    }

    public static void update(ServerPlayer player, int ticks, int fadeInTicks, int fadeOutTicks, float maxDist, RenderMode mode) {
        ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ClientboundSonarUpdatePacket(ticks, ticks, fadeInTicks, fadeOutTicks, maxDist, mode));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(ticks);
        buf.writeVarInt(lastTicks);
        buf.writeVarInt(fadeInTicks);
        buf.writeVarInt(fadeOutTicks);
        buf.writeFloat(maxDist);
        buf.writeEnum(mode);
    }
}

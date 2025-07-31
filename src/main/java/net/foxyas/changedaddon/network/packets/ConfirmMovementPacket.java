package net.foxyas.changedaddon.network.packets;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ConfirmMovementPacket {
    public static final ResourceLocation ID = ChangedAddonMod.resourceLoc("confirm_movement");
    private final boolean isMoving;
    private Vec3 motion = Vec3.ZERO;

    public ConfirmMovementPacket(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public ConfirmMovementPacket(Vec3 motion) {
        this.isMoving = motion.length() > 0;
        this.motion = motion;
    }

    public static void encode(ConfirmMovementPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isMoving);
        if (msg.motion != Vec3.ZERO) {
            Vec3 motion = msg.motion;
            buf.writeDouble(motion.x());
            buf.writeDouble(motion.y());
            buf.writeDouble(motion.z());
        }
    }

    public static ConfirmMovementPacket decode(FriendlyByteBuf buf) {
        return new ConfirmMovementPacket(buf.readBoolean());
    }

    public static void handle(ConfirmMovementPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender != null) {
                boolean isMoving = msg.isMoving;
                // Agora você pode agir com base nisso no servidor:
                sender.sendMessage(new TextComponent("Client is moving: " + isMoving), sender.getUUID());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

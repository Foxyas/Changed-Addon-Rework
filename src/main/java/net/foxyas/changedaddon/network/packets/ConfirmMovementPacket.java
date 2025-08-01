package net.foxyas.changedaddon.network.packets;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.customHandle.SyncTrackMotion;
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
    private Vec3 motion = null;

    public ConfirmMovementPacket(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public ConfirmMovementPacket(boolean isMoving, Vec3 motion) {
        this.isMoving = isMoving;
        this.motion = motion;
    }

    public static void encode(ConfirmMovementPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isMoving);
        if (msg.motion != Vec3.ZERO && msg.motion != null) {
            Vec3 motion = msg.motion;
            buf.writeDouble(motion.x());
            buf.writeDouble(motion.y());
            buf.writeDouble(motion.z());
        }
    }

    public static ConfirmMovementPacket decode(FriendlyByteBuf buf) {
        try {
            boolean isMoving = buf.readBoolean();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            Vec3 vec = new Vec3(x, y, z);
            return new ConfirmMovementPacket(isMoving, vec);
        } catch (Exception ignored) {
            return new ConfirmMovementPacket(buf.readBoolean());
        }

    }

    public static void handle(ConfirmMovementPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender != null) {
                boolean isMoving = msg.isMoving;
                if (sender instanceof SyncTrackMotion syncTrackMotion) {
                    syncTrackMotion.setIsMoving(isMoving);

                    if (msg.motion != Vec3.ZERO && msg.motion != null) {
                        syncTrackMotion.setLastKnownMotion(msg.motion);
                    }

                    // Agora você pode agir com base nisso no servidor:
                    if (syncTrackMotion.getLastKnownMotion() != null) {
                        sender.sendMessage(new TextComponent("Client motion is: " + syncTrackMotion.getLastKnownMotion()), sender.getUUID());
                    }
                    sender.sendMessage(new TextComponent("Client is moving: " + syncTrackMotion.isMoving()), sender.getUUID());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

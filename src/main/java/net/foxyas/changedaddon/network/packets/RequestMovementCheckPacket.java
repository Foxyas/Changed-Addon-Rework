package net.foxyas.changedaddon.network.packets;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RequestMovementCheckPacket(boolean syncMotion) {
    public static final ResourceLocation ID = ChangedAddonMod.resourceLoc("request_movement");

    public static void handle(RequestMovementCheckPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER || ctx.get().getDirection() == NetworkDirection.LOGIN_TO_SERVER) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) return;
            Vec2 moveVector = player.input.getMoveVector();

            boolean isMoving = moveVector.x != 0.0F || moveVector.y != 0.0F;

            // Enviar resposta de volta para o servidor
            if (msg.syncMotion) {
                ChangedAddonMod.PACKET_HANDLER.reply(new ConfirmMovementPacket(isMoving, player.getDeltaMovement()), ctx.get());
            } else {
                ChangedAddonMod.PACKET_HANDLER.reply(new ConfirmMovementPacket(isMoving), ctx.get());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void encode(RequestMovementCheckPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.syncMotion);
    }

    public static RequestMovementCheckPacket decode(FriendlyByteBuf buf) {
        return new RequestMovementCheckPacket(buf.readBoolean());
    }
}

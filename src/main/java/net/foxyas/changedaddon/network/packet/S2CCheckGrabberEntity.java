package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.entity.LivingEntityDataExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CCheckGrabberEntity(int grabberId, int grabbedId) {

    // Decode
    public S2CCheckGrabberEntity(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt());
    }

    // Handler
    public static void handle(S2CCheckGrabberEntity message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();

        ctx.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity grabbedEntity = level.getEntity(message.grabbedId);
            if (grabbedEntity == null) return;

            LivingEntityDataExtension ext = (LivingEntityDataExtension) grabbedEntity;

            if (ext.getGrabbedBy() == null && level.getEntity(message.grabberId) != null) {
                ChangedAddonMod.PACKET_HANDLER.reply(new C2SReleaseGrabbedEntity(grabbedEntity.getId()), ctx);
            }
        });

        ctx.setPacketHandled(true);
    }

    // Encode
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(grabberId);
        buf.writeVarInt(grabbedId);
    }
}

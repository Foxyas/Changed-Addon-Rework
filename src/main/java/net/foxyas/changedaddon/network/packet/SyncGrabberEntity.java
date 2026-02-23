package net.foxyas.changedaddon.network.packet;

import net.ltxprogrammer.changed.entity.LivingEntityDataExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncGrabberEntity(int grabberId, int grabbedId) {

    // Decode
    public SyncGrabberEntity(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt());
    }

    // Handler
    public static void handle(SyncGrabberEntity message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();

        ctx.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity grabbedEntity = level.getEntity(message.grabbedId);
            if (grabbedEntity == null) return;

            LivingEntityDataExtension ext = (LivingEntityDataExtension) grabbedEntity;

            // -1 significa "ninguém está te agarrando"
            if (message.grabberId == -1) {
                ext.setGrabbedBy(null);
                return;
            }

            // Encontra a entidade no mundo client-side
            Entity e = level.getEntity(message.grabberId);

            if (e instanceof LivingEntity living) {
                ext.setGrabbedBy(living);
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

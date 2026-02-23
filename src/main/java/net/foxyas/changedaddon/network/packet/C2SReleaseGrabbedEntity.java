package net.foxyas.changedaddon.network.packet;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.LivingEntityDataExtension;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public record C2SReleaseGrabbedEntity(int grabbedId) {

    // Decode
    public C2SReleaseGrabbedEntity(FriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    // Handler — SERVER SIDE
    public static void handle(C2SReleaseGrabbedEntity message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();

        ctx.enqueueWork(() -> {
            ServerLevel level = ctx.getSender().serverLevel();
            Entity grabbed = level.getEntity(message.grabbedId);
            if (!(grabbed instanceof LivingEntity livingGrabbed)) {
                return;
            }
            if (!(grabbed instanceof LivingEntityDataExtension ext))
                return;

            LivingEntity grabbedBy = ext.getGrabbedBy();
            if (grabbedBy != null) {
                IAbstractChangedEntity latexSource = IAbstractChangedEntity.forEither(grabbedBy);
                if (latexSource != null) {
                    latexSource.getAbilityInstanceSafe(ChangedAbilities.GRAB_ENTITY_ABILITY.get()).ifPresent((ability) -> {
                        ability.releaseEntity(false);
                        // manda packet de GRAB (tipo ARMS)
                        Changed.PACKET_HANDLER.send(
                                PacketDistributor.TRACKING_ENTITY.with(() -> grabbedBy),
                                new GrabEntityPacket(grabbedBy, livingGrabbed, GrabEntityPacket.GrabType.RELEASE)
                        );
                    });
                }

            }
        });

        ctx.setPacketHandled(true);
    }

    // Encode
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(grabbedId);
    }
}

package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.event.UntransfurEvent.UntransfurType;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncUntransfurImmunityPacket {
    public final int playerId;
    public final UntransfurType type;
    public final boolean value;

    public SyncUntransfurImmunityPacket(int playerId, UntransfurType type, boolean value) {
        this.playerId = playerId;
        this.type = type;
        this.value = value;
    }

    public SyncUntransfurImmunityPacket(FriendlyByteBuf buffer) {
        this.playerId = buffer.readInt();
        this.type = buffer.readEnum(UntransfurType.class);
        this.value = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(playerId);
        buffer.writeEnum(type);
        buffer.writeBoolean(this.value);
    }
}
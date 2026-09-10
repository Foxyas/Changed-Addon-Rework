package net.foxyas.changedaddon.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record ExtraGrabDataSyncPacket(int targetId, CompoundTag data) {

    public ExtraGrabDataSyncPacket createPacket(int targetId, boolean safeMode, boolean damageMode) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("safeMode", safeMode);
        tag.putBoolean("transfurDamageMode", damageMode);
        return new ExtraGrabDataSyncPacket(targetId, tag);
    }

    public ExtraGrabDataSyncPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readNbt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(targetId).writeNbt(data);
    }

    public boolean safeMode() {
        return this.data.contains("safeMode") && this.data.getBoolean("safeMode");
    }

    public boolean transfurDamageMode() {
        return this.data.contains("transfurDamageMode") && this.data.getBoolean("transfurDamageMode");
    }
}

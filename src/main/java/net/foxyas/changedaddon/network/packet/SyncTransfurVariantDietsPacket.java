package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.process.variantsExtraStats.diets.ClientTransfurVariantDietManager;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncTransfurVariantDietsPacket {
    private final List<TransfurVariantDiet> diets;

    public SyncTransfurVariantDietsPacket(List<TransfurVariantDiet> diets) {
        this.diets = diets;
    }

    public SyncTransfurVariantDietsPacket(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        this.diets = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            CompoundTag nbt = buf.readNbt();
            TransfurVariantDiet.CODEC.parse(NbtOps.INSTANCE, nbt)
                .result()
                .ifPresent(this.diets::add);
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(diets.size());
        for (TransfurVariantDiet diet : diets) {
            TransfurVariantDiet.CODEC.encodeStart(NbtOps.INSTANCE, diet)
                .result()
                .ifPresent(tag -> buf.writeNbt((CompoundTag) tag));
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Client-side handling
            ClientTransfurVariantDietManager.setClientDiets(this.diets);
        });
        ctx.get().setPacketHandled(true);
    }
}
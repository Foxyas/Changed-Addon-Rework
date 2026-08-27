package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.process.variantsExtraStats.diets.ClientTransfurVariantDietManager;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTransfurVariantDietsPacket {
    private final Map<ResourceLocation, TransfurVariantDiet> diets;

    public SyncTransfurVariantDietsPacket(Map<ResourceLocation, TransfurVariantDiet> diets) {
        this.diets = diets;
    }

    public SyncTransfurVariantDietsPacket(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        this.diets = new HashMap<>(size);

        for (int i = 0; i < size; i++) {
            ResourceLocation id = buf.readResourceLocation();
            CompoundTag nbt = buf.readNbt();

            if (nbt != null) {
                TransfurVariantDiet.CODEC.parse(NbtOps.INSTANCE, nbt)
                        .result()
                        .ifPresent(diet -> this.diets.put(id, diet));
            }
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(diets.size());

        diets.forEach((id, diet) -> {
            buf.writeResourceLocation(id);
            TransfurVariantDiet.CODEC.encodeStart(NbtOps.INSTANCE, diet)
                    .result()
                    .ifPresent(tag -> buf.writeNbt((CompoundTag) tag));
        });
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Client-side handling update
            ClientTransfurVariantDietManager.setClientDiets(this.diets);
        });
        ctx.get().setPacketHandled(true);
    }
}
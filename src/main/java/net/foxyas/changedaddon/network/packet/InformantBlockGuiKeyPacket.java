package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.block.entity.InformantBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public record InformantBlockGuiKeyPacket(String text, TransfurVariant<?> selectedTf, BlockPos pos) {

    public static final ResourceLocation NULL_LOC = ResourceLocation.fromNamespaceAndPath("null", "null");

    public InformantBlockGuiKeyPacket(FriendlyByteBuf buf) {
        this(buf.readUtf(), tf(buf), buf.readBlockPos());
    }

    private static TransfurVariant<?> tf(FriendlyByteBuf buf) {
        ResourceLocation loc = buf.readResourceLocation();
        return loc.equals(NULL_LOC) ? null : ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(text);
        buf.writeResourceLocation(selectedTf == null ? NULL_LOC : Objects.requireNonNullElse(ChangedRegistry.TRANSFUR_VARIANT.get().getKey(selectedTf), NULL_LOC));
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            if (player == null) return;

            Level level = player.level();
            if (!level.isLoaded(pos)) return;

            BlockState state = level.getBlockState(pos);
            if (!state.is(ChangedAddonBlocks.INFORMANT_BLOCK.get())
                    || !(level.getBlockEntity(pos) instanceof InformantBlockEntity blockEntity)) return;

            blockEntity.updateInternal(text, selectedTf);
        });
        context.setPacketHandled(true);
    }
}

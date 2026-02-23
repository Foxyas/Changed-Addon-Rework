package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.block.advanced.TimedKeypadBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UpdateTimedKeypadTimerPacket(BlockPos pos, int timer) {

    // -----------------------
    //     DECODE
    // -----------------------
    public UpdateTimedKeypadTimerPacket(FriendlyByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readInt()
        );
    }

    // -----------------------
    //     ENCODE
    // -----------------------
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(timer); // escreve o array inteiro
    }

    // -----------------------
    //     HANDLE
    // -----------------------
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            Player player = context.getSender();
            if (player == null)
                return;

            Level level = player.level;
            if (!level.isLoaded(pos))
                return;

            BlockState state = level.getBlockState(pos);
            if (!state.is(ChangedAddonBlocks.TIMED_KEYPAD.get()))
                return;

            if (!(level.getBlockEntity(pos) instanceof TimedKeypadBlockEntity blockEntity))
                return;

            // --- Atualiza o BlockEntity ---
            blockEntity.setTimer(timer); // Você precisa implementar isso no BE

            // Marca como atualizado no servidor
            blockEntity.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        });

        context.setPacketHandled(true);
    }
}
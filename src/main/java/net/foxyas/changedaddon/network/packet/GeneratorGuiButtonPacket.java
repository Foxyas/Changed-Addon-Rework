package net.foxyas.changedaddon.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record GeneratorGuiButtonPacket(int buttonId, BlockPos pos) {

    public GeneratorGuiButtonPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), new BlockPos(buf.readVarInt(), buf.readVarInt(), buf.readVarInt()));
    }

    public static void handler(GeneratorGuiButtonPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() ->
                handleButtonAction(context.getSender(), message.buttonId, message.pos));
        context.setPacketHandled(true);
    }

    public static void handleButtonAction(Player player, int buttonID, BlockPos pos) {
        if (player == null) return;
        Level level = player.level;
        // security measure to prevent arbitrary chunk generation
        if (!level.hasChunkAt(pos)) return;

        if (buttonID == 0) {
            if (level.isClientSide) return;

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) return;

            BlockState state = level.getBlockState(pos);
            boolean enabled = blockEntity.getPersistentData().getBoolean("turn_on");

            if (enabled) {
                blockEntity.getPersistentData().putBoolean("turn_on", false);
                player.displayClientMessage(Component.literal("generator disabled"), true);
            } else {
                blockEntity.getPersistentData().putBoolean("turn_on", true);
                player.displayClientMessage(Component.literal("generator enabled"), true);
            }

            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(buttonId);
        buf.writeVarInt(pos.getX());
        buf.writeVarInt(pos.getY());
        buf.writeVarInt(pos.getZ());
    }
}

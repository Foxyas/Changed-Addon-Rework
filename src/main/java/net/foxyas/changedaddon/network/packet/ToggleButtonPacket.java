package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.block.UnifuserBlock;
import net.foxyas.changedaddon.block.entity.CatalyzerBlockEntity;
import net.foxyas.changedaddon.block.entity.UnifuserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ToggleButtonPacket(int buttonId, BlockPos pos) {

    public ToggleButtonPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), new BlockPos(buf.readVarInt(), buf.readVarInt(), buf.readVarInt()));
    }

    public static void handler(ToggleButtonPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() ->
                handleButtonAction(context.getSender(), message.buttonId, message.pos));
        context.setPacketHandled(true);
    }

    public static void handleButtonAction(Player player, int buttonID, BlockPos pos) {
        if (player == null) return;
        Level level = player.level;
        if (buttonID == 0) {
            if (level.isClientSide) return;

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) return;

            BlockState state = level.getBlockState(pos);

            if (blockEntity instanceof UnifuserBlockEntity unifuserBlockEntity) {
                Component customName = unifuserBlockEntity.getCustomName();
                if (customName == null) customName = unifuserBlockEntity.getDisplayName();
                String name = customName.getString();

                if (unifuserBlockEntity.startRecipe) {
                    unifuserBlockEntity.startRecipe = false;
                    unifuserBlockEntity.setChanged();
                    level.setBlockAndUpdate(pos, state);
                    player.displayClientMessage(Component.literal("you stop the " + name), true);
                } else {
                    unifuserBlockEntity.startRecipe = true;
                    unifuserBlockEntity.setChanged();
                    level.setBlockAndUpdate(pos, state);
                    player.displayClientMessage(Component.literal("you start the " + name), true);
                }
            }

            if (blockEntity instanceof CatalyzerBlockEntity catalyzerBlockEntity) {
                Component customName = catalyzerBlockEntity.getCustomName();
                if (customName == null) customName = catalyzerBlockEntity.getDisplayName();
                String name = customName.getString();

                if (catalyzerBlockEntity.startRecipe) {
                    catalyzerBlockEntity.startRecipe = false;
                    catalyzerBlockEntity.setChanged();
                    level.setBlockAndUpdate(pos, state);
                    player.displayClientMessage(Component.literal("you stop the " + name), true);
                } else {
                    catalyzerBlockEntity.startRecipe = true;
                    catalyzerBlockEntity.setChanged();
                    level.setBlockAndUpdate(pos, state);
                    player.displayClientMessage(Component.literal("you start the " + name), true);
                }
            }

        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(buttonId);
        buf.writeVarInt(pos.getX());
        buf.writeVarInt(pos.getY());
        buf.writeVarInt(pos.getZ());
    }
}

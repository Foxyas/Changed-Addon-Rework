package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.block.InformantBlock;
import net.foxyas.changedaddon.block.entity.InformantBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record InformantBlockGuiKeyMessage(String selectedForm, BlockPos pos) {

    public static InformantBlockGuiKeyMessage decode(FriendlyByteBuf buf) {
        String selectedForm = buf.readUtf();
        BlockPos position = buf.readBlockPos();
        return new InformantBlockGuiKeyMessage(selectedForm, position);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(selectedForm);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            var player = context.getSender();
            if (player != null) {
                Level level = player.getLevel();

                if (level.isLoaded(pos)) {
                    var state = level.getBlockState(pos);
                    if (state.getBlock() instanceof InformantBlock informantBlock) {
                        BlockEntity blockEntity = level.getBlockEntity(pos);
                        if (blockEntity instanceof InformantBlockEntity informantBlockEntity) {
                            informantBlockEntity.setSelectedForm(selectedForm);
                            level.sendBlockUpdated(pos, state, state, 3);

                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}

package net.foxyas.changedaddon.network;

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

import java.util.function.Supplier;

public record InformantBlockGuiKeyMessage(String text, TransfurVariant<?> selectedTf, BlockPos pos) {

    public static final ResourceLocation NULL_LOC = new ResourceLocation("null", "null");

    public static InformantBlockGuiKeyMessage decode(FriendlyByteBuf buf) {
        String selectedForm = buf.readUtf();
        ResourceLocation loc = buf.readResourceLocation();
        BlockPos position = buf.readBlockPos();
        return new InformantBlockGuiKeyMessage(selectedForm, loc.equals(NULL_LOC) ? null : ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc), position);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(text);
        buf.writeResourceLocation(selectedTf == null ? NULL_LOC : selectedTf.getRegistryName());
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            if(player == null) return;

            Level level = player.getLevel();
            if(!level.isLoaded(pos)) return;

            BlockState state = level.getBlockState(pos);
            if(!state.is(ChangedAddonBlocks.INFORMANT_BLOCK.get())
                    || !(level.getBlockEntity(pos) instanceof InformantBlockEntity blockEntity)) return;

            blockEntity.updateInternal(text, selectedTf);
        });
        context.setPacketHandled(true);
    }
}

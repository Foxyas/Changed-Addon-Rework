package net.foxyas.changedaddon.network.packet;

import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @param keyCode Pode ser o código da tecla (ex: GLFW.GLFW_KEY_LEFT)
 */
public record AbilityKeyPressPacket(int keyCode) {

    public AbilityKeyPressPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public static void handle(AbilityKeyPressPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            int key = msg.keyCode;
            ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent((variantInstance -> {
                for (AbstractAbilityInstance value : variantInstance.abilityInstances.values()) {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("keyPressed", key);
                    value.acceptPayload(tag);
                }
            }));
        });
        context.setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(keyCode);
    }
}


package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.ability.PsychicGrab;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @param keyCode Pode ser o código da tecla (ex: GLFW.GLFW_KEY_LEFT)
 */
public record KeyPressPacket(int keyCode) {

    public KeyPressPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public static void handle(KeyPressPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            int key = msg.keyCode;
            ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent((variantInstance -> {
                if (variantInstance.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_GRAB.get()) != null && variantInstance.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_GRAB.get()).ability instanceof PsychicGrab psychicGrab) {
                    psychicGrab.addOffset(key, player);
                }
            }));
        });
        context.setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(keyCode);
    }
}


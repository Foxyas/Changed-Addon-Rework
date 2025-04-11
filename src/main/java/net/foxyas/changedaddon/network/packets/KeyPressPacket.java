package net.foxyas.changedaddon.network.packets;

import net.foxyas.changedaddon.abilities.ChangedAddonAbilities;
import net.foxyas.changedaddon.abilities.PsychicGrab;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class KeyPressPacket {
    private final int keyCode; // Pode ser o código da tecla (ex: GLFW.GLFW_KEY_LEFT)

    public KeyPressPacket(int keyCode) {
        this.keyCode = keyCode;
    }

    public static void encode(KeyPressPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.keyCode);
    }

    public static KeyPressPacket decode(FriendlyByteBuf buf) {
        return new KeyPressPacket(buf.readInt());
    }

    public int getKeyCode() {
        return keyCode;
    }

    public static void handle(KeyPressPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                int key = msg.keyCode;
                ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent((variantInstance -> {
                    if (variantInstance.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_GRAB.get()) != null && variantInstance.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_GRAB.get()).ability instanceof PsychicGrab psychicGrab) {
                        psychicGrab.addOffset(key, player);
                    }
                }));
            }
        });
        context.setPacketHandled(true);
    }
}


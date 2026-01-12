package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record TurnOffTransfurPacket(int type, int pressedMs) {

    public TurnOffTransfurPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(type);
        buf.writeVarInt(pressedMs);
    }

    public static void handler(TurnOffTransfurPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender(), message.type));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        if (player == null) return;

        if (type == 0) {
            if (!ProcessTransfur.isPlayerTransfurred(player)) return;

            TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
            TransfurMode mode = tf.transfurMode;

            if (tf.getParent().transfurMode != TransfurMode.NONE) {
                tf.transfurMode = mode == TransfurMode.NONE ? tf.getParent().transfurMode : TransfurMode.NONE;
                if (player.level().isClientSide()) {
                    player.displayClientMessage(Component.translatable("key.changed_addon.turn_off_transfur.safe_mode", tf.transfurMode != TransfurMode.NONE), false);
                }
            }

            if (tf.getSelectedAbility() instanceof GrabEntityAbilityInstance grabEntityAbilityInstance) {
                if (grabEntityAbilityInstance instanceof GrabEntityAbilityExtensor abilityExtensor) {
                    boolean safeMode = !abilityExtensor.isSafeMode();
                    abilityExtensor.setSafeModeAuthoritative(safeMode);
                    if (!player.level().isClientSide()) {
                        player.displayClientMessage(Component.translatable("key.changed_addon.turn_off_transfur.grab_safe_mode", safeMode), false);
                    }
                }
            }
        }
    }
}

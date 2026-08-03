package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ToggleSafeGrabPacket() {

    public ToggleSafeGrabPacket(FriendlyByteBuf buf) {
        this();
    }

    public static void handler(ToggleSafeGrabPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender()));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player) {
        if (player == null) return;

        if (!ProcessTransfur.isPlayerTransfurred(player)) return;
        TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
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

    public void encode(FriendlyByteBuf buf) {
    }
}

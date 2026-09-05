package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.TransfurSoundsDetails.TransfurSoundAction;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record TransfurSoundsGuiButtonPacket(int actionId) {

    public TransfurSoundsGuiButtonPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    public static void handler(TransfurSoundsGuiButtonPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                handleButtonAction(ctx.get().getSender(), msg.actionId)
        );
        ctx.get().setPacketHandled(true);
    }

    public static void handleButtonAction(Player player, int actionId) {
        if (player == null) return;
        if (!ProcessTransfur.isPlayerTransfurred(player)) return;

        ChangedAddonVariables.PlayerVariables vars =
                ChangedAddonVariables.ofOrDefault(player);

        if (vars.isActInCooldown()) return;

        TransfurSoundAction action = getAction(actionId);
        if (action == null) return;

        if (!action.canUse(player)) return;

        action.playAndApplyCooldown(player);
    }

    // ===============================
    // Core logic
    // ===============================

    private static TransfurSoundAction getAction(int id) {
        TransfurSoundAction[] values = TransfurSoundAction.values();
        return id >= 0 && id < values.length ? values[id] : null;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(actionId);
    }
}
package net.foxyas.changedaddon.qte;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.packet.ClientboundOpenFTKCScreenPacket;
import net.foxyas.changedaddon.qte.FightToKeepConsciousness.MinigameType;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class ProcessFTKC {

    public static void setAndStartFTKCMinigameForPlayer(Player player, MinigameType minigameType) {
        if (!ProcessTransfur.isPlayerTransfurred(player)) {
            return;
        }

        ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.of(player);
        if (playerVariables == null) return;
        if (!(player instanceof ServerPlayer target)) return;

        playerVariables.FTKCminigameType = minigameType;
        playerVariables.consciousnessFightProgress = 0;
        playerVariables.syncPlayerVariables(target);
        ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> target), new ClientboundOpenFTKCScreenPacket(minigameType));
    }

    public static void setAndStartFTKCMinigameForPlayer(Player player) {
        setAndStartFTKCMinigameForPlayer(player, MinigameType.getRandom(player.getRandom()));
    }

    public static void stopFTKCMinigameForPlayer(Player player) {
        if (!ProcessTransfur.isPlayerTransfurred(player)) {
            return;
        }

        ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.of(player);
        if (playerVariables == null) return;

        playerVariables.FTKCminigameType = null;
        playerVariables.consciousnessFightProgress = 0;
        playerVariables.isTransfuredBySafeMethod = true;
        playerVariables.syncPlayerVariables(player);

    }
}

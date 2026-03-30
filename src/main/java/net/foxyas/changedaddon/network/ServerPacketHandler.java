package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.network.packet.ServerboundProgressFTKCPacket;
import net.foxyas.changedaddon.qte.FightToKeepConsciousness;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ServerPacketHandler {

    public static void handleProgressFTKCPacket(ServerboundProgressFTKCPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.ofOrDefault(player);
            FightToKeepConsciousness.MinigameType ftkCminigameType = vars.FTKCminigameType;
            if (ftkCminigameType == null) return;

            if (!ProcessTransfur.isPlayerTransfurred(player)) {
                FightToKeepConsciousness.successFTKC(vars, player);
                return;
            }

            vars.consciousnessFightProgress += ftkCminigameType.progressAmount;

            if (vars.consciousnessFightProgress >= FightToKeepConsciousness.getStruggleNeed()) {
                safeSoundPlay(player.level(), null, player, ftkCminigameType.getSuccessSound(), SoundSource.PLAYERS, 1, 1);
                FightToKeepConsciousness.successFTKC(vars, player);
                return;
            }
            safeSoundPlay(player.level(), null, player, ftkCminigameType.getStruggleSound(), SoundSource.PLAYERS, 1, 1);
            vars.syncPlayerVariables(player);
        });
        context.setPacketHandled(true);
    }

    private static void safeSoundPlay(Level level, @Nullable Player pPlayer, Entity pEntity, @Nullable SoundEvent soundEvent, SoundSource pCategory, float pVolume, float pPitch) {
        if (soundEvent == null) {
            return;
        } else {
            level.playSound(pPlayer, pEntity, soundEvent, pCategory, pVolume, pPitch);
        }
    }
}

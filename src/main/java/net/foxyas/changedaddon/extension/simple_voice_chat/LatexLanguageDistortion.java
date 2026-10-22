package net.foxyas.changedaddon.extension.simple_voice_chat;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import net.foxyas.changedaddon.process.features.ProcessLatexLanguage;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class LatexLanguageDistortion {

    private final VoicechatApi api;

    public LatexLanguageDistortion(VoicechatApi api) {
        this.api = api;
    }

    public void onMicrophonePacket(MicrophonePacketEvent event) {
        if (event.getSenderConnection() == null) return;

        Object playerObj = event.getSenderConnection().getPlayer().getPlayer();
        if (!(playerObj instanceof ServerPlayer sender)) return;

        if (!ProcessLatexLanguage.isPlayerConsiderateLatex(sender) || ProcessLatexLanguage.canUnderstandLatex(sender)) return;

        byte[] raw = event.getPacket().getOpusEncodedData();
        byte[] distorted = VoiceAudioUtils.applyLatexDistortion(api, sender.getUUID(), raw);
        if (distorted != null) {
            event.getPacket().setOpusEncodedData(distorted);
        }
    }

    public void onDisconnect(UUID playerId) {
        VoiceAudioUtils.remove(playerId);
    }
}
package net.foxyas.changedaddon.extension.simple_voice_chat;

import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.PlayerDisconnectedEvent;
import net.foxyas.changedaddon.ChangedAddonMod;

@ForgeVoicechatPlugin
public class ModVoiceChatPlugin implements VoicechatPlugin {

    private LatexLanguageDistortion latexLanguageDistortion;
    private SpeakerAudioTransmitter speakerAudioTransmitter;

    @Override
    public String getPluginId() {
        return ChangedAddonMod.resourceLocString("voice_chat_plugin");
    }

    @Override
    public void initialize(VoicechatApi api) {
        this.latexLanguageDistortion = new LatexLanguageDistortion(api);
        this.speakerAudioTransmitter = new SpeakerAudioTransmitter(api);
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket);
        registration.registerEvent(PlayerDisconnectedEvent.class, this::onPlayerDisconnected);
    }

    private void onMicrophonePacket(MicrophonePacketEvent event) {
        // Speakers receive whatever is in the packet at this point.
        // Distortion first = speakers play the distorted voice.
        // Swap the two calls if speakers should carry the clean voice.
        latexLanguageDistortion.onMicrophonePacket(event);
        speakerAudioTransmitter.onMicrophonePacket(event);
    }

    private void onPlayerDisconnected(PlayerDisconnectedEvent event) {
        latexLanguageDistortion.onDisconnect(event.getPlayerUuid());
        speakerAudioTransmitter.onDisconnect(event.getPlayerUuid());
    }
}
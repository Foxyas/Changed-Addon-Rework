package net.foxyas.changedaddon.extension.simple_voice_chat;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class VoiceAudioUtils {

    // Opus is stateful: every player needs their own decoder/encoder pair.
    private static final Map<UUID, OpusDecoder> DECODERS = new ConcurrentHashMap<>();
    private static final Map<UUID, OpusEncoder> ENCODERS = new ConcurrentHashMap<>();

    private VoiceAudioUtils() {
    }

    public static byte[] applyLatexDistortion(VoicechatApi api, UUID playerId, byte[] opusData) {
        if (api == null || playerId == null || opusData == null) return opusData;

        // An empty packet means the player stopped talking: reset codec state.
        if (opusData.length == 0) {
            reset(playerId);
            return opusData;
        }

        OpusDecoder decoder = DECODERS.get(playerId);
        if (decoder == null || decoder.isClosed()) {
            decoder = api.createDecoder();
            DECODERS.put(playerId, decoder);
        }

        OpusEncoder encoder = ENCODERS.get(playerId);
        if (encoder == null || encoder.isClosed()) {
            encoder = api.createEncoder();
            ENCODERS.put(playerId, encoder);
        }

        short[] pcm = decoder.decode(opusData);
        if (pcm == null || pcm.length == 0) return opusData;

        // Bitcrush + square-wave ring modulation (no Math.sin for performance)
        for (int i = 0; i < pcm.length; i++) {
            int sample = (pcm[i] >> 4) << 4;
            if ((i & 16) == 0) {
                sample = -sample;
            }
            pcm[i] = (short) Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, sample));
        }

        return encoder.encode(pcm);
    }

    public static void reset(UUID playerId) {
        OpusDecoder decoder = DECODERS.get(playerId);
        if (decoder != null && !decoder.isClosed()) decoder.resetState();
        OpusEncoder encoder = ENCODERS.get(playerId);
        if (encoder != null && !encoder.isClosed()) encoder.resetState();
    }

    /** Call when the player disconnects from voice chat. */
    public static void remove(UUID playerId) {
        OpusDecoder decoder = DECODERS.remove(playerId);
        if (decoder != null && !decoder.isClosed()) decoder.close();
        OpusEncoder encoder = ENCODERS.remove(playerId);
        if (encoder != null && !encoder.isClosed()) encoder.close();
    }
}
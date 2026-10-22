package net.foxyas.changedaddon.extension.simple_voice_chat;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Decodes a player's packet ONCE (Opus decoders are stateful, decoding twice would corrupt the stream),
 * then produces one degraded copy per requested quality bucket.
 */
public class AudioQualityProcessor {

    private record EncoderKey(UUID player, int bucket) {
    }

    private final VoicechatApi api;
    private final Map<UUID, OpusDecoder> decoders = new ConcurrentHashMap<>();
    private final Map<EncoderKey, OpusEncoder> encoders = new ConcurrentHashMap<>();

    public AudioQualityProcessor(VoicechatApi api) {
        this.api = api;
    }

    /** @return bucket -> encoded opus data. Buckets that failed to process are missing from the map. */
    public Map<Integer, byte[]> process(UUID player, byte[] opusData, Set<Integer> buckets) {
        Map<Integer, byte[]> out = new HashMap<>();
        if (buckets.isEmpty() || opusData == null || opusData.length == 0) return out;

        OpusDecoder decoder = decoders.get(player);
        if (decoder == null || decoder.isClosed()) {
            decoder = api.createDecoder();
            decoders.put(player, decoder);
        }

        short[] pcm = decoder.decode(opusData);
        if (pcm == null || pcm.length == 0) return out;

        for (int bucket : buckets) {
            EncoderKey key = new EncoderKey(player, bucket);
            OpusEncoder encoder = encoders.get(key);
            if (encoder == null || encoder.isClosed()) {
                encoder = api.createEncoder();
                encoders.put(key, encoder);
            }

            short[] copy = pcm.clone();
            AudioQuality.apply(copy, bucket);
            out.put(bucket, encoder.encode(copy));
        }
        return out;
    }

    /** Call when the player stops talking (empty packet). */
    public void reset(UUID player) {
        OpusDecoder decoder = decoders.get(player);
        if (decoder != null && !decoder.isClosed()) decoder.resetState();
        encoders.forEach((key, encoder) -> {
            if (key.player().equals(player) && !encoder.isClosed()) encoder.resetState();
        });
    }

    public void remove(UUID player) {
        OpusDecoder decoder = decoders.remove(player);
        if (decoder != null && !decoder.isClosed()) decoder.close();
        encoders.keySet().removeIf(key -> {
            if (!key.player().equals(player)) return false;
            OpusEncoder encoder = encoders.get(key);
            if (encoder != null && !encoder.isClosed()) encoder.close();
            return true;
        });
    }
}
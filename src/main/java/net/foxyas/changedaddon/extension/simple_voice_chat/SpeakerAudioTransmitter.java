package net.foxyas.changedaddon.extension.simple_voice_chat;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import net.ltxprogrammer.changed.block.MicrophoneBlock;
import net.ltxprogrammer.changed.block.SpeakerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpeakerAudioTransmitter {

    public static final BooleanProperty ENABLED = MicrophoneBlock.ENABLED;

    private static final int MICROPHONE_RADIUS = 4;
    private static final int SPEAKER_RADIUS = 32;
    private static final long SCAN_TTL_MS = 1000;
    private static final boolean EXCLUDE_SENDER = false; // true = the talker won't hear the speaker (no echo)

    /**
     * 0.0 = awful, 1.0 = perfectly clean. Final quality = microphone * speaker.
     */
    private static final float DEFAULT_MICROPHONE_QUALITY = 0.75f;
    private static final float DEFAULT_SPEAKER_QUALITY = 0.75f;

    private record Speaker(BlockPos pos, float quality) {
    }

    /**
     * micQuality < 0 means "no active microphone nearby".
     */
    private record ScanResult(ResourceKey<Level> dim, float micQuality, List<Speaker> speakers, long time) {
    }

    private record ChannelKey(UUID sender, ResourceKey<Level> dim, long speakerPos) {
    }

    private final VoicechatApi api;
    private final AudioQualityProcessor processor;
    private final Map<UUID, ScanResult> scanCache = new ConcurrentHashMap<>();
    private final Set<UUID> scanning = ConcurrentHashMap.newKeySet();
    private final Map<ChannelKey, LocationalAudioChannel> channels = new ConcurrentHashMap<>();

    public SpeakerAudioTransmitter(VoicechatApi api) {
        this.api = api;
        this.processor = new AudioQualityProcessor(api);
    }

    // ---- Quality hooks (called on the main thread during the scan) ----
    // Change these to read your own block properties / tiers, e.g.:
    //   return state.hasProperty(MY_TIER) ? state.getValue(MY_TIER) / 4f : DEFAULT_SPEAKER_QUALITY;

    private static float getMicrophoneQuality(ServerLevel level, BlockPos pos, BlockState state) {
        return DEFAULT_MICROPHONE_QUALITY;
    }

    private static float getSpeakerQuality(ServerLevel level, BlockPos pos, BlockState state) {
        return DEFAULT_SPEAKER_QUALITY;
    }

    private static int bucketFor(ScanResult scan, Speaker speaker) {
        return AudioQuality.toBucket(scan.micQuality() * speaker.quality());
    }

    /**
     * Runs on the voice chat thread, NOT the main server thread. Never touch the world directly here.
     */
    public void onMicrophonePacket(MicrophonePacketEvent event) {
        VoicechatServerApi serverApi = event.getVoicechat();

        VoicechatConnection connection = event.getSenderConnection();
        if (connection == null) return;

        Object playerObj = connection.getPlayer().getPlayer();
        if (!(playerObj instanceof ServerPlayer sender)) return;

        UUID senderId = sender.getUUID();
        ServerLevel level = sender.serverLevel();

        ScanResult scan = scanCache.get(senderId);
        long now = System.currentTimeMillis();
        if (scan == null || scan.dim() != level.dimension() || now - scan.time() > SCAN_TTL_MS) {
            requestScan(sender, level);
        }
        if (scan == null || scan.dim() != level.dimension() || scan.micQuality() < 0f) return;
        if (scan.speakers().isEmpty()) return;

        byte[] voiceData = event.getPacket().getOpusEncodedData();
        if (voiceData == null) return;

        boolean endOfTransmission = voiceData.length == 0;

        // Decode once, then build one degraded copy per distinct quality bucket
        Map<Integer, byte[]> processed = Map.of();
        if (endOfTransmission) {
            processor.reset(senderId);
        } else {
            Set<Integer> buckets = new HashSet<>();
            for (Speaker speaker : scan.speakers()) {
                int bucket = bucketFor(scan, speaker);
                if (bucket < AudioQuality.STEPS) buckets.add(bucket);
            }
            processed = processor.process(senderId, voiceData, buckets);
        }

        for (Speaker speaker : scan.speakers()) {
            ChannelKey key = new ChannelKey(senderId, level.dimension(), speaker.pos().asLong());
            LocationalAudioChannel channel = channels.computeIfAbsent(key,
                    k -> createChannel(serverApi, level, speaker.pos(), senderId));
            if (channel == null) continue;

            if (endOfTransmission) {
                channel.flush();
            } else {
                // Clean bucket (or failed processing) falls back to the untouched audio
                channel.send(processed.getOrDefault(bucketFor(scan, speaker), voiceData));
            }
        }
    }

    private LocationalAudioChannel createChannel(VoicechatServerApi serverApi, ServerLevel level,
                                                 BlockPos speakerPos, UUID senderId) {
        LocationalAudioChannel channel = serverApi.createLocationalAudioChannel(
                UUID.randomUUID(),
                serverApi.fromServerLevel(level),
                serverApi.createPosition(speakerPos.getX() + 0.5, speakerPos.getY() + 0.5, speakerPos.getZ() + 0.5)
        );
        if (channel == null) return null;

        Entity sender = level.getEntity(senderId);
        channel.setDistance(sender != null ? (float) sender.getEyePosition().distanceTo(Vec3.atCenterOf(speakerPos)) * 8f : SPEAKER_RADIUS);
        channel.setCategory("speakers");
        if (EXCLUDE_SENDER) {
            channel.setFilter(p -> !p.getUuid().equals(senderId));
        }
        return channel;
    }

    // ---- Block scanning (main thread only) ----

    private void requestScan(ServerPlayer sender, ServerLevel level) {
        UUID id = sender.getUUID();
        if (!scanning.add(id)) return;

        BlockPos center = sender.blockPosition();
        level.getServer().execute(() -> {
            try {
                ScanResult result = scan(level, center);
                scanCache.put(id, result);

                Set<Long> alive = new HashSet<>();
                for (Speaker s : result.speakers()) alive.add(s.pos().asLong());
                channels.keySet().removeIf(k -> k.sender().equals(id)
                        && k.dim() == level.dimension()
                        && !alive.contains(k.speakerPos()));
            } finally {
                scanning.remove(id);
            }
        });
    }

    private ScanResult scan(ServerLevel level, BlockPos center) {
        return new ScanResult(level.dimension(), findMicrophoneQuality(level, center),
                findNearbySpeakers(level, center), System.currentTimeMillis());
    }

    /**
     * @return quality of the best active microphone nearby, or -1 if there is none.
     */
    private float findMicrophoneQuality(ServerLevel level, BlockPos center) {
        float best = -1f;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-MICROPHONE_RADIUS, -MICROPHONE_RADIUS, -MICROPHONE_RADIUS),
                center.offset(MICROPHONE_RADIUS, MICROPHONE_RADIUS, MICROPHONE_RADIUS))) {
            if (!level.hasChunkAt(pos)) continue;
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof MicrophoneBlock
                    && state.hasProperty(ENABLED) && state.getValue(ENABLED)) {
                best = Math.max(best, getMicrophoneQuality(level, pos.immutable(), state));
            }
        }
        return best;
    }

    private List<Speaker> findNearbySpeakers(ServerLevel level, BlockPos center) {
        List<Speaker> speakers = new ArrayList<>();

        int minCX = (center.getX() - SPEAKER_RADIUS) >> 4;
        int maxCX = (center.getX() + SPEAKER_RADIUS) >> 4;
        int minCZ = (center.getZ() - SPEAKER_RADIUS) >> 4;
        int maxCZ = (center.getZ() + SPEAKER_RADIUS) >> 4;
        double maxDistSqr = (double) SPEAKER_RADIUS * SPEAKER_RADIUS;

        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
                if (chunk == null) continue;

                LevelChunkSection[] sections = chunk.getSections();
                for (int i = 0; i < sections.length; i++) {
                    LevelChunkSection section = sections[i];
                    if (section.hasOnlyAir()
                            || !section.maybeHas(s -> s.getBlock() instanceof SpeakerBlock)) continue;

                    int baseY = chunk.getSectionYFromSectionIndex(i) << 4;
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                BlockState state = section.getBlockState(x, y, z);
                                if (!(state.getBlock() instanceof SpeakerBlock)) continue;

                                BlockPos pos = new BlockPos((cx << 4) + x, baseY + y, (cz << 4) + z);
                                if (pos.distSqr(center) <= maxDistSqr) {
                                    speakers.add(new Speaker(pos, getSpeakerQuality(level, pos, state)));
                                }
                            }
                        }
                    }
                }
            }
        }
        return speakers;
    }

    public void onDisconnect(UUID playerId) {
        scanCache.remove(playerId);
        scanning.remove(playerId);
        channels.keySet().removeIf(k -> k.sender().equals(playerId));
        processor.remove(playerId);
    }
}
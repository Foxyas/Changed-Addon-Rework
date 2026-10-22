package net.foxyas.changedaddon.extension.simple_voice_chat;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Turns a quality value (0.0 = awful, 1.0 = perfectly clean) into audio degradation.
 * Quality is quantised into STEPS buckets so packets can be processed once per bucket.
 */
public final class AudioQuality {

    /** Bucket STEPS means "clean": the audio is passed through untouched. */
    public static final int STEPS = 16;

    private AudioQuality() {
    }

    public static int toBucket(float quality) {
        float q = Math.max(0f, Math.min(1f, quality));
        return Math.round(q * STEPS);
    }

    public static void apply(short[] pcm, int bucket) {
        if (bucket >= STEPS) return;

        float bad = 1f - (bucket / (float) STEPS); // 0 = clean, 1 = worst

        int dropBits = Math.round(bad * 10f);       // bit depth: 16 -> 6 bits at worst
        int hold = 1 + Math.round(bad * 7f);        // sample rate: 48kHz -> ~6kHz at worst
        float drive = 1f + bad * 2f;                // overdrive / clipping
        int noise = (int) (bad * bad * 1500f);      // background hiss

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int held = 0;

        for (int i = 0; i < pcm.length; i++) {
            if (i % hold == 0) held = pcm[i];       // sample-and-hold

            int s = (int) (held * drive);
            s = clamp(s);
            s = (s >> dropBits) << dropBits;        // bitcrush
            if (noise > 0) s += rng.nextInt(-noise, noise + 1);

            pcm[i] = (short) clamp(s);
        }
    }

    private static int clamp(int s) {
        return Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, s));
    }
}
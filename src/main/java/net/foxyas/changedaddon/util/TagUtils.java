package net.foxyas.changedaddon.util;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.slf4j.Logger;

import java.util.Optional;

public class TagUtils {
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Encodes any object into a CompoundTag under a specific key using a Codec.
     */
    public static <T> void saveWithCodec(Codec<T> codec, T value, CompoundTag tag, String key) {
        codec.encodeStart(NbtOps.INSTANCE, value)
                .resultOrPartial(error -> LOGGER.error("Failed to encode NBT with codec for key '{}': {}", key, error))
                .ifPresent(encodedTag -> tag.put(key, encodedTag));
    }

    /**
     * Decodes an object from a CompoundTag strictly using a Codec.
     */
    public static <T> Optional<T> loadWithCodec(Codec<T> codec, CompoundTag tag, String key) {
        if (!tag.contains(key)) {
            return Optional.empty();
        }

        Tag nbtTag = tag.get(key);
        if (nbtTag == null) {
            return Optional.empty();
        }

        return codec.parse(new Dynamic<>(NbtOps.INSTANCE, nbtTag))
                .resultOrPartial(error -> LOGGER.error("Failed to decode NBT with codec for key '{}': {}", key, error));
    }

    /**
     * Decodes an object from a CompoundTag supporting partial results.
     * If parts of the data are corrupted or missing, it recovers whatever is valid.
     */
    public static <T> Optional<T> loadPartialWithCodec(Codec<T> codec, CompoundTag tag, String key) {
        if (!tag.contains(key)) {
            return Optional.empty();
        }

        Tag nbtTag = tag.get(key);
        if (nbtTag == null) {
            return Optional.empty();
        }

        DataResult<T> result = codec.parse(NbtOps.INSTANCE, nbtTag);

        return result.resultOrPartial(error ->
                LOGGER.warn("Encountered partial or failure decoding NBT with codec for key '{}': {}", key, error)
        );
    }
}
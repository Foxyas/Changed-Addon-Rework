package net.foxyas.changedaddon.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.stream.Stream;

public class TagKeyUtil {

    public static  <T> Stream<T> getTagContents(Level level, TagKey<T> tagKey) {
        // 1. Get the registry from the level using the tag's registry key
        Optional<Registry<T>> registryOptional = level.registryAccess().registry(tagKey.registry());

        if (registryOptional.isPresent()) {
            Registry<T> registry = registryOptional.get();

            // 2. Fetch the tag's elements from the registry
            return registry.getTag(tagKey)
                    .map(named -> named.stream().map(Holder::value)) // Unwrap the Holders to get the raw objects (T)
                    .orElse(Stream.empty());
        }

        return Stream.empty();
    }

}

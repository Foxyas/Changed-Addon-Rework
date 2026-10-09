package net.foxyas.changedaddon.process.bestiary;

import net.foxyas.changedaddon.init.ChangedAddonBestiaryEntries;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class BestiaryEntriesManager {

    /**
     * Gets the Bestiary Registry from the level/player's registry access.
     */
    public static Optional<Registry<BestiaryEntry>> getRegistry(Player player) {
        if (player == null) {
            return Optional.empty();
        }
        RegistryAccess registryAccess = player.level().registryAccess();
        return registryAccess.registry(ChangedAddonBestiaryEntries.BESTIARY_ENTRIES_KEY);
    }

    /**
     * Gets all native Holders for registry entries, sorted by their 'order' field.
     */
    public static List<Reference<BestiaryEntry>> getAllHolders(Player player) {
        return getRegistry(player)
                .map(registry -> registry.holders()
                        .sorted(Comparator.comparingInt(h -> h.value().order()))
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    /**
     * Checks if a specific native Holder entry is unlocked for the player.
     */
    public static boolean isUnlocked(Holder<BestiaryEntry> holder, Player player) {
        return holder != null && holder.value().isUnlockedForPlayer(player);
    }

    /**
     * Checks if a specific entry (by ResourceKey ID) is unlocked.
     */
    public static boolean isUnlocked(Player player, ResourceKey<BestiaryEntry> entryId) {
        if (entryId == null) return false;
        return getRegistry(player)
                .flatMap(registry -> registry.getHolder(entryId)) // Returns a native Optional<Holder.Reference<T>>
                .map(holder -> isUnlocked(holder, player))
                .orElse(false);
    }

    /**
     * Searches for the native Holder of the bestiary entry containing the specified TransfurVariant.
     */
    public static Optional<Reference<BestiaryEntry>> getHolderForVariant(TransfurVariant<?> variant, Player player) {
        if (variant == null) return Optional.empty();
        return getAllHolders(player).stream()
                .filter(holder -> holder.value().variants() != null &&
                        holder.value().variants().stream().anyMatch(v -> v.matches(variant)))
                .findFirst();
    }

    /**
     * Searches for the native Holder of the bestiary entry containing the specified TransfurVariant.
     */
    public static List<Reference<BestiaryEntry>> getHoldersForVariant(TransfurVariant<?> variant, Player player) {
        if (variant == null) return List.of();
        return getAllHolders(player).stream()
                .filter(holder -> holder.value().variants() != null &&
                        holder.value().variants().stream().anyMatch(v -> v.matches(variant))).toList();
    }

    /**
     * Checks if the bestiary entry corresponding to a TransfurVariant is unlocked.
     */
    public static boolean isVariantBestiaryUnlocked(TransfurVariant<?> variant, Player player) {
        if (variant == null || player == null) {
            return false;
        }
        return getHolderForVariant(variant, player)
                .map(holder -> isUnlocked(holder, player))
                .orElse(false);
    }

    /**
     * Returns all unlocked entries mapped by their ResourceLocation in the registry.
     */
    public static Map<ResourceLocation, BestiaryEntry> getUnlockedEntries(Player player) {
        return getRegistry(player)
                .map(registry -> registry.holders()
                        .filter(holder -> isUnlocked(holder, player))
                        .collect(Collectors.toMap(
                                holder -> holder.key().location(),
                                Holder::value
                        )))
                .orElseGet(Collections::emptyMap);
    }

    /**
     * Returns the total count of unlocked entries for the player.
     */
    public static int getUnlockedCount(Player player) {
        return getRegistry(player)
                .map(registry -> (int) registry.holders()
                        .filter(holder -> isUnlocked(holder, player))
                        .count())
                .orElse(0);
    }
}
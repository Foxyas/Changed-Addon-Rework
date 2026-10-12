package net.foxyas.changedaddon.entity.bosses;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class PatDynamicAngerManagement extends DynamicAngerManagement {

    private static final Logger LOGGER = LogUtils.getLogger();

    public enum AngerSource {
        PATS,
        OTHER
    }

    // Separate maps for tracking anger based on source
    protected final Object2IntMap<LivingEntity> patAngerBySuspect = new Object2IntOpenHashMap<>();
    protected final Object2IntMap<LivingEntity> otherAngerBySuspect = new Object2IntOpenHashMap<>();

    public static Codec<PatDynamicAngerManagement> patCodec(Predicate<LivingEntity> entityPredicate) {
        return RecordCodecBuilder.create((instance) -> instance.group(
                SUSPECT_ANGER_PAIR.listOf().fieldOf("suspects").orElse(Collections.emptyList())
                        .forGetter(PatDynamicAngerManagement::createUuidAngerPairs)
        ).apply(instance, (pairs) -> new PatDynamicAngerManagement(entityPredicate, pairs)));
    }

    public PatDynamicAngerManagement(Predicate<LivingEntity> pFilter, List<Pair<UUID, Integer>> pAngerByUuid) {
        super(pFilter, pAngerByUuid);
    }

    /**
     * Increases anger specifying whether it came from pats or other actions.
     */
    public int increaseAnger(LivingEntity pEntity, int pOffset, AngerSource source) {
        // Also update the main map for vanilla compatibility / highest anger tracking
        super.increaseAnger(pEntity, pOffset);

        Object2IntMap<LivingEntity> targetMap = (source == AngerSource.PATS) ? patAngerBySuspect : otherAngerBySuspect;

        int current = targetMap.getInt(pEntity);
        int updated = Math.min(MAX_ANGER, current + pOffset);
        targetMap.put(pEntity, updated);

        return updated;
    }

    /**
     * Gets anger specifically from a source, or total if no source is specified.
     */
    public int getAngerFrom(LivingEntity pEntity, AngerSource source) {
        if (source == AngerSource.PATS) {
            return patAngerBySuspect.getInt(pEntity);
        } else {
            return otherAngerBySuspect.getInt(pEntity);
        }
    }

    @Override
    public void clearAnger(net.minecraft.world.entity.Entity pEntity) {
        super.clearAnger(pEntity);
        if (pEntity instanceof LivingEntity living) {
            patAngerBySuspect.removeInt(living);
            otherAngerBySuspect.removeInt(living);
        }
    }

    @Override
    public void save(CompoundTag tag, String key) {
        patCodec(this.filter).encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(LOGGER::error)
                .ifPresent(nbt -> tag.put(key, nbt));
    }

    @Override
    public void load(CompoundTag tag, String key) {
        if (!tag.contains(key)) return;

        Tag nbtTag = tag.get(key);
        if (nbtTag == null) return;

        patCodec(this.filter).parse(new Dynamic<>(NbtOps.INSTANCE, nbtTag))
                .resultOrPartial(LOGGER::error)
                .ifPresent(loaded -> {
                    this.angerByUuid.clear();
                    this.angerBySuspect.clear();
                    this.suspects.clear();
                    this.patAngerBySuspect.clear();
                    this.otherAngerBySuspect.clear();

                    this.angerByUuid.putAll(loaded.angerByUuid);
                    this.angerBySuspect.putAll(loaded.angerBySuspect);
                    this.suspects.addAll(loaded.suspects);
                    this.sortAndUpdateHighestAnger();
                });
    }
}
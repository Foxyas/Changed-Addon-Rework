package net.foxyas.changedaddon.entity.bosses;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class DynamicAngerManagement {
    @VisibleForTesting
    protected static final int CONVERSION_DELAY = 2;
    @VisibleForTesting
    protected static final int MAX_ANGER = 150;
    protected static final int DEFAULT_ANGER_DECREASE = 1;
    protected int conversionDelay = Mth.randomBetweenInclusive(RandomSource.create(), 0, 2);
    int highestAnger;
    protected static final Codec<Pair<UUID, Integer>> SUSPECT_ANGER_PAIR = RecordCodecBuilder.create((p_253580_) -> {
        return p_253580_.group(UUIDUtil.CODEC.fieldOf("uuid").forGetter(Pair::getFirst), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("anger").forGetter(Pair::getSecond)).apply(p_253580_, Pair::of);
    });
    protected final Predicate<LivingEntity> filter;
    @VisibleForTesting
    protected final ArrayList<LivingEntity> suspects;
    protected final DynamicAngerManagement.Sorter suspectSorter;
    @VisibleForTesting
    protected final Object2IntMap<LivingEntity> angerBySuspect;
    @VisibleForTesting
    protected final Object2IntMap<UUID> angerByUuid;

    public static Codec<DynamicAngerManagement> codec(Predicate<LivingEntity> entityPredicate) {
        return RecordCodecBuilder.create((instance) -> instance.group(SUSPECT_ANGER_PAIR.listOf().fieldOf("suspects").orElse(Collections.emptyList()).forGetter(DynamicAngerManagement::createUuidAngerPairs))
                .apply(instance, (pairs) -> new DynamicAngerManagement(entityPredicate, pairs)));
    }

    public DynamicAngerManagement(Predicate<LivingEntity> pFilter, List<Pair<UUID, Integer>> pAngerByUuid) {
        this.filter = pFilter;
        this.suspects = new ArrayList<>();
        this.suspectSorter = new DynamicAngerManagement.Sorter(this);
        this.angerBySuspect = new Object2IntOpenHashMap<>();
        this.angerByUuid = new Object2IntOpenHashMap<>(pAngerByUuid.size());
        pAngerByUuid.forEach((p_219272_) -> {
            this.angerByUuid.put(p_219272_.getFirst(), p_219272_.getSecond());
        });
    }

    protected List<Pair<UUID, Integer>> createUuidAngerPairs() {
        return Streams.concat(this.suspects.stream().map((entity) -> {
            return Pair.of(entity.getUUID(), this.angerBySuspect.getInt(entity));
        }), this.angerByUuid.object2IntEntrySet().stream().map((entry) -> {
            return Pair.of(entry.getKey(), entry.getIntValue());
        })).collect(Collectors.toList());
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Saves the anger management state into a CompoundTag under the specified key.
     */
    public void save(CompoundTag tag, String key) {
        codec(this.filter).encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(LOGGER::error)
                .ifPresent(nbt -> tag.put(key, nbt));
    }

    /**
     * Convenience method to save under a default "anger" key.
     */
    public void save(CompoundTag tag) {
        this.save(tag, "anger");
    }

    /**
     * Loads and populates data into this instance from a CompoundTag.
     */
    public void load(CompoundTag tag, String key) {
        if (!tag.contains(key)) return;

        Tag nbtTag = tag.get(key);
        if (nbtTag == null) return;

        codec(this.filter).parse(new Dynamic<>(NbtOps.INSTANCE, nbtTag))
                .resultOrPartial(LOGGER::error)
                .ifPresent(loaded -> {
                    this.angerByUuid.clear();
                    this.angerBySuspect.clear();
                    this.suspects.clear();

                    this.angerByUuid.putAll(loaded.angerByUuid);
                    this.angerBySuspect.putAll(loaded.angerBySuspect);
                    this.suspects.addAll(loaded.suspects);
                    this.sortAndUpdateHighestAnger();
                });
    }

    /**
     * Convenience method to load from a default "anger" key.
     */
    public void load(CompoundTag tag) {
        this.load(tag, "anger");
    }

    public void tick(ServerLevel pLevel, Predicate<LivingEntity> pPredicate) {
        --this.conversionDelay;
        if (this.conversionDelay <= 0) {
            this.convertFromUuids(pLevel);
            this.conversionDelay = 2;
        }

        ObjectIterator<Object2IntMap.Entry<UUID>> angerByUuid = this.angerByUuid.object2IntEntrySet().iterator();

        while (angerByUuid.hasNext()) {
            Object2IntMap.Entry<UUID> entry = angerByUuid.next();
            int i = entry.getIntValue();
            if (i <= 1) {
                angerByUuid.remove();
            } else {
                entry.setValue(i - 1);
            }
        }

        ObjectIterator<Object2IntMap.Entry<LivingEntity>> angerBySuspect = this.angerBySuspect.object2IntEntrySet().iterator();

        while (angerBySuspect.hasNext()) {
            Object2IntMap.Entry<LivingEntity> entry1 = angerBySuspect.next();
            int j = entry1.getIntValue();
            LivingEntity entity = entry1.getKey();
            LivingEntity.RemovalReason entity$removalreason = entity.getRemovalReason();
            if (j > 1 && pPredicate.test(entity) && entity$removalreason == null) {
                entry1.setValue(j - 1);
            } else {
                this.suspects.remove(entity);
                angerBySuspect.remove();
                if (j > 1 && entity$removalreason != null) {
                    switch (entity$removalreason) {
                        case CHANGED_DIMENSION:
                        case UNLOADED_TO_CHUNK:
                        case UNLOADED_WITH_PLAYER:
                            this.angerByUuid.put(entity.getUUID(), j - 1);
                    }
                }
            }
        }

        this.sortAndUpdateHighestAnger();
    }

    protected void sortAndUpdateHighestAnger() {
        this.highestAnger = 0;
        this.suspects.sort(this.suspectSorter);
        if (this.suspects.size() == 1) {
            this.highestAnger = this.angerBySuspect.getInt(this.suspects.get(0));
        }

    }

    protected void convertFromUuids(ServerLevel pLevel) {
        ObjectIterator<Object2IntMap.Entry<UUID>> objectiterator = this.angerByUuid.object2IntEntrySet().iterator();

        while (objectiterator.hasNext()) {
            Object2IntMap.Entry<UUID> entry = objectiterator.next();
            int i = entry.getIntValue();
            Entity entity = pLevel.getEntity(entry.getKey());
            if (entity instanceof LivingEntity livingEntity) {
                this.angerBySuspect.put(livingEntity, i);
                this.suspects.add(livingEntity);
                objectiterator.remove();
            }
        }

    }

    public int increaseAnger(LivingEntity pEntity, int pOffset) {
        boolean flag = !this.angerBySuspect.containsKey(pEntity);
        int i = this.angerBySuspect.computeInt(pEntity, (p_219259_, p_219260_) -> {
            return Math.min(150, (p_219260_ == null ? 0 : p_219260_) + pOffset);
        });
        if (flag) {
            int j = this.angerByUuid.removeInt(pEntity.getUUID());
            i += j;
            this.angerBySuspect.put(pEntity, i);
            this.suspects.add(pEntity);
        }

        this.sortAndUpdateHighestAnger();
        return i;
    }

    public void clearAnger(Entity pEntity) {
        this.angerBySuspect.removeInt(pEntity);
        this.suspects.remove(pEntity);
        this.sortAndUpdateHighestAnger();
    }

    @Nullable
    protected Entity getTopSuspect() {
        return this.suspects.stream().filter(this.filter).findFirst().orElse(null);
    }

    public int getActiveAnger(@Nullable Entity pEntity) {
        return pEntity == null ? this.highestAnger : this.angerBySuspect.getInt(pEntity);
    }

    public Optional<LivingEntity> getActiveEntity() {
        return Optional.ofNullable(this.getTopSuspect()).filter((p_219293_) -> {
            return p_219293_ instanceof LivingEntity;
        }).map((p_219290_) -> {
            return (LivingEntity) p_219290_;
        });
    }

    @VisibleForTesting
    protected record Sorter(DynamicAngerManagement angerManagement) implements Comparator<LivingEntity> {
        public int compare(LivingEntity pFirst, LivingEntity pSecond) {
            if (pFirst.equals(pSecond)) {
                return 0;
            } else {
                int i = this.angerManagement.angerBySuspect.getOrDefault(pFirst, 0);
                int j = this.angerManagement.angerBySuspect.getOrDefault(pSecond, 0);
                this.angerManagement.highestAnger = Math.max(this.angerManagement.highestAnger, Math.max(i, j));
                boolean flag = AngerLevel.byAnger(i).isAngry();
                boolean flag1 = AngerLevel.byAnger(j).isAngry();
                if (flag != flag1) {
                    return flag ? -1 : 1;
                } else {
                    boolean flag2 = pFirst instanceof Player;
                    boolean flag3 = pSecond instanceof Player;
                    if (flag2 != flag3) {
                        return flag2 ? -1 : 1;
                    } else {
                        return Integer.compare(j, i);
                    }
                }
            }
        }
    }
}
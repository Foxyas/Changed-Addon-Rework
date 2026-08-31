package net.foxyas.changedaddon.entity.defaults;

import com.mojang.serialization.Dynamic;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Base class for ChangedEntities using Minecraft's Brain AI system.
 */
public abstract class AbstractChangedEntityWithBrain extends ChangedEntity {

    public AbstractChangedEntityWithBrain(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    // ==========================================
    // ABSTRACT HOOKS FOR SUBCLASSES
    // ==========================================

    /**
     * Return memory module types needed by this entity's brain.
     * Example: List.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET)
     */
    protected abstract List<MemoryModuleType<?>> getMemoryTypes();

    /**
     * Return sensor types used by this entity's brain.
     * Example: List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS)
     */
    protected abstract List<SensorType<? extends Sensor<? super ChangedEntity>>> getSensorTypes();

    /**
     * Called in makeBrain() to configure activities, behaviors, and default states.
     * Example: FelinesBrainChangedEntityAi.makeBrain(brain);
     */
    protected abstract Brain<?> configureBrain(Brain<ChangedEntity> brain);

    /**
     * Called every tick inside customServerAiStep() to manage activity state transitions.
     * Example: FelinesBrainChangedEntityAi.updateActivity(this);
     */
    protected abstract void updateBrainActivities();

    /**
     * Initializes dynamic memories on first spawn or when missing after loading.
     * Example: FelinesBrainChangedEntityAi.initMemories(this, randomSource);
     */
    protected abstract void initMemories(RandomSource randomSource);

    // ==========================================
    // MINECRAFT BRAIN SYSTEM OVERRIDES
    // ==========================================

    @Override
    protected Brain.@NotNull Provider<ChangedEntity> brainProvider() {
        return Brain.provider(getMemoryTypes(), getSensorTypes());
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        Brain<ChangedEntity> rawBrain = this.brainProvider().makeBrain(dynamic);
        return configureBrain(rawBrain);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Brain<ChangedEntity> getBrain() {
        return (Brain<ChangedEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        if (this.level() instanceof ServerLevel serverLevel) {
            this.level().getProfiler().push("changedEntityBrain");
            this.getBrain().tick(serverLevel, this);
            this.level().getProfiler().pop();

            this.level().getProfiler().push("changedEntityActivityUpdate");
            this.updateBrainActivities();
            this.level().getProfiler().pop();
        }
        super.customServerAiStep();
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnGroup,
            @Nullable CompoundTag tag
    ) {
        this.initMemories(level.getRandom());
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroup, tag);
    }
}
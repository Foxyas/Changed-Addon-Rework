package net.foxyas.changedaddon.entity.ai.advanced.brainProviders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.LongJumpMidJump;
import net.minecraft.world.entity.ai.behavior.LongJumpToRandomPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

public class FelinesBrainChangedEntityAi {

    public static final ImmutableList<SensorType<? extends Sensor<? super ChangedEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS
    );

    public static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            // 1. Navigation & Target Requirements
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,

            // 2. Long Jump Mechanics Requirements
            MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS,
            MemoryModuleType.LONG_JUMP_MID_JUMP
    );

    private static final UniformInt TIME_BETWEEN_LONG_JUMPS = UniformInt.of(600, 1200);

    public static void initMemories(ChangedEntity changedEntity, RandomSource randomSource) {
        changedEntity.getBrain().setMemory(
                MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS,
                TIME_BETWEEN_LONG_JUMPS.sample(randomSource)
        );
    }

    public static Brain<?> makeBrain(Brain<? extends ChangedEntity> changedEntityBrain) {
        initCoreActivity(changedEntityBrain);
        initIdleActivity(changedEntityBrain);
        initLongJumpActivity(changedEntityBrain);

        changedEntityBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        changedEntityBrain.setDefaultActivity(Activity.IDLE);
        changedEntityBrain.useDefaultActivity();
        return changedEntityBrain;
    }

    private static void initCoreActivity(Brain<? extends ChangedEntity> changedEntityBrain) {
        changedEntityBrain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new CountDownCooldownTicks(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS)
                )
        );
    }

    private static void initIdleActivity(Brain<? extends ChangedEntity> changedEntityBrain) {
        changedEntityBrain.addActivityWithConditions(
                Activity.IDLE,
                ImmutableList.of(),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    private static void initLongJumpActivity(Brain<? extends ChangedEntity> changedEntityBrain) {
        changedEntityBrain.addActivityWithConditions(
                Activity.LONG_JUMP,
                ImmutableList.of(
                        Pair.of(0, new LongJumpMidJump(TIME_BETWEEN_LONG_JUMPS, SoundEvents.GOAT_STEP)),
                        Pair.of(
                                1,
                                new LongJumpToRandomPos<>(
                                        TIME_BETWEEN_LONG_JUMPS,
                                        5, // max jump height
                                        5, // max jump distance
                                        3.5714288F, // jump velocity
                                        entity -> SoundEvents.GOAT_LONG_JUMP
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    public static void updateActivity(ChangedEntity changedEntity) {
        changedEntity.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.LONG_JUMP, Activity.IDLE));
    }
}
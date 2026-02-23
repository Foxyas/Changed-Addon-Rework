package net.foxyas.changedaddon.world.features.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.init.ChangedAddonProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DayTimeStructureProcessor extends StructureProcessor {

    public static final Codec<DayTimeStructureProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.optionalFieldOf("time").forGetter(p -> p.time),
            DayPeriod.CODEC.optionalFieldOf("timeSet", DayPeriod.NOTSET).forGetter(p -> p.dayPeriod)
    ).apply(instance, DayTimeStructureProcessor::new));

    private final Optional<Long> time;
    private final DayPeriod dayPeriod;

    public DayTimeStructureProcessor(Optional<Long> time, DayPeriod dayPeriod) {
        this.time = time;
        this.dayPeriod = dayPeriod;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ChangedAddonProcessors.DAY_TIME.get();
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(
            @NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockPos pivot,
            StructureTemplate.@NotNull StructureBlockInfo original,
            StructureTemplate.@NotNull StructureBlockInfo current,
            @NotNull StructurePlaceSettings settings
            , @Nullable StructureTemplate template) {
        // Aplica o offset aos blocos da estrutura
        if (world instanceof ServerLevelAccessor serverLevelAccessor) {
            ServerLevel level = serverLevelAccessor.getLevel();
            long dayTime = level.getDayTime();
            if (this.time.isEmpty() && this.dayPeriod != DayPeriod.NOTSET) {
                if (this.dayPeriod == DayPeriod.DAY && level.isDay()) {
                    return current;
                } else if (this.dayPeriod == DayPeriod.NIGHT && level.isNight()) {
                    return current;
                }
            } else if (this.time.isPresent() && dayTime == this.time.get()) {
                return current;
            }
        }

        return null;
    }

    public enum DayPeriod implements StringRepresentable {
        NOTSET(),
        DAY(),
        NIGHT();

        public static final Codec<DayPeriod> CODEC =
                StringRepresentable.fromEnum(DayPeriod::values);

        DayPeriod() {
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}

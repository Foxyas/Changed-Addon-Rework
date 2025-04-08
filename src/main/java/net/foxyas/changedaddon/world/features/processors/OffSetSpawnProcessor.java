package net.foxyas.changedaddon.world.features.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.registers.ChangedAddonProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OffSetSpawnProcessor extends StructureProcessor {

    public static final Codec<OffSetSpawnProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").orElse(0).forGetter(p -> p.offsetX),
            Codec.INT.fieldOf("y").orElse(0).forGetter(p -> p.offsetY),
            Codec.INT.fieldOf("z").orElse(0).forGetter(p -> p.offsetZ)
    ).apply(instance, OffSetSpawnProcessor::new));

    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;

    public OffSetSpawnProcessor(int offsetX, int offsetY, int offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ChangedAddonProcessors.OFFSET_SPAWN.get();
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(
            LevelReader world, BlockPos pos, BlockPos pivot,
            StructureTemplate.StructureBlockInfo original,
            StructureTemplate.StructureBlockInfo current,
            StructurePlaceSettings settings
            , @Nullable StructureTemplate template) {
        // Aplica o offset aos blocos da estrutura
        BlockPos newPos = current.pos.offset(offsetX, offsetY, offsetZ);
        return new StructureTemplate.StructureBlockInfo(newPos, current.state, current.nbt);
    }
}

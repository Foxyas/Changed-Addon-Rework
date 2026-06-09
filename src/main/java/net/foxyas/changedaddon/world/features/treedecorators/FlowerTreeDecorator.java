package net.foxyas.changedaddon.world.features.treedecorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.init.ChangedAddonTreeDecorators;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class FlowerTreeDecorator extends TreeDecorator {
    public static final Codec<FlowerTreeDecorator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("intensity").forGetter(d -> d.intensity),
                    BlockStateProvider.CODEC.listOf().fieldOf("flowers").forGetter(d -> d.flowers)
            ).apply(instance, FlowerTreeDecorator::new)
    );

    private final float intensity;
    private final List<BlockStateProvider> flowers;

    public FlowerTreeDecorator(float intensity, List<BlockStateProvider> flowers) {
        this.intensity = intensity;
        this.flowers = flowers;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ChangedAddonTreeDecorators.FLOWER_DECORATOR.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();
        LevelSimulatedReader level = context.level();

        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        List<BlockPos> logs = context.logs();
        if (logs.isEmpty()) return;

        int lowestY = logs.get(0).getY();
        for (BlockPos logPos : logs) {
            if (logPos.getY() < lowestY) {
                lowestY = logPos.getY();
            }
        }

        for (BlockPos logPos : logs) {
            if (logPos.getY() == lowestY) {

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (random.nextFloat() < this.intensity) {

                        BlockPos sidePos = logPos.relative(direction);

                        if (context.isAir(sidePos)) {

                            BlockStateProvider randomProvider = this.flowers.get(random.nextInt(this.flowers.size()));
                            BlockState randomFlower = randomProvider.getState(random, sidePos);

                            if (randomFlower.canSurvive(serverLevel, sidePos)) {
                                context.setBlock(sidePos, randomFlower);
                            }
                        }
                    }
                }
            }
        }
    }
}
package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LuminaraSapling extends SaplingBlock {

    public static class LuminaraTreeGrower extends AbstractTreeGrower {

        @Override
        protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource pRandom, boolean pHasFlowers) {
            return ChangedAddonFeatures.Configured.LUMINARA_TREE;
        }
    }

    public LuminaraSapling() {
        super(new LuminaraTreeGrower(),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_PINK)
                        .noCollission()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.CHERRY_SAPLING)
                        .pushReaction(PushReaction.DESTROY));
    }

}

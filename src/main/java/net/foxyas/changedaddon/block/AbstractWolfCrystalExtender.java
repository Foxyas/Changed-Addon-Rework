package net.foxyas.changedaddon.block;

import net.ltxprogrammer.changed.block.*;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedMaterials;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static net.ltxprogrammer.changed.block.AbstractLatexBlock.getLatexed;
import net.minecraft.world.item.Item;


public class AbstractWolfCrystalExtender {

    public static abstract class AbstractWolfCrystalBlock extends AbstractLatexIceBlock {

        public AbstractWolfCrystalBlock() {
            super(
                    BlockBehaviour.Properties.of(ChangedMaterials.LATEX_CRYSTAL)
                            .sound(SoundType.AMETHYST_CLUSTER)
                            .noOcclusion()
                            .dynamicShape()
                            .strength(1.7F, 0.2F)
            );
        }


        @Override
        public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {

            BlockState plant = plantable.getPlant(world, pos.relative(facing));
            if (plant.getBlock() instanceof WolfCrystal)
                return true;
            else
                return false;
        }

        @Override
        public void stepOn(@NotNull Level level, @NotNull BlockPos position, @NotNull BlockState blockState, @NotNull Entity entity) {
            triggerCrystal(blockState, level, position, entity);
            super.stepOn(level, position, blockState, entity);
        }

        /*
        * public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
            super.tick(state, level, pos, random);
            BlockPos above = pos.above();
            if (level.getBlockState(above).is(Blocks.AIR)) {
                level.setBlock(above, ChangedBlocks.WOLF_CRYSTAL_SMALL.get().defaultBlockState(), 3);
                level.playSound(null, pos, ChangedSounds.ICE2, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }*/

        private void triggerCrystal(BlockState blockState, Level level, BlockPos position, Entity entity) {

            if (entity instanceof LivingEntity le && !(entity instanceof ChangedEntity) && !le.isDeadOrDying()) {
                if (entity instanceof Player player && (ProcessTransfur.isPlayerTransfurred(player) || player.isCreative()))
                    return;
                level.scheduleTick(position, this, 20);
            }
        }
    }
    public static abstract class AbstractWolfCrystalSmall extends TransfurCrystalBlock {

        public AbstractWolfCrystalSmall(Item fragment) {
            super(ChangedTransfurVariants.CRYSTAL_WOLF, fragment,

                    BlockBehaviour.Properties.of(ChangedMaterials.LATEX_CRYSTAL)
                            .sound(SoundType.AMETHYST_CLUSTER)
                            .noOcclusion()
                            .dynamicShape()
                            .strength(1.7F, 0.2F)
            );
        }

        public AbstractWolfCrystalSmall() {
            super(ChangedTransfurVariants.CRYSTAL_WOLF, ChangedItems.WOLF_CRYSTAL_FRAGMENT,

                    BlockBehaviour.Properties.of(ChangedMaterials.LATEX_CRYSTAL)
                            .sound(SoundType.AMETHYST_CLUSTER)
                            .noOcclusion()
                            .dynamicShape()
                            .strength(1.7F, 0.2F)
            );
        }
    }
}

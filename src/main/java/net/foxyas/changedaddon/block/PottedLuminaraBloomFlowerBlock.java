package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PottedLuminaraBloomFlowerBlock extends FlowerPotBlock {

    public PottedLuminaraBloomFlowerBlock() {
        super(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ChangedAddonBlocks.LUMINARA_BLOOM,
                Properties.copy(Blocks.POTTED_DANDELION).noOcclusion()
                        //.emissiveRendering((state, blockGetter, blockPos) -> true)
                        //.hasPostProcess((state, blockGetter, blockPos) -> true)
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightBlock(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return 5;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        pLevel.scheduleTick(pPos, this, 10);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        LuminaraBloomFlowerBlock.tryToPacifyNearbyEntities(pLevel, pPos, 64);
        pLevel.scheduleTick(pPos, this, 10);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, RandomSource random) {
        if (random.nextFloat() >= 0.25) return;

        Vec3 offset = state.getOffset(level, pos);
        float x = (float) offset.x + pos.getX() + 0.5f + (float) (random.nextGaussian() * 0.3f);
        float y = (float) offset.y + pos.getY() + 0.5625f;
        float z = (float) offset.z + pos.getZ() + 0.5f + (float) (random.nextGaussian() * 0.3f);

        level.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, x, y, z, 0, 0.01D, 0);
        if (level instanceof ClientLevel clientLevel) {
            if (random.nextFloat() >= 0.5f) return;
            clientLevel.playLocalSound(pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1, random.nextFloat(), true);
        }
    }
}

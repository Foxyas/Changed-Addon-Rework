package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.util.FoxyasUtils;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class LuminaraBloomFlowerBlock extends FlowerBlock implements BonemealableBlock {

    public LuminaraBloomFlowerBlock() {
        super(ChangedAddonMobEffects.UNTRANSFUR, 60,
                BlockBehaviour.Properties.copy(Blocks.POPPY)
                        .noCollission().dynamicShape().instabreak().sound(SoundType.GRASS));
    }

    public static void tryToPacifyNearbyEntities(@NotNull ServerLevel pLevel, BlockPos pPos, double range) {
        List<LivingEntity> nearChangedBeasts = pLevel.getEntitiesOfClass(LivingEntity.class,
                new AABB(pPos, pPos).inflate(range),
                (entity) -> FoxyasUtils.canEntitySeePosIgnoreGlass(entity, Vec3.atCenterOf(pPos), 90));
        for (LivingEntity livingEntity : nearChangedBeasts) {
            if (livingEntity instanceof ChangedEntity changedEntity) {
                if (changedEntity.getType().is(ChangedAddonTags.EntityTypes.PACIFY_IMMUNE)) {
                    continue;
                }

                if (changedEntity instanceof LuminaraFlowerBeastEntity) {
                    continue;
                }

                if (changedEntity.getType().is(ChangedTags.EntityTypes.LATEX)) {
                    if (!changedEntity.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
                        changedEntity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 60 * 20, 0, true, false, true));
                    }
                }
            } else if (livingEntity instanceof Player player) {
                TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (instance != null) {
                    if (instance.getChangedEntity().getType().is(ChangedAddonTags.EntityTypes.PACIFY_IMMUNE)) {
                        continue;
                    }


                    if ((instance.getChangedEntity() instanceof LuminaraFlowerBeastEntity)) {
                        continue;
                    }

                    if (instance.getParent().getEntityType().is(ChangedTags.EntityTypes.LATEX)) {
                        if (!player.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
                            player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 60 * 20, 0, true, false, true));
                        }
                    }
                }
            }
        }
    }

    public void tryToGrowIntoATree(@NotNull ServerLevel level, BlockPos pos) {
        Stream<BlockPos> blockPoses = FoxyasUtils.betweenClosedStreamSphere(pos, 2, 1);
        for (BlockPos blockPos : blockPoses.toList()) {

        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightBlock(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return 5;
    }

    @Override
    public void onPlace(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        pLevel.scheduleTick(pPos, this, 10);
    }

    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        tryToPacifyNearbyEntities(pLevel, pPos, 64);
        LatexCoverState at = LatexCoverState.getAt(pLevel, pPos);
        if (!at.isAir()) {
            performBonemeal(pLevel, pRandom, pPos, pState);
            LatexCoverState.setAtAndUpdate(pLevel, pPos, ChangedLatexTypes.NONE.get().defaultCoverState());
        }
        pLevel.scheduleTick(pPos, this, 10);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState pState, @NotNull LevelReader pLevel, @NotNull BlockPos pPos) {
        BlockState below = pLevel.getBlockState(pPos.below());
        if (below.getBlock() instanceof AbstractLatexBlock) {
            return true;
        }
        return super.canSurvive(pState, pLevel, pPos);
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


    @Override
    protected boolean mayPlaceOn(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        if (pState.getBlock() instanceof AbstractLatexBlock) {
            return true;
        }
        return super.mayPlaceOn(pState, pLevel, pPos);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader blockGetter, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean pIsClient) {
        return BlockPos.betweenClosedStream(new AABB(blockPos, blockPos).inflate(3, 1, 3))
                .anyMatch(pos -> {
                    BlockState normal = blockGetter.getBlockState(pos);
                    BlockState below = blockGetter.getBlockState(pos.below());
                    return normal.isAir() && (below.getBlock() instanceof AbstractLatexBlock || below.is(BlockTags.DIRT) || below.is(Blocks.FARMLAND));
                });
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel serverLevel, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {

        //Vanilla like behavior
        int tries = 16;
        for (int i = 0; i < tries; i++) {
            BlockPos targetPos = pos.offset(
                    random.nextInt(7) - 3, // X: -2 to +2
                    random.nextInt(3) - 1, // Y: -1 to +1
                    random.nextInt(7) - 3  // Z: -2 to +2
            );
            if (serverLevel.isEmptyBlock(targetPos)) {
                BlockState below = serverLevel.getBlockState(targetPos.below());
                if (below.getBlock() instanceof AbstractLatexBlock || below.is(BlockTags.DIRT) || below.is(Blocks.FARMLAND)) {
                    serverLevel.setBlock(targetPos, state, 3);
                }
            }
        }

        /*
        * less vanilla like behavior
        List<BlockPos> list = FoxyasUtils.betweenClosedStreamSphere(pos, 3,1, 1f).toList();
        for (BlockPos blockPos : list) {
            if (serverLevel.getRandom().nextFloat() >= 0.5f && serverLevel.isEmptyBlock(blockPos)) {
                BlockState below = serverLevel.getBlockState(blockPos.below());
                if (below.getBlock() instanceof AbstractLatexBlock || below.is(BlockTags.DIRT) || below.is(Blocks.FARMLAND)) {
                    serverLevel.setBlock(blockPos, state, 3);
                }
            }
        }*/
    }

}

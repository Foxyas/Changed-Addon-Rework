package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.block.AbstractLatexIceBlock;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LuminarCrystalBlock extends AbstractLatexIceBlock {

    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty DEFROST = BooleanProperty.create("defrost");

    public LuminarCrystalBlock() {
        super(Properties.of() //Fixme : Material.ICE_SOLID
                .mapColor(MapColor.SNOW)
                .friction(0.98F)
                .sound(SoundType.AMETHYST)
                .strength(2.0F, 8.0F).hasPostProcess((blockState, blockGetter, blockPos) -> true)
                .emissiveRendering((blockState, blockGetter, blockPos) -> true).noOcclusion().randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(LuminarCrystalBlock.AGE, 0).setValue(LuminarCrystalBlock.DEFROST, false));

    }

    public static void spawnParticleOnFace(ServerLevel level, BlockPos pos, Direction direction, int count, float particleSpeed) {
        ParticleOptions p_144961_ = ParticleTypes.END_ROD;
        Vec3 vec3 = Vec3.atCenterOf(pos);
        int i = direction.getStepX();
        int j = direction.getStepY();
        int k = direction.getStepZ();
        double d0 = vec3.x + (i == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) i * 0.55D);
        double d1 = vec3.y + (j == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) j * 0.55D);
        double d2 = vec3.z + (k == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) k * 0.55D);
        ParticlesUtil.sendParticles(level, p_144961_, d0, d1, d2, 0.05, 0.05, 0.05, count, particleSpeed);
    }

    public static void moveOrTarget(LivingEntity player, AbstractLuminarcticLeopard leopard) {
        if (player.isInvisible()) {
            leopard.getNavigation().moveTo(player, 0.5f);
        } else {
            leopard.setTarget(player); // Define o jogador como alvo
        }
    }

    @Override
    public boolean skipRendering(@NotNull BlockState blockState, BlockState blockState1, @NotNull Direction direction) {
        return blockState1.is(this) || super.skipRendering(blockState, blockState1, direction);
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 4;
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull Entity entity) {
        super.stepOn(level, blockPos, blockState, entity);

        if (entity instanceof LivingEntity le && !(entity instanceof ChangedEntity) && !le.isDeadOrDying()) {
            if (entity instanceof Player player && (ProcessTransfur.isPlayerTransfurred(player) || player.isCreative()))
                return;
            level.scheduleTick(blockPos, this, 20);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(AGE, DEFROST);
    }

    @Override
    public boolean canSustainPlant(@NotNull BlockState state, @NotNull BlockGetter world, BlockPos pos, @NotNull Direction facing, net.minecraftforge.common.IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.relative(facing));
        if (plant.getBlock() instanceof LuminarCrystalSmall)
            return true;
        else
            return super.canSustainPlant(state, world, pos, facing, plantable);
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(DEFROST)) {
            if (state.getValue(AGE) < MAX_AGE) {
                level.setBlockAndUpdate(pos, state.setValue(AGE, state.getValue(AGE) + 1));
            } else {
                level.destroyBlock(pos, false, null);
            }
            level.scheduleTick(pos, this, 70); //delay de 20 ticks antes de agir
        } else {
            BlockPos above = pos.above();
            if (level.getBlockState(above).is(Blocks.AIR)) {
                level.setBlock(above, ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState(), 3);
                level.playSound(null, pos, ChangedSounds.CRYSTAL_EXTEND.get(), SoundSource.BLOCKS, 1.0f, 1.0f);

            }
            //level.scheduleTick(pos, this, 20); //delay de 20 ticks antes de agir
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!(player.isCreative() || player.isSpectator())) {
            List<AbstractLuminarcticLeopard> lumiList = level.getEntitiesOfClass(AbstractLuminarcticLeopard.class, new AABB(pos).inflate(10));
            for (AbstractLuminarcticLeopard boss : lumiList) {
                if (!boss.canAttack(player) || !boss.hasLineOfSight(player)) continue;// Verifica se pode atacar e ver o jogador

                if (player.level() instanceof ServerLevel) {
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false, false));
                }

                moveOrTarget(player, boss);
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(DEFROST)) {
            if (random.nextFloat() >= 0.99f) {
                for (Direction direction : Direction.values()) {
                    spawnParticleOnFace(level, pos, direction, 1, 0f);
                }
            }
            level.scheduleTick(pos, this, 70);
            return;
        }

        for (Direction direction : Direction.values()) {
            if (direction == Direction.UP || random.nextFloat() < 0.8) continue;

            BlockPos relative = pos.relative(direction);
            BlockState relativeState = level.getBlockState(relative);
            if (relativeState.getBlock() instanceof LuminarCrystalSmall) {
                continue;
            }

            // Verifica se o bloco pode ser substituído
            if (relativeState.isAir() || relativeState.getFluidState().isSourceOfType(Fluids.WATER) && (relativeState.canBeReplaced() && !(relativeState.getFluidState().getType() instanceof LavaFluid))) {
                BlockState smallCrystalStage = ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState();
                smallCrystalStage = smallCrystalStage.setValue(LuminarCrystalSmall.FACING, direction);
                smallCrystalStage = smallCrystalStage.setValue(LuminarCrystalSmall.WATERLOGGED, relativeState.getFluidState().isSourceOfType(Fluids.WATER));
                level.setBlock(relative, smallCrystalStage, 3);
            }
        }
    }
}

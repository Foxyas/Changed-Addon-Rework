package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.block.AbstractLatexIceBlock;
import net.ltxprogrammer.changed.block.TransfurCrystalBlock;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedMaterials;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


public class AbstractLuminarCrystal {

    public static void spawnParticleOnFace(ServerLevel level, BlockPos pos, Direction direction, int count, float particleSpeed) {
        ParticleOptions p_144961_ = ParticleTypes.END_ROD;
        Vec3 vec3 = Vec3.atCenterOf(pos);
        int i = direction.getStepX();
        int j = direction.getStepY();
        int k = direction.getStepZ();
        double d0 = vec3.x + (i == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) i * 0.55D);
        double d1 = vec3.y + (j == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) j * 0.55D);
        double d2 = vec3.z + (k == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) k * 0.55D);
        double d3 = i == 0 ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
        double d4 = j == 0 ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
        double d5 = k == 0 ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
        PlayerUtilProcedure.ParticlesUtil.sendParticles(level, p_144961_, d0, d1, d2, 0.05, 0.05, 0.05, count, particleSpeed);
    }

    public static void spawnEndRodParticleOnFace(ServerLevel level, BlockPos pos, Direction direction, int count, float particleSpeed) {
        ParticleOptions p_144961_ = ParticleTypes.END_ROD;
        Vec3 vec3 = Vec3.atCenterOf(pos);
        int i = direction.getStepX();
        int j = direction.getStepY();
        int k = direction.getStepZ();
        double d0 = vec3.x + (i == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) i * 0.55D);
        double d1 = vec3.y + (j == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) j * 0.55D);
        double d2 = vec3.z + (k == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) k * 0.55D);
        double d3 = i == 0 ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
        double d4 = j == 0 ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
        double d5 = k == 0 ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
        PlayerUtilProcedure.ParticlesUtil.sendParticles(level, p_144961_, d0, d1, d2, d3, d4, d5, count, particleSpeed);
    }

    public static void spawnSnowParticleOnFace(ServerLevel level, BlockPos pos, Direction direction, int count, float particleSpeed) {
        ParticleOptions p_144961_ = ParticleTypes.SNOWFLAKE;
        Vec3 vec3 = Vec3.atCenterOf(pos);
        int i = direction.getStepX();
        int j = direction.getStepY();
        int k = direction.getStepZ();
        double d0 = vec3.x + (i == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) i * 0.55D);
        double d1 = vec3.y + (j == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) j * 0.55D);
        double d2 = vec3.z + (k == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) k * 0.55D);
        double d3 = i == 0 ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
        double d4 = j == 0 ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
        double d5 = k == 0 ? Mth.nextDouble(level.random, -1.0D, 1.0D) : 0.0D;
        PlayerUtilProcedure.ParticlesUtil.sendParticles(level, p_144961_, d0, d1, d2, 0.2, 0.2, 0.2, count, particleSpeed);
    }

    public static abstract class Block extends AbstractLatexIceBlock {
        public static final int MAX_AGE = 3;
        public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
        public static final BooleanProperty DEFROST = BooleanProperty.create("defrost");
        private static final int NEIGHBORS_TO_AGE = 4;
        private static final int NEIGHBORS_TO_MELT = 2;

        public Block() {
            super(Properties.of(Material.ICE_SOLID, MaterialColor.SNOW)
                    .friction(0.98F)
                    .sound(SoundType.AMETHYST)
                    .strength(2.0F, 8.0F).hasPostProcess((blockState, blockGetter, blockPos) -> true)
                    .emissiveRendering((blockState, blockGetter, blockPos) -> true).noOcclusion().randomTicks());
            this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(DEFROST, false));
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
            triggerCrystal(blockState, level, blockPos, entity);

        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
            builder.add(AGE, DEFROST);
        }

        @Override
        public boolean canSustainPlant(@NotNull BlockState state, @NotNull BlockGetter world, BlockPos pos, @NotNull Direction facing, net.minecraftforge.common.IPlantable plantable) {
            BlockState plant = plantable.getPlant(world, pos.relative(facing));
            if (plant.getBlock() instanceof AbstractLuminarCrystal.CrystalSmall)
                return true;
            else
                return super.canSustainPlant(state, world, pos, facing, plantable);
        }

        @Override
        public void onPlace(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean moving) {
            super.onPlace(blockstate, world, pos, oldState, moving);
        }

        @Override
        public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
            super.tick(state, level, pos, random);

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
                    level.playSound(null, pos, ChangedSounds.ICE2, SoundSource.BLOCKS, 1.0f, 1.0f);

                }
                //level.scheduleTick(pos, this, 20); //delay de 20 ticks antes de agir
            }
		/*BlockPos above = pos.above();
		if (level.getBlockState(above).is(Blocks.AIR)) {
			level.setBlock(above, ChangedAddonModBlocks.WHITE_WOLF_CRYSTAL_SMALL.get().defaultBlockState(), 3);
			level.playSound(null, pos, ChangedSounds.ICE2, SoundSource.BLOCKS, 1.0f, 1.0f);
		}*/
        }

        @Override
        public void playerDestroy(@NotNull Level p_49827_, @NotNull Player p_49828_, @NotNull BlockPos p_49829_, @NotNull BlockState p_49830_, @Nullable BlockEntity p_49831_, @NotNull ItemStack p_49832_) {
            super.playerDestroy(p_49827_, p_49828_, p_49829_, p_49830_, p_49831_, p_49832_);
        }

        @Override
        public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
            if (!(player.isCreative() || player.isSpectator())) {
                List<AbstractLuminarcticLeopard> lumiList = level.getEntitiesOfClass(AbstractLuminarcticLeopard.class, new AABB(pos).inflate(10));
                for (AbstractLuminarcticLeopard boss : lumiList) {
                    if (boss.canAttack(player) && boss.hasLineOfSight(player)) { // Verifica se pode atacar e ver o jogador
                        if (player.getLevel() instanceof ServerLevel) {
                            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false, false));
                        }
                        boss.setTarget(player); // Define o jogador como alvo
                    }
                }
            }
            return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        }

        @Override
        public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootBuilder) {
            ResourceLocation resourcelocation = this.getLootTable();
            if (resourcelocation == BuiltInLootTables.EMPTY) {
                return Collections.emptyList();
            } else {
                LootContext lootcontext = lootBuilder.withParameter(LootContextParams.BLOCK_STATE, blockState).create(LootContextParamSets.BLOCK);
                ServerLevel serverlevel = lootcontext.getLevel();
                LootTable loottable = serverlevel.getServer().getLootTables().get(resourcelocation);
                return loottable.getRandomItems(lootcontext);
            }

        }

        @Override
        public void randomTick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
            if (state.getValue(DEFROST)) {
                if (random.nextFloat() >= 0.99f) {
                    for (Direction direction : Direction.values()) {
                        AbstractLuminarCrystal.spawnParticleOnFace(level, pos, direction, 1, 0f);
                    }
                }
                level.scheduleTick(pos, this, 70);
            } else {
                for (Direction direction : Direction.values()) {
                    if (direction == Direction.UP) {
                        continue;
                    }
                    if (random.nextFloat() >= 0.80f) {
                        BlockPos relative = pos.relative(direction);
                        BlockState relativeState = level.getBlockState(relative);
                        if (relativeState.getBlock() instanceof AbstractLuminarCrystal.CrystalSmall) {
                            continue;
                        }

                        // Verifica se o bloco pode ser substituído
                        if (relativeState.isAir() || relativeState.getFluidState().isSourceOfType(Fluids.WATER) && (relativeState.getMaterial().isReplaceable() && !(relativeState.getFluidState().getType() instanceof LavaFluid))) {
                            BlockState smallCrystalStage = ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState();
                            smallCrystalStage = smallCrystalStage.setValue(AbstractLuminarCrystal.CrystalSmall.FACING, direction);
                            smallCrystalStage = smallCrystalStage.setValue(AbstractLuminarCrystal.CrystalSmall.WATERLOGGED, relativeState.getFluidState().isSourceOfType(Fluids.WATER));
                            level.setBlock(relative, smallCrystalStage, 3);
                        }
                    }
                }
            }
        }

        @Override
        public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Random random) {
            super.animateTick(state, level, pos, random);
        }

        private void triggerCrystal(BlockState blockState, Level level, BlockPos position, Entity entity) {
            if (entity instanceof LivingEntity le && !(entity instanceof ChangedEntity) && !le.isDeadOrDying()) {
                if (entity instanceof Player player && (ProcessTransfur.isPlayerTransfurred(player) || player.isCreative()))
                    return;
                level.scheduleTick(position, this, 20);
            }
        }

    }

    public static abstract class CrystalSmall extends TransfurCrystalBlock implements SimpleWaterloggedBlock {

        public static final BooleanProperty HEARTED = BooleanProperty.create("hearted");

        public static final DirectionProperty FACING = BlockStateProperties.FACING;
        public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

        protected static final VoxelShape NORTH_AABB = Block.box(2, 2, 2, 14, 14, 16.0);
        protected static final VoxelShape SOUTH_AABB = Block.box(2, 2, 0, 14, 14, 14.0); // Corrigido
        protected static final VoxelShape EAST_AABB = Block.box(0, 2, 2, 14, 14, 14); // Corrigido
        protected static final VoxelShape WEST_AABB = Block.box(2, 2, 2, 16.0, 14, 14);
        protected static final VoxelShape UP_AABB = Block.box(2, 0, 2, 14, 14.0, 14);
        protected static final VoxelShape DOWN_AABB = Block.box(2, 2, 2, 14, 16.0, 14);


        public CrystalSmall() {
            super(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD,
                    BlockBehaviour.Properties.of(ChangedMaterials.LATEX_CRYSTAL)
                            .sound(SoundType.AMETHYST_CLUSTER)
                            .noOcclusion()
                            .dynamicShape()
                            .randomTicks()
                            .strength(1.7F, 2.5F)
                            .hasPostProcess((blockState, blockGetter, blockPos) -> true)
                            .emissiveRendering((blockState, blockGetter, blockPos) -> true)
                            .noOcclusion());
            this.registerDefaultState(this.stateDefinition.any().setValue(HEARTED, false).setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            Direction direction = state.getValue(FACING);
            return switch (direction) {
                case NORTH -> NORTH_AABB;
                case SOUTH -> SOUTH_AABB;
                case EAST -> EAST_AABB;
                case WEST -> WEST_AABB;
                case UP -> UP_AABB;
                case DOWN -> DOWN_AABB;
            };
        }

        @Override
        public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
            return Shapes.empty();
        }


        @Override
        public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
            return 0;
        }

        @Override
        public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
            return 4;
        }


        @Override
        public boolean canBeReplaced(@NotNull BlockState thisState, @NotNull Fluid fluid) {
            if (fluid instanceof LavaFluid || (fluid instanceof WaterFluid)) {
                if (thisState.getValue(HEARTED)) {
                    return false;
                } else {
                    return true;
                }
            }
            return super.canBeReplaced(thisState, fluid);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
            builder.add(HEARTED);
            builder.add(FACING);
            builder.add(WATERLOGGED);
        }

        @Override
        public @NotNull FluidState getFluidState(BlockState state) {
            return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
        }

        @Override
        public void randomTick(@NotNull BlockState thisState, @NotNull ServerLevel serverLevel, @NotNull BlockPos pos, @NotNull Random random) {
            super.randomTick(thisState, serverLevel, pos, random);
            if (random.nextFloat() >= 0.99f) {
                for (Direction direction : Direction.values()) {
                    spawnParticleOnFace(serverLevel, pos, direction, 2, 0.01f);
                }
            }
        }

        @Override
        public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
            if (state.getValue(WATERLOGGED)) {
                world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
            return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
        }

        @Override
        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            if (!(entity instanceof AbstractLuminarcticLeopard)) {
                super.entityInside(state, level, pos, entity);
                if (entity instanceof LivingEntity livingEntity) {
                    MobEffectInstance EffectInstance = new MobEffectInstance(MobEffects.WITHER, 20 * 20, 1, false, true, true);
                    if (livingEntity instanceof Player player && (player.isCreative() || player.isSpectator())) {
                        return;
                    }
                    if (!livingEntity.canBeAffected(EffectInstance) || livingEntity instanceof AbstractLuminarcticLeopard) {
                        return;
                    }
                    if (livingEntity instanceof Player player && ProcessTransfur.getPlayerTransfurVariant(player) != null) {
                        Stream<TransfurVariant<?>> variantStream = Stream.of(ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD.get(), ChangedAddonTransfurVariants.FEMALE_LUMINARCTIC_LEOPARD.get());
                        if (variantStream.anyMatch((variant -> variant.is(ProcessTransfur.getPlayerTransfurVariant(player).getParent())))) {
                            return;
                        }
                    }

                    if (!livingEntity.hasEffect(MobEffects.WITHER)) {
                        livingEntity.addEffect(EffectInstance);
                    }
                    livingEntity.setTicksFrozen(livingEntity.getTicksFrozen() + 5);
                }
            }
        }

        @Override
        public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
            return true;
        }

        @Override
        public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
            return Shapes.empty();
        }

        @Override
        public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootBuilder) {
            ResourceLocation resourcelocation = this.getLootTable();
            if (resourcelocation == BuiltInLootTables.EMPTY) {
                return Collections.emptyList();
            } else {
                LootContext lootcontext = lootBuilder.withParameter(LootContextParams.BLOCK_STATE, blockState).create(LootContextParamSets.BLOCK);
                ServerLevel serverlevel = lootcontext.getLevel();
                LootTable loottable = serverlevel.getServer().getLootTables().get(resourcelocation);
                return loottable.getRandomItems(lootcontext);
            }

        }

        @Override
        protected boolean mayPlaceOn(BlockState blockState, BlockGetter level, BlockPos blockPos) {
            return blockState.getBlock() == ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get();
        }

        @Nullable
        public BlockState getStateForPlacement(BlockPlaceContext p_152019_) {
            LevelAccessor levelaccessor = p_152019_.getLevel();
            BlockPos blockpos = p_152019_.getClickedPos();
            return this.defaultBlockState().setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER).setValue(FACING, p_152019_.getClickedFace());
        }

        @Override
        public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
            Direction oppositeDirection = blockState.getValue(FACING).getOpposite();
            BlockState blockStateOn = level.getBlockState(blockPos.relative(oppositeDirection));
            if (!canSupportRigidBlock(level, blockPos.relative(oppositeDirection)))
                return false;
            return blockStateOn.getBlock() == ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get();
        }

        @Override
        public @NotNull BlockState rotate(BlockState state, Rotation rot) {
            return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
        }

        @Override
        public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
            return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
        }

        @Override
        public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Random random) {
            super.animateTick(state, level, pos, random);
        }

        @Override
        public PushReaction getPistonPushReaction(BlockState p_60584_) {
            return super.getPistonPushReaction(p_60584_);
        }

        @Override
        public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
            return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        }

        @Override
        public void destroy(LevelAccessor p_49860_, BlockPos p_49861_, BlockState p_49862_) {
            super.destroy(p_49860_, p_49861_, p_49862_);
        }

        @Override
        public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
            if (!level.isClientSide && !oldState.is(newState.getBlock())) {
                ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
                if (serverLevel == null) return;

                // Procura a entidade viva mais próxima (excluindo leopardos)
                LivingEntity closestEntity = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(8)).stream()
                        .filter(entity -> {
                            if (entity instanceof Player player) {
                                return !player.isSpectator() && !player.isCreative();
                            }
                            return !(entity instanceof AbstractLuminarcticLeopard);
                        })
                        .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())))
                        .orElse(null);

                // Leopardo já existente
                List<AbstractLuminarcticLeopard> nearbyLeopards = level.getEntitiesOfClass(AbstractLuminarcticLeopard.class, new AABB(pos).inflate(10));

                if (!nearbyLeopards.isEmpty()) {
                    if (closestEntity != null) {
                        for (AbstractLuminarcticLeopard leopard : nearbyLeopards) {
                            if (leopard.canAttack(closestEntity) && leopard.hasLineOfSight(closestEntity)) {
                                leopard.setTarget(closestEntity);
                                level.playSound(null, pos, SoundEvents.ENDERMAN_SCREAM, SoundSource.MASTER, 1, 0);
                            }
                        }
                    }
                } else {
                    // Spawna novo leopardo
                    var leopardType = level.random.nextBoolean()
                            ? ChangedAddonModEntities.FEMALE_LUMINARCTIC_LEOPARD.get()
                            : ChangedAddonModEntities.LUMINARCTIC_LEOPARD.get();

                    AbstractLuminarcticLeopard newLeopard = leopardType.create(serverLevel);
                    if (newLeopard != null) {
                        if (oldState.getValue(HEARTED)) {
                            newLeopard.setBoss(true);
                        }

                        BlockPos spawnPos = pos;

                        /*if (oldState.hasProperty(BlockStateProperties.FACING)) {
                            Direction facing = oldState.getValue(BlockStateProperties.FACING);
                            spawnPos = pos.relative(facing);
                        }
*/

                        Vec3 spawnVec = new Vec3(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D);
                        newLeopard.setPos(spawnVec.x, spawnVec.y, spawnVec.z);
                        newLeopard.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
                        if (closestEntity != null) {
                            newLeopard.setTarget(closestEntity);
                        }
                        level.addFreshEntity(newLeopard);
                        newLeopard.playSound(SoundEvents.ENDERMAN_SCREAM, 1, 0);
                    }
                }
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
}

package net.foxyas.changedaddon.block.debug;

import net.foxyas.changedaddon.block.debug.entity.StructureSpawnerBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructureSpawnerBlock extends Block implements EntityBlock {

    public StructureSpawnerBlock() {
        super(Properties.copy(Blocks.STRUCTURE_VOID).noLootTable().strength(-1.0F, 3600000.8F));
        this.drops = BuiltInLootTables.EMPTY;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, StructureSpawnerBlockEntity structureSpawnerBlockEntity) {
        structureSpawnerBlockEntity.tick(level, blockPos, state);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState state, StructureSpawnerBlockEntity structureSpawnerBlockEntity) {
        structureSpawnerBlockEntity.tick(level, blockPos, state);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new StructureSpawnerBlockEntity(pos, state);
    }

    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pContext instanceof EntityCollisionContext entityCollisionContext &&
                entityCollisionContext.getEntity() instanceof Player player && player.isCreative()) {
            return Shapes.block(); // pequeno cubo
        }
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        if (ctx instanceof EntityCollisionContext entityCollisionContext &&
                entityCollisionContext.getEntity() instanceof Player player && player.isCreative()) {
            return Shapes.block(); // pequeno cubo
        }
        return Shapes.empty();
    }

    @Override
    public boolean isAir(@NotNull BlockState state) {
        return false; // ainda é um bloco
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ChangedAddonBlockEntities.STRUCTURE_SPAWNER.get(), pLevel.isClientSide ? StructureSpawnerBlock::clientTick : StructureSpawnerBlock::serverTick);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.isClientSide()) {
            LocalPlayer player = Minecraft.getInstance().player;
            // Apenas no cliente e apenas se o jogador está em modo criativo
            if (player != null && player.isCreative()) {
                Vec3 center = pos.getCenter();
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK_MARKER, Blocks.DARK_PRISMARINE.defaultBlockState()), center.x(), center.y(), center.z(), 0, 0, 0);
            }
        }
    }
}

package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.entity.SnepPlushyBlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class SnepPlushyBlock extends AbstractPlushyBlock {

    public static final EnumProperty<CansEnum> CANS = EnumProperty.create("cans", CansEnum.class);

    public SnepPlushyBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).sound(SoundType.WOOL).strength(0.5f, 5f)
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(CANS, CansEnum.NONE));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> box(4, 0, 3.5, 12, 16, 12);
            case EAST -> box(4, 0, 4, 12.5, 16, 12);
            case WEST -> box(3.5, 0, 4, 12, 16, 12);
            default -> box(4, 0, 4, 12, 16, 12.5);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, CANS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, flag)
                .setValue(CANS, CansEnum.NONE); // NONE by Default
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        var retValue = super.use(blockstate, world, pos, player, hand, hit);

        ItemStack itemInHand = player.getItemInHand(hand);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!world.isClientSide() && blockEntity instanceof SnepPlushyBlockEntity snepPlushyBlockEntity) {
            if (itemInHand.is(Items.GLOW_INK_SAC) && !snepPlushyBlockEntity.glowingEyes) {
                if (!player.isCreative()) {
                    itemInHand.shrink(1);
                }

                snepPlushyBlockEntity.glowingEyes = true;
                world.sendBlockUpdated(pos, blockstate, blockstate, 1);
                snepPlushyBlockEntity.setChanged();

                world.playSound(null, hit.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1, 1);
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemInHand);
                }

                return InteractionResult.SUCCESS;
            }
        }
        return retValue;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SnepPlushyBlockEntity(pos, state);
    }

    @Override
    public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(blockstate, world, pos, oldState, moving);

        // Chance muito pequena (ex: 45 em 100)
        Random random = new Random();
        if (random.nextInt(100) <= 25) {  // 25% de chance
            // Gerar um valor aleatório para CANS, ignorando NONE
            CansEnum[] possibleValues = {CansEnum.RIGHT, CansEnum.LEFT, CansEnum.HUG, CansEnum.BOTH};
            CansEnum randomCans = possibleValues[random.nextInt(possibleValues.length)];

            // Criar um novo estado de bloco com o valor alterado de CANS
            BlockState newBlockState = blockstate.setValue(CANS, randomCans);

            // Atualizar o estado do bloco no mundo
            world.setBlock(pos, newBlockState, 3);  // Muda o estado do bloco
        }

        world.scheduleTick(pos, this, 10);  // Continua o tick
    }

    public enum CansEnum implements StringRepresentable {
        NONE("none"),
        RIGHT("right"),
        LEFT("left"),
        HUG("hug"),
        BOTH("both");

        private final String name;

        CansEnum(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}

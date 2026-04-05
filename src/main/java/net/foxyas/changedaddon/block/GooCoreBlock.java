package net.foxyas.changedaddon.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.ForgeSoundType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.world.level.block.Blocks.STONE;

@ParametersAreNonnullByDefault
public class GooCoreBlock extends Block {

    public GooCoreBlock() {
        super(BlockBehaviour.Properties.copy(STONE)
                .sound(new ForgeSoundType(1.0f, 1.0f, () -> SoundEvents.STONE_BREAK, () -> SoundEvents.SCULK_SENSOR_STEP,
                        () -> SoundEvents.SCULK_SENSOR_PLACE, () -> SoundEvents.SCULK_SENSOR_HIT, () -> SoundEvents.STONE_FALL))
                .strength(20f, 5f).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("A Gooey Core which appears to be covered in obsidian"));
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);

        if (pRandom.nextFloat() <= 0.25) {
            pLevel.playSound(null, pPos, SoundEvents.WARDEN_HEARTBEAT, SoundSource.BLOCKS, 0.5f, 0.25f);
        }
    }
}

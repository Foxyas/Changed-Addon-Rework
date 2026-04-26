package net.foxyas.changedaddon.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.NotNull;

public class DeepslatePainiteOreBlock extends DropExperienceBlock {

    public DeepslatePainiteOreBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.COLOR_RED).sound(SoundType.STONE).strength(15f, 25f).requiresCorrectToolForDrops(),
                UniformInt.of(20, 35)
        );
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        return 0;
    }


    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        ItemStack selectedItem = player.getInventory().getSelected();
        if (selectedItem.getItem() instanceof TieredItem tieredItem && tieredItem.isCorrectToolForDrops(selectedItem, state)) {
            return TierSortingRegistry.isCorrectTierForDrops(tieredItem.getTier(), state);
        }
        return super.canHarvestBlock(state, world, pos, player);
    }
}

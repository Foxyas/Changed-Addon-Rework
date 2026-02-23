package net.foxyas.changedaddon.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.NotNull;

import static net.foxyas.changedaddon.block.interfaces.ConditionalLatexCoverableBlock.NonLatexCoverableBlock;

public class IridiumBlock extends Block implements NonLatexCoverableBlock {

    public IridiumBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.SNOW).sound(SoundType.NETHERITE_BLOCK).strength(30f, 50f).requiresCorrectToolForDrops());
    }

    @Override
    public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        return 15;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        ItemStack selectedItem = player.getInventory().getSelected();
        if (selectedItem.getItem() instanceof TieredItem tieredItem && tieredItem.isCorrectToolForDrops(selectedItem, state)) {
            return TierSortingRegistry.isCorrectTierForDrops(Tiers.NETHERITE, state) || tieredItem.getTier().getLevel() >= 4;
        }
        return super.canHarvestBlock(state, world, pos, player);
    }
}

package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ForgeSoundType;

import java.util.List;

public class GooCoreBlock extends Block {
    public GooCoreBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY)
                .sound(new ForgeSoundType(1.0f, 1.0f, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse("block.stone.break")), () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse("block.sculk_sensor.step")),
                        () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse("block.sculk_sensor.place")), () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse("block.sculk_sensor.hit")), () -> SoundEvent.createVariableRangeEvent(ResourceLocation.parse("block.stone.fall"))))
                .strength(20f, 5f).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true));
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("removal")
    public static void registerRenderLayer() {
        ItemBlockRenderTypes.setRenderLayer(ChangedAddonModBlocks.GOO_CORE.get(), renderType -> renderType == RenderType.cutout());
    }

    @Override
    public void appendHoverText(ItemStack itemstack, BlockGetter world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("A Gooey Core which appears to be covered in obsidian"));
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }
}

package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class EmptySprayItem extends Item {

    public EmptySprayItem() {
        super(new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.BLOCK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(ChangedAddonItems.EMPTY_SPRAY.get())) return InteractionResultHolder.pass(stack);

        BlockHitResult result = player.level.clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(5)), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player));
        if (!level.getFluidState(result.getBlockPos()).createLegacyBlock().is(ChangedAddonBlocks.LITIX_CAMONIA_FLUID.get()))
            return InteractionResultHolder.pass(stack);

        player.swing(hand, true);
        level.playSound(null, player, SoundEvents.BOTTLE_FILL, SoundSource.MASTER, 1, 1);

        return InteractionResultHolder.success(new ItemStack(ChangedAddonItems.LITIX_CAMONIA_SPRAY.get()));
    }
}

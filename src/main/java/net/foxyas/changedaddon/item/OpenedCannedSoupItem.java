package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.item.api.IDrinkItem;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class OpenedCannedSoupItem extends AbstractCanItem implements IDrinkItem {

    public OpenedCannedSoupItem() {
        super(new Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)

                .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).alwaysEat().build()));
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        ItemStack stack = event.getItemStack();

        if (stack.is(ChangedBlocks.CANNED_SOUP.get().asItem())) {
            if (player.isShiftKeyDown()
            //        && PlayerUtil.getBlockThatEntityIsLookingAt(player, player.getBlockReach()).getType() == HitResult.Type.MISS
            ) {
                event.setCanceled(true);

                if (!player.level.isClientSide) {
                    stack.shrink(1);
                    player.swing(event.getHand(), true);
                    ItemStack opened = new ItemStack(ChangedAddonItems.OPENED_CANNED_SOUP.get());
                    if (!player.addItem(opened)) {
                        player.drop(opened, true);
                    }
                    player.level().playSound(null, player, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.PLAYERS, 1, 2);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = player.level();
        BlockPos blockPos = event.getHitVec().getBlockPos();
        BlockState state = level.getBlockState(blockPos);

        if (stack.is(ChangedBlocks.CANNED_SOUP.get().asItem())) {
            if (player.isShiftKeyDown()) {
                event.setCanceled(true);
            }
        } else if (state.is(ChangedBlocks.CANNED_PEACHES.get())) {
            if (event.getHand() == InteractionHand.MAIN_HAND) {
                if (event.getItemStack().isEmpty() || event.getItemStack().getItem() instanceof ShovelItem) {
                    if (player.isShiftKeyDown()) {
                        if (!player.getFoodData().needsFood()) {
                            return;
                        }
                        event.setCanceled(true);
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                        level.levelEvent(player, 2001, blockPos, Block.getId(state));
                        Block.popResource(level, blockPos, new ItemStack(ChangedAddonItems.EMPTY_CAN.get()));
                        player.getFoodData().eat(4, 1);
                        player.swing(event.getHand());
                    }
                }
            }

        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if (pPlayer.isShiftKeyDown()) {
            ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            ItemStack closedCan = new ItemStack(ChangedBlocks.CANNED_SOUP.get().asItem(), 1);
            pPlayer.swing(pUsedHand);
            if (!pPlayer.addItem(closedCan)) {
                pPlayer.drop(closedCan, true);
            }
            pLevel.playSound(null, pPlayer, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 1, 2);
            return InteractionResultHolder.pass(itemStack);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        ItemStack itemstack = super.finishUsingItem(stack, level, livingEntity);
        if (livingEntity instanceof Player player) {
            if (!player.getAbilities().instabuild) {
                if (!player.level.isClientSide) {

                    ItemStack opened = new ItemStack(ChangedAddonItems.EMPTY_CAN.get());
                    if (!player.addItem(opened)) {
                        player.drop(opened, true);
                    }
                }
            }
        }


        return itemstack;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.DRINK;
    }
}

package net.foxyas.changedaddon.item;

import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractSyringeItem extends Item implements SpecializedAnimations {

    public AbstractSyringeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 20;
    }

    public void applyEffectsAfterUse(@NotNull ItemStack pStack, Level level, LivingEntity entity) {
        ChangedSounds.broadcastSound(entity, ChangedSounds.SYRINGE_PRICK, 1.0F, 1.0F);
    }

    @Nullable
    public SpecializedAnimations.AnimationHandler getAnimationHandler() {
        return new Syringe.SyringeAnimation(this);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        applyEffectsAfterUse(pStack, pLevel, pLivingEntity);
        if (pLivingEntity instanceof ServerPlayer player) player.awardStat(Stats.ITEM_USED.get(this));
        return onUse(pStack, ChangedItems.SYRINGE.get().getDefaultInstance(), pLivingEntity);
    }

    //copy from a_changed
    protected ItemStack onUse(@NotNull ItemStack inUse, @NotNull ItemStack result, @NotNull LivingEntity entity) {
        if (!(entity instanceof Player player) || !player.isCreative()) {
            if (inUse.getCount() == 1) {
                return result;
            }
            inUse.shrink(1);
            if (result.isEmpty()) return inUse;
        }

        if (entity instanceof Player player) {
            if (!player.isCreative()) ItemHandlerHelper.giveItemToPlayer(player, result);
        } else Block.popResource(entity.level, entity.blockPosition(), result);
        return inUse;
    }
}


package net.foxyas.changedaddon.item;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;

import net.ltxprogrammer.changed.item.SpecializedAnimations;

import net.foxyas.changedaddon.process.util.ChangedAddonLaethinminatorUtil;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.init.ChangedAddonModFluids;

import javax.annotation.Nullable;

public class LaethinminatorItem extends Item implements SpecializedAnimations {
	public LaethinminatorItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).durability(160).rarity(Rarity.UNCOMMON));
	}

	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer() != null && context.getPlayer().isUsingItem())
			return InteractionResult.PASS;
		return super.useOn(context);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(itemstack);
	}

	@Override
	public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int ticks) {
		super.onUseTick(level, entity, stack, ticks);
		if (!(entity instanceof Player player)) {
			return;
		}
		
		if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
			BlockHitResult hitResult = level.clip(new ClipContext(player.getEyePosition(1.0F), // Posição inicial (olhos do jogador)
					player.getEyePosition(1.0F).add(player.getLookAngle().scale(5.0)), // Posição final (olhando 5 blocos à frente)
					ClipContext.Block.OUTLINE, // Modo de colisão com blocos
					ClipContext.Fluid.ANY, // Considera apenas fontes de fluido
					player // Entidade que está fazendo o ray trace
			));
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				BlockPos pos = hitResult.getBlockPos();
				BlockState state = level.getBlockState(pos);
				if (state.getFluidState().is(ChangedAddonModFluids.LITIX_CAMONIA_FLUID.get()) || state.getFluidState().is(ChangedAddonModFluids.FLOWING_LITIX_CAMONIA_FLUID.get())) {
					stack.setDamageValue(0);
				}
			}
			entity.stopUsingItem(); // Stop before breaking
			return;
		}
		
		if (level.isClientSide)
			return;
			
		stack.hurtAndBreak(1, entity, (livingEntity) -> {
			livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		
		ChangedAddonLaethinminatorUtil.shootDynamicLaser(player.getLevel(), player, 16, 4, 4);
	}

	@Nullable
	@Override
	public AnimationHandler getAnimationHandler() {
		return ANIMATION_CACHE.computeIfAbsent(this, Animator::new);
	}

	public static class Animator extends AnimationHandler {
		public Animator(Item item) {
			super(item);
		}

		@Override
		public void setupUsingAnimation(ItemStack itemStack, EntityStateContext entity, UpperModelContext model, HumanoidArm arm, float progress) {
			super.setupUsingAnimation(itemStack, entity, model, arm, progress);
			model.rightArm.xRot = model.head.xRot - 1.570796f - (entity.livingEntity.isCrouching() ? 0.2617994F : 0.0F);
			model.rightArm.yRot = model.head.yRot;
		}
	}
}

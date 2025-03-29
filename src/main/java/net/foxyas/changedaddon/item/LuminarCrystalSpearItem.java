
package net.foxyas.changedaddon.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.entity.LuminarCrystalSpearEntity;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LuminarCrystalSpearItem extends Item implements Vanishable {

	public static final int THROW_THRESHOLD_TIME = 10;
	public static final float BASE_DAMAGE = 8.0F;
	public static final float SHOOT_POWER = 2.5F;
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;
	public LuminarCrystalSpearItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).durability(500));
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 9.0D, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	public boolean canAttackBlock(BlockState p_43409_, Level p_43410_, BlockPos p_43411_, Player p_43412_) {
		return !p_43412_.isCreative();
	}

	public UseAnim getUseAnimation(ItemStack itemStack) {
		return UseAnim.SPEAR;
	}

	public int getUseDuration(ItemStack itemStack) {
		return 72000;
	}

	public void releaseUsing(ItemStack itemStack, Level world, LivingEntity livingEntity, int time) {
		if (livingEntity instanceof Player) {
			Player player = (Player)livingEntity;
			int i = this.getUseDuration(itemStack) - time;
			if (i >= 10) {
				int j = EnchantmentHelper.getRiptide(itemStack);
				if (j <= 0 || player.isInWaterOrRain()) {
					if (!world.isClientSide) {
						itemStack.hurtAndBreak(1, player, (p_43388_) -> {
							p_43388_.broadcastBreakEvent(livingEntity.getUsedItemHand());
						});
						if (j == 0) {
							LuminarCrystalSpearEntity LuminarCrystalSpear = new LuminarCrystalSpearEntity(world, player, itemStack);
							LuminarCrystalSpear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
							if (player.getAbilities().instabuild) {
								LuminarCrystalSpear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							}

							world.addFreshEntity(LuminarCrystalSpear);
							world.playSound((Player)null, LuminarCrystalSpear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
							if (!player.getAbilities().instabuild) {
								player.getInventory().removeItem(itemStack);
							}
						}
					}

					player.awardStat(Stats.ITEM_USED.get(this));
					if (j > 0) {
						float f7 = player.getYRot();
						float f = player.getXRot();
						float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
						float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
						float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
						float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
						float f5 = 3.0F * ((1.0F + (float)j) / 4.0F);
						f1 *= f5 / f4;
						f2 *= f5 / f4;
						f3 *= f5 / f4;
						player.push((double)f1, (double)f2, (double)f3);
						player.startAutoSpinAttack(20);
						if (player.isOnGround()) {
							float f6 = 1.1999999F;
							player.move(MoverType.SELF, new Vec3(0.0D, (double)1.1999999F, 0.0D));
						}

						SoundEvent soundevent;
						if (j >= 3) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
						} else if (j == 2) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
						} else {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
						}

						world.playSound((Player)null, player, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
					}

				}
			}
		}
	}

	public InteractionResultHolder<ItemStack> use(Level world, Player p_43406_, InteractionHand hand) {
		ItemStack itemstack = p_43406_.getItemInHand(hand);
		if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
			return InteractionResultHolder.fail(itemstack);
		} else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !p_43406_.isInWaterOrRain()) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			p_43406_.startUsingItem(hand);
			return InteractionResultHolder.consume(itemstack);
		}
	}

	public boolean hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2) {
		itemStack.hurtAndBreak(1, livingEntity2, (item) -> {
			item.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}

	public boolean mineBlock(ItemStack itemStack, Level world, BlockState blockState, BlockPos pos, LivingEntity livingEntity) {
		if ((double)blockState.getDestroySpeed(world, pos) != 0.0D) {
			itemStack.hurtAndBreak(2, livingEntity, (item) -> {
				item.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});
		}

		return true;
	}

	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
	}

	public int getEnchantmentValue() {
		return 1;
	}
}

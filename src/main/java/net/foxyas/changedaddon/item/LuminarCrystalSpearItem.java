package net.foxyas.changedaddon.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.foxyas.changedaddon.entity.projectile.LuminarCrystalSpearEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LuminarCrystalSpearItem extends Item implements Vanishable {

    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float BASE_DAMAGE = 8.0F;
    public static final float SHOOT_POWER = 2.5F;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public LuminarCrystalSpearItem() {
        super(new Item.Properties()
                .durability(500).fireResistant());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 9.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public boolean canAttackBlock(@NotNull BlockState p_43409_, @NotNull Level p_43410_, @NotNull BlockPos p_43411_, Player p_43412_) {
        return !p_43412_.isCreative();
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(@NotNull ItemStack itemStack) {
        return 72000;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.SWEEPING_EDGE) {
            return false;
        }
        return enchantment.category == EnchantmentCategory.TRIDENT
                || enchantment.category == EnchantmentCategory.WEAPON
                || enchantment.category == EnchantmentCategory.BREAKABLE
                || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return Items.TRIDENT.getEnchantmentValue(); // você pode usar 1 (tridente usa esse valor) ou ajustar para mais
    }

    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level world, @NotNull LivingEntity livingEntity, int time) {
        if (livingEntity instanceof Player player) {
            int i = this.getUseDuration(itemStack) - time;
            if (i >= THROW_THRESHOLD_TIME) {
                int j = EnchantmentHelper.getRiptide(itemStack);
                if (j <= 0 || player.isInWaterOrRain()) {
                    if (!world.isClientSide) {
                        itemStack.hurtAndBreak(1, player, (p_43388_) -> {
                            p_43388_.broadcastBreakEvent(livingEntity.getUsedItemHand());
                        });
                        if (j == 0) {
                            LuminarCrystalSpearEntity LuminarCrystalSpear = new LuminarCrystalSpearEntity(world, player, itemStack);
                            LuminarCrystalSpear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float) j * 0.5F, 1.0F);
                            if (player.getAbilities().instabuild) {
                                LuminarCrystalSpear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            world.addFreshEntity(LuminarCrystalSpear);
                            world.playSound(null, LuminarCrystalSpear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (!player.getAbilities().instabuild) {
                                player.getInventory().removeItem(itemStack);
                            }
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (j > 0) {
                        float f7 = player.getYRot();
                        float f = player.getXRot();
                        float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                        float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
                        float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                        float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                        float f5 = 3.0F * ((1.0F + (float) j) / 4.0F);
                        f1 *= f5 / f4;
                        f2 *= f5 / f4;
                        f3 *= f5 / f4;
                        player.push(f1, f2, f3);
                        player.startAutoSpinAttack(20);
                        if (player.onGround()) {
                            float f6 = 1.1999999F;
                            player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999F, 0.0D));
                        }

                        SoundEvent soundevent;
                        if (j >= 3) {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if (j == 2) {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
                        }

                        world.playSound(null, player, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }

                }
            }
        }
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player p_43406_, @NotNull InteractionHand hand) {
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

    public boolean hurtEnemy(ItemStack itemStack, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2) {
        itemStack.hurtAndBreak(1, livingEntity2, (item) -> {
            item.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(@NotNull ItemStack itemStack, @NotNull Level world, BlockState blockState, @NotNull BlockPos pos, @NotNull LivingEntity livingEntity) {
        if ((double) blockState.getDestroySpeed(world, pos) != 0.0D) {
            itemStack.hurtAndBreak(2, livingEntity, (item) -> {
                item.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}

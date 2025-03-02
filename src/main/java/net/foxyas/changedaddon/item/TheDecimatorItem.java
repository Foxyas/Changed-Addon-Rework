
package net.foxyas.changedaddon.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TheDecimatorItem extends Item {
	private float AttackDamage(){
		return -2f + 15f;
	}
	private float AttackSpeed(){
		return -4f + 1.25f;
	}
	private static final UUID BASE_ATTACK_REACH_UUID = UUID.fromString("fa02d244-9771-415c-8789-fd03b5252c8c");
	public TheDecimatorItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON_COMBAT_OPTIONAL).durability(100));
	}

	@Override
	public float getDestroySpeed(ItemStack itemstack, BlockState blockstate) {
		return 1f;
	}

	@Override
	public boolean mineBlock(ItemStack itemstack, Level world, BlockState blockstate, BlockPos pos, LivingEntity entity) {
		itemstack.hurtAndBreak(1, entity, i -> i.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		itemstack.hurtAndBreak(1, entity, i -> i.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public int getEnchantmentValue() {
		return 15;
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null) return InteractionResult.PASS; // Evita NullPointerException

		if (!player.getCooldowns().isOnCooldown(context.getItemInHand().getItem())) {
			Level world = context.getLevel();
			BlockPos pos = context.getClickedPos();
			Direction face = context.getClickedFace(); // Obtém a face do bloco que foi clicada

			// Área de ataque fixa (raio de 3 blocos em todas as direções)
			AABB attackArea = new AABB(pos).inflate(3);
			List<LivingEntity> attackHitBox = world.getEntitiesOfClass(LivingEntity.class, attackArea);

			for (LivingEntity livingEntity : attackHitBox) {
				if (livingEntity != player) {
					livingEntity.hurt(DamageSource.mobAttack(player), 6f);
					livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(new Vec3(0, 2.5f, 0)));
				}
			}

			// Definir área de partículas baseada na orientação
			AABB particleArea;
			switch (face) {
				case UP, DOWN -> particleArea = new AABB(pos).inflate(1, 0, 1); // 3x1x3 (horizontal)
				case NORTH, SOUTH -> particleArea = new AABB(pos).inflate(1, 1, 0); // 3x3 na direção X-Y
				case EAST, WEST -> particleArea = new AABB(pos).inflate(0, 1, 1); // 3x3 na direção Z-Y
				default -> particleArea = new AABB(pos); // Fallback
			}

			// Gerar partículas de "break" na área definida
			for (BlockPos p : BlockPos.betweenClosed(
					(int) particleArea.minX, (int) particleArea.minY, (int) particleArea.minZ,
					(int) particleArea.maxX, (int) particleArea.maxY, (int) particleArea.maxZ)) {
				world.levelEvent(2001, p, Block.getId(world.getBlockState(p)));
			}

			// Adiciona um cooldown de 6 segundos (120 ticks)
			player.getCooldowns().addCooldown(context.getItemInHand().getItem(), 120);

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}



	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
		if (equipmentSlot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getAttributeModifiers(equipmentSlot, stack));
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", AttackDamage(), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", AttackSpeed(), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_REACH_UUID, "Tool modifier", 0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL));
			return builder.build();
		}
		return super.getAttributeModifiers(equipmentSlot, stack);
	}

	@Override
	public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
		return super.getDefaultAttributeModifiers(equipmentSlot);
	}
}

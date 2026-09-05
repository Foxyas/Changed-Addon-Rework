package net.foxyas.changedaddon.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TheDecimatorItem extends Item {

    private static final UUID BASE_ATTACK_REACH_UUID = UUID.fromString("fa02d244-9771-415c-8789-fd03b5252c8c");

    public TheDecimatorItem() {
        super(new Item.Properties().durability(1025));
    }

    private float attackDamage() {
        return -2f + 15f;
    }

    private float attackSpeed() {
        return -4f + 0.8f;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemstack, @NotNull BlockState blockstate) {
        return 1.5f;
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull BlockState blockstate, @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            itemstack.hurtAndBreak(1, entity, i -> i.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemstack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        Player player = (Player) attacker;
        if (!player.getAbilities().instabuild) {
            // Danifica o item na mão
            itemstack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.SHARPNESS) {
            return false;
        }

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return super.canPerformAction(stack, toolAction);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        if (player.getAttackStrengthScale(0.0f) >= 0.9 && player.onGround()) {
            if (player.isSprinting()) {
                if (player.isSprinting() && player.getAttackStrengthScale(0f) >= 0.9f) {
                    Vec3 knockback = player.position().subtract(target.position()).normalize();
                    float pStrength = 2f;
                    if (target instanceof LivingEntity livingEntity) {
                        livingEntity.knockback(pStrength, knockback.x, knockback.z);
                    } else {
                        Vec3 targetDeltaMovement = target.getDeltaMovement();
                        Vec3 motion = new Vec3(
                                targetDeltaMovement.x / 2.0D - knockback.x,
                                target.onGround() ? Math.min(0.4D, targetDeltaMovement.y / 2.0D + pStrength) : targetDeltaMovement.y,
                                targetDeltaMovement.z / 2.0D - knockback.z
                        );
                        target.push(motion.x, motion.y, motion.z);
                    }
                    target.hurt(player.level().damageSources().mobAttack(player), attackDamage() * 0.25f);
                }
            }
            // ⚔ Área de efeito: Raio de 1.5 blocos ao redor do alvo
            double radius = 1.25;
            AABB attackArea = target.getBoundingBox().inflate(radius, 0.25, radius);
            List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(LivingEntity.class, attackArea);
            // 🔥 Knockback em todos os alvos próximos (exceto o atacante)
            double entityReachSq = Mth.square(player.getEntityReach()); // Use entity reach instead of constant 9.0. Vanilla uses bottom center-to-center checks here, so don't update this to use canReach, since it uses closest-corner checks.
            for (LivingEntity livingEntity : nearbyEntities) {
                if (livingEntity != target && livingEntity != player && !player.isAlliedTo(livingEntity)
                        && (!(livingEntity instanceof ArmorStand) || !((ArmorStand) livingEntity).isMarker())
                        && player.distanceToSqr(livingEntity) < entityReachSq) {
                    Vec3 knockbackVec = target.position().subtract(livingEntity.position()).normalize();
                    // 🏹 Knockback horizontal (respeita resistência)
                    livingEntity.knockback(0.8, knockbackVec.x, knockbackVec.z);
                    livingEntity.hurt(player.level().damageSources().mobAttack(player), 7f / nearbyEntities.size());
                }
            }
            // 💥 Partículas para indicar o ataque em área
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ChangedAddonSoundEvents.HAMMER_SWING.get(), SoundSource.PLAYERS, 1f, 1f);
            double d0 = (double) (-Mth.sin(player.getYRot() * 0.017453292F)) * 1;
            double d1 = (double) Mth.cos(player.getYRot() * 0.017453292F) * 1;
            Level var7 = player.level();
            if (var7 instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5), player.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            }
            //((ServerLevel) player.level).sendParticles(ParticleTypes.SWEEP_ATTACK, target.getX(), target.getY() + 1, target.getZ(), 1, 0, 0, 0, 0);
        }
        return super.onLeftClickEntity(stack, player, target);
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null)
            return InteractionResult.PASS; // Evita NullPointerException
        if (!player.getCooldowns().isOnCooldown(context.getItemInHand().getItem())) {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Direction face = context.getClickedFace(); // Obtém a face do bloco que foi clicada
            // Área de ataque fixa (raio de 3 blocos em todas as direções)
            AABB attackArea = new AABB(pos).inflate(3);
            List<LivingEntity> attackHitBox = world.getEntitiesOfClass(LivingEntity.class, attackArea);
            for (LivingEntity livingEntity : attackHitBox) {
                if (livingEntity != player) {
                    livingEntity.hurt(player.level().damageSources().mobAttack(player), 6.5f);
                }
                Vec3 vecMath = livingEntity.position().subtract(Vec3.atCenterOf(pos)).normalize();
                var distance = vecMath.length();
                Vec3 newVec = new Vec3(vecMath.x() / Math.max(0.75f, distance), vecMath.y() / Math.max(0.75f, distance), vecMath.z() / Math.max(0.75f, distance));
                livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(newVec));
            }
            // Definir AABB manualmente conforme a face clicada
            AABB particleArea = getArea(face, pos);
            int radius = 2;
            int radiusY = 2;
            // 🔥 Gerar partículas de "break" dentro da área
            for (BlockPos p : BlockPos.betweenClosed((int) particleArea.minX, (int) particleArea.minY, (int) particleArea.minZ, (int) particleArea.maxX, (int) particleArea.maxY, (int) particleArea.maxZ)) {
                double dx = (p.getX() - pos.getX()) / (double) radius;
                double dy = (p.getY() - pos.getY()) / (double) radiusY;
                double dz = (p.getZ() - pos.getZ()) / (double) radius;
                double distanceSq = dx * dx + dy * dy + dz * dz;
                if (distanceSq <= 1.0) {
                    world.levelEvent(2001, p, Block.getId(world.getBlockState(p)));
                }
            }
            // 🔊 Reproduzir som de explosão no local
            world.playSound(null, pos, ChangedAddonSoundEvents.HAMMER_GUN_SHOT.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            // 💥 Criar uma partícula de explosão centralizada
            Vec3 center = Vec3.atCenterOf(pos);
            world.addParticle(ParticleTypes.EXPLOSION, center.x, center.y, center.z, 0, 0, 0);
            // ⏳ Adiciona um cooldown de 6 segundos (120 ticks)
            player.getCooldowns().addCooldown(context.getItemInHand().getItem(), 120);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @NotNull
    private AABB getArea(Direction face, BlockPos pos) {
        AABB particleArea;
        switch (face) {
            case UP, DOWN -> particleArea = new AABB(pos.getX() - 2, pos.getY(), pos.getZ() - 2, // MinPos
                    pos.getX() + 2, pos.getY(), pos.getZ() + 2 // MaxPos
            ); // 3x1x3 (horizontal)
            case NORTH, SOUTH -> particleArea = new AABB(pos.getX() - 2, pos.getY() - 2, pos.getZ(), // MinPos
                    pos.getX() + 2, pos.getY() + 2, pos.getZ() // MaxPos
            ); // 3x3 na direção X-Y
            case EAST, WEST -> particleArea = new AABB(pos.getX(), pos.getY() - 2, pos.getZ() - 2, // MinPos
                    pos.getX(), pos.getY() + 2, pos.getZ() + 2 // MaxPos
            ); // 3x3 na direção Z-Y
            default -> particleArea = new AABB(pos); // Fallback (1 bloco)
        }
        return particleArea;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
        if (equipmentSlot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(equipmentSlot, stack);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(equipmentSlot, stack));
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed(), AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(BASE_ATTACK_REACH_UUID, "Tool modifier", 0.5f, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ATTACK_REACH_UUID, "Tool modifier", 0.5f, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }
}

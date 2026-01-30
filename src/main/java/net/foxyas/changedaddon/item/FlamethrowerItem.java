package net.foxyas.changedaddon.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlamethrowerItem extends FlamethrowerLike {

    public FlamethrowerItem() {
        super(new Properties().durability(320).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int ticks) {
        super.onUseTick(level, entity, stack, ticks);
        if (!(entity instanceof Player player)) {
            return;
        }

        if (stack.getDamageValue() >= stack.getMaxDamage() - 1 || entity.isShiftKeyDown()) {
            BlockHitResult hitResult = level.clip(new ClipContext(player.getEyePosition(1.0F), // Posição inicial (olhos do jogador)
                    player.getEyePosition(1.0F).add(player.getLookAngle().scale(5.0)), // Posição final (olhando 5 blocos à frente)
                    ClipContext.Block.OUTLINE, // Modo de colisão com blocos
                    ClipContext.Fluid.ANY, // Considera apenas fontes de fluido
                    player // Entidade que está fazendo o ray trace
            ));
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                BlockState state = level.getBlockState(pos);
                if (state.getFluidState().is(Fluids.LAVA) || state.getFluidState().is(Fluids.FLOWING_LAVA)) {
                    stack.setDamageValue(0);
                    entity.playSound(SoundEvents.BUCKET_FILL, 1f, 1f);
                }
            }
            entity.stopUsingItem(); // Stop before breaking
            return;
        }

        if (level.isClientSide)
            return;

        stack.hurtAndBreak(1, entity, (livingEntity) -> livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND));

        shoot(player.level(), player, 16, 2, 2);
    }

    @Override
    protected ParticleOptions particle() {
        return ParticleTypes.FLAME;
    }

    protected void applyEffectToEntities(ServerLevel level, Player player, List<Vec3> gasVolume) {
        Set<LivingEntity> affected = new HashSet<>();

        for (Vec3 pos : gasVolume) {
            AABB area = new AABB(pos, pos).inflate(1);

            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);

            for (LivingEntity entity : entities) {
                if (!player.canAttack(entity)) continue;
                if (player.isAlliedTo(entity)) continue;

                // evita dano duplicado exagerado
                if (!affected.add(entity)) continue;
                affectEntity(player, entity);
            }
        }
    }

    @Override
    protected void affectEntity(Player shooter, LivingEntity entity) {//needs a new damage type to be ranged
        entity.hurt(new DamageSource(shooter.damageSources().onFire().typeHolder(), shooter), 6);
        entity.setSecondsOnFire(5);
    }
}

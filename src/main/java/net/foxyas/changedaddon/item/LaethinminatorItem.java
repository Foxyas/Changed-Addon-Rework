package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.foxyas.changedaddon.init.ChangedAddonFluids;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class LaethinminatorItem extends FlamethrowerLike {

    public LaethinminatorItem() {
        super(new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .durability(320).rarity(Rarity.UNCOMMON));
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
                if (state.getFluidState().is(ChangedAddonFluids.LITIX_CAMONIA_FLUID.get()) || state.getFluidState().is(ChangedAddonFluids.FLOWING_LITIX_CAMONIA_FLUID.get())) {
                    stack.setDamageValue(0);
                    entity.playSound(SoundEvents.BUCKET_FILL, 1f, 1f);
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

        shoot(player.level(), player, 16, 5, 5);
    }

    @Override
    protected ParticleOptions particle() {
        return ChangedParticles.gas(Color3.fromInt(-1));
    }

    @Override
    protected void affectEntity(Player shooter, LivingEntity entity) {
        DamageSource solvent = new DamageSource(
                ChangedAddonDamageSources.LATEX_SOLVENT
                        .source(shooter.level())
                        .typeHolder(),
                shooter
        ) {
            @Override
            public boolean is(@NotNull TagKey<DamageType> tag) {
                return tag == DamageTypeTags.IS_PROJECTILE || super.is(tag);
            }
        };

        entity.hurt(solvent, 4.0F);
    }
}

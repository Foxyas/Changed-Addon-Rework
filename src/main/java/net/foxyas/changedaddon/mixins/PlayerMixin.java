package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.abilities.ChangedAddonAbilities;
import net.foxyas.changedaddon.abilities.ClawsAbility;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "attack", at = @At("HEAD"))
    private void CustomClawSweepAttack(Entity entity, CallbackInfo ci) {
        //System.out.println("O attack foi disparado!");
        Player player = (Player) (Object) this;
        //System.out.println("O ataque foi feito por um jogador: " + player.getName().getString());
        if (player.getAttackStrengthScale(0.0f) < 0.9)
            return;
        //System.out.println("Ataque carregado o suficiente!");
        ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent((variantInstance -> {
            AbstractAbilityInstance abilityInstance = variantInstance.getAbilityInstance(ChangedAddonAbilities.CLAWS.get());
            if (abilityInstance != null) {
                AbstractAbility<?> clawAbility = variantInstance.getAbilityInstance(ChangedAddonAbilities.CLAWS.get()).ability;
                if (clawAbility instanceof ClawsAbility ability && ability.isActive && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                    //System.out.println("Habilidade ativada!");
                    // ⚔ Área de efeito: Raio de 1.5 blocos ao redor do alvo
                    double radius = 1;
                    AABB attackArea = entity.getBoundingBox().inflate(radius, 0.25, radius);
                    List<LivingEntity> nearbyEntities = player.level.getEntitiesOfClass(LivingEntity.class, attackArea);
                    //System.out.println("Entidades próximas: " + nearbyEntities.size());
                    float f = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    float f3 = nearbyEntities.isEmpty() ? f : f / nearbyEntities.size();
                    // 🔥 Knockback em todos os alvos próximos (exceto o atacante)
                    for (LivingEntity livingEntity : nearbyEntities) {
                        if (livingEntity != entity && livingEntity != player && (!(livingEntity instanceof ArmorStand) || !((ArmorStand) livingEntity).isMarker()) && player.canHit(livingEntity, 0)) {
                            livingEntity.knockback(0.4, Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180F)));
                            livingEntity.hurt(DamageSource.playerAttack(player), f3);
                            //System.out.println("Dano causado em " + livingEntity.getName().getString());
                        }
                    }
                    // Efeito visual
                    double d0 = (double) (-Mth.sin(player.getYRot() * 0.017453292F)) * 1;
                    double d1 = (double) Mth.cos(player.getYRot() * 0.017453292F) * 1;
                    if (player.level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5), player.getZ() + d1, 0, d0, 0.0, d1, 0.0);
                        serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.6), player.getZ() + d1, 0, d0, 0.0, d1, 0.0);
                        serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.7), player.getZ() + d1, 0, d0, 0.0, d1, 0.0);
                        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1f, 0.75f);
                    }
                }
            }
        }));

    }
}

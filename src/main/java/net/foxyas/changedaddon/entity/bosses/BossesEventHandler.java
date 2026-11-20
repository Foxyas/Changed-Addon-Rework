package net.foxyas.changedaddon.entity.bosses;


import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.client.model.animations.parameters.DodgeAnimationParameters;
import net.ltxprogrammer.changed.entity.animation.StunAnimationParameters;
import net.ltxprogrammer.changed.init.ChangedAnimationEvents;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity.getMetalPercentage;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
public class BossesEventHandler {

    @SubscribeEvent
    public static void onBossHurtEntity(LivingHurtEvent event) {
        DamageSource damageSource = event.getSource();
        Entity source = damageSource.getEntity();

        if (!(source instanceof Experiment009BossEntity boss)) return;

        if (boss.isPhase3() && !boss.isDeadOrDying()) {
            boss.heal(0.5f);
        }

        LivingEntity target = event.getEntity();
        float metalPercentage = getMetalPercentage(target);
        if (metalPercentage == 0) return;

        if (metalPercentage > 0.1f) {
            float extraDamage = 5.0f * metalPercentage;
            event.setAmount(event.getAmount() + extraDamage);

            if (!target.level().isClientSide) {
                ChangedAnimationEvents.broadcastEntityAnimation(target, ChangedAnimationEvents.SHOCK_STUN.get(), StunAnimationParameters.INSTANCE);
                target.level.playSound(null, target.getX(), target.getY(), target.getZ(),
                        ChangedSounds.TSC_WEAPON_SHOCK.get(), SoundSource.HOSTILE, 1.0f, 1.0f);
            }

            target.hurtTime = 10;
            target.hurtDuration = 10;
        }

        if (boss.burstAbilityHandle != null)
            boss.burstAbilityHandle.onDamageDealt(event.getSource(), event.getAmount());
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile self = event.getProjectile();
        HitResult hitResult = event.getRayTraceResult();
        if (!(hitResult instanceof EntityHitResult entityHitResult)) {
            return;
        }

        Entity pTarget = entityHitResult.getEntity();
        if (!pTarget.level.isClientSide()) {
            if (pTarget instanceof Experiment009BossEntity experiment009BossEntity) {
                if (!experiment009BossEntity.isVulnerableToProjectiles()) {
                    double speed = Math.min(self.getDeltaMovement().length(), 2.0f);
                    var dodgeAnimationParameters = new DodgeAnimationParameters((float) speed, 1.1f);
                    DodgeAbilityInstance.executeRandomDodgeAnimationWithFade(experiment009BossEntity, dodgeAnimationParameters);
                    experiment009BossEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, true, false));
                    event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
                }
            }
        }
    }
}

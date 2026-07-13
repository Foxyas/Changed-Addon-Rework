package net.foxyas.changedaddon.ability.handle;

import net.foxyas.changedaddon.ability.DodgeAbility;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber
public class DodgeAbilityHandle {

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        HitResult hitResult = event.getRayTraceResult();
        if (!(hitResult instanceof EntityHitResult entityHitResult)) {
            return;
        }

        Entity pTarget = entityHitResult.getEntity();
        if (!pTarget.level.isClientSide()) {
            Entity owner = projectile.getOwner();
            Entity attacker;
            attacker = Objects.requireNonNullElse(owner, projectile);
            if (pTarget instanceof ChangedEntity changedEntity && changedEntity.getUnderlyingPlayer() == null) {
                List<AbstractAbility<?>> dodgeAbilities = ChangedRegistry.ABILITY.get().getValues().stream().filter((abstractAbility -> abstractAbility instanceof DodgeAbility)).toList();
                if (dodgeAbilities.isEmpty()) return;
                for (AbstractAbility<?> ability : dodgeAbilities) {
                    if (!(ability instanceof DodgeAbility dodgeAbility)) continue;
                    DodgeAbilityInstance dodgeAbilityInstance = changedEntity.getAbilityInstance(dodgeAbility);
                    if (dodgeAbilityInstance == null) continue;
                    if (dodgeAbilityInstance.projectilesImmuneTicks > 0)
                        event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);

                    if (dodgeAbilityInstance.willDodge(projectile)) {
                        event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
                        dodgeAbilityInstance.applyDodgeEffects(changedEntity, attacker);
                        dodgeAbilityInstance.applyDodgeMovement(changedEntity, attacker);
                        break;
                    }
                    return;
                }

            }

            if (pTarget instanceof Player player) {
                TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (instance != null) {
                    List<Map.Entry<AbstractAbility<?>, AbstractAbilityInstance>> dodgeAbilityInstances = instance.abilityInstances.entrySet().stream().filter((entrySet) -> (entrySet.getKey() instanceof DodgeAbility && entrySet.getValue() instanceof DodgeAbilityInstance)).toList();
                    if (!dodgeAbilityInstances.isEmpty()) {
                        for (Map.Entry<AbstractAbility<?>, AbstractAbilityInstance> dodgeAbilities : dodgeAbilityInstances) {
                            AbstractAbility<?> key = dodgeAbilities.getKey();
                            AbstractAbilityInstance value = dodgeAbilities.getValue();
                            if (key instanceof DodgeAbility && value instanceof DodgeAbilityInstance dodgeInstance) {
                                if (dodgeInstance.projectilesImmuneTicks > 0) {
                                    event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
                                }

                                if (dodgeInstance.willDodge(projectile)) {
                                    event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
                                    dodgeInstance.applyDodgeEffects(player, attacker);
                                    dodgeInstance.applyDodgeMovement(player, attacker);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getDirectEntity();

        if (attacker == null) {
            attacker = damageSource.getEntity();
        }
        if (attacker == null) return;

        if (attacker instanceof Projectile projectile) { // Let the onProjectileImpact method Handle That
            return;
        }

        if (!target.level.isClientSide()) {
            if (target instanceof ChangedEntity dodger && dodger.getUnderlyingPlayer() == null) {
                List<AbstractAbility<?>> dodgeAbilities = ChangedRegistry.ABILITY.get().getValues().stream().filter((abstractAbility -> abstractAbility instanceof DodgeAbility)).toList();
                if (dodgeAbilities.isEmpty()) return;
                for (AbstractAbility<?> ability : dodgeAbilities) {
                    if (!(ability instanceof DodgeAbility dodgeAbility)) continue;
                    DodgeAbilityInstance dodgeInstance = dodger.getAbilityInstance(dodgeAbility);
                    if (dodgeInstance == null) continue;

                    if (dodgeInstance.getDodgeAmount() <= 0) {
                        dodgeInstance.getController().deactivateAbility();
                        continue;
                    }

                    if (dodgeInstance.willDodge(attacker)) {
                        event.setCanceled(true);
                        dodgeInstance.applyDodgeEffects(dodger.level, attacker, dodger);
                        dodgeInstance.applyDodgeMovement(dodger.level, attacker, dodger, true);
                        break;
                    }
                    return;
                }

            }

            if (target instanceof Player dodger) {
                TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(dodger);
                if (instance != null) {
                    List<Map.Entry<AbstractAbility<?>, AbstractAbilityInstance>> dodgeAbilityInstances = instance.abilityInstances.entrySet().stream().filter((entrySet) -> (entrySet.getKey() instanceof DodgeAbility && entrySet.getValue() instanceof DodgeAbilityInstance)).toList();
                    if (!dodgeAbilityInstances.isEmpty()) {
                        for (Map.Entry<AbstractAbility<?>, AbstractAbilityInstance> dodgeAbilities : dodgeAbilityInstances) {
                            AbstractAbility<?> key = dodgeAbilities.getKey();
                            AbstractAbilityInstance value = dodgeAbilities.getValue();
                            if (key instanceof DodgeAbility && value instanceof DodgeAbilityInstance dodgeInstance) {

                                if (dodgeInstance.getDodgeAmount() <= 0) {
                                    dodgeInstance.getController().deactivateAbility();
                                    continue;
                                }

                                if (dodgeInstance.willDodge(attacker)) {
                                    event.setCanceled(true);
                                    dodgeInstance.applyDodgeEffects(dodger.level, attacker, dodger);
                                    dodgeInstance.applyDodgeMovement(dodger.level, attacker, dodger, true);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

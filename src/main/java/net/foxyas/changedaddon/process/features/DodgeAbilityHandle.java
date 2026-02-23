package net.foxyas.changedaddon.process.features;

import net.foxyas.changedaddon.ability.DodgeAbility;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
        Projectile self = event.getProjectile();
        HitResult hitResult = event.getRayTraceResult();
        if (!(hitResult instanceof EntityHitResult entityHitResult)) {
            return;
        }

        Entity pTarget = entityHitResult.getEntity();
        if (!pTarget.level.isClientSide()) {
            Entity owner = self.getOwner();
            Entity attacker;
            attacker = Objects.requireNonNullElse(owner, self);
            if (pTarget instanceof ChangedEntity changedEntity && changedEntity.getUnderlyingPlayer() == null) {
                List<AbstractAbility<?>> dodgeAbilities = ChangedRegistry.ABILITY.get().getValues().stream().filter((abstractAbility -> abstractAbility instanceof DodgeAbility)).toList();
                if (dodgeAbilities.isEmpty()) return;
                for (AbstractAbility<?> ability : dodgeAbilities) {
                    if (!(ability instanceof DodgeAbility dodgeAbility)) continue;
                    DodgeAbilityInstance dodgeAbilityInstance = changedEntity.getAbilityInstance(dodgeAbility);
                    if (dodgeAbilityInstance == null) continue;
                    if (dodgeAbilityInstance.projectilesImmuneTicks > 0)
                        event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);

                    if (dodgeAbilityInstance.canUse() && dodgeAbilityInstance.canKeepUsing() && dodgeAbilityInstance.isDodgeActive()) {
                        event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
                        dodgeAbilityInstance.executeDodgeEffects(changedEntity, attacker);
                        dodgeAbilityInstance.executeDodgeHandle(changedEntity, attacker);
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

                                if (dodgeInstance.canUse() && dodgeInstance.canKeepUsing() && dodgeInstance.isDodgeActive()) {
                                    event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
                                    dodgeInstance.executeDodgeEffects(player, attacker);
                                    dodgeInstance.executeDodgeHandle(player, attacker);
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
        Entity attacker = event.getSource().getEntity();

        if (attacker == null) return;

        if (attacker instanceof Projectile projectile) {
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

                    if (dodgeInstance.canUse() && dodgeInstance.canKeepUsing() && dodgeInstance.isDodgeActive()) {
                        event.setCanceled(true);
                        dodgeInstance.executeDodgeEffects(dodger.level, attacker, dodger, event);
                        dodgeInstance.executeDodgeHandle(dodger.level, attacker, dodger, event, true);
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

                                if (dodgeInstance.canUse() && dodgeInstance.canKeepUsing() && dodgeInstance.isDodgeActive()) {
                                    event.setCanceled(true);
                                    dodgeInstance.executeDodgeEffects(dodger.level, attacker, dodger, event);
                                    dodgeInstance.executeDodgeHandle(dodger.level, attacker, dodger, event, true);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Keep this method for mixins
    private static void applyDodgeEffects(Player player, LivingEntity attacker, DodgeAbilityInstance dodge, LevelAccessor levelAccessor, LivingAttackEvent event) {
        dodge.executeDodgeEffects(levelAccessor, attacker, player, event);
    }

    private static void applyDodgeHandle(Player player, LivingEntity attacker, DodgeAbilityInstance dodge, LevelAccessor levelAccessor, LivingAttackEvent event) {
        dodge.executeDodgeHandle(levelAccessor, attacker, player, event, true);
    }


    public static void dashBackwards(Player target, boolean includeY) {
        Vec3 look = target.getLookAngle().normalize();
        Vec3 motion = look.scale(1.25);
        Vec3 finalMotion = includeY ?
                new Vec3(-motion.x, target.getDeltaMovement().y, -motion.z) :
                target.getDeltaMovement().add(-motion.x, 0, -motion.z);

        target.setDeltaMovement(finalMotion);
    }

    public static void dashInFacingDirection(LivingEntity target) {
        double yaw = Math.toRadians(target.getYRot());
        double pitch = Math.toRadians(target.getXRot());
        double x = -Math.sin(yaw);
        double y = -Math.sin(pitch);
        double z = Math.cos(yaw);
        double speed = 1.05;

        Vec3 motion = new Vec3(x * speed, y * speed, z * speed);
        target.setDeltaMovement(target.getDeltaMovement().add(motion));
    }

    private static void dodgeAwayFromAttacker(Entity dodger, Entity attacker) {
        Vec3 motion = attacker.position().subtract(dodger.position()).scale(-0.25);
        dodger.setDeltaMovement(motion.x, dodger.getDeltaMovement().y, motion.z);
    }
}

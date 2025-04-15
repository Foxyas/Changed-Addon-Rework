package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.abilities.ChangedAddonAbilities;
import net.foxyas.changedaddon.abilities.DodgeAbilityInstance;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DodgeAbilityHandleProcedure {

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        LivingEntity target = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();

        if (!(target instanceof Player player) || attacker == null)
            return;

        Level world = target.level;

        Vec3 attackerPos = attacker.position();
        Vec3 lookDirection = attacker.getLookAngle().normalize();
        Vec3 targetLookDirection = target.getLookAngle();

        double distanceBehind = 2;
        Vec3 dodgePosBehind = attackerPos.subtract(lookDirection.scale(distanceBehind));

        TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (variant == null)
            return;

        DodgeAbilityInstance dodge = (DodgeAbilityInstance) variant.abilityInstances.get(ChangedAddonAbilities.DODGE.get());
        if (dodge == null)
            return;

        if (!dodge.isDodgeActive())
            return;

        if (dodge.getDodgeAmount() <= 0) {
            dodge.setDodgeActivate(false);
            return;
        }

        if (player.invulnerableTime > 0 || player.hurtTime > 0)
            return;

        double distance = attacker.distanceTo(target);

        if (world instanceof ServerLevel serverLevel) {
            applyDodgeEffects(player, dodge, serverLevel, event);
            if (distance <= 1.5f) {
                BlockPos teleportPos = new BlockPos(dodgePosBehind.x, target.getY(), dodgePosBehind.z);
                if (world.isEmptyBlock(teleportPos) || world.isEmptyBlock(teleportPos.above())) {
                    target.teleportTo(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
                }
            } else {
                dodgeAwayFromAttacker(player, attacker);
            }

            ChangedSounds.broadcastSound(player, ChangedSounds.BOW2, 2.5f, 1);
        }
    }

    private static void applyDodgeEffects(Player player, DodgeAbilityInstance dodge, ServerLevel serverLevel, LivingAttackEvent event) {
        player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount_left", dodge.getDodgeStaminaRatio()), true);
        dodge.subDodgeAmount();
        player.invulnerableTime = 30;
        player.hurtDuration = 60;
        player.hurtTime = player.hurtDuration;
        player.causeFoodExhaustion(8f);
        event.setCanceled(true);
        spawnDodgeParticles(serverLevel, player, 0.5f, 0.3f, 0.3f, 0.3f, 10, 0.25f);
    }

    private static void spawnDodgeParticles(ServerLevel level, Entity entity, float middle, float xV, float yV, float zV, int count, float speed) {
        level.sendParticles(ParticleTypes.POOF,
                entity.getX(), entity.getY() + middle, entity.getZ(), count, xV, yV, zV, speed);
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

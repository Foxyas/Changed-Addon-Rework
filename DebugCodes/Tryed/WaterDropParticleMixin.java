package net.foxyas.changedaddon.mixins.client;

import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Particle.class)
public abstract class WaterDropParticleMixin {

    @Shadow @Final protected ClientLevel level;

    @Shadow public abstract AABB getBoundingBox();

    @Shadow protected double xd;
    @Shadow protected double yd;
    @Shadow protected double zd;
    @Shadow protected double x;
    @Shadow protected double y;
    @Shadow protected double z;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void applyMotion(CallbackInfo ci) {
        List<Entity> entities = this.level.getEntities((Entity) null, this.getBoundingBox().inflate(4), (entity -> entity instanceof Player || entity instanceof ChangedEntity));
        for (Entity entity : entities) {
            if (entity instanceof ChangedEntity changedEntity) {
                ChangedAddon$handleForChangedEntities(changedEntity);
            } else if (entity instanceof Player player) {
                ChangedAddon$handleForPlayers(player, ProcessTransfur.getPlayerTransfurVariant(player));
            }
        }

    }

    @Unique
    private void ChangedAddon$handleForChangedEntities(ChangedEntity changedEntity) {
        SimpleAbilityInstance psychicPulseAbility = changedEntity.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_PULSE.get());
        SimpleAbilityInstance psychicHoldInstance = changedEntity.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_HOLD.get());
        if (psychicPulseAbility != null) {
            if (psychicPulseAbility.getController().getHoldTicks() > 0) {
                if (PlayerUtil.isMovingTowardsEntity(this.ChangedAddon$getPos(), this.ChangedAddon$getMotion(), changedEntity)) {
                    Vec3 NegativeMotion = this.ChangedAddon$getMotion().scale(-1);
                    Vec3 Motion = NegativeMotion.multiply(1.5, 1.5, 1.5);
                    this.ChangedAddon$setMovement(Motion);
                    return;
                }
            }
        }

        if (psychicHoldInstance != null && psychicHoldInstance.getController().getHoldTicks() > 0) {
            double maxRange = 8.0;
            double repelRange = 1.5;
            double stopSpeedThreshold = 0.05;

            Vec3 particlePos = ChangedAddon$getPos();
            Vec3 motion = ChangedAddon$getMotion();
            Vec3 toEntity = changedEntity.position().subtract(particlePos);

            double distance = toEntity.length();

            if (distance > maxRange)
                return;

            // parar partículas muito lentas
            if (distance > repelRange && motion.lengthSqr() <= stopSpeedThreshold * stopSpeedThreshold) {
                ChangedAddon$setMovement(Vec3.ZERO);
                return;
            }

            Vec3 direction = toEntity.normalize();

            if (distance <= repelRange) {
                // repelir muito perto
                Vec3 repelForce = direction.scale(-0.15 * (repelRange - distance));
                ChangedAddon$setMovement(motion.add(repelForce));
            } else {

                // desacelerar se estiver indo em direção ao player
                if (motion.dot(direction) > 0) {

                    double slowFactor = Math.max(0.2, 1.0 - (distance / maxRange));
                    Vec3 reducedMotion = motion.scale(slowFactor);

                    ChangedAddon$setMovement(reducedMotion);
                }
            }
        }
    }

    @Unique
    private void ChangedAddon$handleForPlayers(Player player, TransfurVariantInstance<?> transfurVariantInstance) {
        if (transfurVariantInstance == null) {
            return;
        }

        SimpleAbilityInstance psychicPulseAbility = transfurVariantInstance.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_PULSE.get());
        SimpleAbilityInstance psychicHoldInstance = transfurVariantInstance.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_HOLD.get());
        if (psychicPulseAbility != null) {
            if (psychicPulseAbility.getController().getHoldTicks() > 0) {
                if (PlayerUtil.isMovingTowardsEntity(this.ChangedAddon$getPos(), this.ChangedAddon$getMotion(), player)) {
                    Vec3 NegativeMotion = this.ChangedAddon$getMotion().scale(-1);
                    Vec3 Motion = NegativeMotion.multiply(1.5, 1.5, 1.5);
                    this.ChangedAddon$setMovement(Motion);
                    return;
                }
            }
        }

        if (psychicHoldInstance != null && psychicHoldInstance.getController().getHoldTicks() > 0) {
            double maxRange = 8.0;
            double repelRange = 1.5;
            double stopSpeedThreshold = 0.05;

            Vec3 particlePos = ChangedAddon$getPos();
            Vec3 motion = ChangedAddon$getMotion();
            Vec3 toEntity = player.position().subtract(particlePos);

            double distance = toEntity.length();

            if (distance > maxRange)
                return;

            // parar partículas muito lentas
            if (distance > repelRange && motion.lengthSqr() <= stopSpeedThreshold * stopSpeedThreshold) {
                ChangedAddon$setMovement(Vec3.ZERO);
                return;
            }

            Vec3 direction = toEntity.normalize();

            if (distance <= repelRange) {
                // repelir muito perto
                Vec3 repelForce = direction.scale(-0.15 * (repelRange - distance));
                ChangedAddon$setMovement(motion.add(repelForce));
            } else {

                // desacelerar se estiver indo em direção ao player
                if (motion.dot(direction) > 0) {

                    double slowFactor = Math.max(0.2, 1.0 - (distance / maxRange));
                    Vec3 reducedMotion = motion.scale(slowFactor);

                    ChangedAddon$setMovement(reducedMotion);
                }
            }
        }
    }


    @Unique
    private Vec3 ChangedAddon$getPos() {
        return new Vec3(this.x, this.y, this.z);
    }

    @Unique
    private Vec3 ChangedAddon$getMotion() {
        return new Vec3(this.xd, this.yd, this.zd);
    }

    @Unique
    private void ChangedAddon$setMovement(Vec3 motion) {
        this.xd = motion.x;
        this.yd = motion.y;
        this.zd = motion.z;
    }

}

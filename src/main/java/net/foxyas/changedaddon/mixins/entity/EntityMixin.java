package net.foxyas.changedaddon.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.entity.api.LivingEntityDataExtensor;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.SeatEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements LivingEntityDataExtensor {

    @ModifyExpressionValue(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPassengerOfSameVehicle(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean stopPushWithBody(boolean original, Entity entity) {
        var self = (Entity) (Object) this;
        if (!self.level().isClientSide() && self instanceof ServerPlayer serverPlayer) {
            boolean isSpectating = serverPlayer.getCamera().is(entity);
            if (isSpectating) {
                return true;
            }
        }

        if (entity instanceof ServerPlayer serverPlayer) {
            boolean isSpectating = serverPlayer.getCamera().is(self);
            if (isSpectating) {
                return true;
            }
        }

        if (self instanceof ChangedEntity changedEntity) {
            if (entity instanceof LivingEntity livingEntity) {
                LivingEntity underlyingPlayer = EntityUtil.maybeGetUnderlying(livingEntity);

                if (underlyingPlayer instanceof ChangedEntity otherEntity) {
                    if (changedEntity.getUnderlyingPlayer() == otherEntity.getUnderlyingPlayer()) {
                     return true;
                    }
                }

                TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(underlyingPlayer));
                if (entity.is(underlyingPlayer) && variantInstance instanceof TransfurVariantInstanceExtensor extensor) {
                    if (self.is(extensor.getChangedEntityInControl())) {
                        return true;
                    }
                }
            }
        }


        return original;
    }

    @Inject(method = "isInWater", at = @At("RETURN"), cancellable = true)
    private void customIsInWater(CallbackInfoReturnable<Boolean> cir) {
        Boolean returnValue = cir.getReturnValue();
        if (returnValue != null) {
            if (!returnValue) {
                cir.setReturnValue(overrideIsInWater());
            }
        }
    }

    @ModifyReturnValue(method = "isPickable", at = @At("RETURN"))
    private boolean stopPickableIfSittingInvisibleSeat(boolean original) {
        var self = (Entity) (Object) this;
        if (self.getVehicle() instanceof SeatEntity seatEntity) {
            if (seatEntity.shouldSeatedBeInvisible()) {
                return false;
            }
        }
        return original;
    }

    @ModifyReturnValue(method = "canBeCollidedWith", at = @At("RETURN"))
    private boolean stopCollisionIfSittingInvisibleSeat(boolean original) {
        var self = (Entity) (Object) this;
        if (self.getVehicle() instanceof SeatEntity seatEntity) {
            if (seatEntity.shouldSeatedBeInvisible()) {
                return false;
            }
        }
        return original;
    }

    @ModifyReturnValue(method = "canBeHitByProjectile", at = @At("RETURN"))
    private boolean stopCanBeHitByProjectileIfSittingInvisibleSeat(boolean original) {
        var self = (Entity) (Object) this;
        if (self.getVehicle() instanceof SeatEntity seatEntity) {
            if (seatEntity.shouldSeatedBeInvisible()) {
                return false;
            }
        }
        return original;
    }

    @ModifyReturnValue(method = "isPushable", at = @At("RETURN"))
    private boolean stopPushableIfSittingInvisibleSeat(boolean original) {
        var self = (Entity) (Object) this;
        if (self.getVehicle() instanceof SeatEntity seatEntity) {
            if (seatEntity.shouldSeatedBeInvisible()) {
                return false;
            }
        }
        return original;
    }
}

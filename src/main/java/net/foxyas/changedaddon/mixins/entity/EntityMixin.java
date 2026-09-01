package net.foxyas.changedaddon.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.entity.api.LivingEntityDataExtensor;
import net.ltxprogrammer.changed.entity.SeatEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements LivingEntityDataExtensor {

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

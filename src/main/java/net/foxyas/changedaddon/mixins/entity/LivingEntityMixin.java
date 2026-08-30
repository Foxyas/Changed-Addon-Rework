package net.foxyas.changedaddon.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.ability.ToggleClimbAbilityInstance;
import net.foxyas.changedaddon.entity.api.ExtraConditions;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IFallFlyAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.entity.SeatEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IFallFlyAbleEntity {

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setXRot(F)V"), method = "tick")
    private void allowLookAroundWhenCuddling(LivingEntity instance, float v, Operation<Void> original) {
        if (!(instance instanceof Player player)) return;

        if (!ChangedAddonVariables.ofOrDefault(player).isCuddling) original.call(instance, v);
    }

    @Inject(method = "onClimbable", at = @At("HEAD"), cancellable = true)
    public void onClimbable(CallbackInfoReturnable<Boolean> callback) {
        LivingEntity self = (LivingEntity) (Object) this;
        ProcessTransfur.ifPlayerTransfurred(EntityUtil.playerOrNull(self), (variant) -> {
            AbstractAbilityInstance instance = variant.getAbilityInstance(ChangedAddonAbilities.TOGGLE_CLIMB.get());
            if (!variant.getParent().canClimb || !self.horizontalCollision) return;

            if (!(instance instanceof ToggleClimbAbilityInstance abilityInstance)) return;

            if (!(variant.getChangedEntity() instanceof ExtraConditions.Climb climb)) return;

            if (climb.canClimb()) {
                callback.setReturnValue(abilityInstance.isActivated());
            } else {
                if (callback.getReturnValue() != null && callback.getReturnValue() != true) {
                    callback.setReturnValue(false);
                }
            }
        });
    }

    @Inject(method = "getScale", at = @At("RETURN"), cancellable = true)
    private void getScaleHook(CallbackInfoReturnable<Float> cir) {
        float originalValue = cir.getReturnValue();
        var self = (LivingEntity) (Object) this;
        if (self instanceof IAlphaAbleEntity iAlphaAbleEntity && iAlphaAbleEntity.isAlpha()) {
            float alphaScale = iAlphaAbleEntity.alphaAdditionalScale();
            cir.setReturnValue(originalValue + alphaScale);
        }
    }

    @Override
    public void startToFallFlying() {
        this.setSharedFlag(7, true);
    }

    @Override
    public void stopToFallFlying() {
        this.setSharedFlag(7, true);
        this.setSharedFlag(7, false);
    }

    @ModifyReturnValue(method = "isPickable", at = @At("RETURN"))
    private boolean stopPickableIfSittingInvisibleSeat(boolean original) {
        var self = (LivingEntity) (Object) this;
        if (self.getVehicle() instanceof SeatEntity seatEntity) {
            if (seatEntity.shouldSeatedBeInvisible()) {
                return false;
            }
        }
        return original;
    }
}
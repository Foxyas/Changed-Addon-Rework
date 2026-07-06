package net.foxyas.changedaddon.mixins.entity.mobEffects;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.mobEffects.ICustomEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(net.minecraft.world.effect.MobEffectInstance.class)
public class EffectInstanceMixin {

    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/effect/MobEffectInstance;readCurativeItems(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/effect/MobEffectInstance;", remap = false
            ),
            method = "loadSpecifiedEffect"
    )
    private static MobEffectInstance specifiedEffectInstanceMixin(MobEffectInstance mobEffectInstance, CompoundTag data, Operation<MobEffectInstance> original) {
        if (mobEffectInstance.getEffect() instanceof ICustomEffectInstance<?> iCustomEffectInstance) {
            var otherInstance = iCustomEffectInstance.load(mobEffectInstance, data);
            return original.call(otherInstance, data);
        }

        return original.call(mobEffectInstance, data);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(at = @At("RETURN"), method = "writeDetailsTo")
    private void writeDetailsToInstance(CompoundTag pNbt, CallbackInfo ci) {

        MobEffectInstance instance = ((MobEffectInstance) (Object) this);
        if (!(instance.getEffect() instanceof ICustomEffectInstance iCustomEffectInstance)) {
            return;
        }
        iCustomEffectInstance.save(instance, pNbt);
    }
}
package net.foxyas.changedaddon.mixins.entity.variant;

import net.ltxprogrammer.changed.server.ServerTransfurVariantInstance;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerTransfurVariantInstance.class, remap = false)
public class ServerTransfurVariantInstanceMixin {

    @Inject(method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;moveTo(Lnet/minecraft/world/level/pathfinder/Path;D)Z",
                    shift = At.Shift.BY , remap = true),
            cancellable = true)
    private void tickInjector(CallbackInfo ci) {
        ServerTransfurVariantInstance<?> self = (ServerTransfurVariantInstance<?>) (Object) this;
        if (self.getHost().hasEffect(MobEffects.HERO_OF_THE_VILLAGE)){
            ci.cancel();
        }
    }

    @Inject(method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;setSpeedModifier(D)V",
                    shift = At.Shift.BY, remap = true),
            cancellable = true)
    private void tickInjectorSpeedModifier(CallbackInfo ci) {
        ServerTransfurVariantInstance<?> self = (ServerTransfurVariantInstance<?>) (Object) this;
        if (self.getHost().hasEffect(MobEffects.HERO_OF_THE_VILLAGE)){
            ci.cancel();
        }
    }

}

package net.foxyas.changedaddon.mixins.entity.variant;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.server.ServerTransfurVariantInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerTransfurVariantInstance.class, remap = false)
public abstract class ServerTransfurVariantInstanceMixin<T extends ChangedEntity> extends TransfurVariantInstance<T> {

    public ServerTransfurVariantInstanceMixin(TransfurVariant<T> parent, Player host) {
        super(parent, host);
    }

    @WrapOperation(method = "tickScare",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;moveTo(Lnet/minecraft/world/level/pathfinder/Path;D)Z",
                    shift = At.Shift.BY, remap = true)
    )
    private boolean tickInjector(PathNavigation instance, Path path, double speed, Operation<Boolean> original, @Local(name = "v") PathfinderMob v) {
//        if (v.isSleeping()) {
//            return false;
//        }
        ServerTransfurVariantInstance<?> self = (ServerTransfurVariantInstance<?>) (Object) this;
        if (self.getHost().hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            return false;
        } else if (self.getHost().hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
            return false;
        }
        return original.call(instance, path, speed);
    }

    @Inject(method = "tickScare",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;setSpeedModifier(D)V",
                    shift = At.Shift.BY, remap = true),
            cancellable = true)
    private void tickInjectorSpeedModifier(CallbackInfo ci) {
        ServerTransfurVariantInstance<?> self = (ServerTransfurVariantInstance<?>) (Object) this;
        if (self.getHost().hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            ci.cancel();
        } else if (self.getHost().hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
            ci.cancel();
        }
    }

}

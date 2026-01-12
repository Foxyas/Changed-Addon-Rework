package net.foxyas.changedaddon.mixins.mods.changed;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.event.TransfurVariantEvents;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = TransfurVariant.class, remap = false)
public abstract class TransfurVariantMixin {

    @Inject(method = "replaceEntity(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Lnet/ltxprogrammer/changed/ability/IAbstractChangedEntity;", at = @At("RETURN"), cancellable = true)
    private void injectReplaceEntity(LivingEntity entity, LivingEntity cause, CallbackInfoReturnable<IAbstractChangedEntity> cir) {
        TransfurVariantEvents.KillAfterTransfurredEvent event = new TransfurVariantEvents.KillAfterTransfurredEvent(entity, cause);
        if (ChangedAddonMod.postEvent(event)) {
            cir.cancel();
        }
    }

    @ModifyReturnValue(method = "replaceEntity(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/ability/IAbstractChangedEntity;)Lnet/ltxprogrammer/changed/ability/IAbstractChangedEntity;", at = @At("RETURN"))
    private IAbstractChangedEntity injectReplaceEntity(IAbstractChangedEntity original, LivingEntity entity, @Nullable IAbstractChangedEntity cause) {
        TransfurVariantEvents.KillAfterTransfurredSpecificEvent event = new TransfurVariantEvents.KillAfterTransfurredSpecificEvent(entity, cause);
        if (ChangedAddonMod.postEvent(event)) {
            return event.getiAbstractChangedEntity();
        }
        return original;
    }

    @ModifyReturnValue(method = "spawnAtEntity", at = @At("RETURN"))
    private ChangedEntity injectSpawnAtEntity(ChangedEntity original, LivingEntity spawnAt) {
        TransfurVariantEvents.SpawnAtTransfurredEntityEvent event = new TransfurVariantEvents.SpawnAtTransfurredEntityEvent(spawnAt, original);
        if (ChangedAddonMod.postEvent(event)) {
            return event.changedEntity;
        }
        return original;
    }

}

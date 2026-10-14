package net.foxyas.changedaddon.mixins.mods.changed;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.event.TransfurEvents;
import net.foxyas.changedaddon.mixins.entity.LivingEntityAccessor;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LatexAssimilationDecision.class, remap = false)
public abstract class LatexAssimilationDecisionMixin {

    @Shadow
    public abstract DamageSource getDamageSource(RegistryAccess registryAccess);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;discard()V", remap = true),
            method = "lambda$transfurByAbsorption$8")
    private void dropEquipment(LivingEntity target, IAbstractChangedEntity transfurSource, CallbackInfoReturnable<IAbstractChangedEntity> cir) {
        LivingEntityAccessor access = (LivingEntityAccessor) target;
        access._dropEquipment();
        access._dropCustomDeathLoot(getDamageSource(target.level().registryAccess()), 0, transfurSource.isPlayer());
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;onAbsorbEntity(Lnet/ltxprogrammer/changed/ability/IAbstractChangedEntity;)V", remap = true),
            method = "lambda$transfurByAbsorption$8", cancellable = true)
    private void specificOnAssimilateEntity(LivingEntity target, IAbstractChangedEntity transfurSource, CallbackInfoReturnable<IAbstractChangedEntity> cir) {
        TransfurEvents.AbsorbedEntitySpecificEvent absorbedEntitySpecificEvent = new TransfurEvents.AbsorbedEntitySpecificEvent(target, transfurSource);
        if (ChangedAddonMod.postEvent(absorbedEntitySpecificEvent)) {
            cir.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;onAssimilateEntity(Lnet/ltxprogrammer/changed/ability/IAbstractChangedEntity;)V"),
            method = "lambda$transfurByReplication$7", cancellable = true)
    private void specificOnAbsorbEntity(LivingEntity target, IAbstractChangedEntity transfurSource, CallbackInfoReturnable<IAbstractChangedEntity> cir) {
        TransfurEvents.AssimilatedEntitySpecificEvent assimilatedEntitySpecificEvent = new TransfurEvents.AssimilatedEntitySpecificEvent(target, transfurSource);
        if (ChangedAddonMod.postEvent(assimilatedEntitySpecificEvent)) {
            cir.cancel();
        }
    }
}

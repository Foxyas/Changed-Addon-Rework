package net.foxyas.changedaddon.mixins.mods.changed;

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

@Mixin(LatexAssimilationDecision.class)
public abstract class LatexAssimilationDecisionMixin {

    @Shadow
    public abstract DamageSource getDamageSource(RegistryAccess registryAccess);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;discard()V"),
            method = "lambda$transfurByAbsorption$8")
    private void dropEquipment(LivingEntity target, IAbstractChangedEntity transfurSource, CallbackInfoReturnable<IAbstractChangedEntity> cir) {
        LivingEntityAccessor access = (LivingEntityAccessor) target;
        access._dropEquipment();
        access._dropCustomDeathLoot(getDamageSource(target.level().registryAccess()), 0, transfurSource.isPlayer());
    }
}

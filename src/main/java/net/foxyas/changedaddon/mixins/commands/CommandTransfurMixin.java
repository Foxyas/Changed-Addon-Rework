package net.foxyas.changedaddon.mixins.commands;

import com.llamalad7.mixinextras.sugar.Local;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.ai.AssimilationBehavior;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedFusions;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChangedFusions.class, remap = false)
public class CommandTransfurMixin {

    @Inject(method = "getFusionBehavior", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z", ordinal = 2, shift = At.Shift.AFTER), cancellable = true)
    private void stopCall(LivingEntity assimVictim,
                          TransfurContext transfurContext,
                          CallbackInfoReturnable<AssimilationBehavior> cir) {
        if (transfurContext.source().left().get().isPlayer() && !ProcessTransfur.isPlayerTransfurred(EntityUtil.playerOrNull(transfurContext.source().left().get().getEntity()))) {
            cir.setReturnValue(null);
        }
    }
}

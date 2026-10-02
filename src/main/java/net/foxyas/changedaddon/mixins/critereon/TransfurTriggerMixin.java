package net.foxyas.changedaddon.mixins.critereon;

import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.ltxprogrammer.changed.advancements.critereon.TransfurTrigger;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TransfurTrigger.class, remap = false)
public class TransfurTriggerMixin {

    @Inject(method = "trigger", at = @At("RETURN"))
    private void dynamicTransfurTriggerHook(ServerPlayer player, TransfurVariantInstance<?> form, CallbackInfo ci) {
        ChangedAddonCriteriaTriggers.DYNAMIC_TRANSFUR_TRIGGER.trigger(player, form);
    }
}

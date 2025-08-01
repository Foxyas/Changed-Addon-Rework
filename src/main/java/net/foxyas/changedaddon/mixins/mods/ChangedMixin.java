package net.foxyas.changedaddon.mixins.mods;

import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.Changed;
import net.minecraftforge.eventbus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Changed.class, remap = false)
public class ChangedMixin {
    @Inject(method = "registerLoadingEventListeners", at = @At("HEAD"), cancellable = true)
    private void CustomAbilitiesCode(IEventBus eventBus, CallbackInfo ci){
      eventBus.addListener(ChangedAddonAbilities::addUniversalAbilities);
    }
}

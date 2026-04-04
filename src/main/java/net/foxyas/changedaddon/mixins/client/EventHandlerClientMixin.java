package net.foxyas.changedaddon.mixins.client;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.client.EventHandlerClient;
import net.minecraftforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EventHandlerClient.class, remap = false)
public abstract class EventHandlerClientMixin {

    @Inject(at = @At(value = "HEAD"), method = "lambda$onInputEvent$0", cancellable = true)
    private static void allowBlockInteract(InputEvent.InteractionKeyMappingTriggered event, GrabEntityAbilityInstance ability, CallbackInfo ci) {
        if (event.isUseItem() && ability.grabbedEntity != null && ((GrabEntityAbilityExtensor)ability).isSafeMode()) ci.cancel();
    }
}

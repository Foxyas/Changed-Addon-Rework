package net.foxyas.changedaddon.mixins.entity.variant;

import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.client.LocalPlayerAccessor;
import net.ltxprogrammer.changed.client.LocalTransfurVariantInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LocalTransfurVariantInstance.class, remap = false)
public abstract class LostControl$LocalTransfurVariantInstanceMixin<T extends ChangedEntity> implements TransfurVariantInstanceExtensor {

    @Shadow
    @Final
    private LocalPlayer host;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/client/ClientTransfurVariantInstance;tick()V", shift = At.Shift.AFTER))
    private void lostControl$makeHandsBusy(CallbackInfo ci) {
        ChangedEntity entityInControl = this.getChangedEntityInControl();
        if (!this.hasControlOverBody() && !this.host.isSpectator()) {
            if (entityInControl != null) {
                if (entityInControl.isRemoved() || entityInControl.isDeadOrDying()) {
                    return;
                }

                ((LocalPlayerAccessor) this.host).setHandsBusy(true);
            }
        }
    }

    @Unique
    private void ChangedAddon$resetHostHands() {
        ((LocalPlayerAccessor) this.host).setHandsBusy(false);
    }

    @Override
    public void onControlOverBodyUpdate(boolean inControl) {
        if (!inControl) {
            ChangedAddon$resetHostHands();
        }
    }
}

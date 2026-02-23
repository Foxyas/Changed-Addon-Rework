package net.foxyas.changedaddon.mixins.abilities;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GrabEntityPacket.class, remap = false)
public class GrabEntityPacketMixin {

    @Shadow
    @Final
    public GrabEntityPacket.GrabType type;

    @ModifyExpressionValue(
            method = "lambda$handle$4",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;isPlayerNotLatex(Lnet/minecraft/world/entity/player/Player;)Z"
            )
    )
    private boolean changedAddon$overrideIsPlayerNotLatex(boolean original) {
        return false;
    }

    @Inject(
            method = "lambda$handle$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/ltxprogrammer/changed/init/ChangedSounds;broadcastSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraftforge/registries/RegistryObject;FF)V", shift = At.Shift.BY
            ), cancellable = true
    )
    private void changedAddon$playSound(ServerPlayer sender, LivingEntity livingTarget, TransfurVariantInstance<?> variant, CallbackInfo ci) {
        if (this.type == GrabEntityPacket.GrabType.ARMS) {
            GrabEntityAbilityInstance ability = variant.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
            if (ability instanceof GrabEntityAbilityExtensor abilityExtensor && abilityExtensor.isSafeMode()) {
                ci.cancel();
                ChangedSounds.broadcastSound(sender, ChangedAddonSoundEvents.PLUSHY_SOUND, 1.0F, 1.0F);
            }
        }
    }
}

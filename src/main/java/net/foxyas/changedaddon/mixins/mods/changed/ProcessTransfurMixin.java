package net.foxyas.changedaddon.mixins.mods.changed;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.simple.WolfyEntity;
import net.foxyas.changedaddon.event.ProgressTransfurEvents;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ProcessTransfur.class, remap = false)
public class ProcessTransfurMixin {

    @Inject(method = "tickPlayerTransfurProgress", at = @At("HEAD"), cancellable = true)
    private static void InjectTick(Player player, CallbackInfo ci) {
        ProgressTransfurEvents.TickPlayerTransfurProgressEvent event = new ProgressTransfurEvents.TickPlayerTransfurProgressEvent(player);
        if (ChangedAddonMod.postEvent(event)) {
            ci.cancel();
        }
    }

    @Inject(method = "onNewlyTransfurred", at = @At("HEAD"), cancellable = true)
    private static void onNewlyTransfuredHook(IAbstractChangedEntity entity, CallbackInfo ci) {
        ProgressTransfurEvents.NewlyTransfurred event = new ProgressTransfurEvents.NewlyTransfurred(entity);
        if (ChangedAddonMod.postEvent(event)) {
            ci.cancel();
        }
    }

    @Inject(method = "progressTransfur(Lnet/minecraft/world/entity/LivingEntity;FLnet/ltxprogrammer/changed/entity/variant/TransfurVariant;Lnet/ltxprogrammer/changed/entity/TransfurContext;)Z", at = @At("RETURN"), cancellable = true)
    private static void progressTransfurEventHook(LivingEntity entity, float amount, TransfurVariant<?> transfurVariant, TransfurContext context, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() != null) {
            ProgressTransfurEvents.ProgressTransfurEvent event = new ProgressTransfurEvents.ProgressTransfurEvent(entity, amount, transfurVariant, context, cir.getReturnValue());
            if (ChangedAddonMod.postEvent(event)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "onLivingAttacked", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/living/LivingAttackEvent;setCanceled(Z)V", ordinal = 3), cancellable = true)
    private static void cancelDamageHook(LivingAttackEvent event, CallbackInfo ci) {
        if (!(event.getSource().getEntity() instanceof LivingEntity sourceEntity))
            return;

        if (sourceEntity instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof WolfyEntity) {
                ci.cancel();
            }
        }
        if (sourceEntity instanceof WolfyEntity) {
            ci.cancel();
        }
    }

//    @Inject(method = "removePlayerTransfurVariant", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;setPlayerTransfurVariant(Lnet/minecraft/world/entity/player/Player;Lnet/ltxprogrammer/changed/entity/variant/TransfurVariant;Lnet/ltxprogrammer/changed/entity/TransfurContext;F)Lnet/ltxprogrammer/changed/entity/variant/TransfurVariantInstance;"), cancellable = true)
//    private static void removePlayerTransfurVariantHook(Player player, CallbackInfo ci) {
//        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
//        TransfurVariant<?> transfurVariant = null;
//        if (transfurVariantInstance != null) transfurVariant = transfurVariantInstance.getParent();
//        UntransfurEvent untransfurEvent = new UntransfurEvent(player, transfurVariant, UntransfurEvent.UntransfurType.SURVIVAL);
//        if (ChangedAddonMod.postEvent(untransfurEvent)) {
//            if (untransfurEvent.newVariant != null) {
//                ProcessTransfur.setPlayerTransfurVariant(player, untransfurEvent.newVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), 1, false);
//                ci.cancel();
//                return;
//            }
//            ci.cancel();
//        }
//    }

}

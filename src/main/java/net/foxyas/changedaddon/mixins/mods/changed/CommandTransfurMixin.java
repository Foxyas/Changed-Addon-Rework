package net.foxyas.changedaddon.mixins.mods.changed;

import net.ltxprogrammer.changed.command.CommandTransfur;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = CommandTransfur.class, remap = false)
public class CommandTransfurMixin {

//    @Inject(method = "untransfurPlayer", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;ifPlayerTransfurred(Lnet/minecraft/world/entity/player/Player;Ljava/util/function/Consumer;)Z"), cancellable = true)
//    private static void UntransfurPlayerHook(CommandSourceStack source, ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
//        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
//        TransfurVariant<?> transfurVariant = null;
//        if (transfurVariantInstance != null) transfurVariant = transfurVariantInstance.getParent();
//        UntransfurEvent untransfurEvent = new UntransfurEvent(player, transfurVariant, UntransfurEvent.UntransfurType.COMMAND);
//        if (ChangedAddonMod.postEvent(untransfurEvent)) {
//            if (untransfurEvent.newVariant != null) {
//                ProcessTransfur.setPlayerTransfurVariant(player, untransfurEvent.newVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), 1, false);
//                cir.cancel();
//                return;
//            }
//            cir.cancel();
//        }
//    }
}
package net.foxyas.changedaddon.mixins.items;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.item.LatexFlask;
import net.ltxprogrammer.changed.item.LatexSyringe;
import net.ltxprogrammer.changed.item.Syringe;
import net.ltxprogrammer.changed.util.UniversalDist;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(value = LatexFlask.class, remap = false)
public class LatexFlaskMixin {

    @Inject(method = "fillItemList", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/entity/variant/TransfurVariant;getPublicTransfurVariants()Ljava/util/stream/Stream;", shift = At.Shift.AFTER))
    private void fillItemListHook(Predicate<TransfurVariant<?>> predicate, CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output, CallbackInfo ci) {
        var self = (LatexFlask) (Object) this;
        TransfurVariant.getPublicTransfurVariants()
                .filter((transfurVariant -> transfurVariant.getFormId().getNamespace().equals(ChangedAddonMod.MODID)))
                .forEach((variant) -> output.accept(Syringe.setOwner(Syringe.setPureVariant(new ItemStack(self), variant.getFormId()), UniversalDist.getLocalPlayer())));
    }
}

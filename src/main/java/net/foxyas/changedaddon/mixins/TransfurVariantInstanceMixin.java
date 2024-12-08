package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.item.DarkLatexCoatItem;
import net.foxyas.changedaddon.item.HazmatSuitItem;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TransfurVariantInstance.class, remap = false)
public abstract class TransfurVariantInstanceMixin {

    @Shadow public abstract TransfurVariant<?> getParent();

    @Inject(method = "canWear", at = @At("HEAD"), cancellable = true)
    private void negateArmor(Player player, ItemStack itemStack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir){
        if (itemStack.getItem() instanceof HazmatSuitItem || itemStack.getItem() instanceof DarkLatexCoatItem){
            cir.setReturnValue(false);
        }
    }


    @Inject(method = "canWear", at = @At("HEAD"), cancellable = true)
    private void negateArmorForms(Player player, ItemStack itemStack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir){
        if (this.getParent() == ChangedAddonTransfurVariants.LATEX_SNEP_FERAL_FORM.get() || this.getParent() == ChangedAddonTransfurVariants.LATEX_SNEP.get()){
            cir.setReturnValue(false);
        }
    }
}

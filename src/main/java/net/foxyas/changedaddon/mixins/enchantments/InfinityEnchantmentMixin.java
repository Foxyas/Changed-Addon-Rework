package net.foxyas.changedaddon.mixins.enchantments;

import net.foxyas.changedaddon.item.DarkLatexSprayItem;
import net.foxyas.changedaddon.item.LitixCamoniaSprayItem;
import net.foxyas.changedaddon.item.WhiteLatexSprayItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ArrowInfiniteEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Enchantment.class, remap = true)
public abstract class InfinityEnchantmentMixin {


    @Inject(method = "canEnchant",at = @At("TAIL"),cancellable = true)
    public void InfinityMixin_2(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir){
        if ((Enchantment) (Object) this instanceof ArrowInfiniteEnchantment){
            if (itemStack.getItem() instanceof DarkLatexSprayItem
                    || itemStack.getItem() instanceof WhiteLatexSprayItem
                    || itemStack.getItem() instanceof LitixCamoniaSprayItem) {
                cir.setReturnValue(true);
            }
        }
    }

}

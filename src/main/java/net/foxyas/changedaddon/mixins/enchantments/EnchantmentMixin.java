package net.foxyas.changedaddon.mixins.enchantments;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Enchantment.class, remap = true)
public abstract class EnchantmentMixin {

    @Inject(method = "canEnchant",at = @At("TAIL"),cancellable = true)
    public void InfinityMixin_2(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir){
    }

}

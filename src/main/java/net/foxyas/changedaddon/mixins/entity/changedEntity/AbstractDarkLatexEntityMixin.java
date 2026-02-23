package net.foxyas.changedaddon.mixins.entity.changedEntity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AbstractDarkLatexEntity.class, remap = false)
public class AbstractDarkLatexEntityMixin {

    @ModifyReturnValue(method = "isTameItem", at = @At("RETURN"))
    private boolean orGoldenOrange(boolean original, ItemStack stack) {
        if (stack.is(ChangedAddonItems.GOLDEN_ORANGE.get())) return true;
        return original;
    }
}

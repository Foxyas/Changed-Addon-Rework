package net.foxyas.changedaddon.mixins.entity.variant;

import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.item.loot.RandomVariantFunction;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = RandomVariantFunction.Builder.class, remap = false)
public class RandomVariantFunctionMixin {

    @Shadow
    private List<ResourceLocation> variants;

    @Inject(method = "withAllVariants", at = @At(value = "RETURN"))
    private void RemoveVariant(CallbackInfoReturnable<Boolean> cir) {
        // Obtém a lista de variantes a serem removidas
        List<ResourceLocation> list = ChangedAddonTransfurVariants.getRemovedVariantsList()
                .stream()
                .map(TransfurVariant::getFormId)
                .toList();

        // Remove as variantes se elas existirem na lista
        variants.removeIf(list::contains);
    }
}


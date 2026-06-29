package net.foxyas.changedaddon.mixins.entity.variant;

import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.item.loot.RandomVariantFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = RandomVariantFunction.Builder.class, remap = false)
public abstract class RandomVariantFunctionMixin extends LootItemConditionalFunction.Builder<RandomVariantFunction.Builder> {

    @Shadow
    private List<ResourceLocation> variants;

    @Inject(method = "withAllVariants", at = @At(value = "RETURN"))
    private void RemoveVariant(CallbackInfoReturnable<Boolean> cir) {
        // Obtém a lista de variantes a serem removidas
        List<ResourceLocation> list = ChangedAddonTransfurVariants.getHardCodedRemovedVariantsList()
                .stream()
                .map(TransfurVariant::getFormId)
                .toList();

        // Remove as variantes se elas existirem na lista
        variants.removeIf(list::contains);
    }
}


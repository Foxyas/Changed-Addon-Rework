package net.foxyas.changedaddon.mixins.entity.variant;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.item.loot.RandomVariantFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = RandomVariantFunction.Builder.class, remap = false)
public class RandomVariantFunctionMixin {
    @Shadow private List<ResourceLocation> variants;

    @Inject(method = "withAllVariants", at = @At(value = "INVOKE", target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z", remap = true), cancellable = true)
    private void RemoveVariant(CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        List<ResourceLocation> variantList = new ArrayList<>(TransfurVariant.getPublicTransfurVariants().map(ForgeRegistryEntry::getRegistryName).toList());
        // Obtém a lista de variantes a serem removidas
        List<ResourceLocation> list = ChangedAddonTransfurVariants.getRemovedVariantsList()
                .stream()
                .map(TransfurVariant::getRegistryName)
                .toList();


        // Remove as variantes se elas existirem na lista
        variantList.removeIf(list::contains);

        this.variants.addAll(variantList);
    }
}


package net.foxyas.changedaddon.mixins.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IForgeItemStack {
    @Shadow
    public abstract Item getItem();

//    @Override
//    public boolean canElytraFly(LivingEntity entity) {
//        ItemStack self = (ItemStack)(IForgeItemStack)this;
//        boolean variantCanFly = ProcessTransfur.getPlayerTransfurVariantSafe(EntityUtil.playerOrNull(entity))
//                .map(latexVariant -> {
//                    if (latexVariant.getChangedEntity() instanceof VariantExtraStats variantExtraStats) {
//                        return variantExtraStats.getFlyType().canGlide();
//                    }
//                    return latexVariant.getParent().canGlide;
//                }).orElse(false);
//        return variantCanFly || this.getItem().canElytraFly(self, entity);
//    }
}

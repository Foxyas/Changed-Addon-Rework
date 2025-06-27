/*package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.block.IridiumoreBlock;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Inject(method = "isCorrectToolForDrops",at = @At("HEAD"),cancellable = true)
    private void AllowItemsDrops(BlockState state, CallbackInfoReturnable<Boolean> cir){
        if (this.getItem() instanceof PickaxeItem tieredItem){
            if (tieredItem.getTier().level() >= Tiers.DIAMOND.level()){
                if (state.is(ChangedAddonModBlocks.IRIDIUM_ORE.get()) || state.is(ChangedAddonModBlocks.IRIDIUM_BLOCK.get())) {
                    cir.setReturnValue(true);
                }
            } else if (tieredItem.getTier().level() >= Tiers.NETHERITE.level()){
               if (state.is(ChangedAddonModBlocks.PAINITE_ORE.get()) || state.is(ChangedAddonModBlocks.PAINITE_BLOCK.get())){
                   cir.setReturnValue(true);
               }
            }
        }
    }
}

// PlaceHolder

*/

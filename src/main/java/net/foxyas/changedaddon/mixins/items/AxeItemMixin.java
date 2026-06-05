package net.foxyas.changedaddon.mixins.items;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.block.interfaces.IStrippableLog;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(AxeItem.class)
public class AxeItemMixin {

//    @WrapOperation(method = "getAxeStrippingState", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
//    private static Object ChangedAddon$getAxeStrippingState(Map<Block, Block> instance, Object object, Operation<Object> original, @Local(argsOnly = true) BlockState blockState) {
//        if (object instanceof Block block) {
//            Object call = original.call(instance, object);
//            if (call == null && blockState.getBlock() instanceof IStrippableLog strippableLog) {
//                return strippableLog.getStripedVariant(blockState);
//            }
//        }
//        return original;
//    }

    @ModifyReturnValue(method = "getAxeStrippingState", at = @At("RETURN"), remap = false)
    private static BlockState ChangedAddon$getAxeStrippingState(BlockState newState, @Local(argsOnly = true) BlockState originalState) {
        if (newState == null // Default Fail.
                && originalState.getBlock() instanceof IStrippableLog strippableLog) {
            return strippableLog.getStripedVariant(originalState);
        }
        return newState;
    }
}

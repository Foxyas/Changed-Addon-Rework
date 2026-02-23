package net.foxyas.changedaddon.mixins.mods.changed;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.block.WhiteLatexCoverBlock;
import net.ltxprogrammer.changed.block.WhiteLatexTransportInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = WhiteLatexTransportInterface.EventSubscriber.class, remap = false)
public class WhiteLatexTransportInterfaceMixin {


    @WrapOperation(method = "onPlayerTick", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/block/WhiteLatexTransportInterface;isBoundingBoxInWhiteLatex(Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/Optional;"))
    private static Optional<BlockPos> cancelAutoTravel(LivingEntity entity, Operation<Optional<BlockPos>> original) {
        Optional<BlockPos> call = original.call(entity);
        if (call.isPresent()) {
            BlockPos blockPos = call.get();
            BlockState blockState = entity.level.getBlockState(blockPos);
            if (blockState.getBlock() instanceof WhiteLatexCoverBlock) {
                return Optional.empty();
            }
        }

        return call;
    }
}

package net.foxyas.changedaddon.mixins.mods.changed;

import net.ltxprogrammer.changed.client.LatexCoveredBlocksRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LatexCoveredBlocksRenderer.class, remap = false)
public abstract class LatexCoveredBlocksRendererMixin {

//    @Unique
//    private static final ThreadLocal<LatexCoverGetter> threadLocal = ThreadLocal.withInitial(()-> null);
//
//    @Inject(at = @At(value = "HEAD"), method = "getLatexCoverStateGetter", cancellable = true)
//    private static void replaceGetter(CallbackInfoReturnable<Optional<LatexCoverGetter>> cir){
//        cir.setReturnValue(Optional.ofNullable(threadLocal.get()));
//    }
//
//    @Inject(at = @At(value = "FIELD", target = "Lnet/ltxprogrammer/changed/client/LatexCoveredBlocksRenderer;latexCoverStateGetter:Lnet/ltxprogrammer/changed/world/LatexCoverGetter;", ordinal = 0, opcode = Opcodes.PUTSTATIC), method = "wrappedTesselate")
//    private void setGetter(BlockAndTintGetter level, LatexCoverGetter latexCoverGetter, BlockPos blockPos, VertexConsumer bufferBuilder, BlockState blockState, LatexCoverState coverState, RandomSource random, CallbackInfoReturnable<Boolean> cir){
//        threadLocal.set(latexCoverGetter);
//    }
//
//    @Inject(at = @At(value = "FIELD", target = "Lnet/ltxprogrammer/changed/client/LatexCoveredBlocksRenderer;latexCoverStateGetter:Lnet/ltxprogrammer/changed/world/LatexCoverGetter;", ordinal = 1, opcode = Opcodes.PUTSTATIC), method = "wrappedTesselate")
//    private void clearGetter(BlockAndTintGetter level, LatexCoverGetter latexCoverGetter, BlockPos blockPos, VertexConsumer bufferBuilder, BlockState blockState, LatexCoverState coverState, RandomSource random, CallbackInfoReturnable<Boolean> cir){
//        threadLocal.remove();
//    }
}

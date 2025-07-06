package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.init.ChangedAddonModGameRules;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedGameRules;
import net.ltxprogrammer.changed.process.LatexCoveredBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Random;

import static net.foxyas.changedaddon.process.util.FoxyasUtils.isConnectedToSource;

@Mixin(value = AbstractLatexBlock.class)
public class AbstractLatexBlockMixin {


    @Inject(method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Ljava/util/Random;Lnet/ltxprogrammer/changed/entity/LatexType;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", shift = At.Shift.BY),
            cancellable = true, remap = false)
    private static void injectMethod(BlockState state, ServerLevel level, BlockPos position, Random random, LatexType latexType, CallbackInfo ci) {
        boolean gameRule = level.getGameRules().getBoolean(ChangedAddonModGameRules.NEED_FULL_SOURCE_TO_SPREAD);
        if (gameRule) {
            if (latexType == LatexType.DARK_LATEX) {
                if (!isConnectedToSource(level, position, latexType, ChangedBlocks.DARK_LATEX_BLOCK.get(), 16)) {
                    ci.cancel();
                }
            } else if (latexType == LatexType.WHITE_LATEX) {
                if (!isConnectedToSource(level, position, latexType, ChangedBlocks.WHITE_LATEX_BLOCK.get(), 16)) {
                    ci.cancel();
                }
            }
        }
    }
}

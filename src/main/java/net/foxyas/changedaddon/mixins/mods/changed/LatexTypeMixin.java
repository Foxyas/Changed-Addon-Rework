package net.foxyas.changedaddon.mixins.mods.changed;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.event.LatexCoverStateEvent;
import net.foxyas.changedaddon.event.LatexTypePlayerEvent;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LatexType.class, remap = false)
public class LatexTypeMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void hookUse(LatexCoverState state, Level level, Player player, InteractionHand hand, BlockHitResult hitVec, CallbackInfoReturnable<InteractionResult> cir) {
        LatexType self = (LatexType) (Object) this;
        LatexTypePlayerEvent.RightClick event = new LatexTypePlayerEvent.RightClick(player, level, self, state, hand, hitVec, cir.getReturnValue(), player.getRandom());
        if (ChangedAddonMod.postEvent(event)) {
            cir.setReturnValue(event.getInteractionResult());
        }
    }

    @Inject(
            method = "randomTick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hookRandomTick(LatexCoverState state, ServerLevel level, BlockPos blockPos, RandomSource random, CallbackInfo ci) {
        LatexType self = (LatexType) (Object) this;
        var event = new LatexCoverStateEvent.RandomTick(state, self, level, blockPos, random);
        if (ChangedAddonMod.postEvent(event)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onPlace",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hookOnPlace(LatexCoverState state, Level level, BlockPos blockPos, LatexCoverState oldState, boolean flag, CallbackInfo ci) {
        LatexType self = (LatexType) (Object) this;
        var event = new LatexCoverStateEvent.OnPlace(state, self, level, blockPos, oldState, flag);
        if (ChangedAddonMod.postEvent(event)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onRemove",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hookOnRemove(LatexCoverState state, Level level, BlockPos blockPos, LatexCoverState oldState, boolean flag, CallbackInfo ci) {
        LatexType self = (LatexType) (Object) this;
        var event = new LatexCoverStateEvent.OnRemove(state, self, level, blockPos, oldState, flag, null);
        if (ChangedAddonMod.postEvent(event)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "updateInPlace",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hookUpdateInPlace(LatexCoverState state, BlockState oldState, BlockState newState, LevelAccessor level, BlockPos pos, CallbackInfoReturnable<LatexCoverState> cir) {
        LatexType self = (LatexType) (Object) this;
        var event = new LatexCoverStateEvent.UpdateInPlace(state, self, oldState, newState, level, pos, cir.getReturnValue());
        if (ChangedAddonMod.postEvent(event)) {
            cir.setReturnValue(event.modReturnValue);
        }
    }
}

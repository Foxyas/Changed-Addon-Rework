package net.foxyas.changedaddon.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerMixin {

    @Shadow
    public ServerPlayer player;

    @ModifyVariable(name = "d10", at = @At(value = "LOAD", ordinal = 0), method = "handleMovePlayer")
    private double acceptHeadRotWhenCuddling(double original, @Local(name = "f") float f, @Local(name = "f1") float f1) {
        if (!ChangedAddonVariables.ofOrDefault(player).isCuddling || original > 1) return original;

        player.absMoveTo(player.getX(), player.getY(), player.getZ(), f, f1);
        return original;
    }
}

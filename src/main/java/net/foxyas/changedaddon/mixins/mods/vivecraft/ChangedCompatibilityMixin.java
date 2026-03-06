package net.foxyas.changedaddon.mixins.mods.vivecraft;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.ltxprogrammer.changed.extension.ChangedCompatibility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.vivecraft.client.ClientVRPlayers;
import org.vivecraft.client_vr.settings.VRSettings;

@Mixin(value = ChangedCompatibility.class, remap = false)
@RequiredMods("vivecraft")
public class ChangedCompatibilityMixin {

    /*@ModifyReturnValue(method = "isFirstPersonRendering", at = @At("RETURN"))
    private static boolean VRShowPlayerModelHook(boolean original) {

        LocalPlayer player = Minecraft.getInstance().player;
        if (ClientVRPlayers.getInstance().isVRPlayer(player)) {
            boolean shouldRenderSelf = VRSettings.INSTANCE.shouldRenderSelf;
            return shouldRenderSelf || original;
        }

        return original;
    }*/
}

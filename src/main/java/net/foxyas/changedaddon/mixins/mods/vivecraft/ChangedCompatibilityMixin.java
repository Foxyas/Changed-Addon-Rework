package net.foxyas.changedaddon.mixins.mods.vivecraft;

import net.foxyas.changedaddon.extension.RequiredMods;
import net.ltxprogrammer.changed.extension.ChangedCompatibility;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ChangedCompatibility.class, remap = false)
@RequiredMods("vivecraft")
@Deprecated(forRemoval = true, since = "Changed:0.16.0")
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

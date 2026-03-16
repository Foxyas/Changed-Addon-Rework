package net.foxyas.changedaddon.mixins.mods.changed;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FormRenderHandler.class, remap = false)
public class FormRenderHandleMixin {

    @ModifyReturnValue(method = "maxPackedLight" , at = @At("RETURN"))
    private static int maxPackedLightHook(int original,
                                          int packedLight0,
                                          int packedLight1) {
        ModList modList = ModList.get();
        if (modList.isLoaded("oculus") || modList.isLoaded("embeddium")) {
            return packedLight0;
        }
        return original;
    }
}

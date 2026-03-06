package net.foxyas.changedaddon.mixins.mods.vivecraft;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.vivecraft.client.utils.ScaleHelper;

@Mixin(value = ScaleHelper.class, remap = false)
@RequiredMods("vivecraft")
public class AutoCalibrationMixin {


    @ModifyReturnValue(method = "getEntityEyeHeightScale", at = @At("RETURN"))
    private static float getEntityEyeHeightScaleHook(float original, LivingEntity entity, float partialTick) {
        LivingEntity livingEntity = EntityUtil.maybeGetOverlaying(entity);
        if (livingEntity instanceof IAlphaAbleEntity alphaAbleEntity && alphaAbleEntity.isAlpha()) {
            return original + alphaAbleEntity.alphaAdditionalScale();
        }
        return original;
    }

    @ModifyReturnValue(method = "getEntityBbScale", at = @At("RETURN"))
    private static float getEntityBbScaleHook(float original, LivingEntity entity, float partialTick) {
        LocalPlayer player = Minecraft.getInstance().player;
        LivingEntity livingEntity = EntityUtil.maybeGetOverlaying(player);
        if (livingEntity instanceof IAlphaAbleEntity alphaAbleEntity && alphaAbleEntity.isAlpha()) {
            return original + alphaAbleEntity.alphaAdditionalScale();
        }
        return original;
    }
}

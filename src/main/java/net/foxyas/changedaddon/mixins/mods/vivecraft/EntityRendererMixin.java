package net.foxyas.changedaddon.mixins.mods.vivecraft;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.vivecraft.client_vr.ClientDataHolderVR;
import org.vivecraft.client_vr.render.helpers.VREffectsHelper;

@Mixin(EntityRenderer.class)
@RequiredMods("vivecraft")
@Deprecated(forRemoval = true, since = "Changed:0.16.0")
public class EntityRendererMixin {

    @ModifyReturnValue(method = "getRenderOffset", at = @At("RETURN"))
    private Vec3 getRenderOffsetHook(Vec3 original, @Local(argsOnly = true) Entity entity, @Local(argsOnly = true) float pPartialTicks) {
        var self = (EntityRenderer<?>) (Object) this;
        if (self instanceof AdvancedHumanoidRenderer<?, ?> advancedHumanoidRenderer) {
            if (entity instanceof ChangedEntity changedEntity) {
                if (changedEntity.getUnderlyingPlayer() instanceof AbstractClientPlayer player) {
                    if (VREffectsHelper.isFirstPersonPlayer(player)) {
                        return player.isVisuallySwimming() ? new Vec3(0.0F, -0.125F * ClientDataHolderVR.getInstance().vrPlayer.worldScale, 0.0F) : Vec3.ZERO;
                    } else {
                        return player.isVisuallySwimming() ? new Vec3(0.0F, -0.125F, 0.0F) : Vec3.ZERO;
                    }
                }

            }
        }

        return original;
    }
}

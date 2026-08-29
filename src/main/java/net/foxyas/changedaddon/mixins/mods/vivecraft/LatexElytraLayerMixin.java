package net.foxyas.changedaddon.mixins.mods.vivecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.ltxprogrammer.changed.client.renderer.layers.LatexElytraLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.vivecraft.client.ClientVRPlayers;
import org.vivecraft.client.utils.ModelUtils;
import org.vivecraft.common.utils.MathUtils;

@Mixin(value = LatexElytraLayer.class)
@RequiredMods("vivecraft")
@Deprecated(forRemoval = true, since = "Changed:0.16.0")
public abstract class LatexElytraLayerMixin<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends RenderLayer<T, M> {

    @Unique
    private final Vector3f ChangedAddon$tempV = new Vector3f();

    @Unique
    private final Matrix3f ChangedAddon$bodyRot = new Matrix3f();


    public LatexElytraLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    // Copied from ElytraLayerMixin
    // https://github.com/Vivecraft/VivecraftMod/blob/Multiloader-1.20/common/src/main/java/org/vivecraft/mixin/client/renderer/entity/layers/ElytraLayerMixin.java

    @WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"))
    private void ChangedAddon$elytraPosition(
            PoseStack instance, double pX, double pY, double pZ, Operation<Void> original,
            @Local(argsOnly = true) ChangedEntity changedEntity, @Local(argsOnly = true, ordinal = 2) float partialTick) {

        if (changedEntity.getUnderlyingPlayer() instanceof AbstractClientPlayer entity) {
            // don't care about interpolation here, only needs the scales which aren't interpolated
            ClientVRPlayers.RotInfo rotInfo = ClientVRPlayers.getInstance().getLatestRotationsForPlayer(entity.getUUID());
            // only do this if it's a player model and a vr player
            M model = getParentModel();
            if (rotInfo != null) {
                ModelPart body = model.getTorso();
                this.ChangedAddon$bodyRot.rotationZYX(body.zRot, -body.yRot, -body.xRot);

                this.ChangedAddon$bodyRot.transform(MathUtils.UP, this.ChangedAddon$tempV);
                float xRotation = (float) Math.atan2(this.ChangedAddon$tempV.y, this.ChangedAddon$tempV.z) - Mth.HALF_PI;

                this.ChangedAddon$bodyRot.transform(MathUtils.LEFT, this.ChangedAddon$tempV);
                float yRotation = (float) -Math.atan2(this.ChangedAddon$tempV.x, this.ChangedAddon$tempV.y) + Mth.HALF_PI;

                // position the cape behind the body
                float yOffset = 0F;
                if (entity.isFallFlying()) {
                    // move it down, to not be in the players face
                    yOffset = 2F;
                } else if (entity.isCrouching()) {
                    // undo vanilla crouch offset
                    yOffset = -3F;
                }
                // transform offset to be body relative
                this.ChangedAddon$tempV.set(0F, yOffset, 2F - 0.5F * (body.xRot / Mth.HALF_PI));
                this.ChangedAddon$tempV.rotateX(xRotation);
                this.ChangedAddon$tempV.rotateZ(yRotation);

                // +24 because it should be the offset to the default position, which is at 24
                this.ChangedAddon$tempV.add(body.x, body.y + 24F, body.z);

                // no yaw, since we  need the vector to be player rotated anyway
                ModelUtils.modelToWorld(entity, this.ChangedAddon$tempV, rotInfo, 0F, false, false, this.ChangedAddon$tempV);
                original.call(instance, (double) this.ChangedAddon$tempV.x, (double) -this.ChangedAddon$tempV.y, (double) -this.ChangedAddon$tempV.z);

                // rotate elytra
                instance.mulPose(Axis.XP.rotation(xRotation));
                instance.mulPose(Axis.YP.rotation(yRotation));
            }
        } else {
            original.call(instance, pX, pY, pZ);
        }
    }

}

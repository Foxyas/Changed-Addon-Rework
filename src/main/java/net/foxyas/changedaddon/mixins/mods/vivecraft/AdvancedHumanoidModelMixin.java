package net.foxyas.changedaddon.mixins.mods.vivecraft;

import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.client.render.VRPlayerModel;
import org.vivecraft.client.render.VRPlayerRenderer;

@Mixin(value = AdvancedHumanoidModel.class, remap = false)
@RequiredMods("vivecraft")
public abstract class AdvancedHumanoidModelMixin<T extends ChangedEntity> extends PlayerModel<T> {


    @Shadow public abstract ModelPart getArm(HumanoidArm humanoidArm);

    @Shadow public abstract ModelPart getLeg(HumanoidArm humanoidArm);

    public AdvancedHumanoidModelMixin(ModelPart pRoot, boolean pSlim) {
        super(pRoot, pSlim);
    }

    @Inject(method = "setupAnim(Lnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFF)V", at = @At("TAIL"))
    private void hook(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
        var self = (AdvancedHumanoidModel<?>) (Object) this;
        Player player = entity.getUnderlyingPlayer();
        if (player instanceof AbstractClientPlayer clientPlayer) {
            EntityRenderer<?> renderer =
                    Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(clientPlayer);

            if (renderer instanceof VRPlayerRenderer vrPlayerRenderer) {
                PlayerModel<AbstractClientPlayer> playerModel = vrPlayerRenderer.getModel();
                if (playerModel instanceof VRPlayerModel<AbstractClientPlayer>) {
                    playerModel.setupAnim(clientPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    // ====== Mapear partes ======
                    ModelPart selfHead = self.getHead();
                    ModelPart selfBody = self.getTorso();
                    ModelPart selfLeftArm = self.getArm(HumanoidArm.LEFT);
                    ModelPart selfRightArm = self.getArm(HumanoidArm.RIGHT);
                    ModelPart selfLeftLeg = self.getLeg(HumanoidArm.LEFT);
                    ModelPart selfRightLeg = self.getLeg(HumanoidArm.RIGHT);

                    // ====== Copiar Pose ======
                    ChangedAddon$copyPart(playerModel.head, selfHead);
                    ChangedAddon$copyPart(playerModel.body, selfBody);
                    ChangedAddon$copyPart(playerModel.leftArm, selfLeftArm);
                    ChangedAddon$copyPart(playerModel.rightArm, selfRightArm);
                    ChangedAddon$copyPart(playerModel.leftLeg, selfLeftLeg);
                    ChangedAddon$copyPart(playerModel.rightLeg, selfRightLeg);
                }

            }

        }
    }

    @Unique
    private static void ChangedAddon$copyPart(ModelPart from, ModelPart to) {
        // Em vez de loadPose (que reseta tudo), tente copiar os ângulos seletivamente
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;

        // Posição é crucial para o VR (posicionamento das mãos)
        to.x = from.x;
        to.y = from.y;
        to.z = from.z;

        to.xScale = from.xScale;
        to.yScale = from.yScale;
        to.zScale = from.zScale;
        to.visible = from.visible;
    }
}

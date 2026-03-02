package net.foxyas.changedaddon.mixins.mods.vivecraft;

import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.client.ClientVRPlayers;
import org.vivecraft.client.render.VRPlayerModel;
import org.vivecraft.client.render.VRPlayerRenderer;

@Mixin(value = AdvancedHumanoidModel.class, remap = false)
@RequiredMods("vivecraft")
public abstract class AdvancedHumanoidModelMixin<T extends ChangedEntity> extends PlayerModel<T> {

    @Shadow
    public abstract @NotNull ModelPart getArm(@NotNull HumanoidArm humanoidArm);

    @Shadow
    public abstract ModelPart getLeg(HumanoidArm humanoidArm);

    @Shadow
    public abstract HumanoidAnimator<T, ?> getAnimator(T t);

    public AdvancedHumanoidModelMixin(ModelPart pRoot, boolean pSlim) {
        super(pRoot, pSlim);
    }

    @Inject(method = "setupAnim(Lnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFF)V", at = @At("TAIL"))
    private void setupAnimHook(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        var self = (AdvancedHumanoidModel<?>) (Object) this;
        Player player = entity.getUnderlyingPlayer();
        if (player instanceof AbstractClientPlayer clientPlayer) {
            if (!(ClientVRPlayers.getInstance().isVRPlayer(player))) {
                return;
            }


            EntityRenderer<?> renderer =
                    Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(clientPlayer);

            if (renderer instanceof VRPlayerRenderer vrPlayerRenderer) {

                PlayerModel<AbstractClientPlayer> playerModel = vrPlayerRenderer.getModel();

                if (playerModel instanceof VRPlayerModel<AbstractClientPlayer> vrPlayerModel) {
                    // ===== Map self model parts =====
                    ModelPart selfHead = self.getHead();
                    ModelPart selfBody = self.getTorso();
                    ModelPart selfLeftArm = self.getArm(HumanoidArm.LEFT);
                    ModelPart selfRightArm = self.getArm(HumanoidArm.RIGHT);
                    ModelPart selfLeftLeg = self.getLeg(HumanoidArm.LEFT);
                    ModelPart selfRightLeg = self.getLeg(HumanoidArm.RIGHT);

                    // Force the VR model to update its animation state
                    // (includes HMD, controller tracking, body yaw logic, etc.)
                    playerModel.swimAmount = this.swimAmount;
                    playerModel.setupAnim(clientPlayer,
                            limbSwing,
                            limbSwingAmount,
                            ageInTicks,
                            netHeadYaw,
                            headPitch);

                    // ===== Copy final VR pose into transformed model =====
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

    /**
     * Copies rotational, positional, scale, and visibility data
     * from one ModelPart to another.
     * <p>
     * We intentionally avoid using loadPose(), because it resets
     * additional internal state that may conflict with VR tracking.
     * <p>
     * Direct field copying ensures we preserve Vivecraft's final
     * computed pose (including controller offsets and head tracking).
     */
    @Unique
    private static void ChangedAddon$copyPart(ModelPart from, ModelPart to) {
        if (from == null || to == null) {
            return;
        }

        // Copy rotation (pitch, yaw, roll)
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;

        // Copy translation (critical for VR hand positioning)
        to.x = from.x;
        to.y = from.y;
        to.z = from.z;

        // Copy scaling
        to.xScale = from.xScale;
        to.yScale = from.yScale;
        to.zScale = from.zScale;

        // Copy visibility state
        to.visible = from.visible;
    }
}

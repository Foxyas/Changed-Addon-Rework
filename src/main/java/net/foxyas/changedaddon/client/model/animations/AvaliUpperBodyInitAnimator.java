package net.foxyas.changedaddon.client.model.animations;

import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator.AnimateStage;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.AbstractUpperBodyAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class AvaliUpperBodyInitAnimator<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends AbstractUpperBodyAnimator<T, M> {
    public AvaliUpperBodyInitAnimator(ModelPart head, ModelPart torso, ModelPart leftArm, ModelPart rightArm) {
        super(head, torso, leftArm, rightArm);
    }

    public HumanoidAnimator.AnimateStage preferredStage() {
        return AnimateStage.INIT;
    }

    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean fallFlying = entity.getFallFlyingTicks() > 4;
        this.torso.yRot = 0.0F;
        this.torso.zRot = 0.0F;
        this.rightArm.z = 0.0F;
        this.rightArm.x = -this.core.torsoWidth;
        this.leftArm.z = 0.0F;
        this.leftArm.x = this.core.torsoWidth;
        float f = 1.0F;
        if (fallFlying) {
            this.rightArm.xRot = 0.0f;
            this.leftArm.xRot = 0.0f;
            this.rightArm.yRot = 0.0f;
            this.leftArm.yRot = 0.0f;
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
        } else {
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
            this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            ModelPart var10000 = this.rightArm;
            var10000.zRot += Mth.lerp(this.core.reachOut, 0.0F, 0.1745329F);
            var10000 = this.leftArm;
            var10000.zRot += Mth.lerp(this.core.reachOut, 0.0F, -0.1745329F);
            this.rightArm.xRot = Mth.lerp(this.core.reachOut, this.rightArm.xRot, (-(float)Math.PI / 6F));
            this.leftArm.xRot = Mth.lerp(this.core.reachOut, this.leftArm.xRot, (-(float)Math.PI / 6F));
        }

    }
}

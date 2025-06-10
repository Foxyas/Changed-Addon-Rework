package net.foxyas.changedaddon.client.model.animations;

import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator.AnimateStage;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.AbstractUpperBodyAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class CustomWingedDragonUpperBodyInitAnimator<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends AbstractUpperBodyAnimator<T, M> {
    public CustomWingedDragonUpperBodyInitAnimator(ModelPart head, ModelPart torso, ModelPart leftArm, ModelPart rightArm) {
        super(head, torso, leftArm, rightArm);
    }

    public HumanoidAnimator.AnimateStage preferredStage() {
        return AnimateStage.INIT;
    }

    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isEyeInFluid(FluidTags.WATER)) {
            boolean fallFlying = entity.getFallFlyingTicks() > 4;
            this.torso.yRot = 0.0F;
            this.torso.zRot = 0.0F;
            this.rightArm.z = 0.0F;
            this.rightArm.x = -this.core.torsoWidth;
            this.leftArm.z = 0.0F;
            this.leftArm.x = this.core.torsoWidth;
            float f = 1.0F;
            if (fallFlying) {
                f = (float) entity.getDeltaMovement().lengthSqr();
                f /= 0.2F;
                f *= f * f;
            }

            if (f < 1.0F) {
                f = 1.0F;
            }

            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.xRot = Mth.lerp(this.core.flyAmount, Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f, 0.0F);
            this.leftArm.xRot = Mth.lerp(this.core.flyAmount, Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f, 0.0F);
            ModelPart var10000 = this.rightArm;
            var10000.zRot += Mth.lerp(this.core.reachOut, 0.0F, 0.1745329F);
            var10000 = this.leftArm;
            var10000.zRot += Mth.lerp(this.core.reachOut, 0.0F, -0.1745329F);
            this.rightArm.xRot = Mth.lerp(this.core.reachOut, this.rightArm.xRot, (-(float) Math.PI / 6F));
            this.leftArm.xRot = Mth.lerp(this.core.reachOut, this.leftArm.xRot, (-(float) Math.PI / 6F));
        }
    }
}

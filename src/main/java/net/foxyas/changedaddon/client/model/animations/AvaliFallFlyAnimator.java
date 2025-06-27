package net.foxyas.changedaddon.client.model.animations;

import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator.AnimateStage;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class AvaliFallFlyAnimator<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends HumanoidAnimator.Animator<T, M> {

    private final ModelPart RightArm;
    private final ModelPart LeftArm;

    public AvaliFallFlyAnimator(ModelPart rightArm, ModelPart leftArm) {
        super();
        RightArm = rightArm;
        LeftArm = leftArm;
    }

    @Override
    public AnimateStage preferredStage() {
        return AnimateStage.FALL_FLY;
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float ticks = (float) entity.getFallFlyingTicks();
        // Aplicamos uma curva ease-in-out para suavizar o início e fim
        float t = Mth.clamp(ticks / 15.0F, 0.0F, 1.0F);
        float flyAmount = smootherStep(t); // Muito mais suave!

        // float old2_flyAmount = t * t * (3.0F - 2.0F * t); // Smoothstep

        // float old_flyAmount = Mth.clamp(ticks * ticks / 100.0F, 0.0F, 1.0F); // Suavização


        float targetY = (float) Math.toRadians(90);
        float targetZ = (float) Math.toRadians(90);

        this.RightArm.yRot = Mth.lerp(flyAmount, this.RightArm.yRot, targetY);
        this.RightArm.zRot = Mth.lerp(flyAmount, this.RightArm.zRot, targetZ);

        this.LeftArm.yRot = Mth.lerp(flyAmount, this.LeftArm.yRot, -targetY);
        this.LeftArm.zRot = Mth.lerp(flyAmount, this.LeftArm.zRot, -targetZ);
    }

    float smootherStep(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

}

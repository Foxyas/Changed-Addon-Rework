package net.foxyas.changedaddon.mixins.cmrs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.zaharenko424.cmrs.client.api.MatrixStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Mixin(PoseStack.class)
public abstract class MixinPoseStack implements MatrixStack {

    @Shadow @Final private Deque<PoseStack.Pose> poseStack;

    @Shadow public abstract PoseStack.Pose last();

    @Shadow public abstract void pushPose();

    @Unique
    private static final ThreadLocal<List<PoseStack.Pose>> cmrs$posePool = ThreadLocal.withInitial(ArrayList::new);

    @Override
    public void cmrs$push() {
        List<PoseStack.Pose> pool = cmrs$posePool.get();
        if(pool.isEmpty()) {
            pushPose();
            return;
        }
        PoseStack.Pose matrix = pool.remove(pool.size() - 1);
        PoseStack.Pose last = last();

        matrix.pose().set(last.pose());
        matrix.normal().set(last.normal());

        poseStack.add(matrix);
    }

    @Override
    public void cmrs$pop() {
        cmrs$posePool.get().add(poseStack.removeLast());
    }

    @Override
    public void cmrs$mul(Matrix4f mat) {
        PoseStack.Pose posestack$pose = this.poseStack.getLast();
        posestack$pose.pose().mul(mat);
        if (!cmrs$isPureTranslation(mat)) {
            if (cmrs$isOrthonormal(mat)) {
                posestack$pose.normal().mul(new Matrix3f(mat));
            } else {
                posestack$pose.normal().set(posestack$pose.pose()).invert().transpose();
            }
        }
    }

    @Unique
    private static boolean cmrs$isPureTranslation(Matrix4f matrix) {
        return (matrix.properties() & 8) != 0;
    }

    @Unique
    private static boolean cmrs$isOrthonormal(Matrix4f matrix) {
        return (matrix.properties() & 16) != 0;
    }
}

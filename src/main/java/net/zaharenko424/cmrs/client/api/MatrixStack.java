package net.zaharenko424.cmrs.client.api;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface MatrixStack {

    static MatrixStack of(PoseStack poseStack){
        return (MatrixStack) poseStack;
    }

    static void push(PoseStack poseStack){
        of(poseStack).cmrs$push();
    }

    static void pop(PoseStack poseStack){
        of(poseStack).cmrs$pop();
    }

    static void translate(PoseStack poseStack, Vector3f translation){
        poseStack.last().pose().translate(translation);
    }

    static void scale(PoseStack poseStack, Vector3f scale){
        poseStack.scale(scale.x, scale.y, scale.z);
    }

    static void mul(PoseStack poseStack, Matrix4f mat) {
        of(poseStack).cmrs$mul(mat);
    }

    void cmrs$push();

    void cmrs$pop();

    void cmrs$mul(Matrix4f mat);
}

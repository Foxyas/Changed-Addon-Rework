package net.foxyas.changedaddon.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class ModelPosUtil {
    private ModelPosUtil() {}

    /** Local point in blocks -> world position. PoseStack must be at the part's transform. */
    public static Vec3 toWorld(PoseStack poseStack, float x, float y, float z) {
        // model space -> view space
        Vector3f v = poseStack.last().pose().transformPosition(new Vector3f(x, y, z));

        // view space -> camera-relative world (undo camera rotation incl. roll)
        RenderSystem.getInverseViewRotationMatrix().transform(v);

        // camera-relative -> absolute world
        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        return new Vec3(cam.x + v.x, cam.y + v.y, cam.z + v.z);
    }

    /** Same, but in model-pixel units (what ModelPart cubes use: 16 px = 1 block). */
    public static Vec3 toWorldPx(PoseStack poseStack, float px, float py, float pz) {
        return toWorld(poseStack, px / 16f, py / 16f, pz / 16f);
    }

    public static Vec3 toWorldPx$(ModelPart part, PoseStack stack, float px, float py, float pz) {
        part.translateAndRotate(stack);
        return toWorld(stack, px / 16f, py / 16f, pz / 16f);
    }
}